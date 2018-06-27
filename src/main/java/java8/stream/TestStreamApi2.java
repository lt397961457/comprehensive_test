package java8.stream;

import java8.Employee;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Stream中间操作:多个中间操作可以连接起来形成一个流水线，
 *              除非流水线出发终止操作，否则中间操作不会执行任何的处理
 *              而在终止操作时一次性全部处理，称为“惰性求值”
 */
public class TestStreamApi2 {

    List<Employee> employees = Arrays.asList(
            new Employee("张三",18,99.99),
            new Employee("李四",28,8.8),
            new Employee("王五",48,7.7),
            new Employee("王五",48,7.7),
            new Employee("赵六",78,4.4),
            new Employee("田七",18,3.3)
    );
    //中间操作

    /**
     * 筛选与切片
     * filter - 接收 Lambda，从流中排除某些元素
     * limit(n) - 截断流，使其元素不操过指定数量
     * skip(n) - 跳过元素，返回一个丢掉了前n个元素的流。若流中元素不足 n 个，则返回一个空流。与limit(n)互补。
     * distinct - 筛选，通过流所生成的元素的 hashCode() 和equals() 去除重复元素。
     */

    //filter
    @Test
    public void test1(){
        //中间操作：不会执行任何操作
        Stream<Employee> stream = employees.stream()
                                            .filter((e) -> {
                                                System.out.println("Stream API 的中间操作,内部迭代");
                                                return e.getAge() > 35;});
        //终止操作：一次性执行全部内容，及“惰性求值”
        stream.forEach(System.out::println); //上面的打印 只会在本次的终止操作（forEach）的时候才执行
    }

    //limit
    @Test  //重点理解下 下面的 两个中间操作的 && 运算
    public void test2(){
        employees.stream()
                .filter((e)->{
                    System.out.println("filter!!!");  // 此处并不一定是所有的元素都会执行，而是跟下面的limit相关
                    return  e.getSalary()>5;       //底层应该是 filter 和 limit内部都有各自的迭代
                })                                  // 且filter 和 limit 是 && 运算
                .limit(2)
                .forEach(System.out::println);
    }

    //skip
    @Test
    public void test3(){
        employees.stream()
                .filter((e) -> {
                    System.out.println("filter!!!");
                    return e.getSalary()>5;
                })
                .skip(2)
                .forEach(System.out::println);
    }

    //distinct 根据hashCode 和 equals 判断去重
    @Test
    public void test4(){
        employees.stream()
                .filter((e) -> {
                    System.out.println("filter");
                    return e.getSalary() > 5;
                })
                .distinct()
                .forEach(System.out::println);
    }


    /**
     * 映射
     * map - 接收 Lambda，将元素转换成其他形式或提取信息。接受一个函数作为参数，
     *          该函数会被应用到每一个元素，并将其映射成新的元素
     * flatMap - 接收一个函数作为参数，将流中的每个值都换成另外一个流，然后把所有流连接成一个流。
     *
     */

    //map
    @Test
    public void test5(){
        List<String> list = Arrays.asList("aaa","bbb","ccc","ddd","eee");
        list.stream()
                .map((str) -> str.toUpperCase())
                .forEach(System.out::println);

        System.out.println("------------------");
        employees.stream()
                .map(e -> e.getName())
                .forEach(System.out::println);

    }
    //flatMap
    @Test
    public void test6(){
        List<String> list = Arrays.asList("aaa","bbb","ccc","ddd","eee");

        // 没有使用flatMap 获取集合中的每个元素的么一个字符
        Stream<Stream<Character>> streamStream = list.stream()
                .map(TestStreamApi2::filterCharacter);
        streamStream.forEach(sm -> {
            sm.forEach(a -> System.out.println(a));
        });

        //使用flatMap
        //注意 与上面的 返回类型
        System.out.println("-------------------------");
        Function<String,Stream<Character>> function = x->{
            List<Character> result = new ArrayList<>();
            for(Character c : x.toCharArray()){
                result.add(c);
            }
            return result.stream();
        };
        Stream<Character> characterStream = list.stream()
                .flatMap(function); //即 将多个同类型流 压缩成一个流
        characterStream.forEach(System.out::println);

        System.out.println("-------------------------");
        Function<String,Stream<String>> function2 = x -> Stream.of(x);
        Stream<String> stringStream = list.stream()
                                    .flatMap(function2);
        stringStream.forEach(System.out::println);


    }

    public static Stream<Character> filterCharacter(String str){
        List<Character> list = new ArrayList<>();
        for (Character c : str.toCharArray()){
            list.add(c);
        }
        return list.stream();
    }


    /**
     * 排序
     * sorted() - 自然排序(Comparable)
     * sorted(Comparator com) - 根据Comparator排序
     */
    @Test
    public void test7(){
        List<String> list = Arrays.asList("ccc","bbb","aaa","ddd","eee");
        list.stream()
                .sorted()
                .forEach(System.out::println);
    }
    @Test
    public void test8(){
        employees.stream()
//                .sorted((e1,e2) -> Integer.compare(e1.getAge(),e2.getAge()))
                .sorted((e1,e2) -> e1.getName().compareTo(e2.getName()))
                .forEach(System.out::println);

    }

}
