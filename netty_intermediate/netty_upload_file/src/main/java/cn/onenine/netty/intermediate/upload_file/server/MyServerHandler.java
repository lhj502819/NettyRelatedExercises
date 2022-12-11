package cn.onenine.netty.intermediate.upload_file.server;

import cn.onenine.netty.intermediate.upload_file.constant.Constants;
import cn.onenine.netty.intermediate.upload_file.domain.FileBurstData;
import cn.onenine.netty.intermediate.upload_file.domain.FileBurstInstruct;
import cn.onenine.netty.intermediate.upload_file.domain.FileDescInfo;
import cn.onenine.netty.intermediate.upload_file.domain.FileTransferProtocol;
import cn.onenine.netty.intermediate.upload_file.util.CacheUtil;
import cn.onenine.netty.intermediate.upload_file.util.FileUtil;
import cn.onenine.netty.intermediate.upload_file.util.MsgUtil;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

import static cn.onenine.netty.intermediate.upload_file.util.DateTimeUtil.getNowStr;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 18:31
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        log.info("链接报告开始");
        log.info("链接报告信息：有以客户端连接到本服务端");
        log.info("链接报告IP：" + channel.localAddress().getHostString());
        log.info("链接报告Port：" + channel.localAddress().getPort());
        log.info("链接报告完毕");
        //通知客户端连接建立成功
        log.info("链接建立完毕");

    }

    /**
     * 当客户端主动断开服务端的连接之后，这个通道就是不活跃的，也就是客户端断开了连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开连接 {}", ctx.channel().localAddress().toString());
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
            case 0:
                FileDescInfo fileDescInfo = (FileDescInfo) fileTransferProtocol.getTransferObj();

                //断点续传信息，实际应用中需要将断点续传信息保存到数据库中
                FileBurstInstruct fileBurstInstructOld = CacheUtil.burstDataMap.get(fileDescInfo.getFileName());
                if (null != fileBurstInstructOld) {
                    if (fileBurstInstructOld.getStatus() == Constants.FileStatus.COMPLETE) {
                        CacheUtil.burstDataMap.remove(fileDescInfo.getFileName());
                    }
                    //传输完成后删除断点信息
                    log.info("{}服务端接收客户端传输文件请求[断点续传]：{}", getNowStr(), JSONObject.toJSONString(fileBurstInstructOld));
                    ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstructOld));
                    return;
                }

                //发送信息
                FileTransferProtocol sendFileTransferProtocol = MsgUtil.buildTransferInstruct(Constants.FileStatus.BEGIN, fileDescInfo.getFileUrl(), 0);
                ctx.writeAndFlush(sendFileTransferProtocol);
                log.info("{}服务端接收客户端传输文件请求：{}", getNowStr(), JSONObject.toJSONString(fileDescInfo));
                break;
            case 2:
                FileBurstData fileBurstData = (FileBurstData) fileTransferProtocol.getTransferObj();
                FileBurstInstruct fileBurstInstruct = FileUtil.writeFile("E:\\workspaces\\uploadFile", fileBurstData);

                //保存断点续传信息
                CacheUtil.burstDataMap.put(fileBurstData.getFileName(), fileBurstInstruct);

                ctx.writeAndFlush(MsgUtil.buildTransferInstruct(fileBurstInstruct));
                log.info("{}服务端接收客户端传输文件数据：{}", getNowStr(), JSONObject.toJSONString(fileBurstData));

                //传输完成后，删除断点信息
                if (fileBurstInstruct.getStatus() == Constants.FileStatus.COMPLETE) {
                    CacheUtil.burstDataMap.remove(fileBurstData.getFileName());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        log.info("异常信息：\r\n {}", cause.getMessage());
    }
}
