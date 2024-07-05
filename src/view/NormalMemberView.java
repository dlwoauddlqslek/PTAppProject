package view;

import java.util.ArrayList;
import java.util.Scanner;

import model.dto.MessageDto;
import model.dto.MemberDto;
import controller.NormalMemberController;

import static controller.NormalMemberController.loginMCode;
import static controller.NormalMemberController.msgPtMemberListPage;


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


    // 회원 먹은 음식 기록 메뉴
    public void recordFoodView(){
        // 화면 열릴 때 하루 기준으로 10건 표시
        // 위 -> 아래 : 최신부터 정렬
        // ==== 2024-07-05 음식 일지 ====
        // 번호 먹은 음식 칼로리 시간

        // CRUD 메뉴
        // p.이전 10건 n.다음 10건
        // 1.기록추가 2.기록수정 3.기록삭제 4.기록 더보기 5.돌아가기

        // 4. 기록 더보기 메뉴
        // 출력 : 처음은 이전메뉴와 같게
        // p.이전 n.다음 P.전날 N.다음날 d.날짜 입력 (0000-00-00)


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
            System.out.println("=================== 쪽지 메뉴 "+ NormalMemberController.msgCurrentPage + " 페이지 ===================");
            System.out.println("번호         제목          보낸 날짜   답장 날짜");
            System.out.println("--------------------------------------------------------");
            //DB에서 현재 페이지 번호에 해당되는 쪽지 목록 가져와 출력
            ArrayList<MessageDto> msgList = NormalMemberController.getInstance().msgView(NormalMemberController.msgCurrentPage);
            //쪽지 출력 for문
            for (int i = 0; i < msgList.size(); i++){
                String title = msgList.get(i).getMsgTitle();
                int maxTitleLen = 15;
                title = title.length() > maxTitleLen ? title.substring(0,maxTitleLen-3) + "..." : title;// 제목 : 10글자 이상이면 말줄임표
                String sentDate = msgList.get(i).getMsgDate().substring(0,10); // 날짜까지만 dateTime 표시
                String replyDate;
                if (msgList.get(i).getReplyDate() == null){
                    replyDate = "";
                } else { replyDate = msgList.get(i).getReplyDate().substring(0,10);}

                System.out.printf("%2d | %-15s | %10s | %10s\n", i+1, title, sentDate, replyDate);
            }
            System.out.println("--------------------------------------------------------");
            System.out.println("1 ~ 10 = 해당 쪽지 보기, p = 이전 페이지, n = 다음 페이지");
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
                msgSendMessage();// 쪽지 보내기 함수, 기본 페이지번호 1
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
    // 1~10, 쪽지 상세내용 보기
    public void msgCheckMessage(int screenNum, MessageDto msgDto){ // 화면 쪽지 번호 ch, 해당 DTO객체 msgDTO
        // 2 \ 제목2 \ 4 \ 2024-07-03 \ 답장이 있습니다
        System.out.println("===================" + screenNum + "번 쪽지 상세보기 ===================");
        System.out.print("쪽지 제목 : "); System.out.println(msgDto.getMsgTitle());
        System.out.print("보낸 시간 : "); System.out.println(msgDto.getMsgDate());
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
        if (msgDto.getHasReply() == 1) {
            System.out.println();
            System.out.print("답장 받은 시간 : ");
            System.out.println(msgDto.getReplyDate());
            System.out.println(msgDto.getReplyContent());
        }
        System.out.println("--------------------------------------------------------");
        System.out.print(">>엔터 키로 돌아가기 : "); String enter = scan.nextLine();
    }
    // 일반) 쪽지 메뉴 1 - 쪽지 보내기
    public void msgSendMessage(){
        while(true) {
            // 받을 PT 강사회원
            System.out.println("====================== 쪽지 보내기 ======================");
            System.out.println("                       페이지 "+msgPtMemberListPage);
            System.out.println("번호          이름        번호           이름");
            System.out.println("--------------------------------------------------------");
            ArrayList<MemberDto> ptMemberList = msgShowPtMemberList(msgPtMemberListPage);
            // 번호 1~5 강사명 | 번호 6~10 강사명
            ArrayList<MemberDto> leftList = new ArrayList<>();
            ArrayList<MemberDto> rightList = new ArrayList<>();
            // 왼쪽 오른쪽 출력할 목록 구분
            for ( int i = 0; i < 10; i++){
                if (i < 5 && i < ptMemberList.size()) {
                    leftList.add(ptMemberList.get(i));
                }
                else if (i >= 5 && i < ptMemberList.size()){
                    rightList.add(ptMemberList.get(i));
                }
            }
            // printf 한줄로 양쪽에서 한 객체씩 뽑아서 출력
            for (int i = 0; i < 5; i++) {
                if (i < rightList.size()) {
                    System.out.printf("%2d | %-15s | %2d | %-10s\n",
                            i+1, leftList.get(i).getMemberName(), i+6, rightList.get(i).getMemberName()
                    );
                }
                else if (i < leftList.size()){
                    System.out.printf("%2d | %-15s |    |\n", i+1, leftList.get(i).getMemberName());
                }
                else {
                    System.out.println("   |                    |    |");
                }
            }
            System.out.println("--------------------------------------------------------");
            System.out.println("p = 이전 페이지, n = 다음 페이지, b = 돌아가기");
            System.out.print("1 ~ 10 = 해당 강사에게 쪽지 보내기 : ");
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scan.nextInt();
                scan.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scan.next().charAt(0);
                scan.nextLine();
            }
            if (ch >= 1 && ch <= 10){ // 강사 번호를 선택
                if (ch <= ptMemberList.size()) { // 강사 화면 번호와 입력 값의 유효성 검사
                    // 쪽지 제목
                    System.out.print(">>쪽지 제목을 입력해 주세요 : "); String title = scan.nextLine();
                    // 쪽지 내용
                    System.out.print(">>쪽지 내용을 입력해 주세요 : "); String content = scan.nextLine();
                    // MessageDTO 포장
                    MessageDto msgDto = new MessageDto();
                    msgDto.setSentMCode(loginMCode);
                    msgDto.setReceivedMCode(ptMemberList.get(ch-1).getMemberCode());
                    msgDto.setMsgTitle(title);
                    msgDto.setMsgContent(content);

                    if (NormalMemberController.getInstance().msgSendMessage(msgDto)){
                        System.out.println(">>쪽지 전송 완료!");
                        return;
                    } else {
                        System.out.println(">>쪽지 전송에 실패하였습니다.");
                    }

                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>PT강사 번호를 다시 확인해주세요.");
                    System.out.println();
                }
            }
            else if (ch == 'P' || ch == 'p'){ // PT회원 내역 이전 10개 출력
                if (msgPtMemberListPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    msgPtMemberListPage--;
                }
            }
            else if (ch == 'N' || ch == 'n'){ // PT회원 내역 다음 10개
                // 불러올 쪽지 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (NormalMemberController.getInstance().msgShowPtMemberList(msgPtMemberListPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    msgPtMemberListPage++;
                }
            }
            else if (ch == 'B' || ch == 'b'){
                break;
            }
        }
    }
    // PT 강사 회원 목록 불러오기
    private ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        return NormalMemberController.getInstance().msgShowPtMemberList(msgPtMemberListPage);
    }

    // 일반 & 강사) 쪽지 메뉴 3 & 4 - 쪽지 송신 내역 보기
    public void msgCheckHistory(){
        // accCode와 memberCode를 받아 쪽지 데이터 가져오기
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

