package hello.hello.yju.exception;

import org.springframework.http.HttpStatus;

public class ItemNotFound extends StudySellException {

        private static final String MESSAGE = "상품을 찾지 못했습니다.";

        public ItemNotFound() {
            super(MESSAGE);
        }

        @Override
        public int getStatusCode() {
            return HttpStatus.NOT_FOUND.value();
        }
}
