package cc.xfl12345.mybigdata.server.model.database.table.curd.impl;

import cc.xfl12345.mybigdata.server.appconst.CoreTableNames;
import cc.xfl12345.mybigdata.server.model.database.table.constant.AuthAccountConstant;
import cc.xfl12345.mybigdata.server.model.database.table.curd.AuthAccountHandler;
import cc.xfl12345.mybigdata.server.model.database.table.curd.base.impl.BeeOrmCoreTableHandler;
import cc.xfl12345.mybigdata.server.model.database.table.pojo.AuthAccount;

public class AuthAccountHandlerImpl extends BeeOrmCoreTableHandler<AuthAccount> implements AuthAccountHandler {
    @Override
    protected String getTableName() {
        return CoreTableNames.AUTH_ACCOUNT;
    }

    @Override
    protected String getIdFieldName() {
        return AuthAccountConstant.ACCOUNT_ID;
    }

    @Override
    protected Long getId(AuthAccount value) {
        return value.getAccountId();
    }

    @Override
    protected AuthAccount getNewPojoInstance() {
        return new AuthAccount();
    }
}




