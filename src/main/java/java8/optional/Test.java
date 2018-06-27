package java8.optional;

import java8.Employee;

import java.util.Optional;

public class Test {

    @org.junit.Test
    public void test1(){
//        Optional<Employee> op = Optional.of(null); //报错
        Optional<Employee> op2 = Optional.empty();
        System.out.println(op2);
//        System.out.println(op2.get()); //报错
    }
}
