package cc.xfl12345.mybigdata.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreLoad {

    public PreLoad() {
        //设置成默认有序
        JSON.DEFAULT_PARSER_FEATURE = Feature.config(
            JSON.DEFAULT_PARSER_FEATURE,
            Feature.OrderedField,
            true
        );
        log.info("PreLoad loaded.");
    }

    /*
    public static EthernetAddress ethernetAddress = EthernetAddress.fromInterface();
    public static FileBasedTimestampSynchronizer fileBasedTimestampSynchronizer;
    public static TimeBasedGenerator timeBasedGenerator;

    static {
        try {
            fileBasedTimestampSynchronizer = new FileBasedTimestampSynchronizer();
            timeBasedGenerator = Generators.timeBasedGenerator(ethernetAddress, fileBasedTimestampSynchronizer);
        } catch (IOException e) {
            e.printStackTrace();
            timeBasedGenerator = Generators.timeBasedGenerator(ethernetAddress);
            logger("fileBasedTimestampSynchronizer initialize failed.Feature disabled.");
        }
    }
     */

}
