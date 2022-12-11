package cn.onenine.netty.intermediate.upload_file.client;

import cn.onenine.netty.intermediate.upload_file.constant.Constants;
import cn.onenine.netty.intermediate.upload_file.domain.FileBurstData;
import cn.onenine.netty.intermediate.upload_file.domain.FileBurstInstruct;
import cn.onenine.netty.intermediate.upload_file.domain.FileTransferProtocol;
import cn.onenine.netty.intermediate.upload_file.util.FileUtil;
import cn.onenine.netty.intermediate.upload_file.util.MsgUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 18:43
 */
@Slf4j
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("链接报告开始");
        log.info("链接报告信息：本客户端链接到服务端：channelId {}", channel.id());
        log.info("链接报告IP：" + channel.localAddress().getHostString());
        log.info("链接报告Port：" + channel.localAddress().getPort());
        log.info("链接报告完毕");
        log.info("链接建立完毕");
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("断开连接 {}", ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //数据格式验证
        if (!(msg instanceof FileTransferProtocol)) {
            return;
        }
        FileTransferProtocol fileTransferProtocol = (FileTransferProtocol) msg;
        //0传输文件‘请求’，1文件传输'指令'、2文件传输‘数据’
        switch (fileTransferProtocol.getTransferType()) {
            case 1:
                FileBurstInstruct fileBurstInstruct = (FileBurstInstruct) fileTransferProtocol.getTransferObj();
                //Constants
                if (Constants.FileStatus.COMPLETE == fileBurstInstruct.getStatus()) {
                    ctx.flush();
                    ctx.close();
                    System.exit(1);
                    return;
                }
                FileBurstData fileBurstData = FileUtil.readFile(fileBurstInstruct.getClientFileUrl(), fileBurstInstruct.getReadPosition());
                ctx.writeAndFlush(MsgUtil.buildTransferData(fileBurstData));
                log.info(" 客户端传输文件信息：File:{} size(byte):{} ", fileBurstData.getFileName(), fileBurstData.getEndPos() - fileBurstData.getBeginPos());
                break;

            default:
                break;
        }

        log.info("跨互动传输文件信息[主动断开连接，模拟断点续传]");
        ctx.flush();
        ctx.close();
        System.exit(-1);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.info("异常信息：\r\n {}", cause.getMessage());
    }
}
