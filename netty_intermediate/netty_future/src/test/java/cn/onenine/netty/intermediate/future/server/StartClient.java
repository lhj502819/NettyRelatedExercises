package cn.onenine.netty.intermediate.future.server;

import cn.onenine.netty.intermediate.future.client.ClientSocket;
import cn.onenine.netty.intermediate.future.future.SyncWrite;
import cn.onenine.netty.intermediate.future.msg.Request;
import cn.onenine.netty.intermediate.future.msg.Response;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/13 22:27
 */
public class StartClient {

    private static ChannelFuture future;

    public static void main(String[] args) {
        ClientSocket client = new ClientSocket();
        new Thread(client).start();

        while (true){
            try {
                //获取future，线程有等待处理时间
                if (null == future){
                    future = client.getFuture();
                    Thread.sleep(500);
                    continue;
                }
                //构建发送参数
                Request request = new Request();
                request.setResult("查询用户信息");
                SyncWrite s = new SyncWrite();
                Response response = s.writeAndSync(future.channel(),request,1000);
                System.out.println("调用结果：" + JSON.toJSONString(response));
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
