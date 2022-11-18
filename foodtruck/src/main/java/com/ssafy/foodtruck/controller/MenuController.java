package com.ssafy.foodtruck.controller;

import com.ssafy.foodtruck.db.entity.FoodtruckImg;
import com.ssafy.foodtruck.db.entity.MenuImg;
import com.ssafy.foodtruck.db.entity.User;
import com.ssafy.foodtruck.db.repository.UserRepository;
import com.ssafy.foodtruck.dto.request.RegisterMenuReq;
import com.ssafy.foodtruck.exception.NotFoundException;
import com.ssafy.foodtruck.model.service.FoodTruckService;
import com.ssafy.foodtruck.model.service.MenuService;
import com.ssafy.foodtruck.model.service.UserService;
import com.ssafy.foodtruck.util.JwtTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.ssafy.foodtruck.constant.FoodtruckConstant.*;
import static com.ssafy.foodtruck.constant.MenuConstant.REGISTER_MENU_SUCCESS;

/**
 * 메뉴 관련 API 요청 처리를 위한 컨트롤러 정의
 */
@Api(value = "메뉴 API", tags = {"Menu"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {

	private final MenuService menuService;

	private final UserService userService;

	private final FoodTruckService foodTruckService;

	private final JwtTokenUtil jwtTokenUtil;
	private final UserRepository userRepository;

	// 메뉴 등록
	@PostMapping
	@ApiOperation(value = " 메뉴 등록", notes = "<strong>내 메뉴를 등록한다.</strong>")
	public ResponseEntity<?> registerFoodTruck(@RequestHeader("Authorization") @ApiParam(value="Access Token", required = true) String bearerToken,
											   @RequestBody @ApiParam(value="메뉴 정보", required = true) RegisterMenuReq registerMenuReq) {

		User user = userService.getUserByEmail(jwtTokenUtil.getEmailFromBearerToken(bearerToken));

		try {
			menuService.registerMenu(registerMenuReq, user);
			return new ResponseEntity<>(REGISTER_MENU_SUCCESS, HttpStatus.CREATED);
		} catch (NotFoundException ex) {
			return new ResponseEntity<>(NOT_FOUNT_FOODTRUCK_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
		} catch (IOException e){
			return new ResponseEntity<>(SAVE_IMAGE_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@PostMapping("/upload/{menu_id}")
	public ResponseEntity<HttpStatus> saveMenuImg(@RequestHeader("Authorization") String bearerToken,
												  @PathVariable Integer menuId,
												  @RequestParam("file") MultipartFile file) throws IOException {
		menuService.saveMenuImg(menuId, file);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/image/{menu_id}")
	public ResponseEntity<UrlResource> getMenuImg(@PathVariable Integer menuId) throws IOException{
		MenuImg file = menuService.getMenuImg(menuId);
		return new ResponseEntity<>(new UrlResource("file:" + file.getSavedPath()), HttpStatus.OK);
	}

}
