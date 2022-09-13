package cc.xfl12345.mybigdata.server.web.interceptor;

import cc.xfl12345.mybigdata.server.common.appconst.field.FileOperationField;
import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;

public class MySaRouteInterceptor extends SaRouteInterceptor {

    public MySaRouteInterceptor() {
        super();
        function = (req, res, handler)->{
            // 根据路由划分模块，不同模块不同鉴权
            SaRouter.match("/upload/**", r -> {
                StpUtil.checkPermission(FileOperationField.UPLOAD);
            });
//            SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
//            SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));
//            SaRouter.match("/notice/**", r -> StpUtil.checkPermission("notice"));
//            SaRouter.match("/comment/**", r -> StpUtil.checkPermission("comment"));
        };
    }

}
