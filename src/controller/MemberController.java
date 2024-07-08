package controller;

import model.dao.MemberDao;
import model.dto.MemberDto;
import view.NormalMemberView;

public class MemberController {

    private static MemberController mControl = new MemberController();
    private MemberController(){}
    public static MemberController getInstance(){return mControl;}

    // 회원가입 함수
    public boolean signup(MemberDto memberDto){
        boolean result = MemberDao.getInstance().signup(memberDto);
        return result;
    }



    // member 관련 static 전역변수들
    public static int loginNo = 0; //로그인 상태
    public static int currentKcal = 12000; //당일 칼로리 계산
    // 현재 로그인중인 회원 코드
    public static int loginMCode = 2;
    // 현재 로그인된 회원 분류 코드
    public static int loginAccCode = 2;

    // 로그인 함수
    public boolean login(MemberDto memberDto){
        loginNo = MemberDao.getInstance().login(memberDto);
        return loginNo==0 ? false : true;
    }


    // 아이디 찾기 함수
    public String findId(MemberDto memberDto){
        return MemberDao.getInstance().findId(memberDto);
    }
    // 비밀번호 찾기 함수
    public String findPw(MemberDto memberDto){
        return MemberDao.getInstance().findPw(memberDto);
    }

    // 회원 수정 함수
    public boolean mUpdate(MemberDto memberDto){
        System.out.println(memberDto);
        memberDto.setMemberCode(loginNo);
        return MemberDao.getInstance().mUpdate(memberDto);
    }

    // 회원 로그아웃 함수
    public void logOut( ){
        loginNo = 0;
    }


}
