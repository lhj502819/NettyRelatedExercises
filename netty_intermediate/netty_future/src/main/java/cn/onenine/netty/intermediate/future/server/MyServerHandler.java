package cn.onenine.netty.intermediate.future.server;

import cn.onenine.netty.intermediate.future.msg.Request;
import cn.onenine.netty.intermediate.future.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 18:31
 */
@Slf4j
public class MyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        Request msg = (Request) obj;
        //返回
        Response response = new Response();
        response.setRequestId(msg.getRequestId());
        response.setParam(msg.getResult() + " 请求成功，反馈结果请接受处理");
        ctx.writeAndFlush(response);
        //释放
        ReferenceCountUtil.release(msg);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
