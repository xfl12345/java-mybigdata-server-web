package ${package};

import ${tableClass.fullClassName};
import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.example.SelectByExampleMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.example.SelectOneByExampleMapper;

/**
* 通用 Mapper 代码生成器
*
* @author mapper-generator
*/
public interface ${tableClass.shortClassName}${mapperSuffix} extends
        BaseSelectMapper<${tableClass.shortClassName}>,
        SelectByExampleMapper<${tableClass.shortClassName}>,
        SelectOneByExampleMapper<${tableClass.shortClassName}>,
        SelectCountByExampleMapper<${tableClass.shortClassName}>{
}




