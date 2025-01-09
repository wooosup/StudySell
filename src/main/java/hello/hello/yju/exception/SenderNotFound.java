package hello.hello.yju.exception;

import org.springframework.http.HttpStatus;

public class SenderNotFound extends StudySellException {

        private static final String MESSAGE = "발신자를 찾지 못했습니다.";

        public SenderNotFound() {
            super(MESSAGE);
        }

        @Override
        public int getStatusCode() {
            return HttpStatus.NOT_FOUND.value();
        }
}
