package view;


import controller.MemberController;
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
        //PT강사회원 메뉴
        //현재 답장을 보내지 않은 쪽지 표시
        // 1~10 쪽지 확인
        // h. 쪽지 내역 관리
        // s. 쪽지를 받은 회원 정보 조회
        // e. 본인 정보 수정
        // o. 로그아웃
        msgCurrentPage = 1; // 메뉴 진입시 현재 페이지 초기화
        while (true) {
            System.out.println("=========== 강사 메뉴 | 답장이 필요한 쪽지  " + msgCurrentPage + " 페이지 ===========");
            System.out.println("번호         제목          받은 날짜");
            System.out.println("--------------------------------------------------------");
            ArrayList<MessageDto> ptMsgList;
            // ArrayList MessageDAO에서 가져오기
            ptMsgList = PtMemberController.getInstance().checkPtMsgList(msgCurrentPage);
            //쪽지 출력 for문
            for (int i = 0; i < ptMsgList.size(); i++){
                String title = ptMsgList.get(i).getMsgTitle();
                int maxTitleLen = 15;
                title = title.length() > maxTitleLen ? title.substring(0,maxTitleLen-3) + "..." : title;// 제목 : 10글자 이상이면 말줄임표
                String sentDate = ptMsgList.get(i).getMsgDate().substring(0,10); // 날짜까지만 dateTime 표시
                System.out.printf("%2d | %-15s | %10s\n", i+1, title, sentDate);
            }
            System.out.println("--------------------------------------------------------");
            System.out.println("1 ~ 10 = 해당 쪽지 보기, p = 이전 페이지, n = 다음 페이지");
            System.out.print("s = 답장 보내기, o = 로그아웃 : ");
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
                if (PtMemberController.getInstance().checkPtMsgList(msgCurrentPage + 1).isEmpty()) {
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                } else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    msgCurrentPage++;
                }
            } else if (ch == 'S' || ch == 's') { // 답장 보내기
                ptWriteReply();
            }
            else if (ch == 'E' || ch == 'e'){ //회원 수정 함수
                System.out.println("변경할 키 : "); int height = scan.nextInt();
                System.out.println("변경할 운동습관(1 ~ 3) : "); int habit = scan.nextInt();
                System.out.println("변경할 연락처 : "); String mphone = scan.next();
                MemberDto memberDto = new MemberDto();
                memberDto.setHeight(height);
                memberDto.setExHabit(habit);
                memberDto.setContact(mphone);
                boolean result = PtMemberController.getInstance().mUpdate(memberDto);
                System.out.println(result);
                if (result){System.out.println("수정성공"); }
                else {System.out.println("수정실패. 다시입력해주세요");}
            }
            else if (ch == 'O' || ch == 'o'){ // 회원 로그아웃 함수
                PtMemberController.getInstance().logOut();
                System.out.println("로그아웃 성공");
            }
            else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }
        }
    }

    // index) 나한테 왔지만 답장을 아직 보내지 않은 쪽지 표시
    //public ArrayList<MessageDto>

    public void ptCheckMessage(int screenNum, MessageDto msgDto){ // 화면 쪽지 번호 ch, 해당 DTO객체 msgDTO
        // 2 \ 제목2 \ 4 \ 2024-07-03 \ 답장이 있습니다
        System.out.println("===================" + screenNum + "번 쪽지 상세보기 ===================");
        System.out.print("쪽지 제목 : "); System.out.println(msgDto.getMsgTitle());
        System.out.print("받은 시간 : "); System.out.println(msgDto.getMsgDate());
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
        System.out.println("--------------------------------------------------------");
        System.out.println("r.답글 작성 b.돌아가기");
        char ch = scan.next().charAt(0);
        scan.nextLine();
        if (ch == 'r' || ch == 'R'){
            //ptWriteReply(msgDto.getSentMCode(), msgDto.getReceivedMCode())
        }

    }

    private void ptWriteReply() {


    }
}