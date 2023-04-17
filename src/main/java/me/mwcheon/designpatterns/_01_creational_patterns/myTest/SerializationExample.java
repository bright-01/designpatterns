package me.mwcheon.designpatterns._01_creational_patterns.myTest;

import java.io.*;

public class SerializationExample {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Student student = new Student("JS", 123);

        //serialization 직렬화
        // 자바 시스템 내부에서 사용되는 객체 또는 데이터를 외부의 자바 시스템에서도 사용 할 수 있도록 바이트(byte) 형태로 데이터 변환하는 기술
        File file = new File("./student.file");
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(student);
            oos.flush();
        }

        // deserialization 역직렬화
        // 바이트로 변환된 데이터를 다시 객체로 변환하는 기술
        Student result = null;
        try(FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis)){
            result = (Student) ois.readObject();
        }

        System.out.println("out put : " + result.toString());
    }
}
