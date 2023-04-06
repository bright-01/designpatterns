package me.mwcheon.designpatterns._01_creational_patterns.myTest;

public class TestClass {

//    private static TestClass instance;

    private TestClass() { }

    private static class TestClassHolder {
        private static final TestClass TEST_CLASS = new TestClass();
    }


    public static TestClass getInstance() {
        return TestClassHolder.TEST_CLASS;
    }


}
