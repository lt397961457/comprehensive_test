package java8.repeatannotation;

public @interface MyAnnotation {
    String value() default "Test1";
}
