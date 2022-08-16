package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.model.database.handler.DataHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public abstract class SingleTableDataHandler<IdType, ValueType, PojoType> extends BaseDataHandler<IdType, ValueType> {
    @Getter
    @Setter
    protected DataHandler<IdType, PojoType> dataHandler;

    protected abstract PojoType getPojo(ValueType value) throws TableDataException;
    protected abstract ValueType getValue(PojoType pojo) throws Exception;

    public SingleTableDataHandler() {
        insertAndReturnId.setDefaultAction(valueType -> dataHandler.insertAndReturnId(getPojo(valueType)));
        insert.setDefaultAction(valueType -> dataHandler.insert(getPojo(valueType)));
        insertBatch.setDefaultAction(values-> {
            ArrayList<PojoType> list = new ArrayList<>((values.size() * 3) >> 1);
            for (ValueType value: values) {
                list.add(getPojo(value));
            }

            return dataHandler.insertBatch(list);
        });
        selectId.setDefaultAction(valueType -> dataHandler.selectId(getPojo(valueType)));
        selectById.setDefaultAction(idType -> getValue(dataHandler.selectById(idType)));
        updateById.setDefaultAction(idAndValue -> {
            dataHandler.updateById(getPojo(idAndValue.value), idAndValue.id);
            return null;
        });
        deleteById.setDefaultAction(idType -> {
            dataHandler.deleteById(idType);
            return null;
        });
    }
}
