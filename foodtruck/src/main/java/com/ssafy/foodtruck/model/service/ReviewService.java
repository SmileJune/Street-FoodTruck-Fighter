package com.ssafy.foodtruck.model.service;

import com.ssafy.foodtruck.db.entity.*;
import com.ssafy.foodtruck.db.repository.OrdersRepository;
import com.ssafy.foodtruck.db.repository.ReviewRepository;
import com.ssafy.foodtruck.dto.request.RegisterFoodtruckReviewReq;
import com.ssafy.foodtruck.dto.response.GetFoodtruckReviewRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ssafy.foodtruck.constant.FoodtruckConstant.NOT_FOUND_ORDERS_ERROR_MESSAGE;

@Service("reviewService")
@RequiredArgsConstructor
public class ReviewService {

	@Value("${file.dir}")
	private String fileDir;

	private final OrdersRepository ordersRepository;
	private final ReviewRepository reviewRepository;

	// 푸드트럭 리뷰 등록
	@Transactional
	public void registerFoodTruckReview(RegisterFoodtruckReviewReq registerFoodTruckReviewReq, User user, MultipartFile file) {
		// 주문내역에서 찾음
		Orders order = ordersRepository.findById(registerFoodTruckReviewReq.getOrdersId())
			.orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_ORDERS_ERROR_MESSAGE));
		// Review 에서 찾음 -> 에러 (테스트 코드 작성) - 주문 내역 1번에 1번의 리뷰만 달 수 있다.

		Review review = Review.builder()
			.user(user)
			.orders(order)
			.content(registerFoodTruckReviewReq.getContent())
			.grade(registerFoodTruckReviewReq.getGrade())
			.build();
		reviewRepository.save(review);

		try{
			saveReviewImg(review, file);
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}

	// 푸드트럭 리뷰 조회
	public List<GetFoodtruckReviewRes> getFoodTruckReview(Integer foodTruckId){
		List<Review> findReviewList = reviewRepository.findAllByFoodTruckId(foodTruckId);
		List<GetFoodtruckReviewRes> reviewList = new ArrayList<>();
		System.out.println("리뷰 갯수 : " + findReviewList.size());

		for(Review review : findReviewList){
			reviewList.add(GetFoodtruckReviewRes.builder()
				.id(review.getId())
				.userId(review.getUser().getId())
				.ordersId(review.getOrders().getId())
				.content(review.getContent())
				.grade(review.getGrade())
				.regDate(review.getRegDate())
				.build());
		}
		return reviewList;
	}

	@Transactional
	public void saveReviewImg(Review review, MultipartFile files) throws IOException {

		//만약 이미지 파일이 들어있지 않다면 바로 종료
		if(files.isEmpty()){
			return;
		}

		// 원래 파일 이름 추출
		String origName = files.getOriginalFilename();

		// 파일 이름으로 쓸 uuid 생성
		String uuid = UUID.randomUUID().toString();

		// 확장자 추출(ex : .png)
		String extension = origName.substring(origName.lastIndexOf("."));

		// uuid와 확장자 결합
		String savedName = uuid + extension;

		// 파일을 불러올 때 사용할 파일 경로
		String savedPath = fileDir + savedName;

		// ReviewImg 생성
		ReviewImg file = ReviewImg.builder()
			.orgNm(origName)
			.savedNm(savedName)
			.savedPath(savedPath)
			.review(review)
			.build();

		// 실제로 로컬에 uuid를 파일명으로 저장
		files.transferTo(new File(savedPath));

		review.setReviewImg(file);
	}

	public ReviewImg getReviewImg(int reviewId) {

		Optional<Review> review = reviewRepository.findById(reviewId);
		if(!review.isPresent()){
			return null;
		}

		return review.get().getReviewImg();
	}

}
