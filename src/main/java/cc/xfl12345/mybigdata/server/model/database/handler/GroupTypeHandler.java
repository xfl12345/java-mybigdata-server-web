package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.GroupTypeResult;

import java.util.List;
import java.util.Set;

public interface GroupTypeHandler {
    GroupTypeResult insertNewGroup(List<Long> groupItems);

    GroupTypeResult insertExistGroupByGlobalId(List<Long> groupItems, Long globalId);

    GroupTypeResult deleteGroupItemByGlobalId(List<Long> groupItems, Long globalId);

    GroupTypeResult replaceAllGroupItemByGlobalId(List<Long> groupItems, Long globalId);

    GroupTypeResult replaceGroupItemByGlobalId(List<Long> groupOldItems, List<Long> groupNewItems, Long globalId);


    GroupTypeResult insertNewGroup(Set<Long> groupItems);

    GroupTypeResult insertExistGroupByGlobalId(Set<Long> groupItems, Long globalId);

    GroupTypeResult deleteGroupItemByGlobalId(Set<Long> groupItems, Long globalId);

    GroupTypeResult replaceAllGroupItemByGlobalId(Set<Long> groupItems, Long globalId);

    GroupTypeResult replaceGroupItemByGlobalId(Set<Long> groupOldItems, Set<Long> groupNewItems, Long globalId);
}
