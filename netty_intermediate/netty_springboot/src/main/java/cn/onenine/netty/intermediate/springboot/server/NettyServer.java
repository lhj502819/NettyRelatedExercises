package cn.onenine.netty.intermediate.springboot.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 11:59
 */
@Slf4j
@Component
public class NettyServer {

    //配置服务端NIO线程组
    private static final EventLoopGroup parentGroup = new NioEventLoopGroup();
    private static final EventLoopGroup childGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture bind(InetSocketAddress address){
        ChannelFuture channelFuture = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup,childGroup)
                    .channel(NioServerSocketChannel.class)//非阻塞模式
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childHandler(new MyChannelInitializer());
            channelFuture = b.bind(address).syncUninterruptibly();
            log.info("netty server starting....");
            channel = channelFuture.channel();
        }catch (Exception e){
            log.error("error ",e);
        }finally {
            if (null != channelFuture && channelFuture.isSuccess()){
                log.info("netty server started....");
            }else {
                log.error("start error...");
            }
        }
        return channelFuture;
    }

    public void destroy(){
        if (null == channel)
            return;
        channel.close();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
    }

    public Channel getChannel(){
        return channel;
    }

}
