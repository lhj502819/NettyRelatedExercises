package cn.onenine.netty.intermediate.springboot.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import static cn.onenine.netty.intermediate.springboot.util.DateTimeUtil.getNowStr;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 11:13
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("链接报告开始");
        log.info("链接报告信息：有以客户端连接到本服务端");
        log.info("链接报告IP：" + channel.localAddress().getHostString());
        log.info("链接报告Port：" + channel.localAddress().getPort());
        log.info("链接报告完毕");
        //通知客户端连接建立成功
        String str = String.format("连接建立成功 %s %s \r\n", getNowStr(), channel.localAddress().getHostString());
        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
        buf.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(buf);
    }

    /**
     * 当客户端主动断开服务端的连接之后，这个通道就是不活跃的，也就是客户端断开了连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接 {}",ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{} 接收到消息 {}" , getNowStr(),msg);
        //通知客户端消息接收到了
        String str = String.format("服务端收到了：%s %s \r\n", getNowStr(), msg);
        ByteBuf buf = Unpooled.buffer(str.getBytes().length);
        buf.writeBytes(str.getBytes("GBK"));
        ctx.writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.info("异常信息：\r\n {}" , cause.getMessage());
    }
}
