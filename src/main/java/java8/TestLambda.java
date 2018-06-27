package java8;

import org.junit.Test;

import java.util.*;

public class TestLambda {
    //原来的匿名内部类
    @Test
    public void test1(){
        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1,o2);
            }
        };
        TreeSet<Integer> ts = new TreeSet<>(comparator);
    }

    //Lambda表达式
    @Test
    public void test(){
        Comparator<Integer> comparator = (x,y) ->Integer.compare(x,y);
        TreeSet<Integer> ts = new TreeSet<>(comparator);
    }

    List<Employee> employees = Arrays.asList(
      new Employee("张三",18,99.99),
      new Employee("李四",28,8.8),
      new Employee("王五",48,7.7),
      new Employee("赵六",78,4.4),
      new Employee("田七",18,3.3)
    );

    @Test
    public void test3(){
        List<Employee> list = filterEmployee(employees);
        for(Employee employee : list){
            System.out.println(employee.toString());
        }
    }

    @Test
    public void test4(){
        List<Employee> list = filterEmployeeByCompartor(employees,(e) -> e.getSalary() > 5);
        list.forEach(System.out::println);
    }

    //使用Stream过滤
    @Test
    public void test5(){
        employees.stream()
                .filter((e)->e.getSalary() > 5)
                .limit(2)
                .forEach(System.out::println);
    }
    //使用Stream 获取name
    @Test
    public void test6(){
        employees.stream()
                .map(Employee::getName)
                .forEach(System.out::println);
    }
    //原来的方式 查询年龄大于35的员工
    public List<Employee> filterEmployee(List<Employee> employees){
        List<Employee> result = new ArrayList<>();
        for(Employee employeee: employees){
            if(employeee.getAge() >= 35){
                result.add(employeee);
            }
        }
        return result;
    }

    //lambda 根据Compartor查询员工
    public List<Employee> filterEmployeeByCompartor(List<Employee> employees,EmployCusComparator<Employee> cusComparator){
        List<Employee> result = new ArrayList<>();
        for(Employee employeee: employees){
            if(cusComparator.compara(employeee)){
                result.add(employeee);
            }
        }
        return result;
    }
}
