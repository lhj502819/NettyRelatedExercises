package cn.onenine.netty.base.netty_09.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/9 20:45
 */
@Slf4j
public class MyClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {


    /**
     * 接收服务端发送的内容
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String msg = packet.content().toString(Charset.forName("GBK"));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " UDP客户端接收到消息：" + msg);

    }
}
