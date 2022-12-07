package cn.onenine.netty.base.netty_06;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Description：Netty客户端
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/7 22:59
 */
@Slf4j
public class NettyClient {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.connect("192.168.100.229",8099);
    }

    @SneakyThrows
    private void connect(String inetHost,int inetPort){
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ,true);
            b.handler(new MyChannelInitializer());
            ChannelFuture f = b.connect(inetHost, inetPort).sync();
            log.info("Netty Client 启动成功......");
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
