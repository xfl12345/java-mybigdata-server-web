package cc.xfl12345.mybigdata.server.model.database.result;

import cc.xfl12345.mybigdata.server.model.database.table.IntegerContent;
import cc.xfl12345.mybigdata.server.model.database.table.StringContent;
import lombok.Getter;

public class NumberTypeResult extends ExecuteResultBase {
    protected Object theNum;

    @Getter
    protected IntegerContent integerContent = null;

    public void setIntegerContent(IntegerContent integerContent) {
        this.integerContent = integerContent;
        theNum = integerContent.getContent();
    }

    @Getter
    protected StringContent stringContent = null;

    public void setStringContent(StringContent stringContent) {
        this.stringContent = stringContent;
        theNum = stringContent.getContent();
    }


    public Double getDouble() {
        Double result = null;
        try {
            if(theNum instanceof String) {
                result = Double.parseDouble((String) theNum);
            } else {
                result = (Double) theNum;
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    public Long getLong() {
        Long result = null;
        try {
            if(theNum instanceof String) {
                result = Long.parseLong((String) theNum);
            } else {
                result = (Long) theNum;
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    public String getString() {
        String result = null;
        try {
            if(theNum instanceof String) {
                result = (String) theNum;
            } else {
                result = theNum.toString();
            }
        } catch (Exception ignored) {
        }
        return result;
    }
}
