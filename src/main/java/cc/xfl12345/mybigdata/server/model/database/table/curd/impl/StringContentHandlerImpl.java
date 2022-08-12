package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.StringContentHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import cc.xfl12345.mybigdata.server.utility.StringEscapeUtils;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;

import java.util.List;

public class StringContentHandlerImpl
    extends AbstractAppTableHandler<StringContent>
    implements StringContentHandler {
    @Override
    public Class<StringContent> getTablePojoType() {
        return StringContent.class;
    }

    @Override
    public List<StringContent> selectByPrefix(String prefix, String[] fields) {
        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
        Condition condition = new ConditionImpl();
        condition.op(
            StringContentConstant.CONTENT,
            Op.like,
            StringEscapeUtils.escapeSql4Like("mysql", prefix) + "%"
        );

        if (fields != null) {
            condition.selectField(fields);
        }

        return suid.select(new StringContent(), condition);
    }
}




