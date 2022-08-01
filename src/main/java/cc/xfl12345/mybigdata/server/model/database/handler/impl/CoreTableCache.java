package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.BooleanContent;
import cc.xfl12345.mybigdata.server.model.database.table.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import cc.xfl12345.mybigdata.server.pojo.StringIdCache;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.Condition;
import org.teasoft.bee.osql.Op;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.ConditionImpl;
import org.teasoft.honey.osql.core.SessionFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
public class CoreTableCache implements InitializingBean {
    @Getter
    protected StringIdCache tableNameCache;

    @Getter
    protected Long idOfTrue;

    @Getter
    protected Long idOfFalse;

    @Getter
    protected Long idOfNumberStringGroup;

    public CoreTableCache() {
        tableNameCache = new StringIdCache(6);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        refreshNumberStringGroupCache();
        refreshBooleanCache();
        refreshCoreTableNameCache();
    }

    public void refreshNumberStringGroupCache() throws Exception {
        // 开启事务
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();

            // 查询数据
            GlobalDataRecord globalDataRecord = new GlobalDataRecord();
            globalDataRecord.setUuid("00004000-cb7a-11eb-0000-f828196a1686");
            globalDataRecord = suid.selectOne(globalDataRecord);
            idOfNumberStringGroup = globalDataRecord.getId();

            transaction.commit();
        } catch (BeeException e) {
            log.error(e.getMessage());
            transaction.rollback();
            throw e;
        }
        log.info("Cache \"global_id\" for \"number in string\" group <---> " + idOfNumberStringGroup);
    }

    public void refreshBooleanCache() throws Exception {
        // 开启事务
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();

            // 查询数据
            List<BooleanContent> booleanContents = suid.select(new BooleanContent());
            for (BooleanContent booleanContent : booleanContents) {
                if (booleanContent.getContent()) {
                    idOfTrue = booleanContent.getGlobalId();
                } else {
                    idOfFalse = booleanContent.getGlobalId();
                }
            }

            transaction.commit();
        } catch (BeeException e) {
            log.error(e.getMessage());
            transaction.rollback();
            throw e;
        }
        log.info("Cache \"global_id\" for JSON constant - boolean value: true <---> " + idOfTrue);
        log.info("Cache \"global_id\" for JSON constant - boolean value: false <---> " + idOfFalse);
    }

    protected void refreshCoreTableNameCache(String... values) throws Exception {
        Condition condition = new ConditionImpl();
        StringBuilder stringBuilder = new StringBuilder(values.length * 32);
        for (String s : values) {
            stringBuilder.append(s).append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        condition.selectField(StringContentConstant.GLOBAL_ID, StringContentConstant.CONTENT).op(StringContentConstant.CONTENT, Op.in, stringBuilder.toString());

        // 开启事务
        Transaction transaction = SessionFactory.getTransaction();
        try {
            transaction.begin();
            transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
            SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();

            // 查询数据
            List<StringContent> contents = suid.select(new StringContent(), condition);
            if (contents.size() != values.length) {
                HashSet<String> actuallyGet = new HashSet<>(contents.stream().map(StringContent::getContent).toList());
                HashSet<String> missing = new HashSet<>(List.of(values));
                missing.removeAll(actuallyGet);
                throw new IllegalArgumentException("We are looking for " + values.length + " records. " +
                    "We get table name data in follow: " +
                    JSONObject.toJSONString(contents, JSONWriter.Feature.PrettyFormat) + ". " +
                    "It is missing " + missing + "."
                );
            }

            for (StringContent content : contents) {
                tableNameCache.put(content.getContent(), content.getGlobalId());
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public void refreshCoreTableNameCache() throws Exception {
        String[] tableNames = Arrays.stream(CoreTableNames.values())
            .map(CoreTableNames::getName)
            .toArray(String[]::new);
        refreshCoreTableNameCache(tableNames);
        log.info("Cache \"global_id\" for core table name: " + JSONObject.toJSONString(tableNameCache.getKey2ValueMap(), JSONWriter.Feature.PrettyFormat));
    }
}
