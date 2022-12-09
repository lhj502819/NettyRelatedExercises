package cn.onenine.netty.base.netty_09.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import static cn.onenine.netty.base.util.DateTimeUtil.getNowStr;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/9 20:54
 */
@Slf4j
public class MyServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String msg = packet.content().toString(Charset.forName("GBK"));
        log.info("{} UDP服务器接收到消息：{}", getNowStr(), msg);

        //向客户端发送消息
        String json = "通知：我们已经收到你的消息\r\n";
        //由于数据报的数据是以字符数组传的形式存储的，所以传输数据
        byte[] bytes = json.getBytes(Charset.forName("GBK"));
        DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
        ctx.writeAndFlush(data);//向客户端发送消息
    }
}
