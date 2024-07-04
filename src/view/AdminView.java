package view;

import controller.AdminController;
import model.dto.FoodDto;

import java.util.Scanner;

public class AdminView {
    // 싱글톤
    private static AdminView admView = new AdminView();
    private AdminView() {}
    public static AdminView getInstance() {

        return admView;
    }

    Scanner scan = new Scanner(System.in);

    // 화면1. 관리자로 로그인시 초기화면 함수
    public void indexAdm() {
        System.out.print(">> 1.음식DB관리 2.운동DB관리 3.회원DB관리 4.뒤로가기 : ");
        int ch = scan.nextInt();

        if (ch == 1) {   // 1을 입력할 경우, 음식 DB 관리를 할 수 있는 화면으로 넘어감
            foodMenu();
        } else if (ch == 2) {

        } else if (ch == 3) {

        } else if (ch == 4) {

        } else {
            System.out.println(">> 잘못된 입력입니다.");
        }

    }

    // 화면2. 음식 DB 관리 화면 함수
    public void foodMenu() {
        System.out.print(">> 1.음식추가 2.음식조회 3.음식수정 4.음식삭제 5.뒤로가기 : ");
        int ch = scan.nextInt();

        if (ch == 1) {   // 음식 추가 함수 호출
            foodAdd();
        } else if (ch == 2) {   // 음식 조회 함수 호출
            foodListView();
        } else if (ch == 3) {   // 음식 수정 함수 호출
            foodUpdate();
        } else if (ch == 4) {   // 음식 삭제 함수 호출
            foodDelete();
        } else if (ch == 5) {  // 관리자 전용 초기화면

        } else {
            System.out.println(">> 잘못된 입력입니다.");
        }
    }   // foodMenu() end

    // 음식 기능1. 음식 추가 함수 -> AdminController에 전달 : FoodDto(음식 이름 String, 칼로리 int) / AdminController로부터 받을 반환값 : 음식 추가 성공 여부 boolean
    public void foodAdd() {
        // AdminController에 전달할 음식 이름과 칼로리 입력 받기
        System.out.print(">> 추가할 음식 이름 : ");
        String foodName = scan.next();
        System.out.print(">> 추가할 칼로리 : ");
        int foodKcal = scan.nextInt();

        FoodDto foodDto = new FoodDto(foodName, foodKcal);

        // AdminController에게 foodDto 전달 / 반환 받은 음식 추가 성공 여부를 result에 대입
        boolean result = AdminController.getInstance().foodAdd(foodDto);

        if (result) {
            System.out.println(">> 음식 추가 성공");
        } else {
            System.out.println(">> 음식 추가 실패");
        }

    }   // foodAdd() end


    // 음식 기능2. 음식 조회 함수
    public void foodListView() {

    }


    // 음식 기능3. 음식 수정 함수
    public void foodUpdate() {

    }


    // 음식 기능4. 음식 삭제 함수 -> AdminController에 전달 : 삭제할 음식 이름 String / AdminController로부터 받을 반환값 : 음식 삭제 성공 여부 boolean
    public void foodDelete() {
        // AdminController에 전달할 음식 이름 입력
        System.out.print(">> 삭제할 음식 이름 : ");
        String foodName = scan.next();

        // AdminController에게 foodName 전달 / 반환 받은 음식 삭제 성공 여부를 result에 대입
        boolean result = AdminController.getInstance().foodDelete(foodName);

        if (result) {
            System.out.println(">> 음식 삭제 성공");
        } else {
            System.out.println(">> 음식 삭제 실패");
        }

    }



}
