package kr.co.openbase.obshell.config;

import org.springframework.shell.ResultHandler;
import org.springframework.stereotype.Component;

@Component
public class PrefixedResultHandler implements ResultHandler<PrefixedResult> {

    @Override
    public void handleResult(PrefixedResult result) {
	System.out.printf("%s --> %s%n", result.getPrefix(), result.getResult());
    }
}