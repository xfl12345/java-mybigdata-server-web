package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.JsonSchemaKeyWords;
import cc.xfl12345.mybigdata.server.model.database.constant.ObjectContentConstant;
import cc.xfl12345.mybigdata.server.model.database.constant.ObjectRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.handler.GroupTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.NumberTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.ObjectTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.schema.JSONSchema;
import com.alibaba.fastjson2.schema.ValidateResult;
import lombok.Getter;
import lombok.Setter;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.util.*;

import static cc.xfl12345.mybigdata.server.utility.BeeOrmUtils.getSuidRich;

public class ObjectTypeHandlerImpl extends AbstractCoreTableHandler implements ObjectTypeHandler {
    @Getter
    @Setter
    protected StringTypeHandler stringTypeHandler;

    @Getter
    @Setter
    protected NumberTypeHandler numberTypeHandler;

    @Getter
    @Setter
    protected GroupTypeHandler groupTypeHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (stringTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("stringTypeHandler"));
        }
        if (numberTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("numberTypeHandler"));
        }
        if (groupTypeHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("groupTypeHandler"));
        }
    }


    protected List<ObjectContent> packUpItems(Long globalId, JSONObject jsonObject, JSONObject jsonSchema) throws Exception {
        int itemCount = jsonObject.size();
        List<ObjectContent> keyValuePairs = new ArrayList<>(itemCount + 1);
        JSONObject prop = jsonSchema.getJSONObject(JsonSchemaKeyWords.PROPERTIES.getName());
        for (String key : jsonObject.keySet()) {
            Object theValue = jsonObject.get(key);

            Long valueId = null;





            ObjectContent objectContent = new ObjectContent();
            objectContent.setGlobalId(globalId);
            objectContent.setTheKey(stringTypeHandler.insertOrSelect4Id(key));
            objectContent.setTheValue(valueId);
            keyValuePairs.add(objectContent);
        }
        return keyValuePairs;
    }



    @Override
    public Long insert(String name, Object obj, Long jsonSchemaId) throws Exception {
        Date date = new Date();

        Long globalId = null;
        SuidRich suidRich = getSuidRich();

        JSONObject jsonObject = (JSONObject) JSON.toJSON(obj);

        TableSchemaRecord tableSchemaRecord = suidRich.selectById(new TableSchemaRecord(), jsonSchemaId);
        JSONObject jsonSchemaInJson = JSONObject.parseObject(tableSchemaRecord.getJsonSchema());

        //TODO 注入 ID 和 schema URL

        JSONSchema jsonSchema = JSONSchema.parseSchema(jsonSchemaInJson.toJSONString());
        ValidateResult validateResult = jsonSchema.validate(jsonObject);
        if (validateResult.isSuccess()) {
            GlobalDataRecord globalDataRecord = getNewRegisteredGlobalDataRecord(
                date,
                coreTableCache.getTableNameCache().getValue(
                    CoreTableNames.OBJECT_RECORD.getName()
                )
            );
            globalId = globalDataRecord.getId();

            ObjectRecord objectRecord = new ObjectRecord();
            if (name != null) {
                objectRecord.setObjectName(stringTypeHandler.insertOrSelect4Id(name));
            }
            objectRecord.setObjectSchema(jsonSchemaId);
            suidRich.insert(objectRecord);

            List<ObjectContent> objectContents = packUpItems(
                globalId,
                jsonObject,
                jsonSchemaInJson
            );
            int itemCount = objectContents.size();
            int affectedRowCount = suidRich.insert(objectContents);
            if (affectedRowCount != itemCount) {
                throw getAffectedRowsCountDoesNotMatch(
                    affectedRowCount,
                    CURD.CREATE,
                    CoreTableNames.OBJECT_CONTENT.getName()
                );
            }
        }

        return globalId;
    }

    @Override
    public JSONObject select(Long globalId) {
        return null;
    }

    @Override
    public void update(Map<String, Long> keyValuePairs, Long globalId) throws TableOperationException {

    }

    @Override
    public void update(Collection<ObjectContent> keyValuePairs, Long globalId) throws TableOperationException {

    }

    @Override
    public void deleteById(Long globalId) throws Exception {
        int affectedRowCount = 0;
        SuidRich suidRich = getSuidRich();
        Condition condition;

        condition = new ConditionImpl();
        condition.op(ObjectContentConstant.GLOBAL_ID, Op.eq, globalId);
        suidRich.delete(new ObjectContent(), condition);

        condition = new ConditionImpl();
        condition.op(ObjectRecordConstant.GLOBAL_ID, Op.eq, globalId);
        affectedRowCount = suidRich.delete(new ObjectRecord(), condition);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.OBJECT_RECORD.getName());

        affectedRowCount = suidRich.deleteById(GlobalDataRecord.class, globalId);
        checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.GLOBAL_DATA_RECORD.getName());
    }
}
