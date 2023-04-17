package me.mwcheon.designpatterns._01_creational_patterns;

import me.mwcheon.designpatterns._01_creational_patterns.myTest.Student;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class App {
    public static void main(String[] args) throws Exception {

//        new Setting(); // private로 생성자를 만들어 외부에서 new로 생성 하지 못 함

//        Settings settings1 = Settings.getInstance();
//        Settings settings2 = Settings.getInstance();
//
//        System.out.println(settings1 == settings2);
//
//
//
//
//        // 1. 싱글톤 패턴 구현 방법을 깨뜨리는 방법 - 리플랙션 사용하기
        Settings s1 = Settings.getInstance();

        Constructor<Settings> constructor = Settings.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Settings newSettings = constructor.newInstance();

        System.out.println(s1 != newSettings);

        // 2. 직렬화 & 역직렬화를 이용

        Settings settings3 = Settings.getInstance();
        Settings settings4 = null;
        try(ObjectOutput out = new ObjectOutputStream(new FileOutputStream("./settings.obj"))){
            out.writeObject(settings3);
        }

        try (ObjectInput in = new ObjectInputStream(new FileInputStream("./settings.obj"))) {
            settings4 = (Settings) in.readObject();
        }
        // 역직렬화를 통해서 객체를 생성 할때는 새로운 객체가 생성된다.
        System.out.println(settings3 == settings4);

        // 대안은 ? 객체안에 readResolve를 통해 객체가 생성될때 getInstance를 호출 하도록 오버라이딩함


    }
}
