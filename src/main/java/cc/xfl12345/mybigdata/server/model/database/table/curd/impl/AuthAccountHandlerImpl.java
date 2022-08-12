package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.model.database.table.curd.AuthAccountHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.AbstractAppTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.AuthAccount;

public class AuthAccountHandlerImpl
    extends AbstractAppTableHandler<AuthAccount>
    implements AuthAccountHandler {
    @Override
    public Class<AuthAccount> getTablePojoType() {
        return AuthAccount.class;
    }
}




