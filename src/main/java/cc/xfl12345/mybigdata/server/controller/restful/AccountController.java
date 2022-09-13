package cc.xfl12345.mybigdata.server.controller.restful;

import cc.xfl12345.mybigdata.server.common.utility.CopyUtils;
import cc.xfl12345.mybigdata.server.common.web.pojo.response.JsonApiResponseData;
import cc.xfl12345.mybigdata.server.service.AccountService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(AccountController.servletName)
public class AccountController {
    @Getter
    protected AccountService accountService;

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public static final String servletName = "user";
    public static final String servletPathCache1 = "/" + servletName;
    public static final String servletPathCache2 = "/" + servletName + "/";

    @PostMapping("reset-password")
    public JsonApiResponseData resetPassword(String passwordHash) {
        return accountService.resetPassword(passwordHash);
    }

    @PostMapping("login")
    public JsonApiResponseData login(String username, String passwordHash) {
        JsonApiResponseData responseData = getNewResponseDataObject();
        CopyUtils.copy(accountService.login(username, passwordHash), responseData);

        return responseData;
    }

    @PostMapping("logout")
    public JsonApiResponseData logout() {
        JsonApiResponseData responseData = getNewResponseDataObject();
        CopyUtils.copy(accountService.logout(), responseData);

        return responseData;
    }

    public JsonApiResponseData getNewResponseDataObject() {
        return new JsonApiResponseData(accountService.getJsonApiVersion());
    }
}
