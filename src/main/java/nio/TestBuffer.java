package nio;

import java.nio.ByteBuffer;

/**
 * NIO 缓冲区测试
 * 缓冲区的4个核心属性：
 * capacity：分配一个指定大小的缓冲区，底层是数组，申明之后不能改变
 * limit: 界限，表示缓冲区中可以操作数据的大小。（limit后的数据不能进行读写）
 * position: 位置，表示缓冲区中正在操作数据的位置。
 * position <= limit <= capacity
 * mark:标记用记录当前的位置，可以同个reset回到mark的位置
 *
 *
 * put() 存入数据到缓冲区，get() 获取缓冲区去的数据
 * hasRemaining()缓冲区中是否还存在可以操作的数据
 * remianning() 获取缓冲区中 可以操作的数据的数量
 *
 * 非直接缓冲区：通过allocate()方法分配，将缓冲区建立在JVM的内存中
 * 直接缓冲区：通过allocateDirect()方法分配，将缓冲区建立在物理内存中，可以提高效率,
 *              但是执行多次可能会出现服务器内存剧增，因为物理内存非JVM控制，不能及时释放
 * buf.isDirect()判断当前的缓冲区是否是直接缓冲区
 */
public class TestBuffer {


    public static void main(String[] args) {
        //分配一个指定大小的缓冲区，底层是数组，申明之后不能改变
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.limit());//1024
        System.out.println(byteBuffer.capacity());
        //put()存入数据
        byteBuffer.put("abcde".getBytes());
        System.out.println(byteBuffer.position()); //5
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        //切换成读取数据的模式 flip() /position 会变为0 最开始位置，limit变为5
        byteBuffer.flip();
        System.out.println(byteBuffer.position()); //0
        System.out.println(byteBuffer.limit()); //5
        System.out.println(byteBuffer.capacity());
        // get 读取缓冲区中的数据 一次获取所有数据,读完以后byteBuffer中的数据依然存在
        byte[] dst = new byte[byteBuffer.limit()]; //大小超出byteBuffer的limit会报错
        byteBuffer.get(dst);  //读取指定数组大小的数据到指定数组中
        System.out.println(new String(dst,0,dst.length));
        System.out.println(byteBuffer.position()); //5
        System.out.println(byteBuffer.limit()); //5
        System.out.println(byteBuffer.capacity());

        //rewind() 可重复读数据，使position 回到0位置
        byteBuffer.rewind();
        System.out.println(byteBuffer.position()); //0
        System.out.println(byteBuffer.limit()); //5
        System.out.println(byteBuffer.capacity());

        byteBuffer.get(dst);
        System.out.println(new String(dst,0,dst.length));

        //clear 清空缓冲区,只是清空‘指针’，
        // 但是缓冲区中的数据还是存在，只是数据还是处于被遗忘状态
        byteBuffer.clear();
        System.out.println(byteBuffer.position()); //0
        System.out.println(byteBuffer.limit()); //1024
        System.out.println(byteBuffer.capacity());
        byteBuffer.get(dst);
        System.out.println(new String(dst,0,5));
    }

}
