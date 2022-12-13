package cn.onenine.netty.intermediate.future.future;

import cn.onenine.netty.intermediate.future.msg.Request;
import cn.onenine.netty.intermediate.future.msg.Response;
import io.netty.channel.Channel;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Description：同步写
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/13 21:42
 */
public class SyncWrite {

    public Response writeAndSync(final Channel channel, final Request request, final long timeout) throws Exception {

        if (channel == null) {
            throw new NullPointerException("channel");
        }

        if (request == null) {
            throw new NullPointerException("request");
        }

        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout <= 0");
        }

        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);

        SyncWriteFuture future = new SyncWriteFuture(requestId);
        SyncWriteMap.syncKey.put(requestId, future);

        Response response = doWriteAndSync(channel, request, timeout, future);

        SyncWriteMap.syncKey.remove(requestId);

        return response;
    }


    private Response doWriteAndSync(final Channel channel, final Request request, final long timeout, final WriteFuture<Response> writeFuture) throws Exception {
        channel.writeAndFlush(request).addListener(future1 -> {
            writeFuture.setWriteResult(future1.isSuccess());
            writeFuture.setCause(future1.cause());
            //失败移除
            if (!writeFuture.isWriteSuccess()){
                SyncWriteMap.syncKey.remove(writeFuture.requestId());
            }
        });

        Response response = writeFuture.get(timeout, TimeUnit.MILLISECONDS);
        if (response == null) {
            if (writeFuture.isTimeout()) {
                throw new TimeoutException();
            }else {
                throw new Exception(writeFuture.cause());
            }
        }

        return response;
    }

}
