package cn.onenine.netty.intermediate.heartbeat.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/15 21:29
 */
@Slf4j
public class MyChannelFutureListener implements ChannelFutureListener {

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
            log.info("netty client start done");
        }
        EventLoop eventLoop = future.channel().eventLoop();
        eventLoop.schedule(() -> {
            try {
                new NettyClient().connect("127.0.0.1", 8099);
                log.info("netty client start done");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                log.error("netty client start error go reconnect...");
            }
        }, 1L, TimeUnit.SECONDS);
    }
}
