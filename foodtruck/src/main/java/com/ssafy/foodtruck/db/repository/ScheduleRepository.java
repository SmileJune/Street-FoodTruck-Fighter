package com.ssafy.foodtruck.db.repository;

import com.ssafy.foodtruck.db.entity.FoodTruck;
import com.ssafy.foodtruck.db.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
//	List<Schedule> findAllByFoodTruck(FoodTruck foodTruck);

	@Query(value = "select * \n" +
		"from schedule \n" +
		"where foodtruck_id = :foodtruckId \n" +
		"order by group_id;", nativeQuery = true)
	List<Schedule> findAllByFoodtruck(Integer foodtruckId);

	@Query(value = "SELECT * \n" +
		"FROM schedule \n" +
		"WHERE latitude BETWEEN :latitude - 0.5 AND :latitude + 0.5 \n" +
		"AND longitude BETWEEN :longitude - 0.5 AND :longitude + 0.5 \n" +
		"And curdate() = working_date \n" +
		"And is_valid = true", nativeQuery = true)
	List<Schedule> findScheduleNearBy(Double latitude, Double longitude);

	// 오늘 날짜에 해당하는 스케줄을 가져온다.
	@Query(value = "SELECT *\n" +
		"FROM schedule\n" +
		"WHERE foodtruck_id = :foodTruckId \n" +
		"And curdate() = working_date;", nativeQuery = true)
	Optional<Schedule> findScheduleByFoodTruckAndDate(int foodTruckId);

	// 이번달에 해당하는 스케줄을 가져온다.
	@Query(value = "SELECT *\n" +
		"FROM schedule\n" +
		"WHERE foodtruck_id = :foodTruckId \n" +
		"And working_date BETWEEN :firstDate And :lastDate \n" +
		"ORDER BY group_id" +
		"AND is_valid = true;", nativeQuery = true)
	List<Schedule> findScheduleByFoodTruckAndThisMonth(int foodTruckId, LocalDate firstDate, LocalDate lastDate);

	@Query(value = "SELECT max(group_id) FROM schedule;", nativeQuery = true)
	Optional<Integer> findMaxGroupId();

}
