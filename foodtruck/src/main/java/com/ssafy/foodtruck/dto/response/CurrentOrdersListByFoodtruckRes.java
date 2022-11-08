package com.ssafy.foodtruck.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentOrdersListByFoodtruckRes {

	private String foodtruckName;
    private String menuName;
}
