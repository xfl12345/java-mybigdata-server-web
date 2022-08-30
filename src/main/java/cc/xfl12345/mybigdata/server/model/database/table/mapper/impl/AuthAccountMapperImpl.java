package cc.xfl12345.mybigdata.server.model.database.table.mapper.impl;

import cc.xfl12345.mybigdata.server.model.database.table.mapper.AuthAccountMapper;
import cc.xfl12345.mybigdata.server.model.database.table.mapper.base.impl.AbstractAppTableMapper;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.AuthAccount;

public class AuthAccountMapperImpl
    extends AbstractAppTableMapper<AuthAccount>
    implements AuthAccountMapper {
    @Override
    public Class<AuthAccount> getTablePojoType() {
        return AuthAccount.class;
    }
}




