package cc.xfl12345.mybigdata.appconst;

public enum NameCase {
    camelCase("camelCase"),
    studlyCase("studlyCase"),
    pascalCase("pascalCase"),
    kebabCase("kebabCase"),
    snakeCase("snakeCase"),
    swapCase("swapCase"),
    originCase("originCase");

    private final String name;

    NameCase(String str) {
        this.name = str;
    }

    public String getName() {
        return name;
    }
}
