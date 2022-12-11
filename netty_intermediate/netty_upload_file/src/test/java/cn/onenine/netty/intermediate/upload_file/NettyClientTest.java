package cn.onenine.netty.intermediate.upload_file;

import cn.onenine.netty.intermediate.upload_file.client.NettyClient;
import cn.onenine.netty.intermediate.upload_file.domain.FileTransferProtocol;
import cn.onenine.netty.intermediate.upload_file.util.MsgUtil;
import io.netty.channel.ChannelFuture;

import java.io.File;

public class NettyClientTest {

    public static void main(String[] args) {

        //启动客户端
        ChannelFuture channelFuture = new NettyClient().connect("127.0.0.1", 8099);

        //文件信息{文件大于1024kb方便测试断点续传}
        File file = new File("E:\\workspaces\\测试传输文件.rar");
        FileTransferProtocol fileTransferProtocol = MsgUtil.buildRequestTransferFile(file.getAbsolutePath(), file.getName(), file.length());

        //发送信息；请求传输文件
        channelFuture.channel().writeAndFlush(fileTransferProtocol);

    }

}