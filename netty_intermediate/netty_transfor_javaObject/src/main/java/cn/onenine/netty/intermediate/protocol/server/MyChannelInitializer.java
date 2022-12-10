package cn.onenine.netty.intermediate.protocol.server;

import cn.onenine.netty.intermediate.protocol.codec.ObjDecoder;
import cn.onenine.netty.intermediate.protocol.codec.ObjEncoder;
import cn.onenine.netty.intermediate.protocol.domain.MsgInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 17:57
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //protobuf处理
        ch.pipeline().addLast(new ObjEncoder(MsgInfo.class),
                new ObjDecoder(MsgInfo.class));

        //在管道中添加我们自己的接收数据实现方式
        ch.pipeline().addLast(new MyServerHandler());
    }
}
