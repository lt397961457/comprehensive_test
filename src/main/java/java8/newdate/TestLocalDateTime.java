package java8.newdate;

import org.junit.Test;

import java.time.*;

/**
 * 新的时间API 基本使用
 */
public class TestLocalDateTime {
    //LocalDate LcoalTime LocalDateTime
    @Test
    public void test1(){
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);

        LocalDateTime localDateTime2 = LocalDateTime.of(2017,1,1,17,26,50);
        System.out.println(localDateTime2);

        LocalDateTime localDateTime3 = localDateTime2.plusDays(50); //时间运算
        System.out.println(localDateTime3);

        //获取时间中的一些值
        System.out.println(localDateTime2.getMonthValue());
        System.out.println(localDateTime2.getMonth());
        System.out.println(localDateTime2.getYear());

    }

    //Instant: 时间戳（一UNIX 元年：1970年1月1号00:00:00 到某个时间之间的毫秒值）
    @Test
    public void test2(){
        Instant now = Instant.now();
        System.out.println(now); //默认 UTC 时区 格林威治时间
        System.out.println(now.toEpochMilli()); //转换成毫秒
        OffsetDateTime nowDateTime = now.atOffset(ZoneOffset.ofHours(8)); //设置时间偏移量

        Instant instant1 = Instant.ofEpochSecond(1000000); //通过设置 距离1970的秒数 获取时间
        System.out.println(instant1);
    }

    //Duration: 计算两个“时间”之间的间隔
    //Period:计算两个“日期” 之间的间隔
    @Test
    public void test3() throws InterruptedException {
        Instant instant1 = Instant.now();
        Thread.sleep(1000);
        Instant instant2 =Instant.now();

        Duration duration = Duration.between(instant1,instant2);
        System.out.println(duration);
        System.out.println(duration.getSeconds());
        System.out.println(duration.toMillis());
        System.out.println(duration.toDays());

        System.out.println("----------------------");

        LocalTime lt1 = LocalTime.now();
        Thread.sleep(1000);
        LocalTime lt2 = LocalTime.now();

        Duration duration1 = Duration.between(lt1,lt2);
        System.out.println(duration1);
        System.out.println(duration1.getSeconds());
        System.out.println(duration1.toMillis());
    }
}
