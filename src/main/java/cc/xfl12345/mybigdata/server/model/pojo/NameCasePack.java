package cc.xfl12345.mybigdata.server.model.pojo;

import strman.Strman;

import java.lang.reflect.Field;
import java.util.HashSet;

public class NameCasePack {
    public String camelCase;

    /**
     * StudlyCase is as known as PascalCase
     */
    public String studlyCase;

    public String kebabCase;

    public String snakeCase;

    public String swapCase;

    public String originCase;

    public final HashSet<String> allNameCase = new HashSet<>();

    public NameCasePack(String name) {
        originCase = name;
        camelCase = Strman.toCamelCase(name);
        studlyCase = Strman.toStudlyCase(camelCase);
        kebabCase = Strman.toKebabCase(camelCase);
        snakeCase = Strman.toSnakeCase(camelCase);
        swapCase = Strman.swapCase(name);

        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                String fieldName = field.getName();
                String fieldSuffix = fieldName.substring(fieldName.length() - 4);
                if (fieldSuffix.equals("Case")) {
                    try {
                        allNameCase.add((String) field.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
