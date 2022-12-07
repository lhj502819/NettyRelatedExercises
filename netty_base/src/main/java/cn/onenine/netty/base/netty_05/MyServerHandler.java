package cn.onenine.netty.base.netty_05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import static cn.onenine.netty.base.util.DateTimeUtil.getNowStr;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/6 22:02
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当客户端主动连接服务端的连接后，这个通道就是活跃的了，
     * 也就是与服务端建立了通信通道并且可以传输数据了
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当有客户端连接后，添加到channelGroup通信组中
        ChannelHandler.channelGroup.add(ctx.channel());
        //日志信息
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("链接报告开始");
        log.info("链接报告信息：有以客户端连接到本服务端");
        log.info("链接报告IP：" + channel.localAddress().getHostString());
        log.info("链接报告Port：" + channel.localAddress().getPort());
        log.info("链接报告完毕");
        //通知客户端连接建立成功
        String str = String.format("连接建立成功 %s %s \r\n", getNowStr(), channel.localAddress().getHostString());
        ctx.writeAndFlush(str);
    }

    /**
     * 当客户端主动断开服务端的连接之后，这个通道就是不活跃的，也就是客户端断开了连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接 {}",ctx.channel().localAddress().toString());
        //当有客户端断开之后，从channelGroup中移除
        ChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{} 接收到消息 {}" , getNowStr(),msg);
        //收到消息后，群发给客户端
        String str = String.format("服务端收到了：%s %s \r\n", getNowStr(), msg);
        ChannelHandler.channelGroup.writeAndFlush(str);
    }

    /**
     * 捕获异常，当发生异常时，可以做一些相应的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.info("异常信息：\r\n {}" , cause.getMessage());
    }
}
