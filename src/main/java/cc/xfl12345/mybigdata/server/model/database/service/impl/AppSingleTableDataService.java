package cc.xfl12345.mybigdata.server.model.database.service.impl;

import cc.xfl12345.mybigdata.server.model.database.service.AppDataService;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.AppTableCurdMapper;

import java.util.List;

public abstract class AppSingleTableDataService<ValueType, PojoType> implements AppDataService<ValueType> {
    public abstract AppTableCurdMapper<PojoType> getMapper();

    protected abstract String[] getSelectContentFieldOnly();

    protected abstract ValueType getValue(PojoType pojoType);

    protected abstract PojoType getPojo(ValueType valueType);

    @Override
    public Object insertAndReturnId(ValueType value) throws Exception {
        return getMapper().insertAndReturnId(getPojo(value));
    }

    @Override
    public long insert(ValueType value) throws Exception {
        return getMapper().insert(getPojo(value));
    }

    @Override
    public long insertBatch(List<ValueType> values) throws Exception {
        return getMapper().insertBatch(values.parallelStream().map(this::getPojo).toList());
    }

    @Override
    public Object selectId(ValueType value) throws Exception {
        return getMapper().selectId(getPojo(value));
    }

    @Override
    public ValueType selectById(Object globalId) throws Exception {
        return getValue(getMapper().selectById(globalId, getSelectContentFieldOnly()));
    }

    @Override
    public void updateById(ValueType value, Object globalId) throws Exception {
        getMapper().updateById(getPojo(value), globalId);
    }

    @Override
    public void deleteById(Object globalId) throws Exception {
        getMapper().deleteById(globalId);
    }
}
