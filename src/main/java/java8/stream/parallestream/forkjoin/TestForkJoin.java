package java8.stream.parallestream.forkjoin;

import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class TestForkJoin {

    /**
     * JAVA7 Fork/Join框架计算累加
     */
    @Test
    public void test1(){
        Instant start = Instant.now();

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoinCaculate(0,10000000000L);
        Long result = pool.invoke(task);
        System.out.println(result);

        Instant end = Instant.now();
        System.out.println("耗费时间："+Duration.between(end,start).toMillis());
    }
    /**
     * 普通for循环计算累加
     */
    @Test
    public void test2(){
        Instant start = Instant.now();
        Long sum = 0L;
        for(long i = 0;i<=10000000000L;i++){
           sum += i;
        }
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println("耗费时间：" + Duration.between(start,end).toMillis());
    }

    //并行流程
    @Test
    public void test3(){
        Instant start =Instant.now();
        OptionalLong sum = LongStream.rangeClosed(0,10000000000L)
                .parallel() //设置为并行，sequential() 为 设置为串行
                .reduce(Long::sum);
        System.out.println(sum);
        Instant end = Instant.now();
        System.out.println("耗费时间："+ Duration.between(start,end).toMillis());
    }
}
