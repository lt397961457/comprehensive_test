package java8.stream;

import java8.Employee;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


/**
 * Stream的三个操作步骤
 * 1.创建Stream
 * 2.中间操作
 * 3.终止操作
 */
public class StreamAPI1 {
    //创建Stream
    @Test
    public void test1(){
        //1.可以通过Collection 系列集合提供的stream()方法创建串行流 或者 paralleStream()创建并行流
        List<String> list = new ArrayList<>();
        Stream<String> stream = list.stream();

        //2.可以通过Arrays中的静态方法strea()获取数组流
        Employee[] emps = new Employee[10];
        Stream<Employee> stream1 = Arrays.stream(emps);

        //3.可以通过Stream类中的静态方法of()
        Stream<String> stringStream = Stream.of("aa","bb","cc");

        //4.创建无限流
        //4.1 迭代
        Stream<Integer> stream2 = Stream.iterate(0,(x) -> x+2);
//        stream2.forEach(System.out::println);
        stream2.limit(10).forEach(System.out::println);

        //4.2生成
        Stream<Double> stream3 = Stream.generate(() -> Math.random());
//        stream3.forEach(System.out::println);
        stream3.limit(10).forEach(System.out::println);

    }
}
