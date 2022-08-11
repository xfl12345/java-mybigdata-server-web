package cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.appconst.SimpleCoreTableCurdResult;
import cc.xfl12345.mybigdata.server.model.database.error.BeeOrmExceptionHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.BeeOrmCurdHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.ConditionSweet;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.TableCurdHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.util.Date;
import java.util.List;

public abstract class BeeOrmCoreTableHandler<TablePojoType>
    extends AbstractCoreTableHandler implements BeeOrmCurdHandler<TablePojoType> {

    protected abstract String getTableName();

    protected abstract String getIdFieldName();

    protected abstract Long getId(TablePojoType value);

    protected abstract TablePojoType getNewPojoInstance();

    @Getter
    protected BeeOrmExceptionHandler beeOrmExceptionHandler;

    @Autowired
    public void setBeeOrmExceptionHandler(BeeOrmExceptionHandler beeOrmExceptionHandler) {
        this.beeOrmExceptionHandler = beeOrmExceptionHandler;
    }

    protected String[] selectIdFieldOnly;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (beeOrmExceptionHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("beeOrmExceptionHandler"));
        }

        selectIdFieldOnly = new String[]{getIdFieldName()};
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
    public Long insertAndReturnId(TablePojoType value) throws Exception {
        return getSuidRich().insertAndReturnId(value);
    }

    @Override
    public List<TablePojoType> select(Condition condition) throws Exception {
        return getSuidRich().select(getNewPojoInstance(), condition);
    }

    @Override
    public TablePojoType selectOne(TablePojoType value, String[] fields) throws Exception {
        List<TablePojoType> items = getSuidRich().select(value, getConditionWithSelectedFields(fields));
        if (items.size() != 1) {
            throw getAffectedRowShouldBe1Exception(items.size(), CURD.RETRIEVE, getTableName());
        }

        return items.get(0);
    }

    @Override
    public TablePojoType selectById(Long globalId, String[] fields) throws Exception {
        Condition condition = getConditionWithSelectedFields(fields);
        addId2Condition(condition, globalId);
        List<TablePojoType> items = getSuidRich().select(getNewPojoInstance(), condition);
        if (items.size() != 1) {
            throw getAffectedRowShouldBe1Exception(items.size(), CURD.RETRIEVE, getTableName());
        }

        return items.get(0);
    }

    @Override
    public Long selectId(TablePojoType value) throws Exception {
        TablePojoType item = selectOne(value, selectIdFieldOnly);
        return getId(item);
    }

    @Override
    public Long insertOrSelect4Id(TablePojoType value) throws Exception {
        Long id;

        try {
            id = insert(value);
        } catch (BeeException beeException) {
            SimpleCoreTableCurdResult curdResult = getBeeOrmExceptionHandler().getSimpleCoreTableCurdResult(beeException);
            if (curdResult.equals(SimpleCoreTableCurdResult.DUPLICATE)) {
                id = selectId(value);
            } else {
                throw beeException;
            }
        }

        return id;
    }

    @Override
    public long update(TablePojoType value, Condition condition) throws Exception {
        return getSuidRich().update(value, condition);
    }

    @Override
    public void updateById(TablePojoType value, Long globalId) throws Exception {
        long affectedRowCount = 0;
        affectedRowCount = getSuidRich().update(value, getConditionWithId(globalId));
        if (affectedRowCount != 1) {
            throw getUpdateShouldBe1Exception(affectedRowCount, getTableName());
        }
    }

    @Override
    public long delete(Condition condition) throws Exception {
        return getSuidRich().delete(condition);
    }

    @Override
    public void deleteById(Long globalId) throws Exception {
        long affectedRowCount = 0;
        affectedRowCount = getSuidRich().delete(getNewPojoInstance(), getConditionWithId(globalId));
        if (affectedRowCount != 1) {
            throw getAffectedRowShouldBe1Exception(affectedRowCount, CURD.DELETE, getTableName());
        }
    }

    @Override
    public GlobalDataRecord getNewRegisteredGlobalDataRecord(Date createTime, Long tableNameId) {
        GlobalDataRecord globalDataRecord = getNewGlobalDataRecord(createTime, tableNameId);
        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
        Long id = suid.insertAndReturnId(globalDataRecord);
        globalDataRecord.setId(id);
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
    public Condition getConditionWithId(Long id) {
        Condition condition = new ConditionImpl();
        addId2Condition(condition, id);
        return condition;
    }

    @Override
    public void addId2Condition(Condition condition, Long id) {
        condition.op(getIdFieldName(), Op.eq, id);
    }
}
