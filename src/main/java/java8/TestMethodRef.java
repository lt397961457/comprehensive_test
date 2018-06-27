package java8;

import org.junit.Test;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 一、方法引用： 若 lambda 体重的内容有方法已经实现了，我们可以使用‘方法引用’
 *      主要有三种语法格式：
 *      对象::实例方法名
 *      类::静态方法名
 *      类::实例方法名  注意有条件限制
 * 二、构造器引用
 *      ClassName::new
 * 三、数组引用
 *      Type[]::new
 */
public class TestMethodRef {

    List<Employee> employees = Arrays.asList(
            new Employee("张三",18,99.99),
            new Employee("李四",28,8.8),
            new Employee("王五",48,7.7),
            new Employee("赵六",78,4.4),
            new Employee("田七",18,3.3)
    );
    @Test
    public void ttt(){
        employees.forEach((e) ->
        {
            String str = e.getName() + e.getAge();
            System.out.println(str);
        });
    }
    @Test
    public void test1(){
        Consumer<String> consumer = (x) -> System.out.println(x);

        PrintStream ps= System.out;
        Consumer<String> consumer2 = ps::println;
    }
    // 对象::实例方法名
    //方法引用的时候  被引用的方法 必须要和函数式接口中方法的  参数、返回值 类型保持一致,且方法引用不能单独使用
    @Test
    public void test2(){
        Employee employee = new Employee();
        Supplier<String> stringSupplier = () -> employee.getName();
        Supplier<String> stringSupplier2 = employee::getName;
        Supplier<String> stringSupplier3 = new Employee()::getName;
    }

    //类::静态方法
    @Test
    public void test3(){
        Comparator<Integer> comparator = (x,y) -> Integer.compare(x,y);
        Comparator<Integer> comparator1 = Integer::compare;
    }
    //类::实例方法名
    //规则：lambda参数列表中 第一个参数 是这个实例方法的调用者，而第二个参数 是这个实例方法的参数的时候 可以使用
    @Test
    public void test4(){
        BiPredicate<String,String> bp = (x,y) ->x.equals(y);

        BiPredicate<String,String> bp2 = String::equals;

        // 也可以是 lambda参数列表只有一个参数，这个参数是就是实例方法调用者
        Function<Employee,Double> fun1 = (e) -> e.getSalary();
        Function<Employee,Double> fun2 = Employee::getSalary;


    }

    //构造器引用
    //同样：会根据函数式接口中参数的个数调用有对应参数的构造
    @Test
    public void test5(){
        Supplier<Employee> supplier = () -> new Employee();
        //调用的无参构造
        Supplier<Employee> supplier1 = Employee::new;

        Function<Integer,Employee> function = (x)-> new Employee(x);
        //调用一个参数的构造
        Function<Integer,Employee> function2 = Employee::new;
        System.out.print(function2.apply(55).getAge());
    }

    //数组引用
    @Test
    public void test6(){
        Function<Integer,String[]> function = (x) -> new String[x]; //长度为x的数组
        function.apply(10);
        Function<Integer,String[]> function1 = String[]::new;
        function1.apply(20);
    }
}

