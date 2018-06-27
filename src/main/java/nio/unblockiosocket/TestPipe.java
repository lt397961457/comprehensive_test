package nio.unblockiosocket;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class TestPipe {
    @Test
    public void test1() throws IOException {
        //获取管道
        Pipe pipe = Pipe.open();
        //将缓冲区的数据写入管道
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Pipe.SinkChannel sinkChannel = pipe.sink();
        buffer.put("测试单向管道".getBytes());
        buffer.flip();
        sinkChannel.write(buffer);

        //读取缓冲区中的数据 注意读取的Pipe 和写入的Pipe 是同一个Pipe
        Pipe.SourceChannel sourceChannel = pipe.source();
        buffer.flip();
        sourceChannel.read(buffer);
        System.out.println(new String(buffer.array(),0,buffer.limit()));

        sourceChannel.close();
        sinkChannel.close();

    }
}
