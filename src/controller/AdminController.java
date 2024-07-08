package controller;

import model.dao.ExerciseDao;
import model.dao.FoodDao;
import model.dto.ExerciseDto;
import model.dto.FoodDto;

import java.util.ArrayList;

public class AdminController {
    // 싱글톤
    private static AdminController admControl = new AdminController();
    private AdminController() {}
    public static AdminController getInstance() {

        return admControl;
    }

    // 음식 메뉴 현재 페이지 번호
    public static int foodCurrentPage = 1;



    // =============================== 음식 부분 =============================== //

    // 음식 기능1. 음식 추가 함수 -> FoodDao에 전달 : FoodDto(음식 이름 String, 칼로리 int) / FoodDao로부터 받을 반환값 : 음식 추가 성공 여부 boolean
    public boolean foodAdd(FoodDto foodDto) {
        return FoodDao.getInstance().foodAdd(foodDto);
    }


    // 음식 기능2. 음식 조회 함수
    public ArrayList<FoodDto> foodListView(int foodCurrentPage) {
        return FoodDao.getInstance().foodListView(foodCurrentPage);
    }


    // 음식 기능3. 음식 수정 함수
    public boolean foodUpdate(String oldFoodName, FoodDto foodDto) {
        return FoodDao.getInstance().foodUpdate(oldFoodName, foodDto);
    }


    // 음식 기능4. 음식 삭제 함수
    public boolean foodDelete(String foodName) {
        return FoodDao.getInstance().foodDelete(foodName);
    }




    // =============================== 운동 부분 =============================== //

    // 운동 기능1. 운동 추가 함수
    public boolean exerAdd(ExerciseDto exerDto) {
        return ExerciseDao.getInstance().exerAdd(exerDto);
    }


    // 운동 기능2. 운동 목록 조회 함수
    public void exerListView() {

    }

    // 운동 기능3. 운동 수정 함수
    public boolean exerUpdate(String oldExName, ExerciseDto exerDto) {
        return ExerciseDao.getInstance().exerUpdate(oldExName, exerDto);
    }

    // 운동 기능4. 운동 삭제 함수
    public boolean exerDelete(String exName) {
        return ExerciseDao.getInstance().exerDelete(exName);
    }


    // =============================== 회원 부분 =============================== //

    // 회원 기능1. 회원 목록 조회 함수
    public void memberListView() {

    }

    // 회원 기능2. 회원 탈퇴 함수
    public void memberDelete() {

    }
}
