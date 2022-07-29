package cc.xfl12345.mybigdata.server.model.api.database.handler.impl;

import com.fasterxml.uuid.NoArgGenerator;
import lombok.Getter;
import lombok.Setter;

public class AbstractCoreTableHandler extends AbstractTableHandler {
    @Getter
    @Setter
    protected volatile NoArgGenerator uuidGenerator;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (uuidGenerator == null) {
            throw new IllegalArgumentException(fieldCanNotBeNullMessageTemplate.formatted("uuidGenerator"));
        }
    }

    public String getUuidInString() {
        return uuidGenerator.generate().toString();
    }
}
