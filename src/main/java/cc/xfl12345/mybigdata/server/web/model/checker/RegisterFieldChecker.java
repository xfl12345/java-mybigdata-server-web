package cc.xfl12345.mybigdata.server.web.model.checker;


import cc.xfl12345.mybigdata.server.common.appconst.api.request.RegisterRequestField;
import cc.xfl12345.mybigdata.server.common.appconst.api.result.RegisterApiResult;
import cc.xfl12345.mybigdata.server.common.appconst.field.AccountField;
import cc.xfl12345.mybigdata.server.common.utility.MyStrIsOK;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;

public class RegisterFieldChecker {
    @Getter
    @Setter
    protected static volatile int emailVerificationCodeLength = 8;

    public static RegisterApiResult autoCheck(JSONObject jsonObject) {
        RegisterApiResult registerApiResult;
        String username;
        String passwordStr;
        String email;
        String gender;
        //获取用户名字段
        username = (String) jsonObject.get(RegisterRequestField.USERNAME);
        //检查用户名数据是否合法
        if (RegisterFieldChecker.isUsernameUnderLegal(username)) {
            //获取性别字段
            gender = (String) jsonObject.get(RegisterRequestField.GENDER);
            //检查性别数据是否合法
            if (RegisterFieldChecker.isGenderUnderLegal(gender)) {
                //获取密码字段
                passwordStr = (String) jsonObject.get(RegisterRequestField.PASSWORD);
                //检查密码复杂度是否符合要求
                if (RegisterFieldChecker.isPasswordComplexityEnough(passwordStr)) {
                    //获取电子邮箱字段
                    email = (String) jsonObject.get(RegisterRequestField.EMAIL);
                    if (RegisterFieldChecker.isEmailUnderLegal(email)) {
                        registerApiResult = RegisterApiResult.SUCCEED;
                    } else {
                        registerApiResult = RegisterApiResult.ILLEGAL_EMAIL;
                    }
                } else {
                    registerApiResult = RegisterApiResult.ILLEGAL_PASSWORD;
                }
            } else {
                registerApiResult = RegisterApiResult.ILLEGAL_GENDER;
            }
        } else {
            registerApiResult = RegisterApiResult.ILLEGAL_USERNAME;
        }

        return registerApiResult;
    }

    /**
     * 检查密码是否符合一定的复杂度
     *
     * @param passwordStr 密码
     * @return 是否达标
     */
    public static boolean isPasswordComplexityEnough(String passwordStr) {
        int okCount = 0;
        if (MyStrIsOK.isContainNum(passwordStr))
            okCount++;
        if (MyStrIsOK.isContainUppercaseLetter(passwordStr))
            okCount++;
        if (MyStrIsOK.isContainLowercaseLetter(passwordStr))
            okCount++;
        if (MyStrIsOK.isContainAllowedSpecialCharacter(passwordStr))
            okCount++;
        return okCount >= 3;
    }

    public static boolean isPasswordUnderLegal(String str) {
        String tmpStr = str;
        tmpStr = MyStrIsOK.removeLetter(MyStrIsOK.removeNum(tmpStr));
        tmpStr = MyStrIsOK.removeAllowedSpecialCharacter(tmpStr);
        return tmpStr.equals("");
    }

    public static boolean isUsernameUnderLegal(String str) {
        String tmpStr = str;
        tmpStr = MyStrIsOK.removeLetterAndDigit(MyStrIsOK.removeNum(tmpStr));
        tmpStr =  MyStrIsOK.removeAllowedSpecialCharacter(tmpStr);
        return tmpStr.equals("");
    }

    public static boolean isGenderUnderLegal(String gender) {
        return MyStrIsOK.isDigitOnly(gender) && (gender.length() == AccountField.GENDER_LENGTH);
    }

    public static boolean isEmailUnderLegal(String email) {
        return MyStrIsOK.isEmailAddress(email);
    }

    public static boolean isEmailVerificationCodeUnderLegal(String str) {
        return str != null && str.length() == emailVerificationCodeLength;
    }
}
