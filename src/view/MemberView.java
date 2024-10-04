package view;

import controller.MemberController;
import model.dto.MemberDto;

import java.util.Scanner;

import static controller.MemberController.loginAccCode;

public class MemberView {

    // 싱글톤
    private static MemberView mView = new MemberView();
    private MemberView() {}
    public static MemberView getInstance() {
        return mView;
    }

    Scanner scan = new Scanner(System.in);

    // 프로그램 시작시 메뉴
    public void index(){
        while (true) {
            // 아스키아트
            indexAsciiArt();

            // 메뉴
            System.out.println(">> 환영합니다. 퍼스널 트레이너 어플리케이션입니다. 메뉴를 선택해 주세요.");
            try {
                System.out.println(">> 다음 중 하나를 선택하세요:");
                System.out.println(">> 1. 회원가입");
                System.out.println(">> 2. 로그인");
                System.out.println(">> 3. 아이디 찾기");
                System.out.println(">> 4. 비밀번호 찾기");
                System.out.println(">> 5. 프로그램 종료");
                int ch = scan.nextInt();
                if (ch == 1) {
                    signUp();
                } else if (ch == 2) {
                    login();
                } else if (ch == 3) {
                    findId();
                } else if (ch == 4) {
                    findPw();
                } else if (ch == 5) {
                    System.out.println(">> 프로그램을 종료합니다.");
                    break;
                } else {
                    System.out.println(">> 입력이 잘못되었습니다. 다시 시도해 주세요.");
                }
            } catch (Exception e) { // 문자열 입력시 오류
                System.out.println(">> 입력이 잘못되었습니다. 다시 시도해 주세요.");
                // Scanner 객체에 남은 줄바꿈 문자나 공백 제거 = 비어있는 Scanner 객체
                scan.nextLine();
            }
        }
    }

    public void signUp(){ // 회원가입 메뉴
        try {
            System.out.println(">> 회원가입 메뉴입니다.");
            // 1. 아이디
            System.out.print(">> 아이디를 입력해주세요: ");
            String id = scan.next();
            scan.nextLine();
            // 2. 비밀번호
            System.out.print(">> 비밀번호를 입력해주세요: ");
            String pw = scan.next();
            scan.nextLine();
            // 3. 이름
            System.out.print(">> 이름을 입력해주세요: ");
            String name = scan.next();
            scan.nextLine();
            // 4. 키 입력 + 유효성 검사 try/catch
            int height;
            while (true) {
                System.out.println(">> 키를 1cm 단위까지 반올림하여 입력해주세요.");
                System.out.println(">> 예시) 175.1cm -> 175");
                System.out.print(">> 키: ");
                // .hasNextInt(): 입력된 값이 Int인지 확인한 후 true/false, 입력값 처리는 하지 않는다.
                if (scan.hasNextInt()) {
                    height = scan.nextInt();
                    if (height > 0) break;
                } else {
                    System.out.println(">> 입력이 잘못되었습니다. 다시 입력해 주세요.");
                    // scanner 입력 초기화
                    scan.nextLine();
                }
            }
            // 5. 운동 습관 입력 + 유효성 검사
            int exHabit;
            while (true) {
                try {
                    System.out.println(">> 평소 운동 습관을 입력해주세요.");
                    System.out.println(">> 1.운동을 거의 하지 않는다.");
                    System.out.println(">> 2.일주일에 3~4번 정도 운동한다.");
                    System.out.println(">> 3.매일 운동 또는 격한 운동을 3~4일 한다.");
                    System.out.print(">> 운동 습관: ");
                    exHabit = scan.nextInt();
                    if (1 <= exHabit && exHabit <= 3) {break;}
                    else {throw new RuntimeException();}
                } catch (Exception e) {
                    System.out.println(">> 입력이 잘못되었습니다. 다시 입력해 주세요.");
                    // scanner 입력 초기화
                    scan.nextLine();
                }
            }
            // 6. 성별 입력 + 유효성 검사 try/catch
            String gender;
            while (true) {
                try {
                    System.out.println(">> 성별을 영문 대문자로 입력해주세요.");
                    System.out.print(">> 남: M / 여: F");
                    gender = String.valueOf(scan.next().charAt(0));
                    if (gender.equals("F") || gender.equals("M")) {break;}
                    else {throw new RuntimeException();}
                } catch (Exception e) {
                    System.out.println(">> 입력이 잘못되었습니다. 다시 입력해 주세요.");
                    // scanner 입력 초기화
                    scan.nextLine();
                }
            }
            // 7. 생년월일 입력 + 유효성 검사 try/catch
            String birthDate;
            while (true) {
                System.out.println(">> 생년월일을 연-월-일 형식으로 입력해주세요.");
                System.out.print(">> 예시)'2001-01-01': ");
                birthDate = scan.next();
                try{
                    String[] splitDate = birthDate.split("-");
                    if (splitDate[0].length() == 4 && splitDate[1].length() == 2 && splitDate[2].length() == 2){
                        break;
                    } else {throw new RuntimeException();}
                }catch (Exception e){
                    System.out.println(">> 입력이 잘못되었습니다. 다시 입력해 주세요.");
                    // scanner 입력 초기화
                    scan.nextLine();
                }
            }
            // 8. 연락처 입력 + 유효성 검사 try/catch
            String phone;
            while (true) {
                System.out.println(">> 연락처를 입력해주세요. ");
                System.out.print(">> 예시) 010-1234-1234 또는 010-123-1234: ");
                phone = scan.next();
                try{
                    String[] splitPhone = phone.split("-");
                    if (splitPhone[0].length() == 3 && (splitPhone[1].length() == 3 || splitPhone[1].length() == 4) && splitPhone[2].length() == 4){
                        break;
                    } else {throw new RuntimeException();}
                }catch (Exception e){
                    System.out.println(">> 입력이 잘못되었습니다. 다시 입력해 주세요.");
                    // scanner 입력 초기화
                    scan.nextLine();
                }
            }
            // 9. 회원 분류 코드 입력 + 유효성 검사
            int accCategory;
            while (true) {
                try {
                    System.out.println(">> 회원 분류를 선택해 주세요.");
                    System.out.println(">> 일반 회원으로 등록하시려면 1을,");
                    System.out.println(">> PT 강사 회원으로 등록하시려면 2를 입력해주세요.");
                    System.out.print(">> 회원 분류: ");
                    int ch = scan.nextInt();
                    if (ch == 1) {
                        // 일반 회원 accCode = 2
                        accCategory = 2;
                        break;
                    }
                    else if (ch == 2) {
                        // PT 강사 회원 accCode = 3
                        accCategory = 3;
                        break;
                    }
                    else {throw new RuntimeException();}
                } catch (Exception e) {
                    System.out.println(">>입력이 잘못되었습니다. 다시 입력해 주세요.");
                    // scanner 입력 초기화
                    scan.nextLine();
                }
            }
            // 10. 모든 입력 성공시 MemberDto 객체 생성 및 DAO로 전달
            MemberDto memberDto = new MemberDto();
            memberDto.setID(id); // 아이디
            memberDto.setPW(pw); // 비밀번호
            memberDto.setMemberName(name); // 이름
            memberDto.setHeight(height); // 키
            memberDto.setExHabit(exHabit); // 운동습관
            memberDto.setGender(gender); // 성별
            memberDto.setBirthDate(birthDate); // 생년월일
            memberDto.setContact(phone); // 연락처
            memberDto.setAccCategory(accCategory); // 회원분류
            // DAO 저장 여부 불린값
            boolean result = MemberController.getInstance().signUp(memberDto);
            if (result){System.out.println(">>회원가입에 성공하였습니다. 회원이 되신 것을 축하드립니다!");}
            else {System.out.println(">>회원가입에 실패하였습니다. 다시 시도해 주세요.");}
        } catch (Exception e) { // 회원가입 전체 과정 try/catch
            System.out.println(">>입력이 잘못되었습니다. 다시 시도해 주세요.");
            // scanner 입력 초기화
            scan.nextLine();
        }
    }

    public void login() {
        System.out.print(">>아이디를 입력해주세요 : ");
        String id = scan.next();
        System.out.print(">>비밀번호를 입력해주세요 : ");
        String pw = scan.next();
        // ID & PW 들어있는 DTO
        MemberDto memberDto = new MemberDto();
        memberDto.setID(id);
        memberDto.setPW(pw);

        if (MemberController.getInstance().login(memberDto)) {
            // 로그인 성공시 이동 메뉴
            // loginAccCode 값이 1이면 관리자 화면으로 / 2면 일반 회원 화면으로 / 3이면 PT 강사 전용 화면으로 이동
            switch (loginAccCode) {
                case 1:
                    System.out.println(">>로그인 성공. 관리자 메뉴로 이동합니다 ");
                    AdminView.getInstance().indexAdm();
                    break;
                case 2:
                    System.out.println(">>로그인 성공. 회원 메뉴로 이동합니다 ");
                    NormalMemberView.getInstance().index();
                    break;
                case 3:
                    System.out.println(">>로그인 성공. PT 강사 회원 메뉴로 이동합니다 ");
                    PtMemberView.getInstance().index();
                    break;
            }
        } else {
            System.out.println(">>로그인이 실패하였습니다. 아이디와 비밀번호를 다시 확인해 주세요.");
        }
    }

    //아이디 찾기 메뉴
    public void findId(){
        System.out.println(">>아이디 찾기 메뉴입니다.");
        System.out.print(">>이름을 입력해주세요 : ");
        String name = scan.next();
        System.out.print(">>연락처를 입력해 주세요. 예시) 010-1234-1234 또는 010-123-1234 : ");
        String phone = scan.next();
        //memberDto 포장
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberName(name); memberDto.setContact(phone);
        // DB 검색후 ID를 String result 로 가져오기
        String result =  MemberController.getInstance().findId( memberDto );
        if( result != null ){  System.out.println(">>회원님의 아이디는 ["+result+"] 입니다.");}
        else{  System.out.println(">>회원정보가 없습니다."); }
    }

    //비밀번호 찾기 메뉴
    public void findPw(){
        System.out.println(">>비밀번호 찾기 메뉴입니다.");
        System.out.print(">>아이디를 입력해주세요 : ");
        String id = scan.next();
        System.out.print(">>이름을 입력해 주세요 : ");
        String name = scan.next();
        System.out.print(">>생년월일을 입력해 주세요. 예시) 2000-01-01 : ");
        String birthDate = scan.next();
        MemberDto memberDto = new MemberDto();
        memberDto.setID(id); memberDto.setMemberName(name); memberDto.setBirthDate(birthDate);
        String result =  MemberController.getInstance().findPw( memberDto );
        if( result != null ){  System.out.println(">>회원님의 비밀번호는 ["+result+"] 입니다.");}
        else{  System.out.println(">>회원정보가 없습니다."); }
    }



    //시작화면 아스키아트
    public void indexAsciiArt(){
        System.out.println(".·:'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''':·.");
        System.out.println(": :  ________   _______    ________   ________   ________   ________    ________   ___           : :");
        System.out.println(": : |\\   __  \\ |\\  ___ \\  |\\   __  \\ |\\   ____\\ |\\   __  \\ |\\   ___  \\ |\\   __  \\ |\\  \\          : :");
        System.out.println(": : \\ \\  \\|\\  \\\\ \\   __/| \\ \\  \\|\\  \\\\ \\  \\___|_\\ \\  \\|\\  \\\\ \\  \\\\ \\  \\\\ \\  \\|\\  \\\\ \\  \\         : :");
        System.out.println(": :  \\ \\   ____\\\\ \\  \\_|/__\\ \\   _  _\\\\ \\_____  \\\\ \\  \\\\\\  \\\\ \\  \\\\ \\  \\\\ \\   __  \\\\ \\  \\        : :");
        System.out.println(": :   \\ \\  \\___| \\ \\  \\_|\\ \\\\ \\  \\\\  \\|\\|____|\\  \\\\ \\  \\\\\\  \\\\ \\  \\\\ \\  \\\\ \\  \\ \\  \\\\ \\  \\____   : :");
        System.out.println(": :    \\ \\__\\     \\ \\_______\\\\ \\__\\\\ _\\  ____\\_\\  \\\\ \\_______\\\\ \\__\\\\ \\__\\\\ \\__\\ \\__\\\\ \\_______\\ : :");
        System.out.println(": :     \\|__|      \\|_______| \\|__|\\|__||\\_________\\\\|_______| \\|__| \\|__| \\|__|\\|__| \\|_______| : :");
        System.out.println(": :                                     \\|_________|                                             : :");
        System.out.println(": :                                                                                              : :");
        System.out.println(": :                                                                                              : :");
        System.out.println(": :  _________   ________   ________   ___   ________    _______    ________                     : :");
        System.out.println(": : |\\___   ___\\|\\   __  \\ |\\   __  \\ |\\  \\ |\\   ___  \\ |\\  ___ \\  |\\   __  \\                    : :");
        System.out.println(": : \\|___ \\  \\_|\\ \\  \\|\\  \\\\ \\  \\|\\  \\\\ \\  \\\\ \\  \\\\ \\  \\\\ \\   __/| \\ \\  \\|\\  \\                   : :");
        System.out.println(": :      \\ \\  \\  \\ \\   _  _\\\\ \\   __  \\\\ \\  \\\\ \\  \\\\ \\  \\\\ \\  \\_|/__\\ \\   _  _\\                  : :");
        System.out.println(": :       \\ \\  \\  \\ \\  \\\\  \\|\\ \\  \\ \\  \\\\ \\  \\\\ \\  \\\\ \\  \\\\ \\  \\_|\\ \\\\ \\  \\\\  \\|                 : :");
        System.out.println(": :        \\ \\__\\  \\ \\__\\\\ _\\ \\ \\__\\ \\__\\\\ \\__\\\\ \\__\\\\ \\__\\\\ \\_______\\\\ \\__\\\\ _\\                 : :");
        System.out.println(": :         \\|__|   \\|__|\\|__| \\|__|\\|__| \\|__| \\|__| \\|__| \\|_______| \\|__|\\|__|                : :");
        System.out.println(": :                                                                                              : :");
        System.out.println(": :                                                                                              : :");
        System.out.println(": :                                                                                              : :");
        System.out.println(": :  ________   ________   ________                                                              : :");
        System.out.println(": : |\\   __  \\ |\\   __  \\ |\\   __  \\                                                             : :");
        System.out.println(": : \\ \\  \\|\\  \\\\ \\  \\|\\  \\\\ \\  \\|\\  \\                                                            : :");
        System.out.println(": :  \\ \\   __  \\\\ \\   ____\\\\ \\   ____\\                                                           : :");
        System.out.println(": :   \\ \\  \\ \\  \\\\ \\  \\___| \\ \\  \\___|                                                           : :");
        System.out.println(": :    \\ \\__\\ \\__\\\\ \\__\\     \\ \\__\\                                                              : :");
        System.out.println(": :     \\|__|\\|__| \\|__|      \\|__|                                                              : :");
        System.out.println("'·:..............................................................................................:·'");
    }
}



















