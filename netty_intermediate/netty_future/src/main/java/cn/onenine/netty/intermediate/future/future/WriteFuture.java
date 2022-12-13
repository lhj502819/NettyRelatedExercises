package cn.onenine.netty.intermediate.future.future;

import cn.onenine.netty.intermediate.future.msg.Response;

import java.util.concurrent.Future;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/13 21:46
 */
public interface WriteFuture<T> extends Future<T> {

    Throwable cause();

    void setCause(Throwable cause);

    boolean isWriteSuccess();

    void setWriteResult(boolean result);

    String requestId();

    T response();

    void setResponse(Response response);

    boolean isTimeout();

}
