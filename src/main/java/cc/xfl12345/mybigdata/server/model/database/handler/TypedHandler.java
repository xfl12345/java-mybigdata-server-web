package cc.xfl12345.mybigdata.server.model.database.handler;

import cc.xfl12345.mybigdata.server.model.database.result.ExecuteResultBase;
import cc.xfl12345.mybigdata.server.model.database.result.SingleDataResultBase;

public interface TypedHandler<T> {
    SingleDataResultBase add(T value);

    T getStringById(Long globalId);

    ExecuteResultBase updateById(Long globalId);

    ExecuteResultBase deleteById(Long globalId);
}
