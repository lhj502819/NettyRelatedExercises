package cn.onenine.netty.base.netty_08.client;

import cn.onenine.netty.base.netty_07.MyDecoder;
import cn.onenine.netty.base.netty_07.MyEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/8 21:27
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 基于换行符号
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        // 解码转String，注意调整自己的编码格式GBK、UTF-8
        ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        //在管道中添加我们自己的接受数据实现方法
        //消息入站处理器
        ch.pipeline().addLast(new MyInMsgHandler());
        //消息出站处理器，在Client发送消息时会触发此处理器
        ch.pipeline().addLast(new MyOutMsgHandler());

    }
}
