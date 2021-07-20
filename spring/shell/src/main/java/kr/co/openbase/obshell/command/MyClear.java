package kr.co.openbase.obshell.command;

import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Clear;

public class MyClear implements Clear.Command {

    @ShellMethod("Clear the screen, only better.")
    public void clear() {
    }
}