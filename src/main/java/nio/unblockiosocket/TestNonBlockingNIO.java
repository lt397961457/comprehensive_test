package nio.unblockiosocket;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 非阻塞NIO
 */
public class TestNonBlockingNIO {
    @Test
    public void client() throws IOException {
        //获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
        //切换成非阻塞模式
        sChannel.configureBlocking(false);
        //分配缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //发送数据给数据端
        int i = 0;
        while(i <= 10){
            String msg = new Date().toString() + "\n" + i;
            buf.put(msg.getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
            i++;
        }


        //关闭通道
        sChannel.close();
    }
    @Test
    public void server() throws IOException {
        //1、获取通道
        ServerSocketChannel ssChannel =  ServerSocketChannel.open();
        //2、切换成非阻塞模式
        ssChannel.configureBlocking(false);
        //3、绑定端口
        ssChannel.bind(new InetSocketAddress(9898));
        //4、获取选择器
        Selector selector = Selector.open();

        //5、将通道注册到选择器上并且指定监听事件,
        // 第二个参数是选择键，用于设定通道的事件监听：
        //SelectionKey.OP_READ(1):读
        //SelectionKey.OP_WRITE(4)：写
        //SelectionKey.OP_CONNECT(8)：连接
        //SelectionKey.OP_ACCEPT(16)：接收
        // 多个键的时候 用“|“运算
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6、轮询式的获取选择器上的已经准备就绪的事件
        while(selector.select() > 0){
            //获取当前选择器中所有注册的且已经就绪的 选择键
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                //获取准备就绪的事件
                SelectionKey sk = it.next();
                //判断具体是什么事件准备就绪
                if(sk.isAcceptable()){
                    //如果接收就绪，获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();
                    // **************将客户端连接 切换成非阻塞********
                    sChannel.configureBlocking(false);
                    //将客户端通道注册到选择器上
                    sChannel.register(selector,SelectionKey.OP_READ);
                }else if(sk.isReadable()){
                    //获取选择器上读就绪的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();

                    //读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len = 0 ;
                    while ((len = sChannel.read(buf)) != -1){
                        buf.flip();
                        System.out.println(new String(buf.array(),0,len));
                        buf.clear();
                    }
                }
                //取消已经执行的选择键，否则每次while进入都会执行一次
                it.remove();
            }
        }



    }
}
