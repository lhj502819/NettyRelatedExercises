package cn.onenine.netty.intermediate.future.server;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/13 22:26
 */
public class StartServer {

    public static void main(String[] args) {
        new Thread(new ServerSocket()).start();
    }


}
