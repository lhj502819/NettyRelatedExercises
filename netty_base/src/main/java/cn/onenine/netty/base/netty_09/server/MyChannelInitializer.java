package cn.onenine.netty.base.netty_09.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/9 20:52
 */
public class MyChannelInitializer extends ChannelInitializer<NioDatagramChannel> {

    private EventLoopGroup group = new NioEventLoopGroup();

    @Override
    protected void initChannel(NioDatagramChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //解码String，注意调整自己的编码格式GBK、UTF-8
        pipeline.addLast(group,new MyServerHandler());
    }
}
