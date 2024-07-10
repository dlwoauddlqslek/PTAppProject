package controller;

import model.dao.MemberDao;
import model.dto.MemberDto;

import java.util.ArrayList;

public class MemberController {

    private static MemberController mControl = new MemberController();
    private MemberController(){}
    public static MemberController getInstance(){return mControl;}

    // 회원가입 함수
    public boolean signUp(MemberDto memberDto){
        return MemberDao.getInstance().signUp(memberDto); // MemberDao 에서 리턴하는 값이 true/false
    }

    // member 관련 static 전역변수들
    // 현재 로그인중인 회원 코드
    public static int loginMCode = 2;
    // 현재 로그인된 회원 분류 코드
    public static int loginAccCode = 0;

    // 로그인 함수 ID,PW ->
    public boolean login(MemberDto memberDto){
        MemberDto memberDto1 = MemberDao.getInstance().login(memberDto);
        // memberCode 검사로 로그인 유효성 검사
        if (memberDto1.getMemberCode() == 0){return false;}
        loginMCode = memberDto1.getMemberCode();
        loginAccCode = memberDto1.getAccCategory();
        NormalMemberController.getInstance().gender = memberDto1.getGender();
        NormalMemberController.getInstance().height = memberDto1.getHeight();
        NormalMemberController.getInstance().birthDate = memberDto1.getBirthDate();
        NormalMemberController.getInstance().exHabit = memberDto1.getExHabit();
        return true;

    }

    // 아이디 찾기 함수
    public ArrayList<String> findId(MemberDto memberDto){
        return MemberDao.getInstance().findId(memberDto);
    }
    // 비밀번호 찾기 함수
    public String findPw(MemberDto memberDto){
        return MemberDao.getInstance().findPw(memberDto);
    }

    // 회원 수정 함수
    public boolean mUpdate(MemberDto memberDto){
        System.out.println(memberDto);
        memberDto.setMemberCode(loginMCode);
        return MemberDao.getInstance().mUpdate(memberDto);
    }

    // 회원 로그아웃 함수
    public void logOut( ){
        loginMCode = 0;
    }

    //회원 탈퇴 함수
    public boolean removeMem(MemberDto memberDto){
        memberDto.setMemberCode(loginMCode);
        return MemberDao.getInstance().removeMem(memberDto);
    }

}
