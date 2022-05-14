package cc.xfl12345.mybigdata.server.model.database.table.builder;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 表名：string_content
*/
@ToString
@SuperBuilder
@Table(name = "`string_content`")
public class StringContent implements Serializable {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @Column(name = "`global_id`")
    private Long globalId;

    /**
     * 字符串结构格式
     */
    @Column(name = "`data_format`")
    private Long dataFormat;

    /**
     * 字符串长度
     */
    @Column(name = "`content_length`")
    private Short contentLength;

    /**
     * 字符串内容，最大长度为 768 个字符
     */
    @Column(name = "`content`")
    private String content;

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
     * 获取字符串结构格式
     *
     * @return dataFormat - 字符串结构格式
     */
    public Long getDataFormat() {
        return dataFormat;
    }

    /**
     * 设置字符串结构格式
     *
     * @param dataFormat 字符串结构格式
     */
    public void setDataFormat(Long dataFormat) {
        this.dataFormat = dataFormat;
    }

    /**
     * 获取字符串长度
     *
     * @return contentLength - 字符串长度
     */
    public Short getContentLength() {
        return contentLength;
    }

    /**
     * 设置字符串长度
     *
     * @param contentLength 字符串长度
     */
    public void setContentLength(Short contentLength) {
        this.contentLength = contentLength;
    }

    /**
     * 获取字符串内容，最大长度为 768 个字符
     *
     * @return content - 字符串内容，最大长度为 768 个字符
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置字符串内容，最大长度为 768 个字符
     *
     * @param content 字符串内容，最大长度为 768 个字符
     */
    public void setContent(String content) {
        this.content = content;
    }
}