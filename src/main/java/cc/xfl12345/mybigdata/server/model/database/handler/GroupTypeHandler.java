package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.GroupTypeResult;

import java.util.List;
import java.util.Set;

public interface GroupTypeHandler {
    GroupTypeResult insertNewGroup(List<Long> groupItems);

    GroupTypeResult insertExistGroupByGlobalId(Long globalId, List<Long> groupItems);

    GroupTypeResult deleteGroupItemByGlobalId(Long globalId, List<Long> groupItems);

    GroupTypeResult replaceAllGroupItemByGlobalId(Long globalId, List<Long> groupItems);

    GroupTypeResult replaceGroupItemByGlobalId(Long globalId, List<Long> groupOldItems, List<Long> groupNewItems);


    GroupTypeResult insertNewGroup(Set<Long> groupItems);

    GroupTypeResult insertExistGroupByGlobalId(Long globalId, Set<Long> groupItems);

    GroupTypeResult deleteGroupItemByGlobalId(Long globalId, Set<Long> groupItems);

    GroupTypeResult replaceAllGroupItemByGlobalId(Long globalId, Set<Long> groupItems);

    GroupTypeResult replaceGroupItemByGlobalId(Long globalId, Set<Long> groupOldItems, Set<Long> groupNewItems);
}
