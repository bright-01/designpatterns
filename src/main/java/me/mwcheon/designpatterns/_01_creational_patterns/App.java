package me.mwcheon.designpatterns._01_creational_patterns;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class App {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

//        new Setting(); // private로 생성자를 만들어 외부에서 new로 생성 하지 못 함

        Settings settings1 = Settings.getInstance();
        Settings settings2 = Settings.getInstance();

        System.out.println(settings1 == settings2);




        // 1. 싱글톤 패턴 구현 방법을 깨뜨리는 방법 - 리플랙션 사용하기
        Settings s1 = Settings.getInstance();

        Constructor<Settings> constructor = Settings.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Settings newSettings = constructor.newInstance();

        System.out.println(s1 != newSettings);

    }
}
