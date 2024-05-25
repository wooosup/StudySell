package hello.hello.yju.dto;


import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {

    public static String getRelativeTime(LocalDateTime regTime) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(regTime, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            return days + "일 전";
        }
    }
}