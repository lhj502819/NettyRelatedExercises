package cn.onenine.netty.base.netty_03;

import cn.onenine.netty.base.netty_02.MyServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 字符串编解码
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/6 21:48
 */
@Slf4j
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        //解码器
        //基于换行符
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        //基于指定的字符串[换行符，这样功能等同于LineBasedFrameDecoder]
//        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,false, Delimiters.lineDelimiter()));
        //基于最大长度
//        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        //在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(new MyServerHandler());
    }
}
