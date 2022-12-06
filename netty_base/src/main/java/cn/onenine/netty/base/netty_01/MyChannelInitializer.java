package cn.onenine.netty.base.netty_01;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/6 21:48
 */
@Slf4j
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        log.info("链接报告开始");
        log.info("链接报告信息：有以客户端连接到本服务端");
        log.info("链接报告IP：" + ch.localAddress().getHostString());
        log.info("链接报告Port：" + ch.localAddress().getPort());
        log.info("链接报告完毕");
    }
}
