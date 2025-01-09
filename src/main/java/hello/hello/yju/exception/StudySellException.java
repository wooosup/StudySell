package hello.hello.yju.exception;

import lombok.Getter;

@Getter
public abstract class StudySellException extends RuntimeException {

    protected StudySellException(String message) {
        super(message);
    }

    public abstract int getStatusCode();
}
