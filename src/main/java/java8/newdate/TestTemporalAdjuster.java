package java8.newdate;

import org.junit.Test;

import javax.xml.transform.sax.SAXSource;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;

/**
 * 1、时间校正器TemporalAdjuster
 * 有时候 我们可能需要获取例如：将日期调整到“下个周日”等操作
 * TemporalAdjusters：工具类，该通过静态方法提供了大量的常用TemporalAdjuster的实现
 *
 * 2、格式化时间/日期：DateTimeFormatter
 *
 * 3、ZoneDate\ZoneTime\ZoneDateTime
 */
public class TestTemporalAdjuster {
    //时间校正器TemporalAdjuster
    @Test
    public void test1(){
        LocalDateTime ltd = LocalDateTime.now();
        System.out.println(ltd);

        LocalDateTime ldt2 = ltd.withDayOfMonth(10);
        System.out.println(ltd);

        LocalDateTime ldt3 = ltd.with(TemporalAdjusters.next(DayOfWeek.MONDAY)); //相对于ltd的下一个周一
        System.out.println(ldt3);


        //自定义：下一个工作日
        LocalDateTime ldt4 =ltd.with((a) -> {
            LocalDateTime tmp = (LocalDateTime) a;
            DayOfWeek dayOfWeek = tmp.getDayOfWeek();

            LocalDateTime result ;
            if(dayOfWeek.equals(DayOfWeek.FRIDAY)){
                result = tmp.plusDays(3);
            }else if(dayOfWeek.equals(DayOfWeek.SATURDAY)){
                result = tmp.plusDays(2);
            }else {
                result = tmp.plusDays(1);
            }
            return result;
        });

        System.out.println(ldt4);
    }

    //格式化时间/日期：DateTimeFormatter
    @Test
    public void test2(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME; //默认就是这个格式
        LocalDateTime ldt = LocalDateTime.now();
        String strDate = ldt.format(dateTimeFormatter);
        System.out.println(ldt);
        System.out.println(strDate);

        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
        String strDate2 = ldt.format(dateTimeFormatter2);
        System.out.println(strDate2);

        LocalDateTime newDate = ldt.parse(strDate2,dateTimeFormatter2); //对于JDK8支持的不太友好，不会给提示
        System.out.println(newDate);
    }

    //Zone
    @Test
    public void test3(){
    }
}
