package controller;

import model.dao.FoodDao;
import model.dto.FoodDto;
import view.AdminView;

public class AdminController {
    // 싱글톤
    private static AdminController admControl = new AdminController();
    private AdminController() {}
    public static AdminController getInstance() {

        return admControl;
    }



    // 음식 기능1. 음식 추가 함수 -> FoodDao에 전달 : FoodDto(음식 이름 String, 칼로리 int) / FoodDao로부터 받을 반환값 : 음식 추가 성공 여부 boolean
    public boolean foodAdd(FoodDto foodDto) {
        return FoodDao.getInstance().foodAdd(foodDto);
    }


    // 음식 기능2. 음식 조회 함수
    public void foodListView() {

    }


    // 음식 기능3. 음식 수정 함수
    public void foodUpdate() {

    }


    // 음식 기능4. 음식 삭제 함수
    public boolean foodDelete(String foodName) {
        return FoodDao.getInstance().foodDelete(foodName);
    }
}
