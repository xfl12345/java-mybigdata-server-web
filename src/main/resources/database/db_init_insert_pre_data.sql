use xfl_mybigdata;

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

# # 注册 label_record 表
# SELECT insert_string_content(
#            '0000003a-cb7a-11eb-0000-f828196a1686',
#            '标签记录表的名称',
#            '0000003b-cb7a-11eb-0000-f828196a1686',
#            'label_record');


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

ALTER TABLE global_data_record
    AUTO_INCREMENT = 65536;

# 补完字符串长度
UPDATE string_content
SET content_length = CHAR_LENGTH(content)
WHERE content_length = default(content_length);
