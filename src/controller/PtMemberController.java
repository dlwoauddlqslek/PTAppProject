package controller;


import model.dao.MessageDao;
import model.dto.MessageDto;

import java.util.ArrayList;

import static controller.MemberController.loginMCode;

public class PtMemberController {
    // 싱글톤 패턴
    private static PtMemberController ptMemberController = new PtMemberController();
    private PtMemberController(){}
    public static PtMemberController getInstance(){ return ptMemberController; }
    // /싱글톤 패턴

    public static int msgCurrentPage = 1;

    public ArrayList<MessageDto> checkPtMsgList(int msgCurrentPage) {
        return MessageDao.getInstance().msgView(msgCurrentPage, loginMCode);
    }
}
