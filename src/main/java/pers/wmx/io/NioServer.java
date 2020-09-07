package pers.wmx.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author: wangmingxin03
 * @date: 2020-09-07
 */
public class NioServer {
    public static void main(String[] args) throws IOException {
        //选择器
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //channel 设置为非阻塞 最终调的是一个native方法
        serverSocketChannel.configureBlocking(false);
        //注册连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //基于serverSocketChannel创建一个服务端Socket
        ServerSocket socket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress("localhost", 8888);
        socket.bind(address);

        while(true) {
            //系统调用 阻塞
            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();

            //把所有注册的key遍历一遍
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                //有新连接
                if (key.isAcceptable()) {
                    ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

                    //为客户端创建一个新的channel
                    SocketChannel clientChannel = serverChannel.accept();
                    System.out.println("新连接...");
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    System.out.println("数据到达...");
                    SocketChannel clientChannel = (SocketChannel)key.channel();
                    readDataFromChannel(clientChannel);
                }

                keyIterator.remove();
            }
        }
    }

    private static void readDataFromChannel(SocketChannel clientChannel) throws IOException {
        ByteBuffer readbuffer = ByteBuffer.allocate(1024);

        // 读取请求码流，返回读取到的字节数
        int readBytes = clientChannel.read(readbuffer);
        // 读取到字节，对字节进行编解码
        if (readBytes > 0) {
            // 将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
            readbuffer.flip();//读写模式反转

            byte[] bytes = new byte[readbuffer.remaining()];
            readbuffer.get(bytes);
            String body = new String(bytes, "UTF-8");
            System.out.println(body);

            output(clientChannel, body);
        }
    }

    private static void output(SocketChannel channel, String response) throws IOException {
        if (response != null && response.length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }

}
