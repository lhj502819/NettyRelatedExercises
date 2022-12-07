package cn.onenine.netty.base.netty_06;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/7 22:59
 */
@Slf4j
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + ch.id());
        System.out.println("链接报告完毕");
    }
}
