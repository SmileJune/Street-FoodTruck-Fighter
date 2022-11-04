package com.ssafy.foodtruck.db.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrdersErrorMessage {

    FAIL_TO_REGISTER("주문내역 등록에 실패하였습니다."),
    NOT_FOUND_MENU("메뉴를 찾을 수 없습니다.."),
    NOT_FOUND_USER("사용자를 찾을 수 없습니다.");

    private final String message;
}
