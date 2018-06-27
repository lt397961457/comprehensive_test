package java8.newdate;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * 1.8以前 时间类不是线程安全的
 */
public class TestSimpleDateFormat {

    //测试1.8以前的SimpleDateFormat的线程不安全
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd"); //线程不安全
//        Callable<Date> task = new Callable<Date>() {
//            @Override
//            public Date call() throws Exception {
//                return sdf.parse("20180124");
//            }
//        };
        //s使用lambda表达式
        Callable<Date> task = () -> sdf.parse("20180124");

        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<Date>> results = new ArrayList<>();
        for(int i=0;i<10;i++){
//            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyMMdd"); //线程不安全
//            Callable<Date> task2 = () -> sdf2.parse("20180124");

            //如果每次提交的是同一个task，就会出现线程问题，会报错
            //如果每次提交的task 和 sdf 都是new的 不会报错
            results.add(pool.submit(task));
        }
        for(Future<Date> future : results){
            System.out.println(future.get());
        }
        pool.shutdown();
    }

    private static final ThreadLocal<SimpleDateFormat> df = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };
    /**
     * 测试1.8以前，SimpleDateFormat 线程不安全的处理办法：使用ThreadLocal<SimmpleDateFormat>
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test2() throws ExecutionException, InterruptedException {
        Callable<Date> task = () -> df.get().parse("20180124");
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<Future<Date>> results = new ArrayList<>();
        for(int i=0;i<10;i++){
            results.add(pool.submit(task));
        }
        for(Future<Date> future : results){
            System.out.println(future.get());
        }
        pool.shutdown();
    }

    /**
     * 使用JDK8 新的线程安全的 时间控件
     */
    @Test
    public void test3() throws ExecutionException, InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyMMdd");
        Callable<LocalDate> task = () -> LocalDate.parse("20110606",dtf);

        ExecutorService pool = Executors.newFixedThreadPool(10);

        List<Future<LocalDate>> results = new ArrayList<>();

        for(int i=0;i<10;i++){
            results.add(pool.submit(task));
        }

        for(Future<LocalDate> future : results){
            System.out.println(future.get());
        }
    }
}
