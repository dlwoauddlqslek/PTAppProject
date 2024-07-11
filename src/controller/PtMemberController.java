package controller;

import model.dao.MemberDao;
import model.dao.MessageDao;
import model.dao.WeightRecordDao;
import model.dto.*;

import java.util.ArrayList;

import static controller.MemberController.loginMCode;

public class PtMemberController {
    // 싱글톤 패턴
    private static PtMemberController ptMemberController = new PtMemberController();
    private PtMemberController(){}
    public static PtMemberController getInstance(){ return ptMemberController; }
    // /싱글톤 패턴

    public static int msgCurrentPage = 1;

    public ArrayList<MessageDto> checkPtMsgNoReply(int msgCurrentPage) {
        return MessageDao.getInstance().checkPtMsgNoReply(msgCurrentPage, loginMCode);
    }

    public boolean mUpdate(MemberDto memberDto){
        memberDto.setMemberCode(loginMCode);
        return MemberDao.getInstance().mUpdate(memberDto);
    }

    public void logOut( ){
        loginMCode = 0;
    }

    public ArrayList<MemberDto> msgShowMemberList(int msgMemberListPage) {
        return MemberDao.getInstance().msgShowMemberList(msgMemberListPage);
    }
    // 쪽지 내역 조회하기
    public ArrayList<MessageDto> msgView(int msgCurrentPage) {
        return MessageDao.getInstance().msgView(msgCurrentPage, loginMCode);
    }
    // 답장 수정하기
    public boolean msgReplyEdit(MessageDto msgDto) {
        return MessageDao.getInstance().msgReplyEdit(msgDto);
    }
    // 답장 삭제하기
    public boolean msgReplyDelete(int messageCode) {
        return MessageDao.getInstance().msgReplyDelete(messageCode);
    }
    // 답장 보내기
    public boolean ptWriteReply(MessageDto messageDto) {
        return MessageDao.getInstance().ptWriteReply(messageDto);
    }

    public WeightRecordDto getWeight(int memberCode) {
        return WeightRecordDao.getInstance().checkWeight(memberCode);
    }

    public MemberDto getMemberInfo(int memberCode) {
        return MemberDao.getInstance().getMemberInfo(memberCode);
    }

    public ArrayList<AteFoodRecordDto> getFoodRecord(int memberCode) {
        return NormalMemberController.getInstance().getDailyFoodList(memberCode, 0);
    }


    public ArrayList<WorkOutRecordDto> getWorkOutRecord(int memberCode) {
        return NormalMemberController.getInstance().getDailyWorkoutList(memberCode, 0);
    }
}
