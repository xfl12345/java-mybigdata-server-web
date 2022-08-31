package cc.xfl12345.mybigdata.server.model.database.table.converter;

public class IdTypeConverter<IdType> {
    protected Class<IdType> idTypeClass;

    protected IdType id;

    public IdTypeConverter(Class<IdType> cls) {
        idTypeClass = cls;
    }

    public IdType cast(Object id) {
        return idTypeClass.cast(id);
    }
}
