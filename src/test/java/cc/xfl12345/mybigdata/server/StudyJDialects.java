package cc.xfl12345.mybigdata.server;

import com.github.drinkjava2.jdialects.Dialect;
import com.github.drinkjava2.jdialects.model.TableModel;

public class StudyJDialects {
    public static void main(String[] args) {
        TableModel t1 = new TableModel("customers");
        t1.column("global_id").BIGINT().notNull().pkey();
        t1.column("email").STRING(20).pkey().entityField("email").updatable(true).insertable(false);
        t1.column("address").VARCHAR(50).defaultValue("'Beijing'").comment("address comment");
        t1.column("phoneNumber").VARCHAR(50).singleIndex("IDX2");
        t1.column("age").INTEGER().notNull().check("'>0'");
        t1.index("idx3").columns("address", "phoneNumber").unique();
        t1.tableTail("engine=innoDB DEFAULT CHARSET=utf8");


        TableModel t2 = new TableModel("orders").comment("order comment");
        t2.column("id").LONG().autoId().pkey();
        t2.column("name").STRING(20);
        t2.column("email").STRING(20);
        t2.column("name2").STRING(20).pkey().tail(" default 'Sam'");
        t2.column("email2").STRING(20);
        t2.fkey().columns("name2", "email2").refs("customers", "name", "email");
        t2.fkey("fk1").columns("name", "email").refs("customers", "name", "email");
        t2.unique("uk1").columns("name2", "email2");

        // TableModel t3 = new TableModel("sampletable");
        // t3.column("id").LONG().identityId().pkey();
        // t3.tableGenerator("table_gen1", "tb1", "pkcol2", "valcol", "pkval", 1, 10);
        // t3.column("id1").INTEGER().idGenerator("table_gen1");
        // t3.sequenceGenerator("seq1", "seq_1", 1, 1);
        // t3.column("id2").INTEGER().idGenerator("seq1");
        // t3.engineTail(" DEFAULT CHARSET=utf8");

        String[] dropAndCreateDDL = Dialect.MySQL57InnoDBDialect.toDropAndCreateDDL(t1, t2);
        for (String ddl : dropAndCreateDDL)
            System.out.println(ddl);
    }

}
