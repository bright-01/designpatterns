package me.mwcheon.designpatterns;

import me.mwcheon.designpatterns._01_creational_patterns.myTest.TestClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesignpatternsApplication {

	public static void main(String[] args) {

		TestClass t1 = TestClass.getInstance();
		TestClass t2 = TestClass.getInstance();

		System.out.println(t1 == t2);

		SpringApplication.run(DesignpatternsApplication.class, args);
	}

}
