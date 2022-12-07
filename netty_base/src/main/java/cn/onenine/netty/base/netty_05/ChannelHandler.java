package cn.onenine.netty.base.netty_05;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/7 22:13
 */
public class ChannelHandler {

    //用于存放用户Channel消息，也可以建立map结构模拟不同的消息群
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
