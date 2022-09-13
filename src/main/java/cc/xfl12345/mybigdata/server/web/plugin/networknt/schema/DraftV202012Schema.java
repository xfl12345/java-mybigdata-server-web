package cc.xfl12345.mybigdata.server.web.plugin.networknt.schema;

import com.networknt.schema.*;

import java.util.Arrays;

public class DraftV202012Schema {
    private static String URI = "https://json-schema.org/draft/2020-12/schema";
    private static final String ID = "$id";

    public JsonMetaSchema getInstance() {
        return new JsonMetaSchema.Builder(URI)
            .idKeyword(ID)
            .addFormats(Version202012.BUILTIN_FORMATS)
            .addKeywords(ValidatorTypeCode.getNonFormatKeywords(SpecVersion.VersionFlag.V202012))
            // keywords that may validly exist, but have no validation aspect to them
            .addKeywords(Arrays.asList(
                new NonValidationKeyword("$vocabulary"),
                new NonValidationKeyword("$dynamicAnchor"),
                new NonValidationKeyword("links"),
                new NonValidationKeyword("$dynamicRef"),
                new NonValidationKeyword("$schema"),
                new NonValidationKeyword("items"),

                new NonValidationKeyword("$id"),
                new NonValidationKeyword("title"),
                new NonValidationKeyword("description"),
                new NonValidationKeyword("default"),
                new NonValidationKeyword("definitions"),
                new NonValidationKeyword("$comment"),
                new NonValidationKeyword("$defs"),
                new NonValidationKeyword("$anchor"),
                new NonValidationKeyword("deprecated"),
                new NonValidationKeyword("contentMediaType"),
                new NonValidationKeyword("contentEncoding"),
                new NonValidationKeyword("examples"),
                new NonValidationKeyword("then"),
                new NonValidationKeyword("unevaluatedItems")
            ))
            .build();
    }
}
