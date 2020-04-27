package xyz.bumbing.main;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class H2DbApplication {

	// 초기 데이터넣기
	@Bean
	InitializingBean saveData(H2Repository repo) {
		return () -> {
			repo.save(new H2Entity("InitializingBean1", "초기화1"));
			repo.save(new H2Entity("InitializingBean2", "초기화2"));
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(H2DbApplication.class, args);
	}

}
