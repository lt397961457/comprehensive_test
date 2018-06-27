package java8;


public class TestImpl implements TetsInterFace1,TetsInterFace2 {
    @Override
    public void test() {

    }

    @Override
    public void test2(String name) {
        TetsInterFace2.super.test2(name);
    }


    public static void main(String[] args) {
        TestImpl impl = new TestImpl();
        impl.test2("nb");
        TetsInterFace1.testStaicMethod();
    }
}
