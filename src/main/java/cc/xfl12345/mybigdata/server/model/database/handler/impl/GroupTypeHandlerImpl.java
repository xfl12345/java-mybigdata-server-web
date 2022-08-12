// package cc.xfl12345.mybigdata.server.model.database.handler.impl;
//
// import cc.xfl12345.mybigdata.server.appconst.CURD;
// import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
// import cc.xfl12345.mybigdata.server.model.api.database.result.GroupData;
// import cc.xfl12345.mybigdata.server.model.database.table.constant.GroupContentConstant;
// import cc.xfl12345.mybigdata.server.model.database.table.constant.GroupRecordConstant;
// import cc.xfl12345.mybigdata.server.model.database.table.constant.StringContentConstant;
// import cc.xfl12345.mybigdata.server.model.database.error.TableOperationException;
// import cc.xfl12345.mybigdata.server.model.database.handler.GroupTypeHandler;
// import cc.xfl12345.mybigdata.server.model.database.handler.StringTypeHandler;
// import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
// import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
// import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupContent;
// import cc.xfl12345.mybigdata.server.model.database.table.pojo.GroupRecord;
// import lombok.Getter;
// import lombok.Setter;
// import org.apache.commons.math3.exception.OutOfRangeException;
// import org.teasoft.bee.osql.Condition;
// import org.teasoft.bee.osql.Op;
// import org.teasoft.bee.osql.SuidRich;
// import org.teasoft.honey.osql.core.ConditionImpl;
//
// import java.util.*;
//
// public class GroupTypeHandlerImpl extends AbstractAppTableHandler implements GroupTypeHandler {
//
//     @Getter
//     @Setter
//     protected StringTypeHandler stringTypeHandler;
//
//     @Override
//     public void afterPropertiesSet() throws Exception {
//         super.afterPropertiesSet();
//         if (stringTypeHandler == null) {
//             throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("stringTypeHandler"));
//         }
//     }
//
//
//     protected List<GroupContent> packUpItems(Long globalId, Collection<Long> groupItems) {
//         int itemCount = groupItems.size();
//         List<GroupContent> groupContents = new ArrayList<>(itemCount + 1);
//         if (groupItems instanceof List<Long> array) {
//             for (int index = 0; index < itemCount; index++) {
//                 GroupContent groupContent = new GroupContent();
//                 groupContent.setGlobalId(globalId);
//                 groupContent.setItemIndex((long) index);
//                 groupContent.setItem(array.get(index));
//                 groupContents.add(groupContent);
//             }
//         } else { // 单纯集合无下标
//             for (Long itemId : groupItems) {
//                 GroupContent groupContent = new GroupContent();
//                 groupContent.setGlobalId(globalId);
//                 groupContent.setItem(itemId);
//                 groupContents.add(groupContent);
//             }
//         }
//         return groupContents;
//     }
//
//     protected void _insertIntoGroupByGlobalId(Long globalId, Collection<Long> groupItems) throws TableOperationException {
//         SuidRich suidRich = getSuidRich();
//         int itemCount = groupItems.size();
//         List<GroupContent> groupContents = packUpItems(globalId, groupItems);
//
//         int affectedRowCount = 0;
//         affectedRowCount = suidRich.insert(groupContents);
//         if (affectedRowCount != itemCount) {
//             throw getAffectedRowsCountDoesNotMatch(
//                 affectedRowCount,
//                 CURD.CREATE,
//                 CoreTableNames.GROUP_CONTENT
//             );
//         }
//     }
//
//     protected void _deleteGroupItemByGlobalId(Long globalId, Collection<Long> groupItems) throws TableOperationException {
//         int affectedRowCount = 0;
//         SuidRich suidRich = getSuidRich();
//         int itemCount = groupItems.size();
//         List<GroupContent> groupContents = packUpItems(globalId, groupItems);
//         affectedRowCount = suidRich.delete(groupContents);
//         if (affectedRowCount != itemCount) {
//             throw getAffectedRowsCountDoesNotMatch(
//                 affectedRowCount,
//                 CURD.DELETE,
//                 CoreTableNames.GROUP_CONTENT
//             );
//         }
//     }
//
//     protected void _replaceGroupItemByGlobalId(Long globalId, Collection<Long> groupOldItems, Collection<Long> groupNewItems) throws TableOperationException {
//         if (groupOldItems != null) {
//             _deleteGroupItemByGlobalId(globalId, groupOldItems);
//         } else { // 为空，则意味着删除全部元素
//             SuidRich suidRich = getSuidRich();
//             Condition condition = new ConditionImpl();
//             condition.op(GroupContentConstant.GLOBAL_ID, Op.eq, globalId);
//             suidRich.delete(new GroupContent(), condition);
//         }
//         _insertIntoGroupByGlobalId(globalId, groupNewItems);
//     }
//
//     protected Long _insertNewGroup(String name, Collection<Long> groupItems) throws Exception {
//         Date date = new Date();
//         int affectedRowCount = 0;
//
//         GlobalDataRecord globalDataRecord = getNewRegisteredGlobalDataRecord(
//             date,
//             coreTableCache.getTableNameCache().getValue(CoreTableNames.GROUP_RECORD)
//         );
//
//         Long globalId = globalDataRecord.getId();
//
//         SuidRich suidRich = getSuidRich();
//
//         GroupRecord groupRecord = new GroupRecord();
//         groupRecord.setGlobalId(globalDataRecord.getId());
//         if (name != null) {
//             Long nameId = stringTypeHandler.insertOrSelect4Id(name);
//             groupRecord.setGroupName(nameId);
//         }
//         affectedRowCount = suidRich.insert(suidRich);
//         checkAffectedRowShouldBe1(affectedRowCount, CURD.CREATE, CoreTableNames.GROUP_RECORD);
//
//         _insertIntoGroupByGlobalId(globalId, groupItems);
//
//         return globalId;
//     }
//
//
//     @Override
//     public Long insertNewGroup(String name, List<Long> groupItems) throws Exception {
//         return _insertNewGroup(name, groupItems);
//     }
//
//     @Override
//     public void insertIntoGroupByGlobalId(Long globalId, List<Long> groupItems) throws TableOperationException {
//         _insertIntoGroupByGlobalId(globalId, groupItems);
//     }
//
//     @Override
//     public void deleteGroupItemByGlobalId(Long globalId, List<Long> groupItems) throws TableOperationException {
//         _deleteGroupItemByGlobalId(globalId, groupItems);
//     }
//
//     @Override
//     public void replaceAllGroupItemByGlobalId(Long globalId, List<Long> groupItems) throws TableOperationException {
//         _replaceGroupItemByGlobalId(globalId, null, groupItems);
//     }
//
//     @Override
//     public void replaceGroupItemByGlobalId(Long globalId, List<Long> groupOldItems, List<Long> groupNewItems) throws TableOperationException {
//         _replaceGroupItemByGlobalId(globalId, groupOldItems, groupNewItems);
//     }
//
//     @Override
//     public Long insertNewGroup(String name, Set<Long> groupItems) throws Exception {
//         return _insertNewGroup(name, groupItems);
//     }
//
//     @Override
//     public void insertIntoGroupByGlobalId(Long globalId, Set<Long> groupItems) throws TableOperationException {
//         _insertIntoGroupByGlobalId(globalId, groupItems);
//     }
//
//     @Override
//     public void deleteGroupItemByGlobalId(Long globalId, Set<Long> groupItems) throws TableOperationException {
//         _deleteGroupItemByGlobalId(globalId, groupItems);
//     }
//
//     @Override
//     public void replaceAllGroupItemByGlobalId(Long globalId, Set<Long> groupItems) throws TableOperationException {
//         _replaceGroupItemByGlobalId(globalId, null, groupItems);
//     }
//
//     @Override
//     public void replaceGroupItemByGlobalId(Long globalId, Set<Long> groupOldItems, Set<Long> groupNewItems) throws TableOperationException {
//         _replaceGroupItemByGlobalId(globalId, groupOldItems, groupNewItems);
//     }
//
//
//     @Override
//     public void deleteGroupByGlobalId(Long globalId) throws Exception {
//         int affectedRowCount = 0;
//         SuidRich suidRich = getSuidRich();
//         Condition condition;
//
//         condition = new ConditionImpl();
//         condition.op(GroupContentConstant.GLOBAL_ID, Op.eq, globalId);
//         suidRich.delete(new GroupContent(), condition);
//
//         condition = new ConditionImpl();
//         condition.op(GroupRecordConstant.GLOBAL_ID, Op.eq, globalId);
//         affectedRowCount = suidRich.delete(new GroupRecord(), condition);
//         checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.GROUP_RECORD);
//
//         affectedRowCount = suidRich.deleteById(GlobalDataRecord.class, globalId);
//         checkAffectedRowShouldBe1(affectedRowCount, CURD.DELETE, CoreTableNames.GLOBAL_DATA_RECORD);
//     }
//
//     @Override
//     public GroupData selectGroupByGlobalId(Long globalId) throws Exception {
//         SuidRich suidRich = getSuidRich();
//         GroupData groupData = new GroupData();
//         groupData.groupName = selectGroupNameByGlobalId(globalId);
//         groupData.group = selectGroupItemsByGlobalId(globalId);
//         return groupData;
//     }
//
//     @Override
//     public Collection<Long> selectGroupItemsByGlobalId(Long globalId) throws Exception {
//         SuidRich suidRich = getSuidRich();
//         Condition condition = new ConditionImpl();
//         condition.op(GroupContentConstant.GLOBAL_ID, Op.eq, globalId);
//
//         List<GroupContent> groupContents = suidRich.select(new GroupContent(), condition);
//
//         List<Long> ordered = new ArrayList<>(groupContents.size() << 1);
//         Set<Long> unordered = new LinkedHashSet<>(groupContents.size() << 1);
//         for (GroupContent groupContent : groupContents) {
//             Long index = groupContent.getItemIndex();
//             if(index != null) {
//                 // 暂时只支持 二进制32位有符号整数最大值 以内的数量……
//                 if (index.compareTo((long) index.intValue()) == 0) {
//                     ordered.set(index.intValue(), groupContent.getItem());
//                 } else {
//                     throw new OutOfRangeException(index, 0, Integer.MAX_VALUE);
//                 }
//             } else {
//                 unordered.add(groupContent.getItem());
//             }
//         }
//
//         ordered.addAll(unordered);
//         return ordered;
//     }
//
//     @Override
//     public String selectGroupNameByGlobalId(Long globalId) throws Exception {
//         SuidRich suidRich = getSuidRich();
//         GroupRecord groupRecord = suidRich.selectById(new GroupRecord(), globalId);
//         Long nameId = groupRecord.getGroupName();
//         if (nameId != null) {
//             return stringTypeHandler.selectById(
//                 nameId, new String[]{StringContentConstant.CONTENT}
//             ).getContent();
//         }
//         return null;
//     }
// }
