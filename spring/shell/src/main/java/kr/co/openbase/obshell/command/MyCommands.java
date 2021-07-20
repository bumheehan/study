package kr.co.openbase.obshell.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class MyCommands {

    @ShellMethod(value = "Display Stuff", key = "disp aaa", prefix = "-")
    public String add(int a, @ShellOption({ "-S", "--second" }) int b, @ShellOption("--third") int c) {
	return String.format("You said a=%d, b=%d, c=%d", a, b, c);
    }

    @ShellMethod(value = "Display Stuff", key = "disp sss", prefix = "-")
    public String sss(int a, @ShellOption({ "-S", "--second" }) int b, @ShellOption("--third") int c) {
	return String.format("You said a=%d, b=%d, c=%d", a, b, c);
    }

    @ShellMethod(value = "Display Stuff", key = "disp", prefix = "")
    public String test(int a, @ShellOption({ "second" }) int b, @ShellOption("third") int c) {
	return String.format("You said a=%d, b=%d, c=%d", a, b, c);
    }

    @ShellMethod("Say Hello")
    public String greet(@ShellOption(defaultValue = "World") String who) {
	return "Hello" + who;
    }

    // Arity? 배열?
//    shell:>add 1 2 3.3
//    6.3
//    shell:>add --numbers 1 2 3.3
//    6.3
    @ShellMethod("Add Numbers.")
    public float add(@ShellOption(arity = 3) float[] numbers) {
	return numbers[0] + numbers[1] + numbers[2];
    }

    // boolean, 해당 옵션있으면 true
//    shell:>shutdown
//    You said false
//    shell:>shutdown --force
//    You said true
    @ShellMethod("Terminate the system")
    public String shutdown(boolean force) {
	return "You said " + force;
    }
}