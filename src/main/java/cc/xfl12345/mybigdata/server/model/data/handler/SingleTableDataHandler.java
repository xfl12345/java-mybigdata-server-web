package cc.xfl12345.mybigdata.server.model.data.handler;

import cc.xfl12345.mybigdata.server.model.database.service.DataService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class SingleTableDataHandler<ValueType> extends BaseDataHandler {
    @Getter
    @Setter
    protected DataService<ValueType> dataService;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (dataService == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("dataService"));
        }
    }

    @SuppressWarnings("unchecked")
    public SingleTableDataHandler() {
        insertAndReturnId.setDefaultAction(valueType -> dataService.insertAndReturnId((ValueType) valueType));
        insert.setDefaultAction(valueType -> dataService.insert((ValueType) valueType));
        insertBatch.setDefaultAction(param -> dataService.insertBatch((List<ValueType>) param));
        selectId.setDefaultAction(valueType -> dataService.selectId((ValueType) valueType));
        selectById.setDefaultAction(idType -> dataService.selectById(idType));
        updateById.setDefaultAction(param -> {
            IdAndValue<ValueType> idAndValue = (IdAndValue<ValueType>) param;
            dataService.updateById(idAndValue.value, idAndValue.id);
            return null;
        });
        deleteById.setDefaultAction(param -> {
            dataService.deleteById(param);
            return null;
        });
    }
}
