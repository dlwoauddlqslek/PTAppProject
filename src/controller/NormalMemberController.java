package controller;

import model.dao.MemberDao;
import model.dao.MessageDao;
import model.dto.MemberDto;
import model.dto.MessageDto;

import java.util.ArrayList;

public class NormalMemberController {
    //싱글톤 패턴
    private static NormalMemberController normalController = new NormalMemberController();
    private NormalMemberController(){};
    public static NormalMemberController getInstance(){
        return normalController;
    }
    // 현재 로그인중인 회원 코드
    public static int loginMCode = 2;


    // 쪽지 내역 출력
    public ArrayList<MessageDto> msgView(int msgCurrentPage){
        return MessageDao.getInstance().msgView(msgCurrentPage, loginMCode);
    }
    // 쪽지 보내기
    public boolean msgSendMessage(MessageDto msgDto){
        // 받을 PT 강사회원 쪽지 제목 쪽지 내용
        MessageDao.getInstance().msgSendMessage(msgDto);
        return false;
    }
    // 일반) 쪽지 메뉴 2 - 답장 확인하기
    public ArrayList<MessageDto> msgCheckReply(){
        return MessageDao.getInstance().msgCheckReply();
    }
    // 일반 & 강사) 쪽지 메뉴 3 & 4 - 쪽지 송신 내역 보기
    public void msgCheckHistory(){
        // accCode와 memberCode를 받아 쪽지 데이터 가져오기
    }
    // 강사) 쪽지 메뉴 - 받은 쪽지 확인하기
    public void msgCheckMessage(){
        //
    }
    // 강사) 쪽지 메뉴 - 받은 쪽지 답장 보내기
    public void msgSendReply(){

    }

    // 강사) 쪽지 메뉴 - 쪽지 보낸 회원 정보(키, 몸무게, 음식기록, 운동기록) 확인하기
    public void msgCheckMemberStat(){
        // 현재 memberCode를 보내 쪽지 기록이 있는 회원 코드와 이름을 불러오기
        // 1. 아무개 ... <- 번호를 고르면 키와 몸무게가 뜨고 1.음식기록 2.운동기록 3.뒤로가기
        // 1/2를 고르면 오늘 날짜 기준으로 기록을 가져온다. 1.전날 2.다음날 3.돌아가기
    }

    public ArrayList<MessageDto> msgPrint(int currentPage) {
        return null;
    }
    // 쪽지 보내기 메뉴 - PT 강사 목록 불러오기
    public ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        return MemberDao.getInstance().msgShowPtMemberList(msgPtMemberListPage);
    }
}
