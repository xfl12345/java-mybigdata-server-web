package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.KeyWords;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import cc.xfl12345.mybigdata.server.pojo.StringIdCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.teasoft.bee.osql.BeeException;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.bee.osql.transaction.Transaction;
import org.teasoft.bee.osql.transaction.TransactionIsolationLevel;
import org.teasoft.honey.osql.core.BeeFactory;
import org.teasoft.honey.osql.core.HoneyFactory;
import org.teasoft.honey.osql.core.SessionFactory;

@Slf4j
public class CoreTableCache {
    @Getter
    protected StringIdCache tableNameCache;

    @Getter
    protected StringIdCache jsonContants;

    public CoreTableCache() throws Exception {
        tableNameCache = new StringIdCache(6);
        jsonContants = new StringIdCache(2);
        refreshJsonContants();
        refreshCoreTableNameCache();
    }

    public Long selectStringByFullText(String content) throws BeeException {
        // 预备一个 StringContent对象 空间
        StringContent searchStringContent = new StringContent();
        searchStringContent.setContent(content);

        // 开启事务，防止 global_id 冲突
        Transaction transaction = SessionFactory.getTransaction();
        transaction.setTransactionIsolation(TransactionIsolationLevel.TRANSACTION_REPEATABLE_READ);
        try {
            transaction.begin();
            HoneyFactory honeyFactory = BeeFactory.getHoneyFactory();
            SuidRich suid = honeyFactory.getSuidRich();

            // 查询数据
            searchStringContent = suid.selectOne(searchStringContent);
            transaction.commit();
        } catch (BeeException e) {
            log.error(e.getMessage());
            transaction.rollback();
            throw e;
        }

        return searchStringContent.getGlobalId();
    }

    protected void refreshMapByName(StringIdCache cache, String name) throws Exception {
        cache.put(name, selectStringByFullText(name));
    }

    protected void refreshCoreTableNameCache() throws Exception {
        refreshMapByName(tableNameCache, CoreTableNames.TABLE_NAME_GLOBAL_DATA_RECORD);
        refreshMapByName(tableNameCache, CoreTableNames.TABLE_NAME_TABLE_SCHEMA_RECORD);
        refreshMapByName(tableNameCache, CoreTableNames.TABLE_NAME_STRING_CONTENT);
        refreshMapByName(tableNameCache, CoreTableNames.TABLE_NAME_INTEGER_CONTENT);
        refreshMapByName(tableNameCache, CoreTableNames.TABLE_NAME_GROUP_RECORD);
        refreshMapByName(tableNameCache, CoreTableNames.TABLE_NAME_GROUP_CONTENT);
    }

    protected void refreshJsonContants() throws Exception{
        refreshMapByName(jsonContants, KeyWords.KEY_WORD_TRUE);
        refreshMapByName(jsonContants, KeyWords.KEY_WORD_FALSE);
    }
}
