package hello.hello.yju.exception;

import org.springframework.http.HttpStatus;

public class ChatRoomNotFound extends StudySellException {

    private static final String MESSAGE = "채팅방을 찾지 못했습니다.";

    public ChatRoomNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
