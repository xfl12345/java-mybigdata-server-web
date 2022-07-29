package cc.xfl12345.mybigdata.server.appconst;

public enum CoreTableNames {
    GLOBAL_DATA_RECORD("global_data_record"),
    TABLE_SCHEMA_RECORD("table_schema_record"),
    STRING_CONTENT("string_content"),
    BOOLEAN_CONTENT("boolean_content"),
    INTEGER_CONTENT("integer_content"),
    GROUP_RECORD("group_record"),
    GROUP_CONTENT("group_content"),
    OBJECT_RECORD("object_record"),
    OBJECT_CONTENT("object_content"),

    AUTH_ACCOUNT("auth_account");

    CoreTableNames(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
