package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.model.database.error.TableDataException;
import cc.xfl12345.mybigdata.server.model.database.handler.DataHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleTableDataHandler<IdType, ValueType, PojoType> extends BaseDataHandler {
    @Getter
    @Setter
    protected DataHandler<IdType, PojoType> dataHandler;

    protected abstract PojoType getPojo(ValueType value) throws TableDataException;
    protected abstract ValueType getValue(PojoType pojo) throws Exception;

    @SuppressWarnings("unchecked")
    public SingleTableDataHandler() {
        insertAndReturnId.setDefaultAction(valueType -> dataHandler.insertAndReturnId(getPojo((ValueType) valueType)));
        insert.setDefaultAction(valueType -> dataHandler.insert(getPojo((ValueType) valueType)));
        insertBatch.setDefaultAction(param -> {
            List<ValueType> values = (List<ValueType>) param;
            ArrayList<PojoType> list = new ArrayList<>((values.size() * 3) >> 1);
            for (ValueType value: values) {
                list.add(getPojo(value));
            }

            return dataHandler.insertBatch(list);
        });
        selectId.setDefaultAction(valueType -> dataHandler.selectId(getPojo((ValueType) valueType)));
        selectById.setDefaultAction(idType -> getValue(dataHandler.selectById((IdType) idType)));
        updateById.setDefaultAction(param -> {
            IdAndValue<IdType, ValueType> idAndValue = (IdAndValue<IdType, ValueType>) param;
            dataHandler.updateById(getPojo(idAndValue.value), idAndValue.id);
            return null;
        });
        deleteById.setDefaultAction(param -> {
            dataHandler.deleteById((IdType) param);
            return null;
        });
    }
}
