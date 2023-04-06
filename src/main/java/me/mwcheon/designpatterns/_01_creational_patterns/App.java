package me.mwcheon.designpatterns._01_creational_patterns;

public class App {
    public static void main(String[] args) {

//        new Setting(); // private로 생성자를 만들어 외부에서 new로 생성 하지 못 함

        Settings settings1 = Settings.getInstance();
        Settings settings2 = Settings.getInstance();

        System.out.println(settings1 == settings2);

    }
}
