package model.dao;

import model.dto.MessageDto;

import java.util.ArrayList;

public class MessageDao {

    private static MessageDao msgDao = new MessageDao();
    private MessageDao(){};
    public static MessageDao getInstance(){
        return msgDao;
    }
    // 쪽지 메뉴
    public void msgPrint(){
        // 로그인된 회원의 accCode 확인
        // 2 = 일반회원 : 1 쪽지 보내기, 2 답장 확인하기, 3 쪽지 내역 보기
        // 3 = 강사회원 : 1 받은 쪽지 확인하기, 2 답장 보내기, 3 쪽지 보낸 회원의 키, 몸무게, 음식기록, 운동기록 확인하기, 4 쪽지 내역 보기
    }
    // 일반) 쪽지 메뉴 1 - 쪽지 보내기
    public void msgSendMessage(){
        // 쪽지 제목
        // 쪽지 내용
        // 받을 PT 강사회원
        // 재확인
    }
    // 일반) 쪽지 메뉴 2 - 답장 확인하기
    public ArrayList<MessageDto> msgCheckReply(){

        return null;
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
}
