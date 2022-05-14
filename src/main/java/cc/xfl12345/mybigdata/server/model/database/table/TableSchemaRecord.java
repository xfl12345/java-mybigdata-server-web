package cc.xfl12345.mybigdata.server.model.database.table;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;

/**
 * 表名：table_schema_record
*/
@ToString
@Table(name = "`table_schema_record`")
public class TableSchemaRecord implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

    /**
     * 插表模型名称
     */
    @Column(name = "`schema_name`")
    private Long schemaName;

    /**
     * json_schema 字段的长度
     */
    @Column(name = "`content_length`")
    private Short contentLength;

    /**
     * 插表模型
     */
    @Column(name = "`json_schema`")
    private String jsonSchema;

    private static final long serialVersionUID = 1L;

    /**
     * 获取当前表所在数据库实例里的全局ID
     *
     * @return globalId - 当前表所在数据库实例里的全局ID
     */
    public Long getGlobalId() {
        return globalId;
    }

    /**
     * 设置当前表所在数据库实例里的全局ID
     *
     * @param globalId 当前表所在数据库实例里的全局ID
     */
    public void setGlobalId(Long globalId) {
        this.globalId = globalId;
    }

    /**
     * 获取插表模型名称
     *
     * @return schemaName - 插表模型名称
     */
    public Long getSchemaName() {
        return schemaName;
    }

    /**
     * 设置插表模型名称
     *
     * @param schemaName 插表模型名称
     */
    public void setSchemaName(Long schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * 获取json_schema 字段的长度
     *
     * @return contentLength - json_schema 字段的长度
     */
    public Short getContentLength() {
        return contentLength;
    }

    /**
     * 设置json_schema 字段的长度
     *
     * @param contentLength json_schema 字段的长度
     */
    public void setContentLength(Short contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * 获取插表模型
     *
     * @return jsonSchema - 插表模型
     */
    public String getJsonSchema() {
        return jsonSchema;
    }

    /**
     * 设置插表模型
     *
     * @param jsonSchema 插表模型
     */
    public void setJsonSchema(String jsonSchema) {
        this.jsonSchema = jsonSchema;
    }
}