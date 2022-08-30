package cc.xfl12345.mybigdata.server.model.database.table.mapper.orm.bee;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractTypedTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.orm.bee.config.BeeTableMapperConfig;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.orm.bee.config.BeeTableMapperConfigGenerator;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import lombok.Getter;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.util.Date;
import java.util.List;

public abstract class AbstractBeeTableMapper<TablePojoType>
    extends AbstractTypedTableMapper<TablePojoType> implements BeeTableMapper<TablePojoType> {
    @Getter
    protected BeeTableMapperConfig<TablePojoType> mapperConfig;

    public void setMapperConfig(BeeTableMapperConfig<TablePojoType> mapperConfig) {
        this.mapperConfig = mapperConfig;
    }

    protected String[] selectIdFieldOnly;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (mapperConfig == null) {
            mapperConfig = BeeTableMapperConfigGenerator.getConfig(getTablePojoType());
        }

        selectIdFieldOnly = new String[]{ mapperConfig.getIdFieldName() };
    }

    @Override
    public long insert(TablePojoType value) throws Exception {
        return getSuidRich().insert(value);
    }

    @Override
    public long insertBatch(List<TablePojoType> values) throws Exception {
        return getSuidRich().insert(values);
    }

    @Override
    public Object insertAndReturnId(TablePojoType value) throws Exception {
        return getSuidRich().insertAndReturnId(value);
    }

    @Override
    public List<TablePojoType> select(Condition condition) throws Exception {
        return getSuidRich().select(mapperConfig.getNewPojoInstance(), condition);
    }

    @Override
    public TablePojoType selectOne(TablePojoType value, String[] fields) throws Exception {
        List<TablePojoType> items = getSuidRich().select(value, getConditionWithSelectedFields(fields));
        if (items.size() != 1) {
            throw getAffectedRowShouldBe1Exception(items.size(), CURD.RETRIEVE, mapperConfig.getTableName());
        }

        return items.get(0);
    }

    @Override
    public TablePojoType selectById(Object globalId, String[] fields) throws Exception {
        Condition condition = getConditionWithSelectedFields(fields);
        addId2Condition(condition, globalId);
        List<TablePojoType> items = getSuidRich().select(mapperConfig.getNewPojoInstance(), condition);
        if (items.size() != 1) {
            throw getAffectedRowShouldBe1Exception(items.size(), CURD.RETRIEVE, mapperConfig.getTableName());
        }

        return items.get(0);
    }

    @Override
    public Object selectId(TablePojoType value) throws Exception {
        TablePojoType item = selectOne(value, selectIdFieldOnly);
        return mapperConfig.getId(item);
    }

    @Override
    public long update(TablePojoType value, Condition condition) throws Exception {
        return getSuidRich().update(value, condition);
    }

    @Override
    public void updateById(TablePojoType value, Object globalId) throws Exception {
        long affectedRowCount = 0;
        affectedRowCount = getSuidRich().update(value, getConditionWithId(globalId));
        if (affectedRowCount != 1) {
            throw getUpdateShouldBe1Exception(affectedRowCount, mapperConfig.getTableName());
        }
    }

    @Override
    public long delete(Condition condition) throws Exception {
        return getSuidRich().delete(condition);
    }

    @Override
    public void deleteById(Object globalId) throws Exception {
        long affectedRowCount = 0;
        affectedRowCount = getSuidRich().delete(mapperConfig.getNewPojoInstance(), getConditionWithId(globalId));
        if (affectedRowCount != 1) {
            throw getAffectedRowShouldBe1Exception(affectedRowCount, CURD.DELETE, mapperConfig.getTableName());
        }
    }

    @Override
    public GlobalDataRecord getNewRegisteredGlobalDataRecord(Date createTime, Object tableNameId) {
        GlobalDataRecord globalDataRecord = getNewGlobalDataRecord(createTime, tableNameId);
        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
        Object id = suid.insertAndReturnId(globalDataRecord);
        globalDataRecord.setId(idTypeConverter.cast(id));
        return globalDataRecord;
    }

    public SuidRich getSuidRich() {
        return BeeFactory.getHoneyFactory().getSuidRich();
    }

    @Override
    public Condition getConditionWithSelectedFields(String... fields) {
        Condition condition = new ConditionImpl();
        addFields2Condition(condition, fields);
        return condition;
    }

    @Override
    public void addFields2Condition(Condition condition, String... fields) {
        if (fields != null) {
            condition.selectField(fields);
        }
    }

    @Override
    public Condition getConditionWithId(Object id) {
        Condition condition = new ConditionImpl();
        addId2Condition(condition, id);
        return condition;
    }

    @Override
    public void addId2Condition(Condition condition, Object id) {
        condition.op(mapperConfig.getIdFieldName(), Op.eq, id);
    }
}
