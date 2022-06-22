package cc.xfl12345.mybigdata.server.appconst;

public enum NameCase {
    CAMEL_CASE("camelCase"),
    STUDLY_CASE("studlyCase"),
    PASCAL_CASE("pascalCase"),
    KEBAB_CASE("kebabCase"),
    SNAKE_CASE("snakeCase"),
    SWAP_CASE("swapCase"),
    ORIGIN_CASE("originCase");

    private final String name;

    NameCase(String str) {
        this.name = str;
    }

    public String getName() {
        return name;
    }
}
