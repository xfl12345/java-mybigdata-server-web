package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import cc.xfl12345.mybigdata.server.model.database.error.SqlErrorHandler;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.StringContent;
import dev.morphia.query.Query;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.List;

@Slf4j
public class StringTypeHandlerImpl extends AbstractCoreTableHandler implements StringTypeHandler {

    @Getter
    @Setter
    protected SqlErrorHandler sqlErrorHandler = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (sqlErrorHandler == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("sqlErrorHandler"));
        }
    }

    @Override
    public void updateStringByFullText(String oldValue, String value) throws TableOperationException {

    }

    @Override
    public List<StringContent> selectStringByPrefix(String prefix, String[] fields) {
        return null;
    }

    @Override
    public void deleteStringByFullText(String value) throws TableOperationException {

    }

    @Override
    public ObjectId insert(StringContent value) throws Exception {
        return null;
    }

    @Override
    public ObjectId selectId(StringContent value) throws Exception {
        return null;
    }

    @Override
    public StringContent selectById(ObjectId globalId, String[] fields) throws Exception {
        return null;
    }

    @Override
    public StringContent selectOne(StringContent value, String[] fields) throws Exception {
        return null;
    }

    @Override
    public List<StringContent> select(Query<StringContent> condition) throws Exception {
        return null;
    }

    @Override
    public ObjectId insertOrSelect4Id(StringContent value) throws Exception {
        return null;
    }

    @Override
    public void updateById(StringContent value, ObjectId globalId) throws Exception {

    }

    @Override
    public void delete(StringContent value) throws Exception {

    }

    @Override
    public void deleteById(StringContent value, ObjectId globalId) throws Exception {

    }
}
