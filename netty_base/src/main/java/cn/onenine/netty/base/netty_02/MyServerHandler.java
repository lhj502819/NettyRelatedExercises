package cn.onenine.netty.base.netty_02;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/6 22:02
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收消息
        ByteBuf buf = (ByteBuf) msg;
        byte[] msgByte = new byte[buf.readableBytes()];
        buf.readBytes(msgByte);
        log.info(LocalDateTime.now() + "接收到消息");
        log.info(new String(msgByte, Charset.forName("GBK")));
    }

}
