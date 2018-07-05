package nio;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 通道测试
 * 通道：用于源节点与目标节点的连接
 * 在JAVA NIO中负责缓冲区中的数据传输。 Channel 本身不存储数据，因此需要配合缓冲区进行传输
 * 通道的主要实现类
 * java.nio.channels.Channel 接口：
 *  |-- FileChannel :本地文件传输
 *  |--SocketChannel ：TCP
 *  |--ServerSocketChannel
 *  |--DatagramChannel ：UDP
 *
 *  获取通道
 *   1、1.7以前Java针对支持通信的类提供了getChannel()方法
 *      FileInputStream/FileOutputStream
 *      RandomAccessFile
 *
 *     网络IO：
 *     Socket
 *     ServerSocket
 *     DatagramSocket
 *   2、在JDK1.7中的NIO.2 针对各个通道提供了静态方法open()
 *   3、在JDK1.7中的NIO.2 的Files工具类的newByteChannel()
 *
 *  通道之间的数据传输
 *  transferFrom()
 *  transferTo()
 *
 *  分散（Scatter）与聚集(Gather)
 *      分散读取（Scattering Reads）：将通道中的数据依次分散到多个缓冲区
 *      聚集写入（Gathering Write）：将多个缓冲区的数据依次聚集到通道中
 *
 * 字符集：Charset
 * 编码：字符串转换成字节数组的过程
 * 解码：字节数组转换成字符串的过程
 *
 */
public class TestChannel {
    //利用通道完成文件的复制
    @Test
    public void test1() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\LiTao\\Desktop\\aaa.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\LiTao\\Desktop\\bbb.txt");

        //获取通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();
        //分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //将通道中的数据存入缓冲区
        while (inChannel.read(buffer) != -1){
            buffer.flip(); //切换成读模式
            //将缓冲区的数据写入通道
            outChannel.write(buffer);
            buffer.clear();
        }
        outChannel.close();
        inChannel.close();
        fileOutputStream.close();
        fileInputStream.close();
    }

    //利用直接内存(只有bytebuffer支持，其他不支持) 和 1.7的FileChannel.open()方法创建Channel的方式 复制文件
    @Test
    public void test2() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\admin\\Desktop\\1.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\admin\\Desktop\\3.txt"),
                StandardOpenOption.READ,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE); //存在就覆盖，不存在就创建

        //创建直接缓冲区，和allocateDirect()方法功能一样，只是创建方式不同
        MappedByteBuffer inmappedbuf =
                inChannel.map(FileChannel.MapMode.READ_ONLY,
                        0,inChannel.size());
        MappedByteBuffer outmappedbuf = outChannel.map(
                FileChannel.MapMode.READ_WRITE, //和outChannel的模式要一致
                0,inChannel.size());

        //直接对缓冲区的数据读写操作
        byte[] tmp = new byte[inmappedbuf.limit()];
        inmappedbuf.get(tmp);
        outmappedbuf.put(tmp);
        inChannel.close();
        outChannel.close();

    }

    //通道（channel）之间的数据传输
    @Test
    public void test3() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("C:\\Users\\admin\\Desktop\\1.txt"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("C:\\Users\\admin\\Desktop\\5.txt"),
                StandardOpenOption.READ,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE); //存在就覆盖，不存在就创建

        //将inChannel数据传输给outChannel 也是使用的非直接缓存的方式
        inChannel.transferTo(0,inChannel.size(),outChannel);
        //或者
//        outChannel.transferFrom(inChannel,0,inChannel.size());
        inChannel.close();
        outChannel.close();
    }

    //分散（Scatter）与聚集(Gather)
    @Test
    public void test4() throws IOException {
        RandomAccessFile raf = new RandomAccessFile("C:\\Users\\admin\\Desktop\\1.txt","rw");
        //获取通道
        FileChannel channel = raf.getChannel();

        //分配制定的缓冲区
        ByteBuffer bf1 = ByteBuffer.allocate(100);
        ByteBuffer bf2 = ByteBuffer.allocate(1024);

        //分散读
        ByteBuffer[] bufs = {bf1,bf2};
        channel.read(bufs);

        for(ByteBuffer buffer : bufs){
            buffer.flip();
        }
        System.out.println(new String(bufs[0].array(),0,bufs[0].limit()));
        System.out.println("-----------------------------");
        System.out.println(new String(bufs[1].array(),0,bufs[1].limit()));

        //聚集写入

        RandomAccessFile randomAccessFile = new RandomAccessFile("C:\\Users\\admin\\Desktop\\6.txt","rw");
        FileChannel channel2 = randomAccessFile.getChannel();
        channel2.write(bufs);
    }

    //Charset 编码器
    @Test
    public void test5() throws CharacterCodingException {
        Charset charset = Charset.forName("GBK");

        CharsetEncoder ce = charset.newEncoder(); //编码器
        CharsetDecoder cd = charset.newDecoder(); //解码器

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("一群人影子嬴政");
        charBuffer.flip();

        //编码：字符串转换成字节数组
        ByteBuffer bbuf = ce.encode(charBuffer);

        byte[] tmp = new byte[bbuf.limit()];
        bbuf.get(tmp);
        for(byte b : tmp){
            System.out.println(b);
        }

        //解码：字节数组转换成字符串
        System.out.println("================");
        bbuf.flip();
        CharBuffer charBuffer2 = cd.decode(bbuf);
        char[] charTmp = new char[charBuffer2.limit()];
        charBuffer2.get(charTmp);

        charBuffer2.flip();
        System.out.println(charBuffer2.toString());
        System.out.println(charTmp);

    }

}
