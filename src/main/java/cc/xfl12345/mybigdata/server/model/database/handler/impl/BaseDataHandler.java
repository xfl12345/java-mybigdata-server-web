package cc.xfl12345.mybigdata.server.model.database.handler.impl;

import org.springframework.beans.factory.InitializingBean;

public class BaseDataHandler implements InitializingBean {
    protected String fieldCanNotBeNullMessageTemplate = "Property [%s] can not be null!";

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
