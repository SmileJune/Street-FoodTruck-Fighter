package com.ssafy.foodtruck.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterOrdersReq {

    private Integer foodtruckId;
    private Integer menuId;
}
