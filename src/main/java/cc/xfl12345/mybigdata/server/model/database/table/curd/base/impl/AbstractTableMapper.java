package cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class AbstractTableMapper implements DisposableBean, InitializingBean {
    @Getter
    @Setter
    protected String fieldCanNotBeNullMessageTemplate = "Property [%s] can not be null!";
    @Getter
    @Setter
    protected String messageAffectedRowShouldBe1 = "Affected row count should be 1.";

    @Getter
    @Setter
    protected String messageAffectedRowsCountDoesNotMatch = "Affected rows count does not match.";

    @Getter
    protected volatile NoArgGenerator uuidGenerator;

    @Autowired
    public void setUuidGenerator(NoArgGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    @Getter
    protected volatile CoreTableCache coreTableCache = null;

    @Autowired
    public void setCoreTableCache(CoreTableCache coreTableCache) {
        this.coreTableCache = coreTableCache;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (coreTableCache == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("coreTableCache"));
        }
        if (uuidGenerator == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("uuidGenerator"));
        }
    }

    @Override
    public void destroy() throws Exception {
    }

    public String getUuidInString() {
        return uuidGenerator.generate().toString();
    }

    public GlobalDataRecord getNewGlobalDataRecord(Date createTime, Long tableNameId) {
        GlobalDataRecord globalDataRecord = new GlobalDataRecord();
        globalDataRecord.setUuid(getUuidInString());
        globalDataRecord.setCreateTime(createTime);
        globalDataRecord.setUpdateTime(createTime);
        globalDataRecord.setModifiedCount(1L);
        globalDataRecord.setTableName(tableNameId);
        return globalDataRecord;
    }

    public abstract GlobalDataRecord getNewRegisteredGlobalDataRecord(Date createTime, Long tableNameId);

    public TableOperationException getUpdateShouldBe1Exception(long affectedRowsCount, String tableName) {
        return getAffectedRowShouldBe1Exception(
            affectedRowsCount,
            CURD.UPDATE,
            tableName
        );
    }

    public TableOperationException getAffectedRowShouldBe1Exception(long affectedRowsCount, CURD operation, String tableName) {
        return new TableOperationException(
            messageAffectedRowShouldBe1,
            affectedRowsCount,
            operation,
            tableName
        );
    }

    public void checkAffectedRowShouldBe1(long affectedRowsCount, CURD operation, String tableName) throws TableOperationException {
        if (affectedRowsCount != 1) {
            throw getAffectedRowShouldBe1Exception(affectedRowsCount, operation, tableName);
        }
    }

    public TableOperationException getAffectedRowsCountDoesNotMatch(long affectedRowsCount, CURD operation, String tableName) {
        return new TableOperationException(
            messageAffectedRowsCountDoesNotMatch,
            affectedRowsCount,
            operation,
            tableName
        );
    }

}
