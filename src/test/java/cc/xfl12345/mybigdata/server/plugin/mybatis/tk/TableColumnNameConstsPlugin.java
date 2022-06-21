package cc.xfl12345.mybigdata.server.plugin.mybatis.tk;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

@Deprecated
public class TableColumnNameConstsPlugin  extends FalseMethodPlugin {

    protected void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            Field field = new Field(
                introspectedColumn.getActualColumnName().toUpperCase(),
                new FullyQualifiedJavaType(String.class.getName())
            );
            field.setVisibility(JavaVisibility.PUBLIC);
            field.setStatic(true);
            field.setFinal(true);
            field.setInitializationString("\"" + introspectedColumn.getJavaProperty() + "\"");
            context.getCommentGenerator().addClassComment(topLevelClass, introspectedTable);
            topLevelClass.addField(field);
            //增加字段名常量,用于pageHelper
            Field columnField = new Field(
                "DB_" + introspectedColumn.getActualColumnName().toUpperCase(),
                new FullyQualifiedJavaType(String.class.getName())
            );
            columnField.setVisibility(JavaVisibility.PUBLIC);
            columnField.setStatic(true);
            columnField.setFinal(true);
            columnField.setInitializationString("\"" + introspectedColumn.getActualColumnName() + "\"");
            topLevelClass.addField(columnField);
        }
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return false;
    }
}
