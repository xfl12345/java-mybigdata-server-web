package cc.xfl12345.mybigdata.server.model.database.constant;

/**
 * 表名：integer_content
*/
@lombok.Data
@javax.persistence.Table(name = "integer_content")
public class IntegerContent {
    /**
     * 当前表所在数据库实例里的全局ID
     */
    @javax.persistence.Column(name = "global_id", nullable = false)
    @javax.persistence.GeneratedValue(generator = "JDBC")
    private Long globalId;

    /**
     * 64位带符号的整型数字
     */
    private Long content;

    public static final String GLOBAL_ID = "globalId";

    public static final String DB_GLOBAL_ID = "global_id";

    public static final String CONTENT = "content";

    public static final String DB_CONTENT = "content";
}
