package com.example.shop2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.support.XmlWebApplicationContext;

@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class })
public class Shop2Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Shop2Application.class, args);

		// ApplicationContext에 등록된 빈 이름 조회
		String[] beanNames = context.getBeanDefinitionNames();
		System.out.println("Registered beans: ");
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}

}
