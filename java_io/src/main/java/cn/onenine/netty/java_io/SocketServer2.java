package cn.onenine.netty.java_io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/11/30 22:22
 */
public class SocketServer2 {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer2.class);

    private static final ConcurrentHashMap<Integer, StringBuffer> MESSAGE_HASH_CONTEXT = new ConcurrentHashMap<>();

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
        int resoucePort = sourceSocketAddress.getPort();

        //拿到这个socket channel使用的缓冲区，准备读取数据
        //在后文中，将详细讲解缓存区的用法概念，实际上重要的就是三个元素capacity、position和limit
        ByteBuffer contextBytes = (ByteBuffer) readyKey.attachment();
        //将通道的数据写入到缓存区中，注意是写到缓存区
        //这次为了延时buff的使用方法，我们故意缩小了buff的容量到50byte
        //以便演示channel对buffer的多次读写操作
        int realLen = 0;
        StringBuffer message = new StringBuffer();
        //这句话的意思是，将目前同道中人的数据写入到缓存区中
        //最大可写入的数据量就是buff的容量
        while ((realLen = clientSocketChannel.read(contextBytes)) != 0) {
            //一定要把buffer切换成“读”模式，否则由于limit = capacity，在read没有写满的情况下，就会导致多读
            contextBytes.flip();
            int position = contextBytes.position();
            int capacity = contextBytes.capacity();
            byte[] messageBytes = new byte[capacity];
            contextBytes.get(messageBytes, position, realLen);

            //这种方式也是可以读取数据的，而且不用关心position的位置
            //因为是目前contextBytes所有的数据全部转换出为一个byte数组
            //使用这种方式时，一定要自己控制好读取的最终位置(realLen很重要)
            //byte[] messageBytes = contextBytes.array();

            //注意中文乱码的问题，可以使用URLDecoder/URLEncoder进行编解码
            String messageEncode = new String(messageBytes, "UTF-8");
            message.append(messageEncode);

            //再切换回“写模式”，直接清空缓存的方式最快捷
            contextBytes.clear();
        }

        //如果发现本次接收的信息中有over关键字，说明信息接收完了
        if (URLDecoder.decode(message.toString(), "UTF-8").contains("over")) {
            //则从messageHashContext中，取出之前已经收到的信息，组合成完整的信息
            int channelUUID = clientSocketChannel.hashCode();
            logger.info("端口：{} 客户端发来的信息======message：{}", resoucePort, message);
            StringBuffer completeMessage;
            //清空MESSAGE_HASH_CONTEXT中的历史记录
            StringBuffer historyMessage = MESSAGE_HASH_CONTEXT.remove(channelUUID);
            if (historyMessage == null) {
                completeMessage = message;
            } else {
                completeMessage = historyMessage.append(message);
            }
            logger.info("端口：{} 客户端发来的完整信息========completeMessage：{}", resoucePort, completeMessage);
            //======================================================
            //          当然接受完成后，可以在这里正式处理业务了
            //======================================================

            //回发数据，并关闭channel
            ByteBuffer sendBuffer = ByteBuffer.wrap(URLEncoder.encode("回发处理结果", "UTF-8").getBytes());
            clientSocketChannel.write(sendBuffer);
            clientSocketChannel.close();
        } else {
            //如果没发现over关键字，说明还没接收完，则将本次接收的信息存入messageHashContext
            logger.info("端口: {} 客户端信息还未接受完，继续接受======message : {}", resoucePort, URLDecoder.decode(message.toString(), "UTF-8"));
            //每一个channel对象都是独立的，所以可以使用对象的hash值作为唯一标识
            int channelUUID = clientSocketChannel.hashCode();

            StringBuffer historyMessage = MESSAGE_HASH_CONTEXT.get(channelUUID);
            if (historyMessage == null) {
                historyMessage = new StringBuffer();
                MESSAGE_HASH_CONTEXT.put(channelUUID,historyMessage.append(message));
            }
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
