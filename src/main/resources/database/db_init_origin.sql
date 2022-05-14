/**
* Source Server Type             : MySQL
* Source Server AppInfo          : 5.7.26
* Source Host                    : 127.0.0.1:3306
* Source Schema                  : xfl_mybigdata
* FileOperation Encoding         : utf-8
* Date: 2021/6/9 17:00:00
*/

drop database if exists xfl_mybigdata;

create
    database xfl_mybigdata DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

use
    xfl_mybigdata;

SET
    FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `test_table`
(
    `ID`  int NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `num` int NOT NULL
) AUTO_INCREMENT = 1
  ENGINE = InnoDB;

INSERT INTO `test_table` (`ID`, `num`)
VALUES (1, 666),
       (233, 678);


# 优先创建 字符串记录表 ，毕竟 全局ID记录表 对 字符串表 存在必要依赖
/**
  一个庞大的 字符串记录表，暂时还没做来源系统
 */
CREATE TABLE string_content
(
    `global_id`      bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    `data_format`    bigint unsigned comment '字符串结构格式',
    `content_length` smallint        not null default -1 comment '字符串长度',
    `content`        varchar(768)    not null comment '字符串内容，最大长度为 768 个字符',
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    index boost_query_id (data_format, content_length) comment '加速查询主键，避免全表扫描',
    unique key boost_query_content (content(768)) comment '加速检索字符串内容'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

INSERT INTO string_content (global_id, data_format, content)
    # 第一个字符串，关于数据格式——text
values (1, 1, 'text');

# 设置字符串格式的默认值为 text
ALTER TABLE string_content
    ALTER COLUMN data_format SET DEFAULT 1;

INSERT INTO string_content (global_id, content)
    # 第二个字符串，是一个空字符串
VALUES (2, ''),
       # 第三个字符串，关于 "描述" 本身
       (3, '说明、描述'),
       # 第四个字符串，关于 第一个字符串 的描述
       (4, '一种字符串内容格式'),
       # 第五个字符串，关于 字符串表 的名称
       (5, '字符串记录表的名称'),
       # 第六个字符串，关于 字符串表 的名称
       (6, 'string_content'),
       # 第七个字符串，关于 全局ID记录表 的描述
       (7, '全局ID记录表的名称'),
       # 第八个字符串，关于 全局ID记录表 的名称
       (8, 'global_data_record');


/**
  全局ID记录表，记录并关联当前数据库内所有表的每一行数据。
  如果 table_name 字段 是空值，则意味着它是未使用的记录（用以加速插入数据），
  而且该行数据有可能会被定期删除。
 */
CREATE TABLE global_data_record
(
    `id`             bigint unsigned not null PRIMARY KEY AUTO_INCREMENT comment '当前表所在数据库实例里的全局ID',
    `uuid`           char(36)        not null comment '关于某行数据的，整个MySQL数据库乃至全球唯一的真正的全局ID',
    `create_time`    datetime        not null DEFAULT CURRENT_TIMESTAMP comment '创建时间',
    `update_time`    datetime        not null DEFAULT CURRENT_TIMESTAMP comment '修改时间',
    `modified_count` bigint unsigned not null default 1 comment '修改次数（版本迭代）',
    `table_name`     bigint unsigned comment '该行数据所在的表名',
    `description`    bigint unsigned          default 2 comment '该行数据的附加简述',
    # 全局ID 记录表，删除乃大忌。拒绝一切外表级联删除行记录，只允许按 global_id 或 uuid 删除行记录
    # 遵循 一切普通文本 由 字符串记录表
    # TODO 这里有问题……………………
    #     foreign key (table_name) references string_content (global_id) on delete restrict on update cascade,
    #     foreign key (description) references string_content (global_id) on delete restrict on update cascade,
    unique key index_uuid (uuid) comment '确保UUID的唯一性',
    index boost_query_all (uuid, create_time, update_time, modified_count, table_name, description)
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;


INSERT INTO global_data_record (id, uuid, table_name, description)
    # 先斩后奏 之 关联已有的 字符串 数据
VALUES (1, '00000000-cb7a-11eb-0000-f828196a1686', 6, 4),
       (2, '00000001-cb7a-11eb-0000-f828196a1686', 6, 2),
       (3, '00000002-cb7a-11eb-0000-f828196a1686', 6, 3),
       (4, '00000003-cb7a-11eb-0000-f828196a1686', 6, 3),
       (5, '00000004-cb7a-11eb-0000-f828196a1686', 6, 3),
       (6, '00000005-cb7a-11eb-0000-f828196a1686', 6, 5),
       (7, '00000006-cb7a-11eb-0000-f828196a1686', 6, 3),
       (8, '00000007-cb7a-11eb-0000-f828196a1686', 6, 7);

# 为 字符串表 添加 全局ID 约束
alter table string_content
    add foreign key (global_id) references global_data_record (id) on delete cascade on update cascade;

# 添加 字符串格式 引用出处（自环）
alter table string_content
    add foreign key (data_format) references string_content (global_id) on delete cascade on update cascade;

/**
  因为自环引用会导致自锁，所以以下给出 将 string_content 表里的
  global_id 从 1 改成 1000 的方法：

UPDATE string_content
SET data_format = 2
WHERE data_format = 1;

UPDATE global_data_record SET global_id = 1000 WHERE global_id = 1;

UPDATE string_content
SET data_format = 1000
WHERE data_format = 2;

 */

# 创建个视图，方便跨表查看数据
CREATE OR REPLACE VIEW view_global_data_record AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s1 WHERE s1.global_id = g.table_name)  AS `table_name`,
       (SELECT content FROM string_content AS s2 WHERE s2.global_id = g.description) AS `description`
FROM global_data_record AS g
ORDER BY `g`.id;

CREATE OR REPLACE VIEW view_string_content AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s WHERE s.global_id = g.description) AS `description`,
       data_src_table.content                                                      AS `content`
FROM string_content AS data_src_table,
     global_data_record AS g
WHERE data_src_table.global_id = g.id
ORDER BY `g`.id;

/**
  专门记录 “数字” 的表
  不过 integer_content 表只记 64位带符号的整型数字
  如果是超长整数、浮点数，则通过 字符串 存在 string_content 里
 */
CREATE TABLE integer_content
(
    `global_id` bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    `content`   bigint          not null comment '64位带符号的整型数字',
    foreign key (global_id) references global_data_record (id) on delete cascade on update cascade,
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    unique key index_content (content) comment '加速查询全部数据'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE VIEW view_integer_content AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s WHERE s.global_id = g.description) AS `description`,
       data_src_table.content                                                      AS content
FROM integer_content AS data_src_table,
     global_data_record AS g
WHERE data_src_table.global_id = g.id
ORDER BY g.id;

/**
  MyBigData 特色功能之一，就是使用JSON Schema完成对MySQL模型化操作
  这个表主要存放JSON模型
 */
CREATE TABLE table_schema_record
(
    `global_id`      bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    `schema_name`    bigint unsigned not null comment '插表模型名称',
    `content_length` smallint        not null default -1 comment 'json_schema 字段的长度',
    # 这里不遵循 “一切普通文本 由 字符串记录表” 的原则
    # 是因为json格式的字符串可以使用json格式存储，MySQL原生支持JSON格式
    # 暂不考虑使用JSON格式存储JSON字符串，暂且先保留修改空间
    `json_schema`    varchar(16000)  not null comment '插表模型',
    foreign key (global_id) references global_data_record (id) on delete cascade on update cascade,
    foreign key (schema_name) references global_data_record (id) on delete restrict on update cascade,
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    unique key index_schema_name (schema_name) comment '确保插表模型名称的唯一性',
    index boost_query_id (global_id, schema_name, content_length) comment '加速查询主键，避免全表扫描'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE VIEW view_table_schema_record AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s WHERE s.global_id = g.description) AS `description`,
       data_src_table.schema_name                                                  AS schema_name,
       data_src_table.content_length                                               AS content_length,
       data_src_table.json_schema                                                  AS json_schema
FROM table_schema_record AS data_src_table,
     global_data_record AS g
WHERE data_src_table.global_id = g.id
ORDER BY g.id;

/**
  专门记录树状结构的表
 */
CREATE TABLE tree_struct_record
(
    `global_id`      bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    `root_id`        bigint unsigned not null comment '根节点对象',
    `item_count`     int unsigned    not null comment '整个树的节点个数',
    `tree_deep`      int unsigned    not null comment '整个树的深度（有几层）',
    `content_length` smallint        not null default -1 comment 'JSON文本长度',
    # 暂不考虑使用JSON格式存储JSON字符串，且先保留修改空间
    `struct_data`    varchar(16000)  not null comment '以JSON字符串形式记载树形结构',
    foreign key (global_id) references global_data_record (id) on delete cascade on update cascade,
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    foreign key (root_id) references global_data_record (id) on delete cascade on update cascade,
    index boost_query_id (root_id, item_count, tree_deep, content_length) comment '加速查询主键，避免全表扫描'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE VIEW view_tree_struct_record AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s WHERE s.global_id = g.description) AS `description`,
       data_src_table.root_id                                                      AS `root_id`,
       data_src_table.item_count                                                   AS `item_count`,
       data_src_table.tree_deep                                                    AS `tree_deep`,
       data_src_table.content_length                                               AS `content_length`,
       data_src_table.struct_data                                                  AS `struct_data`
FROM tree_struct_record AS data_src_table,
     global_data_record AS g
WHERE data_src_table.global_id = g.id
ORDER BY g.id;

/**
  专门记录二元关系的表
 */
CREATE TABLE binary_relationship_record
(
    `global_id` bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    `item_a`    bigint unsigned not null comment '对象A',
    `item_b`    bigint unsigned not null comment '对象B',
    foreign key (global_id) references global_data_record (id) on delete cascade on update cascade,
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    foreign key (item_a) references global_data_record (id) on delete cascade on update cascade,
    foreign key (item_b) references global_data_record (id) on delete cascade on update cascade,
    unique key unique_limit_ab (item_a, item_b) comment '不允许出现重复关系，以免浪费空间',
    index boost_query_all (item_b, item_a) comment '加速查询全部数据'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE VIEW view_binary_relationship_record AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s WHERE s.global_id = g.description) AS `description`,
       data_src_table.item_a                                                       AS `item_a`,
       (SELECT content
        FROM string_content AS s
        WHERE s.global_id = (SELECT description
                             FROM global_data_record AS s
                             WHERE s.id = data_src_table.item_a))                  AS `item_a_description`,
       data_src_table.item_b                                                       AS `item_b`,
       (SELECT content
        FROM string_content AS s
        WHERE s.global_id = (SELECT description
                             FROM global_data_record AS s
                             WHERE s.id = data_src_table.item_b))                  AS `item_b_description`
FROM binary_relationship_record AS data_src_table,
     global_data_record AS g
WHERE data_src_table.global_id = g.id
ORDER BY g.id;

/**
  专门记录 “组” 的表
  不过这 group_record 表只记组号
 */
CREATE TABLE group_record
(
    `global_id`  bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    `group_name` bigint unsigned not null default 2 comment '组名',
    foreign key (global_id) references global_data_record (id) on delete cascade on update cascade,
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    index boost_query_all (group_name) comment '加速查询全部数据'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

/**
  专门记录 “组” 的表，所有关于“列表（一维数组）”、“集合”和“分组”等概念的数据的 关系 都记录于该表
 */
CREATE TABLE group_content
(
    `group_id`   bigint unsigned not null comment '组id',
    `item_index` bigint unsigned not null comment '组内对象的下标',
    `item`       bigint unsigned not null comment '组内对象',
    # 关联 group_record 表。毕竟 “组” 这种概念，本就是一对多的关系。
    foreign key (group_id) references group_record (global_id) on delete cascade on update cascade,
    foreign key (item) references global_data_record (id) on delete cascade on update cascade,
    unique key boost_query_all (group_id, item_index, item) comment '加速查询全部数据'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE VIEW view_group_content AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s WHERE s.global_id = g.description) AS `description`,
       (SELECT content
        FROM string_content AS s
        WHERE s.global_id = (SELECT id
                             FROM group_record AS s
                             WHERE s.global_id = data_src_table.group_id))         AS `group_name`,
       data_src_table.item_index                                                   AS `item_index`,
       data_src_table.item                                                         AS `item`
FROM group_content AS data_src_table,
     global_data_record AS g
WHERE data_src_table.group_id = g.id
ORDER BY g.id;

/**
  专门记录 “标签” 的表——标签记录表
 */
CREATE TABLE label_record
(
    `global_id` bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    group_id    bigint unsigned not null comment '标签集合',
    foreign key (global_id) references global_data_record (id) on delete cascade on update cascade,
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    # 拒绝一切外表级联删除行记录，只允许按 主键id 删除行记录
    foreign key (group_id) references group_record (global_id) on delete restrict on update cascade,
    unique key boost_query_all (group_id) comment '加速查询全部数据'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;

CREATE OR REPLACE VIEW view_label_record AS
SELECT g.id,
       g.uuid,
       g.create_time,
       g.update_time,
       g.modified_count,
       (SELECT content FROM string_content AS s WHERE s.global_id = g.description) AS `description`,
       (SELECT content
        FROM string_content AS s
        WHERE s.global_id = (SELECT id
                             FROM group_record AS s
                             WHERE s.global_id = data_src_table.group_id))         AS `group_name`
FROM label_record AS data_src_table,
     global_data_record AS g
WHERE data_src_table.global_id = g.id
ORDER BY g.id;

# 启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

# 大杀器，修改所有表的引擎为 InnoDB 。适用于对MySQL支持不太好的 ORM框架
# SELECT CONCAT( 'ALTER TABLE ', TABLE_NAME, ' ENGINE=InnoDB;' )
# FROM information_schema.tables
# WHERE table_schema = 'xfl_mybigdata';

# 准备插入一些初始信息
# 来个插入数据的正规流程示范
# 先向 global_data_record 表注册，通过查询 UUID 拿到 global_id
DELIMITER $$
DROP FUNCTION IF EXISTS insert_global_data_record $$
CREATE FUNCTION insert_global_data_record(
    the_uuid char(36),
    table_name_gid bigint unsigned,
    description_gid bigint unsigned)
    RETURNS bigint unsigned

BEGIN
    DECLARE insert_row_gid bigint unsigned;
    # 新增一行全局ID
    INSERT INTO global_data_record (uuid, table_name, description)
    VALUES (the_uuid, table_name_gid, description_gid);
    # 获取全局ID
    SET insert_row_gid = (SELECT id FROM global_data_record WHERE uuid = the_uuid);

    RETURN insert_row_gid;
END $$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS insert_description_to_string_content $$
CREATE FUNCTION insert_description_to_string_content(
    description_uuid char(36),
    description varchar(768))
    RETURNS bigint unsigned

BEGIN
    DECLARE normal_text_description_gid bigint unsigned;
    DECLARE string_content_table_name_gid bigint unsigned;
    DECLARE insert_content_gid bigint unsigned;
    SET normal_text_description_gid = (SELECT global_id FROM string_content WHERE content = '说明、描述');
    SET string_content_table_name_gid = (SELECT global_id FROM string_content WHERE content = 'string_content');
    # 获取全局ID
    SET insert_content_gid = insert_global_data_record(
        description_uuid,
        string_content_table_name_gid,
        normal_text_description_gid);
    # 插入 内容 到 字符串表
    INSERT INTO string_content (global_id, content)
    VALUES (insert_content_gid, description);

    RETURN insert_content_gid;
END $$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS insert_string_content_with_description_gid $$
CREATE FUNCTION insert_string_content_with_description_gid(
    insert_content_uuid char(36),
    insert_content varchar(768),
    description_gid bigint unsigned)
    RETURNS bigint unsigned

BEGIN
    DECLARE string_content_table_name_gid bigint unsigned;
    DECLARE insert_content_gid bigint unsigned;
    SET string_content_table_name_gid = (SELECT global_id FROM string_content WHERE content = 'string_content');
    # 获取全局ID
    SET insert_content_gid = insert_global_data_record(
        insert_content_uuid,
        string_content_table_name_gid,
        description_gid);
    # 插入 内容 到 字符串表
    INSERT INTO string_content (global_id, content)
    VALUES (insert_content_gid, insert_content);

    RETURN insert_content_gid;
END $$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS insert_string_content $$
CREATE FUNCTION insert_string_content(
    description_uuid char(36),
    description varchar(768),
    insert_content_uuid char(36),
    insert_content varchar(768))
    RETURNS bigint unsigned

BEGIN
    DECLARE description_gid bigint unsigned;
    SET description_gid = (SELECT insert_description_to_string_content(description_uuid, description));
    RETURN (SELECT insert_string_content_with_description_gid(
                       insert_content_uuid,
                       insert_content,
                       description_gid));
END $$
DELIMITER ;

DELIMITER $$
DROP FUNCTION IF EXISTS insert_table_schema_record $$
CREATE FUNCTION insert_table_schema_record(
    description_uuid char(36),
    description varchar(768),
    schema_name_uuid char(36),
    schema_name varchar(768),
    json_schema_content_uuid char(36),
    json_schema_content varchar(16000))
    RETURNS bigint unsigned

BEGIN
    DECLARE normal_schema_description_gid bigint unsigned;
    DECLARE table_schema_record_table_name_gid bigint unsigned;
    DECLARE schema_name_gid bigint unsigned;
    DECLARE insert_row_gid bigint unsigned;
    DECLARE description_gid bigint unsigned;
    SET normal_schema_description_gid = (SELECT global_id FROM string_content WHERE content = '插表模型名称');
    SET table_schema_record_table_name_gid =
        (SELECT global_id FROM string_content WHERE content = 'table_schema_record');
    # 插入 插表模型 的 描述
    SET description_gid = (SELECT insert_string_content_with_description_gid(
                                      description_uuid,
                                      description,
                                      normal_schema_description_gid));
    # 插入 插表模型 的 名称
    SET schema_name_gid = (SELECT insert_string_content_with_description_gid(
                                      schema_name_uuid,
                                      schema_name,
                                      description_gid));
    # 新增 一行全局ID
    SET insert_row_gid = (SELECT insert_global_data_record(
                                     json_schema_content_uuid,
                                     table_schema_record_table_name_gid,
                                     description_gid));
    # table_schema_record 表 新增一行数据
    INSERT INTO table_schema_record (global_id, schema_name, json_schema)
    VALUES (insert_row_gid, schema_name_gid, json_schema_content);

    RETURN insert_row_gid;
END $$
DELIMITER ;


# 注册 table_schema_record 表
SELECT insert_string_content(
           '00000030-cb7a-11eb-0000-f828196a1686',
           'table-JSON模型记录表的名称',
           '00000031-cb7a-11eb-0000-f828196a1686',
           'table_schema_record');

# 注册 tree_struct_record 表
SELECT insert_string_content(
           '00000032-cb7a-11eb-0000-f828196a1686',
           '专门记录树状结构的表的名称',
           '00000033-cb7a-11eb-0000-f828196a1686',
           'tree_struct_record');

# 注册 binary_relationship_record 表
SELECT insert_string_content(
           '00000034-cb7a-11eb-0000-f828196a1686',
           '专门记录二元关系的表的名称',
           '00000035-cb7a-11eb-0000-f828196a1686',
           'binary_relationship_record');

# 注册 group_record 表
SELECT insert_string_content(
           '00000036-cb7a-11eb-0000-f828196a1686',
           '组号记录表的名称',
           '00000037-cb7a-11eb-0000-f828196a1686',
           'group_record');

# 注册 group_content 表
SELECT insert_string_content(
           '00000038-cb7a-11eb-0000-f828196a1686',
           '组成员记录表的名称',
           '00000039-cb7a-11eb-0000-f828196a1686',
           'group_content');

# 注册 label_record 表
SELECT insert_string_content(
           '0000003a-cb7a-11eb-0000-f828196a1686',
           '标签记录表的名称',
           '0000003b-cb7a-11eb-0000-f828196a1686',
           'label_record');


SELECT insert_string_content_with_description_gid(
           '00000040-cb7a-11eb-0000-f828196a1686',
           'JSON',
           4);

SELECT insert_string_content_with_description_gid(
           '00000041-cb7a-11eb-0000-f828196a1686',
           'XML',
           4);

SELECT insert_string_content_with_description_gid(
           '00000042-cb7a-11eb-0000-f828196a1686',
           'HTML',
           4);

SELECT insert_description_to_string_content(
           '00000050-cb7a-11eb-0000-f828196a1686',
           '插表模型名称');

# # label_record 表的插表模型
# SELECT insert_table_schema_record(
#                '00000056-cb7a-11eb-0000-f828196a1686',
#                'label_record 表的插表模型',
#                '00000057-cb7a-11eb-0000-f828196a1686',
#                '新增字符串',
#                '00000056-cb7a-11eb-0000-f828196a1686',
#                '{
#    "$schema": "https://json-schema.org/draft/2020-12/schema",
#    "$id": "https://github.com/xfl12345/MyBigData_Python3/blob/main/mybigdata/src/main/resources/json/schema/string_content.json",
#    "title": "string_content",
#    "description": "string_content表的插表模型",
#    "type": "object",
#    "properties": {
#        "data_format": {
#            "description": "字符串结构格式",
#            "type": "string",
#            "maxLength": 16000
#        },
#        "content": {
#            "description": "字符串内容，最大长度为16000个字符",
#            "type": "string",
#            "maxLength": 16000
#        }
#    },
#    "required": [
#        "content"
#    ]
# }');

create table auth_account
(
    `id`            bigint          not null PRIMARY KEY AUTO_INCREMENT comment '账号ID',
    `password_hash` char(128)       NOT NULL comment '账号密码的哈希值',
    `password_salt` char(128)       NOT NULL comment '账号密码的哈希值计算的佐料',
    `extra_info_id` bigint unsigned not null comment '账号额外信息',
    unique key index_account_id (id),
    index boost_query_all (id, password_hash, password_salt, extra_info_id),
    foreign key (extra_info_id) references global_data_record (id) on delete restrict on update restrict
) AUTO_INCREMENT = 10000
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
  ROW_FORMAT = Dynamic;


ALTER TABLE global_data_record
    AUTO_INCREMENT = 65536;

# 补完字符串长度
UPDATE string_content
SET content_length = CHAR_LENGTH(content)
WHERE content_length = default(content_length);


/**
  暂时还没写完，因为发现了很多问题欠缺考虑
 */


/**
  天气数据MySQL表设计目标（检验编程结果是否OK的表）
 */
CREATE TABLE weather_coding_goal
(
    `global_id`      bigint unsigned not null comment '当前表所在数据库实例里的全局ID',
    `weather_like`   bigint unsigned not null comment '天气状况',
    `celsius_degree` int unsigned    not null comment '摄氏度',
    foreign key (global_id) references global_data_record (id) on delete cascade on update cascade,
    unique key unique_global_id (global_id) comment '确保每一行数据对应一个相对于数据库唯一的global_id',
    # 拒绝一切外表级联删除行记录，只允许按 主键id 删除行记录
    foreign key (weather_like) references string_content (global_id) on delete restrict on update cascade,
    index boost_query_all (weather_like, celsius_degree) comment '加速查询全部数据'
) ENGINE = InnoDB
  ROW_FORMAT = DYNAMIC;
