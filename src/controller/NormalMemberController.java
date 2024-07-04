package controller;

import model.dao.AteFoodRecordDao;
import model.dto.AteFoodRecordDto;

public class NormalMemberController {
    private static NormalMemberController normalMemberController=new NormalMemberController();

    private NormalMemberController(){}

    public static NormalMemberController getInstance(){return normalMemberController;}

    public int foodcal(String foodName){
        //1.음식 칼로리,코드
        AteFoodRecordDto result1= AteFoodRecordDao.getInstance().foodCal(foodName);
        if (result1==null){return -1;}
        System.out.println("result1 = " + result1);
        //2. 로그인된회원번호 (샘플 )
        int loginMno=2;
        // 3. 로그인한 회원이 먹은 음식코드의 칼로리 합계
        int result2 = AteFoodRecordDao.getInstance().aaaaa( loginMno );
        return result2;
    }
}
