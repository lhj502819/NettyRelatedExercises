package cn.onenine.netty.intermediate.protocol.client;

import cn.onenine.netty.intermediate.protocol.util.MsgUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import static cn.onenine.netty.intermediate.protocol.util.DateTimeUtil.getNowStr;

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
        log.info("链接报告信息：本客户端链接到服务端：channelId {}",channel.id());
        log.info("链接报告IP：" + channel.localAddress().getHostString());
        log.info("链接报告Port：" + channel.localAddress().getPort());
        log.info("链接报告完毕");
        //通知客户端连接建立成功
        String str = String.format("通知服务端连接建立成功 %s %s \r\n", getNowStr(), channel.localAddress().getHostString());
        ctx.writeAndFlush(MsgUtil.buildMsg(channel.id().toString(), str));
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接 {}",ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("{} 接收到消息类型：{}" , getNowStr(),msg.getClass());
        log.info("{} 接收到消息内容：{}" , getNowStr(), JSONObject.toJSONString(msg));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.info("异常信息：\r\n {}" , cause.getMessage());
    }
}
