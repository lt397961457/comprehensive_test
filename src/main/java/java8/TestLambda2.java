package java8;

import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;

/**
 * 一、Lambda 表达式基础语法
 * lambda 表达式需要函数是接口的支持
 * 函数是接口：接口中只用一个抽象方法。可以是使用注解@FunctionalInterface修饰 该接口，是的该接口中只有一个方法，否则报错
 */
public class TestLambda2 {

    // 无参 lambda
    @Test
    public void test1(){
        Runnable r = () -> System.out.println("wqwqwqw");
        r.run();
        Thread t = new Thread(r);
        t.start();
    }
    //一个参数，无返回值
    @Test
    public void test2(){
        Consumer<String> con = (x) -> System.out.println(x);
        con.accept("sdsdsdsd");
    }

    //若lambda 只有一个参数 ，小括号可以省略
    @Test
    public void test3(){
        Consumer<String> con = x -> System.out.println(x);
        con.accept("sdsdsdsd");
    }

    // 多个参数，lambda体中有多条语句 使用{ }
    @Test
    public void test4(){
        Comparator<Integer> com = (x,y) -> {
            System.out.println("aaaaaa");
            return Integer.compare(x,y);
        };
        System.out.println(com.compare(1,2));
    }

    //如果lambda体中只有一条语句，return 和{ } 都可以省略
    //参数的数据类型可以省略不写，因为JVM的上下文可以推断出数据类型
    //但是如果要写数据类型，就必须所有参数都指定
    @Test
    public void test5(){
        Comparator<Integer> com = (Integer x,Integer y) -> Integer.compare(x,y) ;
        System.out.println(com.compare(1,2));
    }

    //练习

    List<Employee> employees = Arrays.asList(
            new Employee("张三",18,99.99),
            new Employee("李四",28,8.8),
            new Employee("王五",48,7.7),
            new Employee("赵六",78,4.4),
            new Employee("田七",18,3.3)
    );
    @Test
    public void test6(){
        Collections.sort(employees,(e1,e2)->{
            if(e1.getAge() == e2.getAge()){
               return  e1.getName().compareTo(e2.getName());
            }else{
                return Integer.compare(e1.getAge(),e2.getAge());
            }
        });
        employees.forEach(System.out::println);

    }

}
