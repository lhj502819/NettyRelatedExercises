package cn.onenine.netty.intermediate.heartbeat.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import static cn.onenine.netty.intermediate.heartbeat.util.DateTimeUtil.getNowStr;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 18:43
 */
@Slf4j
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("链接报告开始");
        log.info("链接报告信息：本客户端链接到服务端：channelId {}", channel.id());
        log.info("链接报告IP：" + channel.localAddress().getHostString());
        log.info("链接报告Port：" + channel.localAddress().getPort());
        log.info("链接报告完毕");
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接重连：" + ctx.channel().localAddress().toString());
        //使用过程中断重连
        new Thread(() -> {
            try {
                new NettyClient().connect("127.0.0.1", 8099);
                log.info("netty client start done");
                Thread.sleep(500);
            } catch (Exception e) {
                log.error("netty client start error go reconnect");
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) {
        log.info("{} 接收到消息类型：{}", getNowStr(), obj.getClass());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常信息断开重连：{}", cause.getMessage());
        ctx.close();
    }

}
