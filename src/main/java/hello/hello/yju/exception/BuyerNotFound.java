package hello.hello.yju.exception;

import org.springframework.http.HttpStatus;

public class BuyerNotFound extends StudySellException {

    private static final String MESSAGE = "구매자를 찾지 못했습니다.";

    public BuyerNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}


