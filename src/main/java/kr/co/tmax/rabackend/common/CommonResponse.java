package kr.co.tmax.rabackend.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(String message, T data) {
        return (CommonResponse<T>) CommonResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
