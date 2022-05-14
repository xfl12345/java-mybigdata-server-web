package cc.xfl12345.mybigdata.server.model.database.table.builder;

import java.io.Serializable;
import javax.persistence.*;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 表名：test_table
*/
@ToString
@SuperBuilder
@Table(name = "`test_table`")
public class TestTable implements Serializable {
    @Id
    @Column(name = "`ID`")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "`num`")
    private Integer num;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return num
     */
    public Integer getNum() {
        return num;
    }

    /**
     * @param num
     */
    public void setNum(Integer num) {
        this.num = num;
    }
}