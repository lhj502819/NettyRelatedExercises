package cn.onenine.netty.base.netty_07;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import static cn.onenine.netty.base.util.DateTimeUtil.getNowStr;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/8 21:29
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //日志信息
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("链接报告开始");
        log.info("链接报告信息：有以客户端连接到本服务端");
        log.info("链接报告IP：" + channel.localAddress().getHostString());
        log.info("链接报告Port：" + channel.localAddress().getPort());
        log.info("链接报告完毕");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{} 接收到消息 {}" , getNowStr(),msg);
//        String str = String.format("服务端收到了：%s %s \r\n", getNowStr(), msg);
        ctx.writeAndFlush("hi I'm ok");
    }
}
