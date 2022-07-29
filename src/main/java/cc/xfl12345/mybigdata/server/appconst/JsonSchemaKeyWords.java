package cc.xfl12345.mybigdata.server.appconst;

public enum JsonSchemaKeyWords {
    SCHEMA("$schema"),

    ID("$id"),

    TITLE("title"),

    DESCRIPTION("description"),

    PROPERTIES("properties"),

    TYPE_NULL("null"),

    TYPE_BOOLEAN("boolean"),

    TYPE_OBJECT("object"),

    TYPE_ARRAY("array"),

    TYPE_NUMBER("number"),

    TYPE_STRING("string");

    JsonSchemaKeyWords(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
