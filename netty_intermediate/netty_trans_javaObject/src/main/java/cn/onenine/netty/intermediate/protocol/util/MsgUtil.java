package cn.onenine.netty.intermediate.protocol.util;

import cn.onenine.netty.intermediate.protocol.domain.MsgInfo;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 18:33
 */
public class MsgUtil {

    /**
     * 构建protobuf消息体
     */
    public static MsgInfo buildMsg(String channelId,String msgInfo){
        return new MsgInfo(channelId,msgInfo);
    }

}
