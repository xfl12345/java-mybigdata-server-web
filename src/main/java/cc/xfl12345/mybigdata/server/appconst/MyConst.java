package cc.xfl12345.mybigdata.server.appconst;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class MyConst {
    public static final String INFORMATION_SCHEMA_TABLE_NAME = "information_schema";

    public static final String ABOUT_BLANK_URL = "about:blank?";

    public static final int SHA_512_HEX_STR_LENGTH = 128;

    public static final String ANONYMOUS = "anonymous";
}
