package view;

import java.util.ArrayList;
import java.util.Scanner;

import model.dto.MessageDto;
import model.dto.MemberDto;
import controller.NormalMemberController;
import model.dto.AteFoodRecordDto;


public class NormalMemberView {
    //쪽지 메뉴 현재 페이지 번호

    // 싱글톤 패턴
    private static NormalMemberView normalMemberView = new NormalMemberView();
    private NormalMemberView(){}
    public static NormalMemberView getInstance(){ return normalMemberView; }
    // /싱글톤 패턴

    // 스캐너 객체 생성
    Scanner scan = new Scanner(System.in);

    int kcal=2400;
    public void index(){
        while(true) {
            try {
                System.out.println("1.음식칼로리계산");
                int ch = scan.nextInt();
                if (ch == 1) {
                    foodCal();
                } else {
                    System.out.println("없는 기능입니다.");
                }


            } catch (Exception e) {
                System.out.println(e);
                System.out.println("잘못된 입력입니다.");
                scan = new Scanner(System.in);
            }
        }
        }
    public  void foodCal(){
        scan.nextLine();
        System.out.print("먹은 음식을 입력해주세요: ");
        String foodName=scan.nextLine();
        int result= NormalMemberController.getInstance().foodcal(foodName);
        if(result>=0){
            System.out.println("현재 남은 칼로리: " + (kcal - result) );
        }
        else{System.out.println("다시 입력해주세요");}

    }







    // ====================== 쪽지 메뉴 ===================================== //
    public void msgView(){
        // 쪽지 메뉴 화면
        // 로그인된 회원의 accCode 확인 ( 2 )
        // 10개 내역 가져오기, 페이지 변수?

        // 메뉴 들어올 시 띄울 정보
        // 쪽지 번호 \ msgTitle \ msgView \ msgDate \ replyContent 여부 확인?
        // 1 \ 제목1 \ 0 \ 2024-07-03 \ 답장이 아직 없습니다
        // 2 \ 제목2 \ 4 \ 2024-07-03 \ 답장이 있습니다
        // 1~10 = 해당 쪽지 보기
        // p = 이전 10개, n = 다음 10개, s = 쪽지 보내기, b = 돌아가기
        // 3 = 강사회원 : 1 받은 쪽지 확인하기, 2 답장 보내기, 3 쪽지 보낸 회원의 키, 몸무게, 음식기록, 운동기록 확인하기, 4 쪽지 내역 보기
        while (true) {
            //msgPrint(currentPage);
            System.out.println("============ 쪽지 메뉴 "+ NormalMemberController.msgCurrentPage + "페이지 =============");
            System.out.println("쪽지번호    제목    조회수    보낸 날짜    답장 날짜");
            //DB에서 현재 페이지 번호에 해당되는 쪽지 목록 가져와 출력
            ArrayList<MessageDto> msgList = NormalMemberController.getInstance().msgView(NormalMemberController.msgCurrentPage);
            //쪽지 출력 for문
            for (int i = 0; i < msgList.size(); i++){
                String replyDate;
                if (msgList.get(i).getReplyDate() == null){
                    replyDate = "답장이 아직 없습니다";
                } else { replyDate = msgList.get(i).getReplyDate().substring(0,10);}
                System.out.println("  " + (i+1) +
                        " | " + msgList.get(i).getMsgTitle() + " | " + msgList.get(i).getMsgView() +
                        " | " + msgList.get(i).getMsgDate().substring(0,10) + " | " + replyDate
                );
            }
            System.out.println("1~10 = 해당 쪽지 보기, p = 이전 페이지, n = 다음 페이지");
            System.out.print("s = 쪽지 보내기, b = 돌아가기 : ");
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scan.nextInt();
                scan.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scan.next().charAt(0);
                scan.nextLine();
            }
            if (ch >= 1 && ch <= 10){ // 쪽지 상세보기
                if (ch <= msgList.size()) { // 쪽지 화면 번호와 입력 값의 유효성 검사
                    msgCheckMessage(ch, msgList.get(ch - 1));
                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>쪽지번호를 다시 확인해주세요.");
                    System.out.println();
                }
            }
            else if (ch == 'P' || ch == 'p'){ // 쪽지 내역 이전 10개 출력
                if (NormalMemberController.msgCurrentPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    NormalMemberController.msgCurrentPage--;
                }
            }
            else if (ch == 'N' || ch == 'n'){ // 쪽지 내역 다음 10개
                // 불러올 쪽지 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (NormalMemberController.getInstance().msgView(NormalMemberController.msgCurrentPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    NormalMemberController.msgCurrentPage++;
                }
            }
            else if (ch == 'S' || ch == 's'){ // 쪽지 보내기
                if (msgSendMessage()){ // 쪽지 보내기 함수가 성공/실패 true/false, 기본 페이지번호 1
                    System.out.println(">>쪽지 전송 완료!");
                }
            }
            else if (ch == 'B' || ch == 'b'){ // 메뉴 돌아가기(NormalMemberView)
                System.out.println(">>이전 메뉴로 돌아갑니다.");
                break;
            }
            else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }
        }
    }

    // 일반) 쪽지 메뉴 1 - 쪽지 보내기
    public boolean msgSendMessage(){
        while(true) {
            // 받을 PT 강사회원
            System.out.println("페이지 " + NormalMemberController.msgPtMemberListPage);
            ArrayList<MemberDto> ptMemberList = NormalMemberController.getInstance().msgShowPtMemberList(NormalMemberController.msgPtMemberListPage);
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scan.nextInt();
                scan.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scan.next().charAt(0);
                scan.nextLine();
            }
            System.out.println("p = 이전 페이지, n = 다음 페이지, b =돌아가기");
            System.out.print("1~10 = 해당 강사에게 쪽지 보내기 : ");
            if (ch >= 1 && ch <= 10){ // 강사 번호를 선택
                if (ch <= ptMemberList.size()) { // 강사 화면 번호와 입력 값의 유효성 검사
                    // 쪽지 제목
                    System.out.print(">>쪽지 제목을 입력해 주세요 : "); String title = scan.nextLine();
                    // 쪽지 내용
                    System.out.print(">>쪽지 내용을 입력해 주세요 : "); String content = scan.nextLine();
                    // MessageDTO 포장
                    MessageDto msgDto = new MessageDto();
                    msgDto.setReceivedMCode(ptMemberList.get(ch-1).getMemberCode());
                    msgDto.setMsgTitle(title);
                    msgDto.setMsgContent(content);

                    if (NormalMemberController.getInstance().msgSendMessage(msgDto)){
                        System.out.println(">>쪽지 전송 완료!");
                        return true;
                    } else {
                        System.out.println(">>쪽지 전송에 실패하였습니다.");
                    }

                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>PT강사 번호를 다시 확인해주세요.");
                    System.out.println();
                }
            }
            else if (ch == 'P' || ch == 'p'){ // 쪽지 내역 이전 10개 출력
                if (NormalMemberController.msgPtMemberListPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    NormalMemberController.msgPtMemberListPage--;
                }
            }
            else if (ch == 'B' || ch == 'b'){
                break;
            }
        }
        return false;
    }
    // PT 강사 회원 목록 불러오기
    private ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        return null;
    }

    // 일반 & 강사) 쪽지 메뉴 3 & 4 - 쪽지 송신 내역 보기
    public void msgCheckHistory(){
        // accCode와 memberCode를 받아 쪽지 데이터 가져오기
    }
    // 쪽지 상세내용 보기
    public void msgCheckMessage(int screenNum, MessageDto msgDto){ // 화면 쪽지 번호 ch, 해당 DTO객체 msgDTO
        // 2 \ 제목2 \ 4 \ 2024-07-03 \ 답장이 있습니다
        System.out.println("============ " + screenNum + "번 쪽지 상세보기 ================");
        System.out.print("쪽지 제목 : "); System.out.print(msgDto.getMsgTitle());
        System.out.print("\t\t 조회수 : "); System.out.print(msgDto.getMsgView());
        System.out.print("\t\t 보낸 시간 : "); System.out.println(msgDto.getMsgDate());
        System.out.println(msgDto.getMsgContent());
        System.out.println("================ 답장 =================");
        System.out.print("받은 시간 : ");System.out.println(msgDto.getReplyDate());
        System.out.println(msgDto.getReplyContent());

        System.out.print(">>엔터 키로 돌아가기 : "); String enter = scan.nextLine();
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

