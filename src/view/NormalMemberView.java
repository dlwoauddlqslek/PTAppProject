package view;

import controller.NormalMemberController;
import model.dao.MessageDao;
import model.dto.MessageDto;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class NormalMemberView {

    Scanner scanner = new Scanner(System.in);
















    // 쪽지 메뉴 ===================================== //
    public void msgView(){
        // 쪽지 메뉴 화면
        // 로그인된 회원의 accCode 확인 ( 2 )
        // 10개 내역 가져오기, 페이지 변수?
        int currentPage = 1;
        // 메뉴 들어올 시 띄울 정보
        // 쪽지 번호 \ msgTitle \ msgView \ msgDate \ replyContent 여부 확인?
        // 1 \ 제목1 \ 0 \ 2024-07-03 \ 답장이 아직 없습니다
        // 2 \ 제목2 \ 4 \ 2024-07-03 \ 답장이 있습니다
        // 1~10 = 해당 쪽지 보기
        // p = 이전 10개, n = 다음 10개, s = 쪽지 보내기, h = 쪽지 내역 보기
        // 3 = 강사회원 : 1 받은 쪽지 확인하기, 2 답장 보내기, 3 쪽지 보낸 회원의 키, 몸무게, 음식기록, 운동기록 확인하기, 4 쪽지 내역 보기
        while (true) {
            msgPrint(currentPage);
            System.out.print("쪽지 메뉴 화면");
            int ch;
            try {
                ch = scanner.nextInt();
                System.out.println("ch = " + ch);
                scanner.nextLine();
            } catch (Exception e) {
                ch = scanner.next().charAt(0);
                System.out.println("ch = " + ch);
                scanner.nextLine();
            }
            if (ch >= 1 && ch <= 10){
                //쪽지 상세보기
            }
            else if (ch == 80 || ch == 112){
                System.out.println(ch);
            }
             else {
                System.out.println(">>입력이 잘못되었습니다.");
                scanner = new Scanner(System.in);
        }
    }
    // 쪽지 메뉴에서 쪽지 내용 불러오기
    private void msgPrint(int currentPage) {
        ArrayList<MessageDto> msgList = NormalMemberController.getInstance().msgPrint(currentPage);
        msgList.forEach(msg -> {});
    }

    // 일반) 쪽지 메뉴 1 - 쪽지 보내기
    public void msgSendMessage(){
        // 쪽지 제목
        // 쪽지 내용
        // 받을 PT 강사회원
        // 재확인
    }

    // 일반 & 강사) 쪽지 메뉴 3 & 4 - 쪽지 송신 내역 보기
    public void msgCheckHistory(){
        // accCode와 memberCode를 받아 쪽지 데이터 가져오기
    }
    // 일반 & 강사) 쪽지 메뉴 - 받은 쪽지 확인하기
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
