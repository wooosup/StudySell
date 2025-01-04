package hello.hello.yju.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemSellStatus {
    SELL("판매 중"),
    Reservation("예약 중"),
    SOLD_OUT("판매 완료");

    private final String text;
}
