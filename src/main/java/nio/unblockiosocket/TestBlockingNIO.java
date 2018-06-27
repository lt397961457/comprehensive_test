package nio.unblockiosocket;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 使用NIO完成网络通信的三个核心
 * 1.通道（Channel）：负责链接
 *      java.nio.channels.Channel
 *          |-- SelectableChannel
 *              |-- SocketChannel
 *              |-- ServrSocketChannel
 *              |-- DatagramChannel  UDP协议使用
 *
 *              |-- Pipe.SinkChannel
 *              |-- Pipe.SourceChannel
 * 2.缓冲区（Buffer）：负责数据存取
 *
 * 3.选择器（Selector）：是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状态
 *
 */
public class TestBlockingNIO {
    @Test
    public void client() throws IOException {
        //获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));

        //准备一个用于读取本地文件的channel
        FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\admin\\Desktop\\1.txt"), StandardOpenOption.READ);

        //分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //读取本地文件并发送到服务端
        while(inChannel.read(buffer) != -1){
            buffer.flip();
            sChannel.write(buffer);
            buffer.clear();
        }
        //关闭通道
        inChannel.close();
        sChannel.close();
    }
    @Test
    public void sever() throws IOException {
        //获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //准备一个文件通道，用于一会得文件写到本地磁盘
        FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\admin\\Desktop\\testsocket.txt"),
                StandardOpenOption.WRITE,StandardOpenOption.CREATE);

        //绑定链接端口
        ssChannel.bind(new InetSocketAddress(9898));

        //获取客户端链接的通道
        SocketChannel sChannel = ssChannel.accept();

        //分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //接收客户端请求，并保存到本地
        while(sChannel.read(buffer) != -1){
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }

        //关闭通道

        sChannel.close();
        outChannel.close();
        ssChannel.close();
    }
}
