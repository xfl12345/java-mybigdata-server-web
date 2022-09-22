package cc.xfl12345.mybigdata.server.web.controller.restful;

import cc.xfl12345.mybigdata.server.common.web.mapper.DatabaseViewer;
import cc.xfl12345.mybigdata.server.web.appconst.ApiConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(ApiConst.BACKEND_PATH_NAME + '/' + "db/viewer")
public class DatabaseViewerController {
    protected DatabaseViewer databaseViewer;

    @Autowired
    public void setDatabaseViewer(DatabaseViewer databaseViewer) {
        this.databaseViewer = databaseViewer;
    }

    @GetMapping("all-table-name")
    public List<String> getAllTableName() {
        return databaseViewer.getAllTableName();
    }

    @GetMapping("table/field")
    public List<String> getTableFieldNames(String tableName) {
        return databaseViewer.getTableFieldNames(tableName);
    }

    @GetMapping("table/count")
    public long getTableRecordCount(String tableName) {
        return databaseViewer.getTableRecordCount(tableName);
    }

    @GetMapping("table/content")
    public List<Object> getTableContent(String tableName, long offset, long limit) {
        return databaseViewer.getTableContent(tableName, offset, limit);
    }
}
