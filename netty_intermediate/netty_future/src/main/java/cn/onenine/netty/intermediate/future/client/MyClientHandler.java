package cn.onenine.netty.intermediate.future.client;

import cn.onenine.netty.intermediate.future.future.SyncWriteFuture;
import cn.onenine.netty.intermediate.future.future.SyncWriteMap;
import cn.onenine.netty.intermediate.future.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 18:43
 */
@Slf4j
public class MyClientHandler extends ChannelInboundHandlerAdapter {


    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接 {}",ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        Response msg = (Response) obj;
        String requestId = msg.getRequestId();
        SyncWriteFuture future = (SyncWriteFuture) SyncWriteMap.syncKey.get(requestId);
        if (future != null) {
            future.setResponse(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
