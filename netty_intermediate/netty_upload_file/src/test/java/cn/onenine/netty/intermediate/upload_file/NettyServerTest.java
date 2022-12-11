package cn.onenine.netty.intermediate.upload_file;

import cn.onenine.netty.intermediate.upload_file.server.NettyServer;

public class NettyServerTest {

    public static void main(String[] args) {
        //启动服务
        new NettyServer().bing(8099);
    }

}
