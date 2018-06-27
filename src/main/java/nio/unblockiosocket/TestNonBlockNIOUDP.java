package nio.unblockiosocket;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Date;
import java.util.Iterator;

/**
 * 非阻塞UDP通信
 */
public class TestNonBlockNIOUDP {
    @Test
    public void send() throws IOException {
        //获取通道
        DatagramChannel dc = DatagramChannel.open();
        //非阻塞
        dc.configureBlocking(false);
        //设置缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //放入数据
        buf.put(new Date().toString().getBytes());
        buf.flip();
        dc.send(buf,new InetSocketAddress("127.0.0.1",9898)); //此处不能用write
        buf.clear();
    }

    @Test
    public void receive() throws IOException {
        //获取通道
        DatagramChannel dc = DatagramChannel.open();
        //切换非阻塞
        dc.configureBlocking(false);
        //绑定端口
        dc.bind(new InetSocketAddress(9898));
        //选择器
        Selector selector = Selector.open();
        //将通道注册到选择器
        dc.register(selector, SelectionKey.OP_READ);
        while(selector.select() > 0){
            Iterator<SelectionKey> it =  selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey sk = it.next();
                if(sk.isReadable()){
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    dc.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(),0,buf.limit()));
                    buf.clear();
                }
            }
            it.remove();
        }
    }
}
