package view;

import controller.AdminController;
import controller.NormalMemberController;
import model.dto.ExerciseDto;
import model.dto.FoodDto;
import model.dto.MemberDto;

import java.util.ArrayList;
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
                System.out.print(">>1.음식DB관리 2.운동DB관리 3.회원DB관리 4.뒤로가기 : ");
                int ch = scan.nextInt();

                if (ch == 1) {   // 1을 입력할 경우, 음식 DB 관리를 할 수 있는 화면으로 넘어감
                    foodMenu();
                } else if (ch == 2) {
                    exerciseMenu();
                } else if (ch == 3) {
                    memberMenu();
                } else if (ch == 4) {
                    return;
                } else {
                    System.out.println(">>기능이 없는 번호입니다.");
                }
            } catch (Exception e) {
                System.out.println(">>잘못된 입력입니다. " + e);
                scan = new Scanner(System.in);
            }   // catch end
        }   // while end
    }   // indexAdm() end


    // =============================== 음식 부분 CRUD =============================== //

    // 화면2. 음식 DB 관리 화면 함수
    public void foodMenu() {
        System.out.print(">>1.음식추가 2.음식조회 3.음식수정 4.음식삭제 5.뒤로가기 : ");
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
            System.out.println(">>잘못된 입력입니다.");
        }
    }   // foodMenu() end

    // 음식 기능1. 음식 추가 함수 -> AdminController에 전달 : FoodDto(음식 이름 String, 칼로리 int) / AdminController로부터 받을 반환값 : 음식 추가 성공 여부 boolean
    public void foodAdd() {
        // AdminController에 전달할 음식 이름과 칼로리 입력 받기
        scan.nextLine();
        System.out.print(">>추가할 음식 이름을 입력해주세요 : ");
        String foodName = scan.nextLine();
        System.out.print(">>추가할 칼로리를 입력해주세요 : ");
        int foodKcal = scan.nextInt();

        FoodDto foodDto = new FoodDto(foodName, foodKcal);

        // AdminController에게 foodDto 전달 / 반환 받은 음식 추가 성공 여부를 result에 대입
        boolean result = AdminController.getInstance().foodAdd(foodDto);

        if (result) {
            System.out.println(">>음식을 추가하는 데에 성공하였습니다.");
        } else {
            System.out.println(">>음식을 추가하는 데에 실패하였습니다.");
        }

    }   // foodAdd() end

    // 음식 기능2. 음식 목록 조회 함수
    public void foodListView() {
        while (true) {
            System.out.println("============= 음식 목록 " + AdminController.foodCurrentPage + " 페이지 ==============");
            System.out.println("번호    음식                  칼로리");
            System.out.println("============================================");
            // DB에서 현재 페이지 번호에 해당되는 음식 목록 가져와 출력
            ArrayList<FoodDto> foodList = AdminController.getInstance().foodListView(AdminController.foodCurrentPage);

            for (int i = 0; i < foodList.size(); i++) {
                int foodCode = foodList.get(i).getFoodCode();
                String foodName = foodList.get(i).getFoodName();
                int foodKcal = foodList.get(i).getFoodKcal();
                int count = foodName.length();

                foodName = foodName.replace(" ", "ㅤ");      // 출력을 깔끔하게 하기 위해 띄어쓰기 해서 생긴 공백을 한글과 폭이 같은 공백으로 치환

                if (foodName.length() < 13) {       // 음식 이름 글자 길이가 13이하이면 총 길이가 13이 되도록 공백 문자를 이어 붙이는 코드
                    for (int j = 0; j < 13 - count; j++) {
                        foodName += "ㅤ";
                    }
                }

                System.out.printf(" %-5d %-13s %-5d \n", foodCode, foodName, foodKcal);
            }

            System.out.println("============================================");
            System.out.print("p = 이전 페이지, n = 다음 페이지, b = 돌아가기 : ");
            char ch = scan.next().charAt(0);

            if (ch == 'P' || ch == 'p'){ // 음식 메뉴 이전 10개 출력
                if (AdminController.foodCurrentPage == 1) { // 첫 번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    AdminController.foodCurrentPage--;
                }
            }
            else if (ch == 'N' || ch == 'n'){ // 음식 메뉴 다음 10개
                // 불러올 음식 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (AdminController.getInstance().foodListView(AdminController.foodCurrentPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    AdminController.foodCurrentPage++;
                }
            } else if (ch == 'B' || ch == 'b'){     // 관리자 초기 화면으로 돌아가기
                System.out.println(">>이전 메뉴로 돌아갑니다.");
                AdminController.foodCurrentPage = 1;
                break;
            } else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }

        }   // while end
    }   // foodListView() end

    // 음식 기능3. 음식 수정 함수 -> AdminController에 전달 : 수정하려고 하는 기존 음식 이름 String, FoodDto(새 음식 이름 String, 새 칼로리 int)
    //                                      / AdminController로부터 받을 반환값 : 음식 수정 성공 여부 boolean
    public void foodUpdate() {
        // 1. 수정하려고 하는 기존 음식 이름 입력
        scan.nextLine();
        System.out.print(">>수정하려고 하는 기존 음식 이름을 입력해주세요 : ");
        String oldFoodName = scan.nextLine();

        // 2. 수정할 새 음식 이름과 칼로리 입력
        System.out.print(">>수정할 새 음식 이름을 입력해주세요 : ");
        String newFoodName = scan.nextLine();
        System.out.print(">>수정할 칼로리 값을 입력해주세요 : ");
        int newFoodKcal = scan.nextInt();

        FoodDto foodDto = new FoodDto(newFoodName, newFoodKcal);

        boolean result = AdminController.getInstance().foodUpdate(oldFoodName, foodDto);

        if (result) {
            System.out.println(">>음식을 수정하는 데에 성공하였습니다.");
        } else {
            System.out.println(">>음식을 수정하는 데에 실패하였습니다.");
        }

    }   // foodUpdate() end

    // 음식 기능4. 음식 삭제 함수 -> AdminController에 전달 : 삭제할 음식 이름 String / AdminController로부터 받을 반환값 : 음식 삭제 성공 여부 boolean
    public void foodDelete() {
        // AdminController에 전달할 음식 이름 입력
        System.out.print(">>삭제할 음식 이름을 입력해주세요 : ");
        String foodName = scan.next();

        // AdminController에게 foodName 전달 / 반환 받은 음식 삭제 성공 여부를 result에 대입
        boolean result = AdminController.getInstance().foodDelete(foodName);

        if (result) {
            System.out.println(">>음식을 삭제하는 데에 성공하였습니다.");
        } else {
            System.out.println(">>음식을 삭제하는 데에 실패하였습니다.");
        }

    }


    // =============================== 운동 부분 CRUD =============================== //

    // 화면3. 운동 DB 관리 화면 함수
    public void exerciseMenu() {
        System.out.print(">>1.운동추가 2.운동조회 3.운동수정 4.운동삭제 5.뒤로가기 : ");
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
            System.out.println(">>잘못된 입력입니다.");
        }
    }

    // 운동 기능1. 운동 추가 함수 -> AdminController에 전달 : ExerciseDto(운동 이름 String, 칼로리 int, 운동 강도 int) / AdminController로부터 받을 반환값 : 운동 추가 성공 여부 boolean
    public void exerAdd() {
        // 추가할 운동 이름, 칼로리, 강도 입력 받기
        scan.nextLine();
        System.out.print(">>추가할 운동 이름을 입력해주세요 : ");
        String exName = scan.nextLine();
        System.out.print(">>추가할 운동 칼로리를 입력해주세요 : ");
        int exKcal = scan.nextInt();
        System.out.print(">>추가할 운동 강도를 입력해주세요 : ");
        int exIntensity = scan.nextInt();

        ExerciseDto exerDto = new ExerciseDto(exName, exKcal, exIntensity);

        boolean result = AdminController.getInstance().exerAdd(exerDto);

        if (result) {
            System.out.println(">>운동을 추가하는 데에 성공하였습니다.");
        } else {
            System.out.println(">>운동을 추가하는 데에 실패하였습니다.");
        }

    }

    // 운동 기능2. 운동 목록 조회 함수
    public void exerListView() {
        while (true) {
            System.out.println("============== 운동 목록 " + AdminController.exerCurrentPage + " 페이지 ==============");
            System.out.println("번호    운동                 칼로리   운동강도");
            System.out.println("=============================================");
            // DB에서 현재 페이지 번호에 해당되는 운동 목록 가져와 출력
            ArrayList<ExerciseDto> exerList = AdminController.getInstance().exerListView(AdminController.exerCurrentPage);

            for (int i = 0; i < exerList.size(); i++) {
                int exCode = exerList.get(i).getExCode();
                String exName = exerList.get(i).getExName();
                int exKcal = exerList.get(i).getExKcal();
                int exIntensity = exerList.get(i).getExIntensity();
                int count = exName.length();

                exName = exName.replace(" ", "ㅤ");      // 출력을 깔끔하게 하기 위해 띄어쓰기 해서 생긴 공백을 한글과 폭이 같은 공백으로 치환

                if (exName.length() < 13) {     // 운동 이름 글자 길이가 13이하이면 총 길이가 13이 되도록 공백 문자를 이어 붙이는 코드
                    for (int j = 0; j < 13 - count; j++) {
                        exName += "ㅤ";
                    }
                }

                System.out.printf(" %-5d %-13s %-8d %d \n", exCode, exName, exKcal, exIntensity);
            }

            System.out.println("=============================================");
            System.out.print("p = 이전 페이지, n = 다음 페이지, b = 돌아가기 : ");
            char ch = scan.next().charAt(0);

            if (ch == 'P' || ch == 'p'){ // 운동 메뉴 이전 10개 출력
                if (AdminController.exerCurrentPage == 1) { // 첫 번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    AdminController.exerCurrentPage--;
                }
            }
            else if (ch == 'N' || ch == 'n'){ // 운동 메뉴 다음 10개
                // 불러올 운동 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (AdminController.getInstance().exerListView(AdminController.exerCurrentPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    AdminController.exerCurrentPage++;
                }
            } else if (ch == 'B' || ch == 'b'){     // 관리자 초기 화면으로 돌아가기
                System.out.println(">>이전 메뉴로 돌아갑니다.");
                AdminController.exerCurrentPage = 1;
                break;
            } else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }
        }   // while end

    }

    // 운동 기능3. 운동 수정 함수 -> AdminController에 전달 : 수정할 기존 운동 이름 String, ExerciseDto(새 운동 이름 String, 새 칼로리 int, 새 운동 강도 int)
    //                                  / AdminController로부터 받을 반환값 : 운동 삭제 성공 여부 boolean
    public void exerUpdate() {
        // 수정하려고 하는 기존 운동 이름 입력 받기
        scan.nextLine();
        System.out.print(">>수정하려고 하는 기존 운동 이름을 입력해주세요 : ");
        String oldExName = scan.nextLine();

        // 수정할 새 운동 이름, 새 칼로리, 새 운동 강도 입력 받기
        System.out.print(">>수정할 새 운동 이름을 입력해주세요 : ");
        String newExName = scan.nextLine();
        System.out.print(">>수정할 칼로리 값을 입력해주세요 : ");
        int newExKcal = scan.nextInt();
        System.out.print(">>수정할 운동 강도를 입력해주세요 : ");
        int newExIntensity = scan.nextInt();

        ExerciseDto exerDto = new ExerciseDto(newExName, newExKcal, newExIntensity);

        boolean result = AdminController.getInstance().exerUpdate(oldExName, exerDto);

        if (result) {
            System.out.println(">>운동을 수정하는 데에 성공하였습니다.");
        } else {
            System.out.println(">>운동을 수정하는 데에 실패하였습니다.");
        }
    }

    // 운동 기능4. 운동 삭제 함수 -> AdminController에 전달 : 삭제할 운동 이름 String / AdminController로부터 받을 반환값 : 운동 삭제 성공 여부 boolean
    public void exerDelete() {
        // 삭제할 운동 이름 입력 받기
        scan.nextLine();
        System.out.print(">>삭제할 운동 이름을 입력해주세요 : ");
        String exName = scan.nextLine();

        boolean result = AdminController.getInstance().exerDelete(exName);

        if (result) {
            System.out.println(">>운동을 삭제하는 데에 성공하였습니다.");
        } else {
            System.out.println(">>운동을 삭제하는 데에 실패하였습니다.");
        }
    }


    // =============================== 회원 부분 RD =============================== //

    // 화면4. 회원 DB 관리 화면 함수
    public void memberMenu() {
        System.out.print(">>1.회원조회 2.회원탈퇴 3.뒤로가기 : ");
        int ch = scan.nextInt();

        if (ch == 1) {          // 회원 목록 조회 함수 호출
            memberListView();
        } else if (ch == 2) {   // 회원 탈퇴 함수 호출
            memberWithdraw();
        } else if (ch == 3) {   // 뒤로가기 - 관리자 전용 초기화면으로 돌아가기
            return;
        } else {
            System.out.println(">>잘못된 입력입니다.");
        }
    }

    // 회원 기능1. 회원 목록 조회 함수
    public void memberListView() {
        while (true) {
            System.out.println("============================================== 회원 목록 " + AdminController.memberCurrentPage + " 페이지 ==============================================");
            System.out.println("번호    아이디       비밀번호        이름         키    운동습관  성별      생년월일          연락처      회원분류코드");
            System.out.println("=============================================================================================================");
            // DB에서 현재 페이지 번호에 해당되는 회원 목록 가져와 출력
            ArrayList<MemberDto> memberList = AdminController.getInstance().memberListView(AdminController.memberCurrentPage);

            for (int i = 0; i < memberList.size(); i++) {
                int memberCode = memberList.get(i).getMemberCode();
                String ID = memberList.get(i).getID();
                String PW = memberList.get(i).getPW();
                String memberName = memberList.get(i).getMemberName();
                int height = memberList.get(i).getHeight();
                int exHabit = memberList.get(i).getExHabit();
                String gender = memberList.get(i).getGender();
                String birthDate = memberList.get(i).getBirthDate();
                String contact = memberList.get(i).getContact();
                int accCategory = memberList.get(i).getAccCategory();

                // =========== 각 항목들의 길이를 맞춰주는 코드 =========== //
                int count = memberName.length();
                if (memberName.length() < 5) {     // 이름 글자 길이가 5이하이면 총 길이가 5가 되도록 공백 문자를 이어 붙이는 코드
                    for (int j = 0; j < 5 - count; j++) {
                        memberName += "ㅤ";
                    }
                }

                count = contact.length();
                if (contact.length() < 14) {     // 연락처 글자 길이가 14이하이면 총 길이가 14가 되도록 공백 문자를 이어 붙이는 코드
                    for (int j = 0; j < 14 - count; j++) {
                        contact += " ";
                    }
                }
                // ====================================================//

                if (memberCode == 1) {
                    System.out.printf(" %-5d %-12s %-12s %-8s    %-8d %-6d %-6s %-14s %-15s   %d \n",
                            memberCode, ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory);
                } else {
                    System.out.printf(" %-5d %-12s %-12s %-8s %-8d %-6d %-6s %-14s %-17s %d \n",
                            memberCode, ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory);
                }
            }

            System.out.println("=============================================================================================================");
            System.out.print("p = 이전 페이지, n = 다음 페이지, b = 돌아가기 : ");
            char ch = scan.next().charAt(0);

            if (ch == 'P' || ch == 'p'){ // 회원 메뉴 이전 10개 출력
                if (AdminController.memberCurrentPage == 1) { // 첫 번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    AdminController.memberCurrentPage--;
                }
            }
            else if (ch == 'N' || ch == 'n'){ // 회원 메뉴 다음 10개
                // 불러올 회원 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (AdminController.getInstance().memberListView(AdminController.memberCurrentPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    AdminController.memberCurrentPage++;
                }
            } else if (ch == 'B' || ch == 'b'){     // 관리자 초기 화면으로 돌아가기
                System.out.println(">>이전 메뉴로 돌아갑니다.");
                AdminController.memberCurrentPage = 1;
                break;
            } else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }
        }   // while end

    }   // memberListView() end

    // 회원 기능2. 회원 탈퇴 함수 -> AdminController에 전달 : 탈퇴시킬 회원 이름 String / AdminController로부터 받을 반환값 : 회원 탈퇴 성공 여부 boolean
    public void memberWithdraw() {
        // 탈퇴시킬 회원 이름 입력 받기
        System.out.print(">>탈퇴시킬 회원 아이디를 입력해주세요 : ");
        String ID = scan.next();

        boolean result = AdminController.getInstance().memberWithdraw(ID);

        if (result) {
            System.out.println(">>회원을 탈퇴시키는 데에 성공하였습니다.");
        } else {
            System.out.println(">>회원을 탈퇴시키는 데에 실패하였습니다.");
        }

    }   // memberWithdraw() end

}   // class end
