package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.appconst.CoreTables;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.table.constant.GlobalDataRecordConstant;
import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.BooleanContent;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import cc.xfl12345.mybigdata.server.pojo.StringIdCache;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.mongodb.TransactionOptions;
import dev.morphia.Datastore;
import dev.morphia.experimental.MorphiaSession;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
public class CoreTableCache implements InitializingBean {

    @Getter
    protected Datastore datastore;

    @Autowired
    public void setDatastore(Datastore datastore) {
        this.datastore = datastore;
    }

    @Getter
    protected TransactionOptions transactionOptions;

    @Resource(name = "mongodbDefaultTransactionOptions")
    public void setTransactionOptions(TransactionOptions transactionOptions) {
        this.transactionOptions = transactionOptions;
    }

    @Getter
    protected StringIdCache tableNameCache;

    @Getter
    protected ObjectId idOfTrue;

    @Getter
    protected ObjectId idOfFalse;

    @Getter
    protected ObjectId idOfNumberStringGroup;

    public CoreTableCache() {
        tableNameCache = new StringIdCache(6);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // datastore.find(StringContent.class).update().execute().
        // datastore.

        refreshNumberStringGroupCache();
        refreshBooleanCache();
        refreshCoreTableNameCache();
    }

    public void refreshNumberStringGroupCache() throws Exception {
        MorphiaSession session = datastore.startSession();
        try {
            session.startTransaction(transactionOptions);
            // 查询数据
            Query<GlobalDataRecord> query = session.find(GlobalDataRecord.class).filter(
                Filters.eq(GlobalDataRecordConstant.UUID, "00004000-cb7a-11eb-0000-f828196a1686")
            );

            int count = (int) query.count();
            if (count == 1) {
                idOfNumberStringGroup = query.first().getId();
            } else {
                throw new TableOperationException(
                    "Cache \"global_id\" for \"number in string\" group should be unique.",
                    count,
                    CURD.RETRIEVE,
                    CoreTableNames.GLOBAL_DATA_RECORD
                );
            }

            session.commitTransaction();
        } catch (Exception e) {
            log.error(e.getMessage());
            session.abortTransaction();
            throw e;
        } finally {
            session.close();
        }
        log.info("Cache \"global_id\" for \"number in string\" group <---> " + idOfNumberStringGroup);
    }

    public void refreshBooleanCache() throws Exception {
        MorphiaSession session = datastore.startSession();
        try {
            session.startTransaction(transactionOptions);
            // 查询数据
            Query<BooleanContent> query = session.find(BooleanContent.class);

            int count = (int) query.count();
            if (count == 2) {
                BooleanContent[] booleanContents =  query.stream().toArray(BooleanContent[]::new);
                for (BooleanContent booleanContent : booleanContents) {
                    if (booleanContent.getContent()) {
                        idOfTrue = booleanContent.getGlobalId();
                    } else {
                        idOfFalse = booleanContent.getGlobalId();
                    }
                }
            } else {
                throw new TableOperationException(
                    "Cache \"global_id\" for \"boolean value\"  should only have two value.",
                    count,
                    CURD.RETRIEVE,
                    CoreTableNames.BOOLEAN_CONTENT
                );
            }

            session.commitTransaction();
        } catch (Exception e) {
            log.error(e.getMessage());
            session.abortTransaction();
            throw e;
        } finally {
            session.close();
        }

        log.info("Cache \"global_id\" for JSON constant - boolean value: true <---> " + idOfTrue);
        log.info("Cache \"global_id\" for JSON constant - boolean value: false <---> " + idOfFalse);
    }

    protected void refreshCoreTableNameCache(String... values) throws Exception {
        MorphiaSession session = datastore.startSession();
        try {
            session.startTransaction(transactionOptions);
            // 查询数据
            Query<StringContent> query = session.find(StringContent.class).filter(
                Filters.in(
                    StringContentConstant.DB_CONTENT,
                    Arrays.stream(values).toList()
                )
            );

            List<StringContent> contents = query.stream().toList();

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

            session.commitTransaction();
        } catch (Exception e) {
            log.error(e.getMessage());
            session.abortTransaction();
            throw e;
        } finally {
            session.close();
        }
    }

    public void refreshCoreTableNameCache() throws Exception {
        String[] tableNames = Arrays.stream(CoreTables.values())
            .map(CoreTables::getName)
            .toArray(String[]::new);
        refreshCoreTableNameCache(tableNames);
        log.info("Cache \"global_id\" for core table name: " + JSONObject.toJSONString(tableNameCache.getKey2ValueMap(), JSONWriter.Feature.PrettyFormat));
    }
}
