package cc.xfl12345.mybigdata.server.utility;

import org.teasoft.bee.osql.SuidRich;
import org.teasoft.honey.osql.core.BeeFactory;

public class BeeOrmUtils {
    public static SuidRich getSuidRich() {
        return BeeFactory.getHoneyFactory().getSuidRich();
    }
}
