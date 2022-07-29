package cc.xfl12345.mybigdata.server.model.api.database.result;

public class ResultUtils {
    public static void copyResultBase(ExecuteResultBase src, ExecuteResultBase dest) {
        dest.setSimpleResult(src.getSimpleResult());
        dest.setSqlException(src.getSqlException());
        dest.setMessage(src.getMessage());
    }
}
