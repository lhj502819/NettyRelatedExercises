package cn.onenine.netty.base.netty_07;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Description：自定义编码器，在byte 开始可结尾加上02 03
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/8 21:24
 */
public class MyEncoder extends MessageToByteEncoder<String> {


    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.getBytes();
        byte[] send = new byte[bytes.length + 2];
        System.arraycopy(bytes, 0, send, 1, bytes.length);
        send[0] = 0x02;
        send[send.length - 1] = 0x03;

        out.writeInt(send.length);
        out.writeBytes(send);
    }
}
