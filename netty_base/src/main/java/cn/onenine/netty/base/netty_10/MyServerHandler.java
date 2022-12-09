package cn.onenine.netty.base.netty_10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/9 21:30
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) msg;
            System.out.println("URI:" + request.getUri());
            System.err.println(msg);
        }

        if (msg instanceof HttpContent) {
            LastHttpContent httpContent = (LastHttpContent) msg;
            ByteBuf byteData = httpContent.content();
            if(!(byteData instanceof EmptyByteBuf)){
                //接收msg消息
                byte[] msgBytes = new byte[byteData.readableBytes()];
                byteData.readBytes(msgBytes);
                System.out.println(new String(msgBytes, StandardCharsets.UTF_8));
            }
        }
        String sendMsg = "没有靠山，自己就是山！没有天下，自己打天下！\n" +
                "没有资本，自己赚资本！这世界从来没有什么救世主。弱了，所有困难就强了。\n" +
                "强了，所有阻碍就弱了！活着就该逢山开路，遇水架桥。生活，你给我压力，我还你奇迹！\n" +
                "\t\t\t 李宏健";

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(sendMsg.getBytes(StandardCharsets.UTF_8)));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain;charset=UTF-8");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
