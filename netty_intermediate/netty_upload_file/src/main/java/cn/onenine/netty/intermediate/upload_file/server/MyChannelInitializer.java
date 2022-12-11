package cn.onenine.netty.intermediate.upload_file.server;

import cn.onenine.netty.intermediate.upload_file.codec.ObjDecoder;
import cn.onenine.netty.intermediate.upload_file.codec.ObjEncoder;
import cn.onenine.netty.intermediate.upload_file.domain.FileTransferProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 17:57
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) {
        //protobuf处理
        ch.pipeline().addLast(new ObjEncoder(FileTransferProtocol.class),
                new ObjDecoder(FileTransferProtocol.class));

        //在管道中添加我们自己的接收数据实现方式
        ch.pipeline().addLast(new MyServerHandler());
    }
}
