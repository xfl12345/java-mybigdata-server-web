package cc.xfl12345.mybigdata.server.model.database.handler.impl;


import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.handler.GroupTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.NumberTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.IntegerContent;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static cc.xfl12345.mybigdata.server.utility.BeeOrmUtils.getSuidRich;

@Slf4j
public class NumberTypeHandlerImpl extends AbstractCoreTableHandler implements NumberTypeHandler {

    @Getter
    @Setter
    protected SqlErrorHandler sqlErrorHandler = null;

    @Getter
    @Setter
    protected volatile StringTypeHandler stringTypeHandler = null;

    @Getter
    @Setter
    protected volatile GroupTypeHandler groupTypeHandler = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (stringTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("stringTypeHandler"));
        }
        if (groupTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("groupTypeHandler"));
        }
        if (sqlErrorHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("sqlErrorHandler"));
        }
    }

    protected Long insertInteger(Long value) {
        Date date = new Date();
        GlobalDataRecord globalDataRecord = getNewRegisteredGlobalDataRecord(
            date,
            coreTableCache.getTableNameCache().getValue(CoreTableNames.INTEGER_CONTENT.getName())
        );
        SuidRich suid = getSuidRich();
        IntegerContent content = new IntegerContent();
        content.setContent(value);
        content.setGlobalId(globalDataRecord.getId());
        return suid.insertAndReturnId(content);
    }

    protected Long insertNumberAsString(BigDecimal value) throws Exception {
        Long globalId = stringTypeHandler.insertOrSelect4Id(value.toPlainString());
        HashSet<Long> hashSet = new HashSet<>();
        hashSet.add(globalId);
        groupTypeHandler.insertIntoGroupByGlobalId(coreTableCache.getIdOfNumberStringGroup(), hashSet);
        return globalId;
    }

    @Override
    public Long insert(BigDecimal value) throws Exception {
        if (isLongInteger(value)) { // 如果是 long型 整数可表示的 数字
            return insertInteger(value.longValue());
        } else { // 如果超出 long型 整数可表示的 数字范围，或者是浮点数，统一做字符串处理
            return insertNumberAsString(value);
        }
    }

    @Override
    public Long insertOrSelect4Id(BigDecimal value) throws Exception {
        Long globalId;
        try {
            globalId = insert(value);
        } catch (BeeException beeException) {
            SimpleCoreTableCurdResult curdResult = sqlErrorHandler.getSimpleCoreTableCurdResult(beeException);
            if (curdResult.equals(SimpleCoreTableCurdResult.DUPLICATE)) {
                globalId = selectId(value);
            } else {
                throw beeException;
            }
        }

        return globalId;
    }

    @Override
    public Long selectId(BigDecimal value) throws TableOperationException {
        Long result = null;
        SuidRich suid = getSuidRich();

        if (isLongInteger(value)) {
            IntegerContent integerContent = new IntegerContent();
            integerContent.setContent(value.longValue());
            List<IntegerContent> integerContents = suid.select(integerContent);
            int affectedRowCount = integerContents.size();
            // 要么检索到数据，要么无了
            if (affectedRowCount == 1) {
                result = integerContents.get(0).getGlobalId();
            } else if (affectedRowCount != 0) {
                throw getAffectedRowShouldBe1Exception(
                    affectedRowCount,
                    CURD.RETRIEVE,
                    CoreTableNames.INTEGER_CONTENT.getName()
                );
            }
        } else {
            result = stringTypeHandler.selectId(value.toPlainString());
        }

        return result;
    }

    @Override
    public void updateById(BigDecimal value, Long globalId) throws TableOperationException, TableDataException {
        Date date = new Date();
        boolean isInteger = isLongInteger(value);
        String numberInString = value.toPlainString();

        SuidRich suid = getSuidRich();

        GlobalDataRecord gdrInDb = suid.selectById(new GlobalDataRecord(), globalId);
        String tableName = coreTableCache.getTableNameCache().getKey(gdrInDb.getTableName());
        switch (CoreTableNames.valueOf(tableName)) {
            case STRING_CONTENT -> {
                // 如果是内容是 整数，但旧内容又位于 字符串表
                // 按理来说，如果 整数表 存在 目标更新值，
                // 不应该存在两个 global_id 同时指向 一条数据。
                // 所以这里的操作，只能是先删除旧表内容，然后插入新表，
                // 再更新 全局记录表 。
                if (isInteger) {
                    StringContent stringContent = new StringContent();
                    stringContent.setGlobalId(globalId);
                    int affectedRowCount = 0;
                    // 从旧表删除数据
                    affectedRowCount = suid.delete(stringContent);
                    if (affectedRowCount == 1) { // 删除成功了，那就插入新内容到 整数表
                        IntegerContent content = new IntegerContent();
                        content.setContent(value.longValue());
                        content.setGlobalId(globalId);

                        // 在新表插入数据
                        affectedRowCount = suid.insert(content);
                        // 如果插入成功了
                        if (affectedRowCount == 1) {
                            // 那就开始更新 全局记录表
                            GlobalDataRecord gdr4update = new GlobalDataRecord();
                            gdr4update.setId(gdrInDb.getId());
                            // 更新修改时间
                            gdr4update.setUpdateTime(date);
                            // 更新修改次数
                            gdr4update.setModifiedCount(gdrInDb.getModifiedCount() + 1);
                            // 字符串表 替换成 整数表
                            gdr4update.setTableName(
                                coreTableCache.getTableNameCache()
                                    .getValue(CoreTableNames.INTEGER_CONTENT.getName())
                            );

                            // 更新数据
                            affectedRowCount = suid.update(gdr4update);
                            if (affectedRowCount != 1) {
                                throw getUpdateShouldBe1Exception(
                                    affectedRowCount,
                                    CoreTableNames.GLOBAL_DATA_RECORD.getName()
                                );
                            }

                        } else {
                            throw getAffectedRowShouldBe1Exception(
                                affectedRowCount,
                                CURD.CREATE,
                                CoreTableNames.INTEGER_CONTENT.getName()
                            );
                        }

                    } else {
                        throw getAffectedRowShouldBe1Exception(
                            affectedRowCount,
                            CURD.DELETE,
                            CoreTableNames.STRING_CONTENT.getName()
                        );
                    }

                } else {
                    stringTypeHandler.updateStringByGlobalId(numberInString, globalId);
                }
            }

            case INTEGER_CONTENT -> {
                // 新内容是整数，同时旧内容也在整数表里
                if (isInteger) {
                    IntegerContent integerContent = new IntegerContent();
                    integerContent.setGlobalId(globalId);
                    integerContent.setContent(value.longValue());

                    int affectedRowCount = 0;
                    affectedRowCount = suid.update(integerContent);
                    if (affectedRowCount != 1) {
                        throw getUpdateShouldBe1Exception(
                            affectedRowCount,
                            CoreTableNames.INTEGER_CONTENT.getName()
                        );
                    }
                } else {
                    // 如果是新内容是 字符串，但旧内容又位于 整数表
                    IntegerContent integerContent = new IntegerContent();
                    integerContent.setGlobalId(globalId);
                    int affectedRowCount = 0;
                    // 那就先尝试删除。
                    affectedRowCount = suid.delete(integerContent);
                    if (affectedRowCount == 1) { // 删除成功了，那就插入新内容到 字符串表
                        StringContent content = new StringContent();
                        content.setContent(numberInString);
                        content.setGlobalId(gdrInDb.getId());

                        // 插入数据
                        affectedRowCount = suid.insert(content);
                        // 如果插入成功了
                        if (affectedRowCount == 1) {
                            // 那就开始更新 全局记录表
                            GlobalDataRecord gdr4update = new GlobalDataRecord();
                            gdr4update.setId(globalId);
                            // 更新修改时间
                            gdr4update.setUpdateTime(date);
                            // 更新修改次数
                            gdr4update.setModifiedCount(gdrInDb.getModifiedCount() + 1);
                            // 整数表 替换成 字符串表
                            gdr4update.setTableName(
                                coreTableCache.getTableNameCache()
                                    .getValue(CoreTableNames.STRING_CONTENT.getName())
                            );

                            // 更新数据
                            affectedRowCount = suid.update(gdr4update);
                            if (affectedRowCount == 1) {
                                throw getUpdateShouldBe1Exception(
                                    affectedRowCount,
                                    CoreTableNames.GLOBAL_DATA_RECORD.getName()
                                );
                            }


                        } else {
                            throw getAffectedRowShouldBe1Exception(
                                affectedRowCount,
                                CURD.CREATE,
                                CoreTableNames.STRING_CONTENT.getName()
                            );
                        }
                    } else {
                        throw getAffectedRowShouldBe1Exception(
                            affectedRowCount,
                            CURD.DELETE,
                            CoreTableNames.INTEGER_CONTENT.getName()
                        );
                    }
                }

            }
            default -> {
                throw new TableDataException(
                    "数字要么存储在 整数表 ，要么存储在 字符串表 ，发现数据有误，无法完成更新。"
                );
            }
        }
    }

    @Override
    public void delete(BigDecimal value) throws TableOperationException {
        SuidRich suid = getSuidRich();
        Long globalId = selectId(value);
        int affectedRowCount;

        // 删除数据
        if (isLongInteger(value)) {
            IntegerContent integerContent = new IntegerContent();
            integerContent.setGlobalId(globalId);
            affectedRowCount = suid.delete(integerContent);
            checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.INTEGER_CONTENT.getName());
        } else {
            StringContent stringContent = new StringContent();
            stringContent.setGlobalId(globalId);
            affectedRowCount = suid.delete(stringContent);
            checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.STRING_CONTENT.getName());
        }

        GlobalDataRecord globalDataRecord = new GlobalDataRecord();
        globalDataRecord.setId(globalId);
        affectedRowCount = suid.delete(globalDataRecord);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.GLOBAL_DATA_RECORD.getName());
    }

    public boolean isLongInteger(BigDecimal value) {
        return value.scale() == 0 && new BigDecimal(value.longValue()).compareTo(value) == 0;
    }
}
