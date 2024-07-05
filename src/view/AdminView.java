package view;

import controller.AdminController;
import model.dto.ExerciseDto;
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
        while (true) {
            try {
                System.out.print(">> 1.음식DB관리 2.운동DB관리 3.회원DB관리 4.뒤로가기 : ");
                int ch = scan.nextInt();

                if (ch == 1) {   // 1을 입력할 경우, 음식 DB 관리를 할 수 있는 화면으로 넘어감
                    foodMenu();
                } else if (ch == 2) {
                    exerciseMenu();
                } else if (ch == 3) {

                } else if (ch == 4) {
                    return;
                } else {
                    System.out.println(">> 기능이 없는 번호입니다.");
                }
            } catch (Exception e) {
                System.out.println("잘못된 입력입니다." + e);
                scan = new Scanner(System.in);
            }   // catch end
        }   // while end
    }   // indexAdm() end

    // =============================== 음식 부분 =============================== //

    // 화면2. 음식 DB 관리 화면 함수
    public void foodMenu() {
        System.out.print(">> 1.음식추가 2.음식조회 3.음식수정 4.음식삭제 5.뒤로가기 : ");
        int ch = scan.nextInt();

        if (ch == 1) {          // 음식 추가 함수 호출
            foodAdd();
        } else if (ch == 2) {   // 음식 목록 조회 함수 호출
            foodListView();
        } else if (ch == 3) {   // 음식 수정 함수 호출
            foodUpdate();
        } else if (ch == 4) {   // 음식 삭제 함수 호출
            foodDelete();
        } else if (ch == 5) {  // 뒤로가기 - 관리자 전용 초기화면으로 돌아가기
            return;
        } else {
            System.out.println(">> 잘못된 입력입니다.");
        }
    }   // foodMenu() end

    // 음식 기능1. 음식 추가 함수 -> AdminController에 전달 : FoodDto(음식 이름 String, 칼로리 int) / AdminController로부터 받을 반환값 : 음식 추가 성공 여부 boolean
    public void foodAdd() {
        // AdminController에 전달할 음식 이름과 칼로리 입력 받기
        scan.nextLine();
        System.out.print(">> 추가할 음식 이름 : ");
        String foodName = scan.nextLine();
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


    // 음식 기능2. 음식 목록 조회 함수
    public void foodListView() {


    }


    // 음식 기능3. 음식 수정 함수 -> AdminController에 전달 : 수정하려고 하는 기존 음식 이름 String, 수정해줄 새 음식 String
    //                                      / AdminController로부터 받을 반환값 : 음식 수정 성공 여부 boolean
    public void foodUpdate() {
        // 1. 수정하려고 하는 기존 음식 이름 입력
        scan.nextLine();
        System.out.print("수정하려고 하는 기존 음식 이름 : ");
        String oldFoodName = scan.nextLine();

        // 2. 수정해줄 새 음식 이름 입력
        System.out.print("수정해줄 새 음식 이름 : ");
        String newFoodName = scan.nextLine();

        // 수정 성공 여부 받기
        boolean result = AdminController.getInstance().foodUpdate(oldFoodName, newFoodName);

        if (result) {
            System.out.println(">> 음식 이름 수정 성공");
        } else {
            System.out.println(">> 음식 이름 수정 실패");
        }

    }   // foodUpdate() end


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

    // =============================== 운동 부분 =============================== //

    // 화면3. 운동 DB 관리 화면 함수
    public void exerciseMenu() {
        System.out.print(">> 1.운동추가 2.운동조회 3.운동수정 4.운동삭제 5.뒤로가기 : ");
        int ch = scan.nextInt();

        if (ch == 1) {          // 운동 추가 함수 호출
            exerAdd();
        } else if (ch == 2) {   // 운동 목록 조회 함수 호출
            exerListView();
        } else if (ch == 3) {   // 운동 수정 함수 호출
            exerUpdate();
        } else if (ch == 4) {   // 운동 삭제 함수 호출
            exerDelete();
        } else if (ch == 5) {  // 뒤로가기 - 관리자 전용 초기화면으로 돌아가기
            return;
        } else {
            System.out.println(">> 잘못된 입력입니다.");
        }
    }

    // 운동 기능1. 운동 추가 함수 -> AdminController에 전달 : ExerciseDto(운동 이름 String, 칼로리 int, 운동 강도 int) / AdminController로부터 받을 반환값 : 운동 추가 성공 여부 boolean
    public void exerAdd() {
        // 추가할 운동 이름, 칼로리, 강도 입력 받기
        scan.nextLine();
        System.out.print(">> 추가할 운동 이름 : ");
        String exName = scan.nextLine();
        System.out.print(">> 추가할 운동 칼로리 : ");
        int exKcal = scan.nextInt();
        System.out.print(">> 추가할 운동 강도 : ");
        int exIntensity = scan.nextInt();

        ExerciseDto exerDto = new ExerciseDto(exName, exKcal, exIntensity);

        boolean result = AdminController.getInstance().exerAdd(exerDto);

        if (result) {
            System.out.println(">> 운동 추가 성공");
        } else {
            System.out.println(">> 운동 추가 실패");
        }

    }


    // 운동 기능2. 운동 목록 조회 함수
    public void exerListView() {

    }

    // 운동 기능3. 운동 수정 함수
    public void exerUpdate() {

    }

    // 운동 기능4. 운동 삭제 함수 -> AdminController에 전달 : 삭제할 운동 이름 String / AdminController로부터 받을 반환값 : 운동 삭제 성공 여부 boolean
    public void exerDelete() {
        // 삭제할 운동 이름 입력 받기
        scan.nextLine();
        System.out.print(">> 삭제할 운동 이름 : ");
        String exName = scan.nextLine();

        boolean result = AdminController.getInstance().exerDelete(exName);

        if (result) {
            System.out.println(">> 운동 삭제 성공");
        } else {
            System.out.println(">> 운동 삭제 실패");
        }
    }

}   // class end
