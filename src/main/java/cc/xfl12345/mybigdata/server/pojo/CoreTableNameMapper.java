package cc.xfl12345.mybigdata.server.pojo;

import lombok.Getter;
import lombok.Setter;

public class CoreTableNameMapper {
    @Getter
    @Setter
    protected CoreTableSubTableName globalRecord;

    @Getter
    @Setter
    protected CoreTableSubTableName tableSchemaRecord;

    @Getter
    @Setter
    protected CoreTableSubTableName stringType;

    @Getter
    @Setter
    protected CoreTableSubTableName integerNumberType;

    @Getter
    @Setter
    protected CoreTableSubTableName groupType;

    public CoreTableNameMapper() {
        globalRecord = new CoreTableSubTableName();
        globalRecord.setRecord("global_data_record");

        tableSchemaRecord = new CoreTableSubTableName();
        tableSchemaRecord.setRecord("table_schema_record");

        stringType = new CoreTableSubTableName();
        stringType.setContent("string_content");

        integerNumberType = new CoreTableSubTableName();
        integerNumberType.setContent("integer_content");

        groupType = new CoreTableSubTableName();
        groupType.setRecord("group_record");
        groupType.setContent("group_content");
    }
}
