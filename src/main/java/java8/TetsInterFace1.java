package java8;

public interface TetsInterFace1 {
    public void test();
    default public void test2(String name){
        System.out.println(name );
    }
    public static void testStaicMethod(){
        System.out.println("interface static method" );
    }
}
