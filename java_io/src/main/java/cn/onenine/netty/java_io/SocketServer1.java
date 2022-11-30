package cn.onenine.netty.java_io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/11/30 22:22
 */
public class SocketServer1 {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer1.class);

    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket serverSocket = serverChannel.socket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress(83));

        Selector selector = Selector.open();
        //注意，服务器通道只能注册SelectionKey.OP_ACCEPT事件
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        try {
            while (true) {
                //如果条件成立，说明本次询问selector，并没有获取到任何准备好的、感兴趣的事件
                //Java程序对多路复用IO的支持也包括了阻塞模式和非阻塞模式两种。
                if (selector.select(100) == 0) {
                    //======================================
                    //      这里视业务情况，可以做一些然并卵的事情
                    //======================================
                    continue;
                }
                // 这里就是本次询问操作系统，所获取到的“所关心的事件”的事件类型（每一个通道都是独立）
                Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();

                while (selectionKeys.hasNext()) {
                    SelectionKey readyKey = selectionKeys.next();
                    //这个已经处理的readyKey一定要移除，如果不移除就会一直存在在selector.selectedKeys集合中
                    //待到写一次selector.select() > 0时，这个readyKey又会被处理一次
                    selectionKeys.remove();

                    SelectableChannel selectableChannel = readyKey.channel();
                    if (readyKey.isValid() && readyKey.isAcceptable()) {
                        logger.info("=======Channel通道已经准备好=======");
                        /**
                         * 当server socket channel通道已经准备好，就可以从server socket channel中获取socket channel了
                         * 拿到socket channel后，要做的事情就是马上到selector去注册的这个socket channel感兴趣的事件。
                         * 否则无法坚挺到这个socket channel到达的数据
                         */
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        registerSocketChannel(socketChannel, selector);
                    } else if (readyKey.isValid() && readyKey.isConnectable()) {
                        logger.info("========socket channel 建立连接=================");
                    } else if (readyKey.isValid() && readyKey.isReadable()) {
                        logger.info("========socket channel 数据准备完成，可以去读取==================");
                        readSocketChannel(readyKey);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            serverSocket.close();
        }

    }

    /**
     * 这个方法用于读取从客户端传来的消息
     * 并且观察从客户端过来的socket channel在经过多次传输后，是否完成传输
     * 如果传输完成，则返回一个true的标记
     */
    private static void readSocketChannel(SelectionKey readyKey) throws Exception {
        SocketChannel clientSocketChannel = (SocketChannel) readyKey.channel();
        //获取客户端使用的端口
        InetSocketAddress sourceSocketAddress = (InetSocketAddress) clientSocketChannel.getRemoteAddress();
        int resourcePort = sourceSocketAddress.getPort();

        //拿到这个socket channel使用的缓冲区，准备读取数据
        //在后文中，将详细讲解缓存区的用法概念，实际上重要的就是三个元素capacity、position和limit
        ByteBuffer contextBytes = (ByteBuffer) readyKey.attachment();
        //将通道的数据写入到缓存区中，注意是写到缓存区
        //由于之前设置了ByteBuffer的大小为2048 byte，所以可以存在写入不完的情况
        //后边可以调整代码，这里暂时理解为一次可以接收完成
        int realLen = -1;
        try {
            realLen = clientSocketChannel.read(contextBytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //这里跑出了异常，一般就是因为客户端因为某种原因终止了，所以关闭channel就好
            clientSocketChannel.close();
            ;
            return;
        }

        //如果缓存区中没有任何数据(实际不太可能，否则就不会出发OP_READ事件了)
        if (realLen == -1) {
            logger.warn("============缓存区没有数据==========");
            return;
        }

        //将缓存区从写状态改为读状态(实际上这个方法是读写模式互相切换)
        //在读模式下，应用程序只能从Buffer中读取数据，不能进行写操作。
        // 但是在写模式下，应用程序是可以进行读操作的，这就表示可能会出现脏读的情况。
        // 所以一旦您决定要从Buffer中读取数据，一定要将Buffer的状态改为读模式
        //这时Java NIO框架中的这个Socket Channel的写请求将全部等待
        contextBytes.flip();
        //注意中文乱码的问题，可以使用URLDecoder/URLEncoder进行编解码
        byte[] messageBytes = contextBytes.array();
        String messageEncode = new String(messageBytes, "UTF-8");
        String message = URLEncoder.encode(messageEncode, "UTF-8");

        //如果收到了“over”关键字，才会清空buffer，并回返数据
        //否则不清空欢昌，还要还原buffer的写状态
        if (message.indexOf("over") != -1) {
            //清空已经读取的缓存，并重新切换为写状态（这里要注意clear()和capacity()两个方法的区别）
            contextBytes.clear();
            logger.info("端口：{} 客户端发来的消息：{}", resourcePort, message);
            //========================================
            //    接收完之后，可以在这里正是处理业务了
            //======================================

            //回发数据，并关闭channel
            ByteBuffer sendBuffer = ByteBuffer.wrap(URLEncoder.encode("回发处理结果", "UTF-8").getBytes());
            clientSocketChannel.write(sendBuffer);
            clientSocketChannel.close();
        } else {
            logger.info("端口：{} 客户端信息还未接收完，继续接收：{}", resourcePort, message);
            //这时，limit和capacity的值一致，position的位置是realLen的位置
            contextBytes.position(realLen);
            contextBytes.limit(contextBytes.capacity());
        }
    }

    /**
     * 在server socket channel接收到/准备好 一个新的TCP连接后，就会向程序返回一个新的socket channel
     * 但这个新的socket channel并没有在select“选择器/代理期”中注册
     * 所以程序还没办法通过selector通知这个socket channel的事件
     * 于是我们拿到新的socket channel后，要做的第一件事就是到selector“选择器/代理期”中注册这个socket channel感兴趣的事件
     *
     * @param socketChannel 新的socket channel
     * @param selector      selector"选择器/代理器"
     */
    private static void registerSocketChannel(SocketChannel socketChannel, Selector selector) throws IOException {
        socketChannel.configureBlocking(false);
        //socket通道可以且只可以注册三种事件 OP_READ 、OP_WRITE、OP_CONNECT
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2048));
    }

}
