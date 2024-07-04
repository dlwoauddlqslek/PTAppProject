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


    int loginNo = 0; //로그인 상태

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
