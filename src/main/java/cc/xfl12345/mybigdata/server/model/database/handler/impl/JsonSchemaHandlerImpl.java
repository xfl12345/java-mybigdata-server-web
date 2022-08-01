package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.JsonSchemaKeyWords;
import cc.xfl12345.mybigdata.server.model.database.constant.GroupContentConstant;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.handler.GroupTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.JsonSchemaHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.TableSchemaRecord;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.FunctionType;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static cc.xfl12345.mybigdata.server.utility.BeeOrmUtils.getSuidRich;

public class JsonSchemaHandlerImpl extends AbstractCoreTableHandler implements JsonSchemaHandler {

    @Getter
    @Setter
    protected StringTypeHandler stringTypeHandler;

    @Getter
    @Setter
    protected GroupTypeHandler groupTypeHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (stringTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("stringTypeHandler"));
        }
        if (groupTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("groupTypeHandler"));
        }
    }


    @Override
    public Long insert(JSONObject jsonSchema) throws Exception {
        Date date = new Date();
        GlobalDataRecord globalDataRecord = getNewRegisteredGlobalDataRecord(
            date,
            coreTableCache.getTableNameCache().getValue(
                CoreTableNames.TABLE_SCHEMA_RECORD.getName()
            )
        );

        Long globalId = globalDataRecord.getId();
        String jsonSchemaInString = jsonSchema.toJSONString();

        TableSchemaRecord tableSchemaRecord = new TableSchemaRecord();
        tableSchemaRecord.setGlobalId(globalId);

        HashSet<Long> keys = new HashSet<>(2);
        String title = jsonSchema.getString(JsonSchemaKeyWords.TITLE.getName());
        if (title != null) {
            keys.add(stringTypeHandler.insertOrSelect4Id(title));
        }
        String description = jsonSchema.getString(JsonSchemaKeyWords.DESCRIPTION.getName());
        if (title != null) {
            keys.add(stringTypeHandler.insertOrSelect4Id(description));
        }
        tableSchemaRecord.setSchemaName(groupTypeHandler.insertNewGroup(title, keys));

        tableSchemaRecord.setJsonSchema(jsonSchemaInString);
        tableSchemaRecord.setContentLength((short) jsonSchemaInString.length());


        getSuidRich().insert(tableSchemaRecord);
        return globalId;
    }

    @Override
    public List<TableSchemaRecord> selectJsonSchemaByKeyWords(String... keys) {
        int keysCount = keys.length;

        if (keysCount == 0) {
            return null;
        }

        Condition condition = new ConditionImpl();

        StringBuilder stringBuilder = new StringBuilder(keys.length * 11);
        for (String key : keys) {
            Long stringContentId  = stringTypeHandler.selectId(key);
            if (stringContentId == null) {
                return null;
            }

            stringBuilder.append(stringContentId.toString()).append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        condition
            .op(GroupContentConstant.ITEM, Op.in, stringBuilder.toString())
            .groupBy(GroupContentConstant.GLOBAL_ID)
            .having(FunctionType.COUNT, GroupContentConstant.ITEM, Op.eq, keysCount);

        return getSuidRich().select(new TableSchemaRecord(), condition);
    }

    @Override
    public void update(TableSchemaRecord tableSchemaRecord) throws TableOperationException {
        int affectedRowCount = getSuidRich().update(tableSchemaRecord);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.UPDATE, CoreTableNames.TABLE_SCHEMA_RECORD.getName());
    }

    @Override
    public void deleteById(Long globalId) throws TableOperationException {
        int affectedRowCount = 0;
        SuidRich suidRich = getSuidRich();
        affectedRowCount = suidRich.deleteById(TableSchemaRecord.class, globalId);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.TABLE_SCHEMA_RECORD.getName());

        affectedRowCount = suidRich.deleteById(GlobalDataRecord.class, globalId);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.GLOBAL_DATA_RECORD.getName());
    }
}
