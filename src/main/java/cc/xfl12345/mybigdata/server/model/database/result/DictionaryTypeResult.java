package cc.xfl12345.mybigdata.server.model.database.result;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

public class DictionaryTypeResult extends SingleDataResultBase {
    @Getter
    @Setter
    protected LinkedHashMap<String, Long> dictionary = null;
}
