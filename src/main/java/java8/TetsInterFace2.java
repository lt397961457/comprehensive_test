package java8;

public interface TetsInterFace2 {
    public void test();
    default public void test2(String name){
        System.out.println(name +"2");
    }
}
