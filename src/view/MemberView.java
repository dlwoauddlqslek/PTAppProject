package view;

import controller.MemberController;
import model.dto.MemberDto;

import java.util.Scanner;

public class MemberView {
    Scanner scan = new Scanner(System.in);

    public static MemberView mView = new MemberView();
    // 첫번째 회원 종류 선택창
    public void index(){
        while (true){
            try {
                System.out.println(">>>>> 1.일반회원 2.PT강사  :   ");
                int ch = scan.nextInt();
                if (ch == 1){normal();}
                else if (ch == 2){pt();}
                else if (ch == 99) {admin();}
                else {System.out.println("잘못된 입력입니다.");}
            }catch (Exception e){System.out.println(e); scan = new Scanner(System.in); }
        }
    }
    public void normal(){firstMenu(1);}
    public void pt(){firstMenu(2);}
    public void admin(){firstMenu(3);}

    // 처음 메뉴창
    public void firstMenu(int i){
        while (true){
            try {
                System.out.println(">>>>> 1.회원가입 2.로그인 3.아이디찾기 4.비밀번호찾기");
                int ch = scan.nextInt();
                if (ch==1){signup(i);}
                else if (ch==2){login();}
                else if (ch==3){findId();}
                else if (ch==4){findPw();}
                else{
                    System.out.println("잘못된 입력입니다.");
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void signup(int i){
        System.out.println("아이디 : "); String id = scan.next();
        System.out.println("비밀번호 : "); String pw = scan.next();
        System.out.println("이름 : "); String name = scan.next();
        System.out.println("키 : "); int height = scan.nextInt();
        System.out.println("평소 운동 수준 1 ~ 3 : "); int habit = scan.nextInt();
        System.out.println("성별 M/F"); String gender = scan.next();
        System.out.println("생일 ex)2001-01-01 : "); String birthDate = scan.next();
        System.out.println("연락처 : "); String phone = scan.next();

        MemberDto memberDto = new MemberDto();
        memberDto.setID(id); memberDto.setPW(pw);memberDto.setMemberName(name);memberDto.setHeight(height);
        memberDto.setExHabit(habit);memberDto.setGender(gender);memberDto.setBirthDate(birthDate);
        memberDto.setContact(phone);memberDto.setAccCategory(i);

        boolean result = MemberController.getInstance().signup(memberDto);
        if (result){System.out.println("회원가입 성공");}
        else {System.out.println("회원가입 실패");}
    }

    public void login() {
        System.out.print("아이디 : ");
        String id = scan.next();
        System.out.print("비밀번호 : ");
        String ps = scan.next();
        MemberDto memberDto = new MemberDto();
        memberDto.setID(id);
        memberDto.setPW(ps);
        boolean result = MemberController.getInstance().login(memberDto);
        if (result) {
            System.out.println("로그인성공 ");
            // 로그인 성공시 이동 메뉴 만들기 ------->>>>>>
            // loginCate(memberDto);
        } else {
            System.out.println("로그인실패");
        }
    }


//    public void loginCate(MemberDto memberDto) {
//
//        String result = MemberController.getInstance().loginCate(memberDto);
//
//        if( result == 1 ){  }
//        else if (){}
//
//        else{  System.out.println("동일한 회원정보가 없습니다."); }
//        }
//    }

    public void findId(){
        System.out.print("회원가입한 이름을 입력해주세요 : ");
        String name = scan.next();
        System.out.print("회원가입한 연락처를 입력해 주세요 : ");
        String phone = scan.next();
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberName(name); memberDto.setContact(phone);
        String result =  MemberController.getInstance().findId( memberDto );
        if( result != null ){  System.out.println(" 회원님의 아이디는 ["+result+"] 입니다.");}
        else{  System.out.println("동일한 회원정보가 없습니다."); }
    }

    public void findPw(){
        System.out.print("회원가입한 아이디를 입력해주세요 : ");
        String id = scan.next();
        System.out.print("회원가입한 이름을 입력해 주세요 : ");
        String name = scan.next();
        System.out.print("회원가입한 생일 입력해 주세요(xxxx-xx-xx) : ");
        String birth = scan.next();
        MemberDto memberDto = new MemberDto();
        memberDto.setID(id);memberDto.setMemberName(name); memberDto.setBirthDate(birth);
        String result =  MemberController.getInstance().findPw( memberDto );
        if( result != null ){  System.out.println(" 회원님의 비밀번호는 ["+result+"] 입니다.");}
        else{  System.out.println("동일한 회원정보가 없습니다."); }
    }









}



















