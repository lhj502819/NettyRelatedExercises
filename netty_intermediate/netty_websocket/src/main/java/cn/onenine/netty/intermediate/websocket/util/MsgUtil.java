package cn.onenine.netty.intermediate.websocket.util;

import cn.hutool.extra.tokenizer.engine.mmseg.MmsegEngine;
import cn.onenine.netty.intermediate.websocket.domain.ServerMsgProtocol;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Description：消息传输协议
 *
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/11 18:39
 */
public class MsgUtil {

    public static TextWebSocketFrame buildMsgAll(String channelId,String msgInfo){
        //模拟头像
        int i = Math.abs(channelId.hashCode()) % 10;

        ServerMsgProtocol msg = new ServerMsgProtocol();
        //连接信息 1私发消息 2群发消息
        msg.setType(2);
        msg.setChannelId(channelId);
        msg.setUserHeadImg("head" + i + ".jpg");
        msg.setMsgInfo(msgInfo);

        return new TextWebSocketFrame(JSONObject.toJSONString(msg));
    }

    public static TextWebSocketFrame buildMsgOwner(String channelId){
        ServerMsgProtocol msg = new ServerMsgProtocol();
        //1：连接信息  2：消息信息
        msg.setType(1);
        return new TextWebSocketFrame(JSONObject.toJSONString(msg));
    }

}
