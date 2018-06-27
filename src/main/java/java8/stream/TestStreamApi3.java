package java8.stream;

import java8.Employee;
import org.junit.Test;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 终止操作
 */
public class TestStreamApi3 {
    /**
     * 查找与匹配
     * allMatch -- 检查是否匹配所有元素
     * anyMatch -- 检查至少一个匹配
     * noneMatch -- 没有一个匹配
     * findFirst--返回第一个元素
     * findAny -- 返回流中任意的元素
     * count --返回流中的元素总个数
     * max -- 返回流中的最大值
     * min -- 返回流中的最小值
     *
     */

    List<Employee> employees = Arrays.asList(
            new Employee("张三",18,99.99, Employee.Status.BUSY),
            new Employee("李四",28,8.8,Employee.Status.FREE),
            new Employee("王五",48,7.7, Employee.Status.VOCATION),
            new Employee("赵六",78,4.4, Employee.Status.BUSY),
            new Employee("田七",18,3.3, Employee.Status.BUSY),
            new Employee("田七",18,3.3, Employee.Status.BUSY)
    );
    @Test
    public void test1(){
        boolean b1 =employees.stream()
                .allMatch((e) -> e.getStatus().equals(Employee.Status.BUSY));
        System.out.println(b1);

        boolean b2 = employees.stream()
                .anyMatch(e -> e.getStatus().equals(Employee.Status.BUSY));
        System.out.println(b2);

        boolean b3 = employees.stream()
                .noneMatch(e -> e.getStatus().equals(Employee.Status.BUSY));
        System.out.println(b3);

        Optional<Employee> employee = employees.stream()
                .sorted((e1,e2) -> Double.compare(e1.getSalary(),e2.getSalary()))
                .findFirst();
        System.out.println(employee.toString());

        Optional<Employee> employee2 = employees.stream()
                .filter(e -> e.getStatus().equals(Employee.Status.BUSY))
                .findAny();
        System.out.println(employee2.toString());
    }
    @Test
    public void test2(){
        Long count = employees.stream()
                .count();
        System.out.println(count);

        Optional<Employee> employee = employees.stream()
                .max((e1,e2) -> Double.compare(e1.getSalary(),e2.getSalary()));
        System.out.println(employee.toString());

        Optional<Double> minSalary = employees.stream()
                .map(e -> e.getSalary())
                .min(Double::compare);
        System.out.println(minSalary.get());
    }
    /**
     * 归约
     * reduce(T identity, BinaryOperator operator) / reduce(BinaryOperator operator)
     * - 可以将一个流中的元素反复结合起来，得到一个值
     */

    @Test
    public void test3(){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        Integer result = list.stream()
                .reduce(10,(e1,e2) -> e1 + e2); //10表示累加前的和的 基础值
        System.out.println(result);

        Optional<Integer> result2 = list.stream()
                .reduce(Integer::sum);
        System.out.println(result2);
    }

    /**
     * 收集
     * collect - 将流转换为其他形式。接受一个Collector接口的实现，用于给元素做汇总的方法
     * Collector 接口中方法的实现决定了 如何对流执行收集操作（如收集到List、Set、Map）。
     * 在 Collectors 工具实用类提供了很多静态方法，可以很方便的创建常见的收集器实例：
     *
     */
    @Test
    public void test4(){
        //以List收集
        List<String> result = employees.stream()
                .map((e) -> e.getName())
                .collect(Collectors.toList());
        System.out.println(result);

        //以Set收集
        Set<String> result2 = employees.stream()
                .map((e) -> e.getName())
                .collect(Collectors.toSet());
        System.out.println(result2);

        //其他;可以指定收集的集合
        HashSet<String> result3 = employees.stream()
                .map(Employee::getName)
//                .collect(Collectors.toCollection(() -> new HashSet<>()));
                .collect(Collectors.toCollection(HashSet::new));
        System.out.println(result3);

        //总数
        Long count = employees.stream()
                .collect(Collectors.counting());
        System.out.println(count);
        //平均
        Double avg = employees.stream()
                .collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(avg);

        //总和
        Double sum = employees.stream()
                .collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(sum);

        //工资最大值的员工
        Optional<Employee> max = employees.stream()
                .collect(Collectors.maxBy((e1,e2)->Double.compare(e1.getSalary(),e2.getSalary())));
        System.out.println(max);

        //工资最小值
        Optional<Double> minSalary = employees.stream()
                .map(Employee::getSalary)
//                .collect(Collectors.minBy((e1,e2)->Double.compare(e1,e2)));
                .collect(Collectors.minBy(Double::compare));
        System.out.println(minSalary);
    }
    //分组
    @Test
    public void test5(){
        Map<Employee.Status,List<Employee>> map = employees.stream()
//                .collect(Collectors.groupingBy(Employee::getStatus));
                .collect(Collectors.groupingBy((e) -> e.getStatus()));
        System.out.println(map);
    }

    //多列分组
    @Test
    public void test6(){
        Map<Employee.Status,Map<String,List<Employee>>> map =employees.stream()
                .collect(Collectors.groupingBy(Employee::getStatus,Collectors.groupingBy((e)->{
                    if(e.getAge()<=18){
                        return "少年";
                    }else if(e.getAge() <= 28){
                        return "青年";
                    }else
                    {
                        return "中年";
                    }
                })));
        System.out.println(map);
    }

    //分区（分片）
    @Test
    public void test7(){
        Map<Boolean,List<Employee>> map = employees.stream()
//                .collect(Collectors.partitioningBy(e -> e.getSalary() > 5));
                .collect(Collectors.partitioningBy(e -> e.getSalary() > 5));
        System.out.println(map);
    }

    //一次性 搜集 平均值/总和/最大/最小
    @Test
    public void test8(){
        DoubleSummaryStatistics dss = employees.stream()
                .collect(Collectors.summarizingDouble(e -> e.getSalary()));
        System.out.println(dss.getSum());
        System.out.println(dss.getAverage());
        System.out.println(dss.getMax());
        System.out.println(dss.getMin());

    }

    // 连接后收集
    @Test
    public void test9(){
        String str = employees.stream()
                .map(Employee::getName)
                .collect(Collectors.joining("-","start ||","|| end"));
        System.out.println(str);
    }



}
