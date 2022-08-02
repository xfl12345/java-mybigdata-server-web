package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.appconst.CURD;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;
import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;

import java.util.Date;

public class AbstractCoreTableHandler extends AbstractTableHandler {
    @Getter
    @Setter
    protected String messageAffectedRowShouldBe1 = "Affected row count should be 1.";

    @Getter
    @Setter
    protected String messageAffectedRowsCountDoesNotMatch = "Affected rows count does not match.";

    @Getter
    @Setter
    protected volatile NoArgGenerator uuidGenerator;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (uuidGenerator == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("uuidGenerator"));
        }
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

    public GlobalDataRecord getNewRegisteredGlobalDataRecord(Date createTime, Long tableNameId) {
        GlobalDataRecord globalDataRecord = getNewGlobalDataRecord(createTime, tableNameId);
        SuidRich suid = BeeFactory.getHoneyFactory().getSuidRich();
        Long id = suid.insertAndReturnId(globalDataRecord);
        globalDataRecord.setId(id);
        return globalDataRecord;
    }

    public TableOperationException getUpdateShouldBe1Exception(int affectedRowsCount, String tableName) {
        return getAffectedRowShouldBe1Exception(
            affectedRowsCount,
            CURD.UPDATE,
            tableName
        );
    }

    public TableOperationException getAffectedRowShouldBe1Exception(int affectedRowsCount, CURD operation, String tableName) {
        return new TableOperationException(
            messageAffectedRowShouldBe1,
            affectedRowsCount,
            operation,
            tableName
        );
    }

    public void checkAffectedRowShouldBe1(int affectedRowsCount, CURD operation, String tableName) throws TableOperationException {
        if (affectedRowsCount != 1) {
            throw getAffectedRowShouldBe1Exception(affectedRowsCount, operation, tableName);
        }
    }

    public TableOperationException getAffectedRowsCountDoesNotMatch(int affectedRowsCount, CURD operation, String tableName) {
        return new TableOperationException(
            messageAffectedRowsCountDoesNotMatch,
            affectedRowsCount,
            operation,
            tableName
        );
    }
}
