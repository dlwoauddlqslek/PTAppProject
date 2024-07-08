package controller;

import model.dao.MemberDao;
import model.dto.MemberDto;

public class MemberController {

    private static MemberController mControl = new MemberController();
    private MemberController(){}
    public static MemberController getInstance(){return mControl;}


    public boolean signup(MemberDto memberDto){
        boolean result = MemberDao.getInstance().signup(memberDto);
        return result;
    }

    // 하루 칼로리 계산
    // 기초 대사량 : Mifflin-St Jeor Equation
    // member 관련 static 전역변수들
    public static int loginNo = 0; //로그인 상태
    public static int currentKcal = 12000; //당일 칼로리 계산
    // 현재 로그인중인 회원 코드
    public static int loginMCode = 2;
    // 현재 로그인된 회원 분류 코드
    public static int loginAccCode = 2;

    public boolean login(MemberDto memberDto){
        loginNo = MemberDao.getInstance().login(memberDto);
        return loginNo==0 ? false : true;
    }

    public int loginCate(MemberDto memberDto){
        return MemberDao.getInstance().loginCate(memberDto);
    }


    public String findId(MemberDto memberDto){
        return MemberDao.getInstance().findId(memberDto);
    }

    public String findPw(MemberDto memberDto){
        return MemberDao.getInstance().findPw(memberDto);
    }




}
