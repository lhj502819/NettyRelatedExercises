package cn.onenine.netty.base.netty_08.server;

import cn.onenine.netty.base.netty_07.MyDecoder;
import cn.onenine.netty.base.netty_07.MyEncoder;
import cn.onenine.netty.base.netty_08.client.MyInMsgHandler;
import cn.onenine.netty.base.netty_08.client.MyOutMsgHandler;
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
        //解码器
        //基于换行符
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        //解码String，注意调整自己的编码格式GBK或者UTF-8
        ch.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        //解码转String注意调整自己的编码格式GBK、UTF8
        ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        //在管道中添加我们自己的接受数据实现方法
        ch.pipeline().addLast(new MyServerHandler());

    }
}
