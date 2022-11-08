package com.ssafy.foodtruck.dto.request;

import com.ssafy.foodtruck.dto.ScheduleDateDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CreateScheduleReq {
	List<ScheduleDateDto> scheduleDateDtoList;
	private Double latitude;
	private Double longtitude;
	private String address;
}
