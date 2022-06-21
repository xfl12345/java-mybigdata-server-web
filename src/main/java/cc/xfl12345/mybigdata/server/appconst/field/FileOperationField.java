package cc.xfl12345.mybigdata.server.appconst.field;

public class FileOperationField {
    // 文件和文件夹（目录）共有功能
    public static final String CREATE = "create";
    public static final String DELETE = "delete";
    public static final String FIND = "find";
    public static final String CHANGE_NAME = "changeName";
    public static final String COPY = "copy";
    public static final String MOVE = "move";
    //上传下载文件（包括递归上传、下载）
    public static final String UPLOAD = "upload";
    public static final String DOWNLOAD = "download";

    //文件专属功能
    public static final String GET_SHA256_HEX = "getSHA256Hex";
    public static final String GET_MD5_HEX = "getMD5Hex";

    //文件夹（目录）专属功能
    public static final String MAKE_DIRECTORY = "mkdir";
    public static final String LIST_FILES = "ls";
    public static final String CHANGE_DIRECTORY = "cd";
    public static final String PRINT_WORK_DIRECTOR = "pwd";

}
