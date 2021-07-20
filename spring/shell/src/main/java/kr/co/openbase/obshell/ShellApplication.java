package kr.co.openbase.obshell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication
public class ShellApplication {

    public static void main(String[] args) {
	SpringApplication.run(ShellApplication.class, args);
    }

    @Bean
    public PromptProvider obshellProvider() {
	return () -> new AttributedString("OBSHELL:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
    }
}
