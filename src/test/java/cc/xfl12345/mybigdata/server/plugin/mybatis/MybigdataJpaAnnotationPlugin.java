package cc.xfl12345.mybigdata.server.plugin.mybatis;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class MybigdataJpaAnnotationPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType("javax.persistence.*");
        topLevelClass.addAnnotation("@Entity");
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelFieldGenerated(Field field,
                                       TopLevelClass topLevelClass,
                                       IntrospectedColumn introspectedColumn,
                                       IntrospectedTable introspectedTable,
                                       Plugin.ModelClassType modelClassType) {
        if ("globalId".equals(field.getName()) || "id".equals(field.getName())) {
            if (!field.getAnnotations().contains("@Id")) {
                field.addAnnotation("@Id");
            }
        }
        return true;
    }
}
