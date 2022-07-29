package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.api.database.result.GroupData;
import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GroupTypeHandler {
    Long insertNewGroup(String name, List<Long> groupItems) throws Exception;

    void insertIntoGroupByGlobalId(Long globalId, List<Long> groupItems) throws TableOperationException;

    void deleteGroupItemByGlobalId(Long globalId, List<Long> groupItems) throws TableOperationException;

    void replaceAllGroupItemByGlobalId(Long globalId, List<Long> groupItems) throws TableOperationException;

    void replaceGroupItemByGlobalId(Long globalId, List<Long> groupOldItems, List<Long> groupNewItems) throws TableOperationException;


    Long insertNewGroup(String name, Set<Long> groupItems) throws Exception;

    void insertIntoGroupByGlobalId(Long globalId, Set<Long> groupItems) throws TableOperationException;

    void deleteGroupItemByGlobalId(Long globalId, Set<Long> groupItems) throws TableOperationException;

    void replaceAllGroupItemByGlobalId(Long globalId, Set<Long> groupItems) throws TableOperationException;

    void replaceGroupItemByGlobalId(Long globalId, Set<Long> groupOldItems, Set<Long> groupNewItems) throws TableOperationException;


    void deleteGroupByGlobalId(Long globalId) throws Exception;

    GroupData selectGroupByGlobalId(Long globalId) throws Exception;

    Collection<Long> selectGroupItemsByGlobalId(Long globalId) throws Exception;

    String selectGroupNameByGlobalId(Long globalId) throws Exception;
}
