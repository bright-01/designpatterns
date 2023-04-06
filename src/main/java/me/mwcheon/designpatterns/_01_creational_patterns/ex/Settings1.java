package me.mwcheon.designpatterns._01_creational_patterns.ex;

/**
 * private 생성자와 public static 메소드를 사용하는 방법
 */
public class Settings1 {

    private static Settings1 instance;

    private Settings1() { }

    public static Settings1 getInstance() {
        if (instance == null) {
            instance = new Settings1();
        }

        return instance;
    }

}
