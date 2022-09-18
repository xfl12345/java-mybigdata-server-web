package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.utility.CopyUtils;
import cc.xfl12345.mybigdata.server.web.pojo.WebJsonApiResponseData;
import cc.xfl12345.mybigdata.server.web.service.AccountService;
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
    public WebJsonApiResponseData resetPassword(String passwordHash) {
        return accountService.resetPassword(passwordHash);
    }

    @PostMapping("login")
    public WebJsonApiResponseData login(String username, String passwordHash) {
        WebJsonApiResponseData responseData = getNewResponseDataObject();
        CopyUtils.copy(accountService.login(username, passwordHash), responseData);

        return responseData;
    }

    @PostMapping("logout")
    public WebJsonApiResponseData logout() {
        WebJsonApiResponseData responseData = getNewResponseDataObject();
        CopyUtils.copy(accountService.logout(), responseData);

        return responseData;
    }

    public WebJsonApiResponseData getNewResponseDataObject() {
        return new WebJsonApiResponseData();
    }
}
