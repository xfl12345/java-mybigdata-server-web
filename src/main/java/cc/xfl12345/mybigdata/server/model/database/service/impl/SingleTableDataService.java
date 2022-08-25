package cc.xfl12345.mybigdata.server.model.database.service.impl;

import cc.xfl12345.mybigdata.server.model.database.service.DataService;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.TableCurdMapper;

import java.util.List;

public abstract class SingleTableDataService<IdType, ValueType, PojoType, ConditionType> implements DataService<IdType, ValueType> {
    public abstract <MapperType extends TableCurdMapper<IdType, PojoType, ConditionType>> MapperType getMapper();

    protected abstract PojoType getPojo(ValueType valueType);
    protected abstract ValueType getValue(PojoType pojoType);
    protected abstract String[] getSelectContentFieldOnly();

    @Override
    public IdType insertAndReturnId(ValueType value) throws Exception {
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
    public IdType selectId(ValueType value) throws Exception {
        return getMapper().selectId(getPojo(value));
    }

    @Override
    public ValueType selectById(IdType globalId) throws Exception {
        return getValue(getMapper().selectById(globalId, getSelectContentFieldOnly()));
    }

    @Override
    public void updateById(ValueType value, IdType globalId) throws Exception {
        getMapper().updateById(getPojo(value), globalId);
    }

    @Override
    public void deleteById(IdType globalId) throws Exception {
        getMapper().deleteById(globalId);
    }
}
