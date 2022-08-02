package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.table.association.StringContentGlobalRecordAssociation;
import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import cc.xfl12345.mybigdata.server.utility.StringEscapeUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.*;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;
import org.teasoft.honey.osql.core.HoneyFactory;

import java.util.Date;
import java.util.List;

import static cc.xfl12345.mybigdata.server.utility.BeeOrmUtils.getSuidRich;

@Slf4j
public class StringTypeHandlerImpl extends AbstractCoreTableHandler implements StringTypeHandler {

    @Getter
    @Setter
    protected SqlErrorHandler sqlErrorHandler = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (sqlErrorHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("sqlErrorHandler"));
        }
    }

    @Override
    public Long insert(String value) {
        Date date = new Date();

        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
        // 预备一个 StringContent对象 空间
        StringContent content = new StringContent();
        content.setContent(value);
        content.setContentLength((short) value.length());

        GlobalDataRecord globalDataRecord = getNewRegisteredGlobalDataRecord(
            date,
            coreTableCache.getTableNameCache().getValue(CoreTableNames.STRING_CONTENT.getName())
        );

        content.setGlobalId(globalDataRecord.getId());

        // 插入数据
        return suid.insertAndReturnId(content);
    }

    @Override
    public Long insertOrSelect4Id(String value) throws Exception {
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

    protected void updateStringByCondition(String value, Condition condition) throws TableOperationException {
        Date nowTime = new Date();

        HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
        SuidRich suid = honeyFactory.getSuidRich();
        MoreTable moreTable = honeyFactory.getMoreTable();

        // 查询数据
        StringContentGlobalRecordAssociation association = moreTable
            .select(new StringContentGlobalRecordAssociation(), condition).get(0);

        // 创建更新缓存
        GlobalDataRecord gdrDataInDb = association.getGlobalDataRecords().get(0);

        StringContent data2update = new StringContent();
        data2update.setGlobalId(gdrDataInDb.getId());
        data2update.setContent(value);
        data2update.setContentLength((short) value.length());

        GlobalDataRecord gdrData2update = new GlobalDataRecord();
        gdrData2update.setId(gdrDataInDb.getId());
        gdrData2update.setUpdateTime(nowTime);
        gdrData2update.setModifiedCount(gdrDataInDb.getModifiedCount() + 1);

        // 更新数据
        int affectedRowCount = 0;
        // 更新 全局ID表
        affectedRowCount = suid.update(gdrData2update);
        if (affectedRowCount == 1) {
            // 更新 字符串表
            affectedRowCount = suid.update(data2update);
            if (affectedRowCount != 1) {
                throw getUpdateShouldBe1Exception(
                    affectedRowCount,
                    CoreTableNames.STRING_CONTENT.getName()
                );
            }
        } else {
            throw getUpdateShouldBe1Exception(
                affectedRowCount,
                CoreTableNames.GLOBAL_DATA_RECORD.getName()
            );
        }
    }

    @Override
    public void updateStringByGlobalId(String value, Long globalId) throws TableOperationException {
        Condition condition = new ConditionImpl();
        condition.forUpdate().op(StringContentConstant.GLOBAL_ID, Op.eq, globalId);
        updateStringByCondition(value, condition);
    }

    public void updateStringByFullText(String oldValue, String value) throws TableOperationException {
        Condition condition = new ConditionImpl();
        condition.forUpdate().op(StringContentConstant.DB_CONTENT, Op.eq, oldValue);
        updateStringByCondition(value, condition);
    }

    protected StringContent selectOneStringByCondition(Condition condition) {
        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
        // 查询数据
        List<StringContent> stringContents = suid.select(new StringContent(), condition);
        return stringContents.size() == 1 ? stringContents.get(0) : null;
    }

    @Override
    public StringContent selectStringByFullText(String value, String[] fields) {
        Condition condition = new ConditionImpl();
        if (fields != null) {
            condition.selectField(fields);
        }
        condition.op(StringContentConstant.CONTENT, Op.eq, value);
        // 查询数据
        return selectOneStringByCondition(condition);
    }

    @Override
    public List<StringContent> selectStringByPrefix(String prefix, String[] fields) {
        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
        Condition condition = new ConditionImpl();
        condition.op(
            StringContentConstant.DB_CONTENT,
            Op.like,
            StringEscapeUtils.escapeSql4Like("mysql", prefix) + "%"
        );

        if (fields != null) {
            condition.selectField(fields);
        }

        return suid.select(new StringContent(), condition);
    }

    @Override
    public StringContent selectById(Long globalId, String[] fields) {
        Condition condition = new ConditionImpl();
        if (fields != null) {
            condition.selectField(fields);
        }
        condition.op(StringContentConstant.GLOBAL_ID, Op.eq, globalId);
        return selectOneStringByCondition(condition);
    }

    @Override
    public Long selectId(String value) {
        Condition condition = new ConditionImpl();
        condition.selectField(StringContentConstant.GLOBAL_ID);
        condition.op(StringContentConstant.CONTENT, Op.eq, value);
        // 查询数据
        StringContent stringContent = selectOneStringByCondition(condition);
        return stringContent == null ? null : stringContent.getGlobalId();
    }

    @Override
    public void deleteStringByFullText(String value) throws TableOperationException {
        // 预备一个 StringContent对象 空间
        StringContent stringContent = selectStringByFullText(value, null);
        delete(stringContent);
    }

    @Override
    public void deleteById(Long globalId) throws TableOperationException {
        StringContent stringContent = new StringContent();
        stringContent.setGlobalId(globalId);
        delete(stringContent);
    }

    @Override
    public void delete(StringContent value) throws TableOperationException {
        SuidRich suidRich = getSuidRich();
        // 删除数据
        int affectedRowCount = suidRich.delete(value);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.STRING_CONTENT.getName());

        GlobalDataRecord globalDataRecord = new GlobalDataRecord();
        globalDataRecord.setId(value.getGlobalId());
        affectedRowCount = suidRich.delete(globalDataRecord);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.GLOBAL_DATA_RECORD.getName());
    }
}
