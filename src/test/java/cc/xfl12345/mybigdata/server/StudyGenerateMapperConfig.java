package cc.xfl12345.mybigdata.server;

import cc.xfl12345.mybigdata.server.model.database.table.pojo.GlobalDataRecord;
import cc.xfl12345.mybigdata.server.utility.MyReflectUtils;

import java.io.IOException;
import java.util.List;

public class StudyGenerateMapperConfig {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<Class<?>> pojoList = MyReflectUtils.getClasses(GlobalDataRecord.class.getPackageName(), false);
        for (Class<?> pojoClass : pojoList) {
            String pojoSimpleName = pojoClass.getSimpleName();
            String handlerInterfaceClassName = String.format("%sHandler", pojoSimpleName);

            System.out.println("\n" +
                "    @Bean\n" +
                "    @ConditionalOnMissingBean(" + handlerInterfaceClassName + ".class)\n" +
                "    public " + handlerInterfaceClassName + " get"  + handlerInterfaceClassName + "()" +
                " throws Exception {\n" +
                "        return (" + handlerInterfaceClassName + ") getHandler(" + pojoSimpleName + ".class);\n" +
                "    }"
            );
        }
    }
}
