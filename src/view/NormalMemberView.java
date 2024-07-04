package view;

import controller.NormalMemberController;
import model.dto.MessageDto;
import model.dto.MemberDto;

import java.util.ArrayList;
import java.util.Scanner;

public class NormalMemberView {
    public static void main(String[] args) {
        int msgCurrentPage = 1;
        msgView(msgCurrentPage);
    }

















    // 쪽지 메뉴 ===================================== //
    public static void msgView(int msgCurrentPage){
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
        Scanner scanner = new Scanner(System.in);
        while (true) {
            //msgPrint(currentPage);
            System.out.println("============ 쪽지 메뉴 "+ msgCurrentPage + "페이지 =============");
            System.out.println("쪽지번호    제목    조회수    보낸 날짜    답장 날짜");
            ArrayList<MessageDto> msgList = NormalMemberController.getInstance().msgView(msgCurrentPage);
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
                ch = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scanner.next().charAt(0);
                scanner.nextLine();
            }
            if (ch >= 1 && ch <= 10){ // 쪽지 상세보기
                if (ch <= msgList.size()) { // 쪽지 화면 번호와 입력 값의 유효성 검사
                    msgCheckMessage(msgCurrentPage, ch, msgList.get(ch - 1));
                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>쪽지번호를 다시 확인해주세요.");
                    System.out.println();
                    msgView(msgCurrentPage);
                }
            }
            else if (ch == 'P' || ch == 'p'){ // 쪽지 내역 이전 10개 출력
                if (msgCurrentPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    msgCurrentPage--;
                }
                msgView(msgCurrentPage);
            }
            else if (ch == 'N' || ch == 'n'){ // 쪽지 내역 다음 10개
                // 불러올 쪽지 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (NormalMemberController.getInstance().msgView(msgCurrentPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    msgCurrentPage++;
                }
                msgView(msgCurrentPage);
            }
            else if (ch == 'S' || ch == 's'){ // 쪽지 보내기
                if (msgSendMessage(1)){ // 쪽지 보내기 함수가 성공/실패 true/false, 기본 페이지번호 1
                    System.out.println(">>쪽지 전송 완료!");
                }
                msgView(msgCurrentPage);
            }
            else if (ch == 'B' || ch == 'b'){ // 메뉴 돌아가기(NormalMemberView)
                System.out.println(">>이전 메뉴로 돌아갑니다.");
                break;
            }
            else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scanner = new Scanner(System.in); // 새 scanner 객체 부여
                msgView(msgCurrentPage); // 화면 새로고침
            }
        }
    }

    // 일반) 쪽지 메뉴 1 - 쪽지 보내기
    public static boolean msgSendMessage(int msgPtMemberListPage){
        Scanner scanner = new Scanner(System.in);
        while(true) {
            // 받을 PT 강사회원
            System.out.println("페이지 " + msgPtMemberListPage);
            ArrayList<MemberDto> ptMemberList = NormalMemberController.getInstance().msgShowPtMemberList(msgPtMemberListPage);
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scanner.next().charAt(0);
                scanner.nextLine();
            }
            System.out.println("p = 이전 페이지, n = 다음 페이지, b =돌아가기");
            System.out.print("1~10 = 해당 강사에게 쪽지 보내기 : ");
            if (ch >= 1 && ch <= 10){ // 강사 번호를 선택
                if (ch <= ptMemberList.size()) { // 강사 화면 번호와 입력 값의 유효성 검사
                    // 쪽지 제목
                    System.out.print(">>쪽지 제목을 입력해 주세요 : "); String title = scanner.nextLine();
                    // 쪽지 내용
                    System.out.print(">>쪽지 내용을 입력해 주세요 : "); String content = scanner.nextLine();
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
                if (msgPtMemberListPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    msgPtMemberListPage--;
                    msgSendMessage(msgPtMemberListPage);
                }
            }
            else if (ch == 'B' || ch == 'b'){
                break;
            }
        }
        return false;
    }
    // PT 강사 회원 목록 불러오기
    private static ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        return null;
    }

    // 일반 & 강사) 쪽지 메뉴 3 & 4 - 쪽지 송신 내역 보기
    public static void msgCheckHistory(){
        // accCode와 memberCode를 받아 쪽지 데이터 가져오기
    }
    // 쪽지 상세내용 보기
    public static void msgCheckMessage(int msgCurrentPage, int screenNum, MessageDto msgDto){ // 화면 쪽지 번호 ch, 해당 DTO객체 msgDTO
        // 2 \ 제목2 \ 4 \ 2024-07-03 \ 답장이 있습니다
        System.out.println("============ " + screenNum + "번 쪽지 상세보기 ================");
        System.out.print("쪽지 제목 : "); System.out.print(msgDto.getMsgTitle());
        System.out.print("\t\t 조회수 : "); System.out.print(msgDto.getMsgView());
        System.out.print("\t\t 보낸 시간 : "); System.out.println(msgDto.getMsgDate());
        System.out.println(msgDto.getMsgContent());
        System.out.println("================ 답장 =================");
        System.out.print("받은 시간 : ");System.out.println(msgDto.getReplyDate());
        System.out.println(msgDto.getReplyContent());

        Scanner scanner = new Scanner(System.in);
        System.out.print(">>엔터 키로 돌아가기 : "); String enter = scanner.nextLine();
        msgView(msgCurrentPage);
    }
    // 강사) 쪽지 메뉴 - 받은 쪽지 답장 보내기
    public static void msgSendReply(){

    }

    // 강사) 쪽지 메뉴 - 쪽지 보낸 회원 정보(키, 몸무게, 음식기록, 운동기록) 확인하기
    public static void msgCheckMemberStat(){
        // 현재 memberCode를 보내 쪽지 기록이 있는 회원 코드와 이름을 불러오기
        // 1. 아무개 ... <- 번호를 고르면 키와 몸무게가 뜨고 1.음식기록 2.운동기록 3.뒤로가기
        // 1/2를 고르면 오늘 날짜 기준으로 기록을 가져온다. 1.전날 2.다음날 3.돌아가기
    }
}

