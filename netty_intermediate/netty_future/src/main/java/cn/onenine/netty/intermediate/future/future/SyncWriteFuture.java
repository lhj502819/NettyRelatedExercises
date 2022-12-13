package cn.onenine.netty.intermediate.future.future;

import cn.onenine.netty.intermediate.future.msg.Response;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/13 21:45
 */
public class SyncWriteFuture implements WriteFuture<Response> {

    private CountDownLatch latch = new CountDownLatch(1);

    private final long begin = System.currentTimeMillis();

    private long timeout;

    private Response response;

    private final String requestId;

    private boolean writeResult;

    private Throwable cause;

    private boolean isTimeout = false;

    public SyncWriteFuture(String requestId) {
        this.requestId = requestId;
    }

    public SyncWriteFuture(String requestId, boolean isTimeout) {
        this.requestId = requestId;
        this.isTimeout = isTimeout;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public boolean isWriteSuccess() {
        return writeResult;
    }

    @Override
    public void setWriteResult(boolean result) {
        this.writeResult = result;
    }

    @Override
    public String requestId() {
        return requestId;
    }

    @Override
    public Response response() {
        return response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
        latch.countDown();
    }

    @Override
    public boolean isTimeout() {
        if (isTimeout)
            return isTimeout;
        return System.currentTimeMillis() - begin > timeout;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Response get() throws InterruptedException, ExecutionException {
        latch.wait();
        return response;
    }

    @Override
    public Response get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout,unit)){
            return response;
        }
        return null;
    }
}
