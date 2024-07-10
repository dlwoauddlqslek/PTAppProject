package view;


import controller.MemberController;
import controller.NormalMemberController;
import controller.PtMemberController;
import static controller.PtMemberController.msgCurrentPage;

import model.dto.MemberDto;
import model.dto.MessageDto;

import java.util.ArrayList;
import java.util.Scanner;

public class PtMemberView {
    // 싱글톤 패턴
    private static PtMemberView ptMemberView = new PtMemberView();
    private PtMemberView(){}
    public static PtMemberView getInstance(){ return ptMemberView; }
    // /싱글톤 패턴

    // 스캐너 객체
    Scanner scan = new Scanner(System.in);

    public void index(){
        msgCurrentPage = 1; // 메뉴 진입시 현재 페이지 초기화
        while (true) {
            //강사 메뉴 "디스플레이"
            System.out.println("=========== 강사 메뉴 | 답장이 필요한 쪽지  " + msgCurrentPage + " 페이지 ===========");
            System.out.println("번호         제목          받은 날짜");
            System.out.println("--------------------------------------------------------");
            ArrayList<MessageDto> ptMsgList;
            // ArrayList MessageDAO에서 가져오기
            ptMsgList = PtMemberController.getInstance().checkPtMsgNoReply(msgCurrentPage);
            //쪽지 목록 출력 for문
            for (int i = 0; i < ptMsgList.size(); i++){
                String title = ptMsgList.get(i).getMsgTitle();
                int maxTitleLen = 15;
                title = title.length() > maxTitleLen ? title.substring(0,maxTitleLen-3) + "..." : title;// 제목 : 10글자 이상이면 말줄임표
                String sentDate = ptMsgList.get(i).getMsgDate().substring(0,10); // 날짜까지만 dateTime 표시
                System.out.printf("%2d | %-15s | %10s\n", i+1, title, sentDate);
            }
            System.out.println("--------------------------------------------------------");
            // 강사 메뉴 "디스플레이" 끝
            
            System.out.println("1 ~ 10 = 해당 쪽지에 답글 작성하기, p = 이전 페이지, n = 다음 페이지");
            System.out.println("h = 받은 쪽지 내역 보기, s = 쪽지 보낸 회원의 건강 정보 조회");
            System.out.println("e = 개인정보 수정, o = 로그아웃, x = 회원탈퇴 : ");
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scan.nextInt();
                scan.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scan.next().charAt(0);
                scan.nextLine();
            }
            if (ch >= 1 && ch <= 10) { // 쪽지 상세보기
                if (ch <= ptMsgList.size()) { // 쪽지 화면 번호와 입력 값의 유효성 검사
                    ptCheckMessage(ch, ptMsgList.get(ch - 1));
                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>쪽지번호를 다시 확인해주세요.");
                    System.out.println();
                }
            } else if (ch == 'P' || ch == 'p') { // 쪽지 내역 이전 10개 출력
                if (msgCurrentPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                } else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    msgCurrentPage--;
                }
            } else if (ch == 'N' || ch == 'n') { // 쪽지 내역 다음 10개
                // 불러올 쪽지 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (PtMemberController.getInstance().checkPtMsgNoReply(msgCurrentPage + 1).isEmpty()) {
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                } else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    msgCurrentPage++;
                }
            } else if (ch == 'H' || ch == 'h') { // 쪽지 내역 조회하기
                ptMsgHistory();
            } else if (ch == 'S' || ch == 's') { // 쪽지 보낸 회원의 건강 정보 조회
                msgCheckMemberStat();
            } else if (ch == 'E' || ch == 'e'){ // 회원 개인 정보 수정 함수
                System.out.print(">>새 키 측정결과를 1cm 단위까지 입력해주세요 : "); int height = scan.nextInt();
                System.out.println(">>새 운동습관을 설정해주세요.");
                System.out.println(">>운동을 거의 하지 않는다 : 1, 일주일에 3~4번 정도 : 2, 매일 운동 또는 격한 운동 3~4일 : 3"); int habit = scan.nextInt();
                System.out.print(">>변경할 연락처를 입력해주세요 : "); String mphone = scan.next();
                MemberDto memberDto = new MemberDto();
                memberDto.setHeight(height);
                memberDto.setExHabit(habit);
                memberDto.setContact(mphone);
                boolean result = PtMemberController.getInstance().mUpdate(memberDto);
                System.out.println(result);
                if (result){System.out.println(">>수정 완료하였습니다.");}
                else {System.out.println(">>수정 실패. 다시 입력해주세요.");}
            }
            else if (ch == 'O' || ch == 'o'){ // 회원 로그아웃 함수
                PtMemberController.getInstance().logOut();
                System.out.println(">>로그아웃 성공. 메인 화면으로 돌아갑니다.");
                return;
            } else if (ch == 'X' || ch == 'x') {
                System.out.println(">>회원탈퇴 메뉴입니다.");
                System.out.print(">>회원탈퇴할 계정의 아이디를 입력해 주세요 : ");
                String removeId = scan.next();
                System.out.print(">>회원탈퇴할 계정의 비밀번호를 입력해 주세요 : ");
                String removePw = scan.next();
                MemberDto memberDto = new MemberDto();
                memberDto.setID(removeId);memberDto.setPW(removePw);
                boolean result = MemberController.getInstance().removeMem(memberDto);
                if (result){
                    System.out.println();
                    System.out.println("회원탈퇴 성공입니다.");
                    System.out.println();
                    MemberView.getInstance().index();
                }
                else {
                    System.out.println();
                    System.out.println("회원탈퇴 실패입니다. 다시 입력해주세요");
                    System.out.println();
                }
            }
            else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }
        }
    }
    // 쪽지 상세 "디스플레이" 띄우기
    public void showMessage(MessageDto msgDto){
        System.out.print("쪽지 제목 : "); System.out.println(msgDto.getMsgTitle());
        System.out.print("받은 시간 : "); System.out.println(msgDto.getMsgDate());
        System.out.print("보낸 회원 : "); System.out.println(msgDto.getReceivedMName());
        String mainContent = msgDto.getMsgContent();
        if (mainContent.length() > 35){
            for(int i = 0; i <= mainContent.length()/35; i++){
                if (i == mainContent.length()/35) {
                    System.out.println(mainContent.substring(35 * i));
                    break;
                }
                System.out.println(mainContent.substring(35 * i, 35 * (i + 1)));
            }
        }
    }
    // 첫 화면에서 미답신 쪽지 선택시 - 답장 보내기 메뉴
    public void ptCheckMessage(int screenNum, MessageDto msgDto){ // 화면 쪽지 번호 ch, 해당 DTO객체 msgDTO
        // 2 \ 제목2 \ 4 \ 2024-07-03 \ 답장이 있습니다
        System.out.println("===============" + screenNum + "번 쪽지에 답장 보내기 ===============");
        showMessage(msgDto);
        System.out.println("--------------------------------------------------------");
        System.out.println("r.답글 작성, s.해당 회원 건강정보 조회, b.돌아가기");
        while (true) {
            try {
                scan.nextLine();
                char ch = scan.next().charAt(0);
                scan.nextLine();
                if (ch == 'R' || ch == 'r') {
                    System.out.println(">>답글 내용을 입력해주세요.");
                    String reply = scan.nextLine();
                    MessageDto messageDto = new MessageDto();
                    messageDto.setReplyContent(reply);
                    messageDto.setMessageCode(msgDto.getMessageCode());
                    PtMemberController.getInstance().ptWriteReply(messageDto);
                } else if (ch == 'S' || ch == 's') { // 쪽지 보낸 회원의 건강 정보 조회
                    msgCheckMemberStat(msgDto.getSentMCode());
                } else if (ch == 'B' || ch == 'b'){
                    break;
                }
                else {throw new RuntimeException();}
            } catch (Exception e) {
                System.out.println(">>잘못된 입력입니다. 다시 확인해 주세요.");
            }
        }
    }
    // 쪽지 내역 - 쪽지 선택시 상세표시 메뉴
    private void msgCheckMessage(int screenNum, MessageDto msgDto) {
        while (true) {
            try {
                // "디스플레이" 시작
                System.out.println("===================" + screenNum + "번 쪽지 상세보기 ===================");
                showMessage(msgDto);
                if (msgDto.getHasReply() == 1) {
                    System.out.println();
                    System.out.print("답장 보낸 시간 : ");
                    System.out.println(msgDto.getReplyDate());
                    System.out.println(msgDto.getReplyContent());
                }
                System.out.println("--------------------------------------------------------");
                // "디스플레이" 끝
                System.out.print(">>1.돌아가기 2.답장 수정하기 3. 답장 삭제하기 : ");
                int ch = scan.nextInt();
                switch (ch) {
                    case 1:
                        return;
                    case 2:
                        msgReplyEdit(msgDto.getMessageCode());
                        break;
                    case 3:
                        msgReplyDelete(msgDto.getMessageCode());
                        break;
                    default:
                        throw new RuntimeException();
                }
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 확인해 주세요.");
            }
        }
    }
    private void ptMsgHistory() {
        // 메뉴 진입시 페이지 초기화
        msgCurrentPage = 1;
        System.out.println();
        System.out.println(">>받은 쪽지 내역 메뉴입니다.");
        while (true) {
            // "디스플레이" 시작
            System.out.println("=================== 쪽지 메뉴 " + msgCurrentPage + " 페이지 ===================");
            System.out.println("번호         제목          받은 날짜   답장 날짜");
            System.out.println("--------------------------------------------------------");
            //DB에서 현재 페이지 번호에 해당되는 쪽지 목록 가져와 출력
            ArrayList<MessageDto> msgList = PtMemberController.getInstance().msgView(msgCurrentPage);
            //쪽지 출력 for문
            for (int i = 0; i < msgList.size(); i++) {
                String title = msgList.get(i).getMsgTitle();
                // 제목 : 15글자를 넘어가면 말줄임표, 미만이면 빈칸 추가
                int maxTitleLen = 15;
                title = title.length() > maxTitleLen ? title.substring(0, maxTitleLen - 3) + "..." : title;// 제목 : 10글자 이상이면 말줄임표
                String sentDate = msgList.get(i).getMsgDate().substring(0, 10); // 날짜까지만 dateTime 표시
                String replyDate;
                if (msgList.get(i).getReplyDate() == null) {
                    replyDate = "";
                } else {
                    replyDate = msgList.get(i).getReplyDate().substring(0, 10);
                }

                System.out.printf("%2d | %-15s | %10s | %10s\n", i + 1, title, sentDate, replyDate);
            }
            System.out.println("--------------------------------------------------------");
            // "디스플레이" 끝
            System.out.print("1 ~ 10 = 해당 쪽지 보기, p = 이전 페이지, n = 다음 페이지 b = 돌아가기 : ");
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scan.nextInt();
                scan.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scan.next().charAt(0);
                scan.nextLine();
            }
            if (ch >= 1 && ch <= 10) { // 쪽지 상세보기
                if (ch <= msgList.size()) { // 쪽지 화면 번호와 입력 값의 유효성 검사
                    msgCheckMessage(ch, msgList.get(ch - 1));
                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>쪽지번호를 다시 확인해주세요.");
                    System.out.println();
                }
            } else if (ch == 'P' || ch == 'p') { // 쪽지 내역 이전 10개 출력
                if (NormalMemberController.msgCurrentPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                } else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    NormalMemberController.msgCurrentPage--;
                }
            } else if (ch == 'N' || ch == 'n') { // 쪽지 내역 다음 10개
                // 불러올 쪽지 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (NormalMemberController.getInstance().msgView(NormalMemberController.msgCurrentPage + 1).isEmpty()) {
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                } else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    NormalMemberController.msgCurrentPage++;
                }
            } else if (ch == 'B' || ch == 'b') { // 메뉴 돌아가기
                System.out.println(">>이전 메뉴로 돌아갑니다.");
                break;
            } else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }

        }
    }

    private ArrayList<MemberDto> msgShowMemberList(int msgMemberListPage) {
        return PtMemberController.getInstance().msgShowMemberList(msgMemberListPage);
    }

    // 답장 수정하기
    private void msgReplyEdit(int messageCode) {
        scan.nextLine();
        System.out.println(">>새 답장 내용을 입력해 주세요.");
        String newReply = scan.nextLine();
        MessageDto msgDto = new MessageDto();
        msgDto.setMessageCode(messageCode);
        msgDto.setReplyContent(newReply);
        if (PtMemberController.getInstance().msgReplyEdit(msgDto)){
            System.out.println(">>수정이 완료되었습니다.");
            return;
        }
        System.out.println(">>수정 중 오류가 발생하였습니다. 다시 시도해 주세요.");
    }
    // 답장 삭제하기
    private void msgReplyDelete(int messageCode) {
        if (PtMemberController.getInstance().msgReplyDelete(messageCode)){
            System.out.println(">>답장 삭제 완료되었습니다.");
        }
        System.out.println(">>삭제 중 오류가 발생하였습니다. 다시 시도해 주세요.");
    }

    // 강사) 쪽지 메뉴 - 쪽지 보낸 회원 정보(키, 몸무게, 음식기록, 운동기록) 확인하기
    public void msgCheckMemberStat(int sentMCode){

        // 현재 memberCode를 보내 쪽지 기록이 있는 회원 코드와 이름을 불러오기
        // 1. 아무개 ... <- 번호를 고르면 키와 몸무게가 뜨고 1.음식기록 2.운동기록 3.뒤로가기
        // 1/2를 고르면 오늘 날짜 기준으로 기록을 가져온다. 1.전날 2.다음날 3.돌아가기
    }
}