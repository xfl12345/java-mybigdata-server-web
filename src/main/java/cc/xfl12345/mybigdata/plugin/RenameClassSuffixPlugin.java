package cc.xfl12345.mybigdata.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;


public class RenameClassSuffixPlugin extends PluginAdapter {
    private String suffixString;

    @Override
    public boolean validate(List<String> warnings) {
        suffixString = properties.getProperty("suffixString");

        boolean valid = stringHasValue(suffixString);

        if (!valid) {
            warnings.add(getString("ValidationError.18",
                "RenameClassSuffixPlugin",
                "suffixString"));
        }

        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getBaseRecordType();
        oldType += suffixString;
        introspectedTable.setBaseRecordType(oldType);
    }
}
