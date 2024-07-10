package view;

import java.util.ArrayList;
import java.util.Scanner;

import controller.NormalMemberController;
import controller.MemberController;
import model.dto.*;

import static controller.MemberController.loginAccCode;
import static controller.MemberController.loginMCode;
import static controller.NormalMemberController.*;


public class NormalMemberView {
    // 싱글톤 패턴
    private static NormalMemberView normalMemberView = new NormalMemberView();
    private NormalMemberView(){}
    public static NormalMemberView getInstance(){ return normalMemberView; }
    // /싱글톤 패턴

    // 스캐너 객체 생성
    Scanner scan = new Scanner(System.in);

    // 일반 회원 메뉴
    // 현재 남은 칼로리 보여주기 & (하루기준)최근 먹은 음식, 최근 한 운동기록?
    // 1. 몸무게 관리 : 몸무게 측정 내역 띄우기 & 추가/수정/삭제
    // 1. 식단 관리 : 먹은 음식 기록 메뉴 띄우기 & 추가/수정/삭제
    // 2. 운동 관리 : 운동 기록 메뉴 띄우기 & 추가/수정/삭제
    // 3. PT 강사에게 쪽지 : 최근 보낸 쪽지 목록 띄우기 & 보내기/수정/삭제
    // 4. 본인 정보 수정 : 비밀번호 확인 후 변경할 정보 선택 & 새로운 정보 입력 (키, 운동습관, 연락처)
    // + 5. 로그아웃 번호 : 현재 로그인된 회원 로그아웃하기
    int kcal=2400;
    public void index(){
        System.out.println();
        System.out.println(">>일반 회원 메뉴입니다.");
        while(true) {
            // "디스플레이" 시작
            // 현재 회원 MemberDto 가져오기
            MemberDto currentDto = NormalMemberController.getInstance().getCurrentDto();
            System.out.println("================== 안녕하세요, " + currentDto.getMemberName() +"님 ==================");
            // 몸무게 측정 기록 여부에 따라 안내 메시지 출력
            if (hasWeightRecord()){
                double dailyKcal = NormalMemberController.getInstance().calcDailyKcal();
                System.out.println("오늘의 남은 칼로리 : " + dailyKcal + " Kcal");
            } else {
                System.out.println("몸무게를 등록해서 오늘의 남은 칼로리를 알아보세요.");
            }
            System.out.println("    오늘 먹은 음식                  오늘 한 운동");
            System.out.println(" 번호         이름        번호           이름");
            System.out.println("--------------------------------------------------------");

            // 날짜 기준 최근 운동 5개, 먹은 음식 5개
            // 번호 1~5 음식 | 번호 1~5 운동
            ArrayList<AteFoodRecordDto> foodList = NormalMemberController.getInstance().getDailyFoodList(0, 5);
            ArrayList<WorkOutRecordDto> workOutList = NormalMemberController.getInstance().getDailyWorkoutList(0, 5);

            // printf 한줄로 양쪽에서 한 객체씩 뽑아서 출력
            for (int i = 0; i < 5; i++) {
                if (i < foodList.size() && i < workOutList.size()) {
                    System.out.printf("%2d | %-15s | %2d | %-15s\n", i+1, foodList.get(i).getFoodName(), i+1, workOutList.get(i).getExName());
                }
                else if (i < foodList.size() && i >= workOutList.size()){
                    System.out.printf("%2d | %-15s |    |\n", i+1, foodList.get(i).getFoodName());
                }
                else if (i >= foodList.size() && i < workOutList.size()){
                    System.out.printf("   |                    | %2d | %-15s", i+1, workOutList.get(i).getExName());
                }
                else {
                    System.out.println("   |                    |    |");
                }
            }
            System.out.println("--------------------------------------------------------");
            // "디스플레이" 끝
            try {
                System.out.println(">>원하시는 메뉴를 선택해 주세요.");
                System.out.print(">>1.몸무게기록 2.음식기록 3.운동기록 4.쪽지메뉴 5.개인정보수정 6.로그아웃 : ");
                int ch = scan.nextInt();
                if (ch == 1) {
                    weightRecord();
                } else if (ch == 2) {
                    foodCal();
                } else if (ch == 3) {
                    exCal();
                } else if (ch == 4) {
                    msgView();
                } else if (ch == 5) {
                    mUpdate();
                } else if (ch == 6) {
                    logOut();
                } else {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 시도해 주세요.");
                scan = new Scanner(System.in);
            }
        }
    }
    // 몸무게 기록이 있는지 확인
    private boolean hasWeightRecord() {
        return NormalMemberController.getInstance().hasWeightRecord();
    }
    // 몸무게 등록 메뉴
    private void weightRecord() {
        System.out.println(">>몸무게 기록 관리 메뉴입니다.");
    }
    // 음식 입력-> 먹은 음식 레코드 저장 ->권장 칼로리에서 로그인한 회원이 먹은 음식들 칼로리합을 차감
    public void foodCal(){
        scan.nextLine();
        System.out.print("먹은 음식을 입력해주세요: ");
        String foodName=scan.nextLine();
        boolean result1= NormalMemberController.getInstance().foodCheck(foodName);
        boolean result2= NormalMemberController.getInstance().foodRecord(foodName);
        int result3=NormalMemberController.getInstance().foodKcalTotal();
        int result4=NormalMemberController.getInstance().exKcalTotal();
        if(result1) {
            if (result2) {
                if (kcal-result3+result4>=0) {
                    System.out.println("현재 남은 칼로리: " + (kcal - result3+result4));
                }else{System.out.println("현재 초과 칼로리: "+(Math.abs(kcal-result3+result4)));}
            }
        }
        else{System.out.println("다시 입력해주세요");}
    }

    public void exCal2(){
        System.out.println("============================운동선택메뉴=========================");
        System.out.println("1.초급운동(Level)      2.중급운동(Level)      3.고급운동(Level)");
        int choNum = scan.nextInt();
        if (choNum == 1 ) {
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println("===강도===============운동내용===============칼로리===========");
            result.forEach(dto -> {
                System.out.println(dto.getExIntensity() + "     " + dto.getExName() + "    " + dto.getExKcal());
            });
            int ch = scan.nextInt();
            System.out.println(result);
            result.get(ch-1).getExKcal();
            System.out.println(result.get(ch-1).getExKcal());
        }
        else if (choNum == 2){
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println("===강도===============운동내용===============칼로리===========");
            result.forEach(dto -> {
                System.out.println(dto.getExIntensity() + "     " + dto.getExName() + "    " + dto.getExKcal());
            });
            int ch = scan.nextInt();
            System.out.println(result);
            result.get(ch-1).getExKcal();
            System.out.println(result.get(ch-1).getExKcal());
        }
        else if (choNum == 3){
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println("===강도===============운동내용===============칼로리===========");
            result.forEach(dto -> {
                System.out.println(dto.getExIntensity() + "     " + dto.getExName() + "    " + dto.getExKcal());
            });
            int ch = scan.nextInt();
            System.out.println(result);
            int selKcal = result.get(ch-1).getExCode();
            System.out.println(selKcal);
        }

    }// 운동 고르기 함수 종료


    public void exCal(){
        scan.nextLine();
        System.out.print("수행한 운동을 입력해주세요: ");
        String exName=scan.nextLine();
        boolean result1=NormalMemberController.getInstance().exCheck(exName);
        boolean result2=NormalMemberController.getInstance().exRecord(exName);
        int result3=NormalMemberController.getInstance().exKcalTotal();
        int result4=NormalMemberController.getInstance().foodKcalTotal();
        if(result1){
            if (result2){
                if(kcal+result3-result4>=0){
                    System.out.println("현재 남은 칼로리: "+(kcal+result3-result4));
                }else{System.out.println("현재 초과 칼로리: "+(Math.abs(kcal+result3-result4)));}
            }
        }else{System.out.println("다시 입력해주세요");}
    }

    //=======================================================회원수정 함수=======================================================
    public void mUpdate(){
        System.out.print(">>새 키 측정결과를 1cm 단위까지 입력해주세요 : "); int height = scan.nextInt();
        System.out.println(">>새 운동습관을 설정해주세요.");
        System.out.println(">>운동을 거의 하지 않는다 : 1, 일주일에 3~4번 정도 : 2, 매일 운동 또는 격한 운동 3~4일 : 3");int habit = scan.nextInt();
        System.out.print(">>변경할 연락처를 입력해주세요 : "); String mphone = scan.next();
        MemberDto memberDto = new MemberDto();
        memberDto.setHeight(height);
        memberDto.setExHabit(habit);
        memberDto.setContact(mphone);
        boolean result = MemberController.getInstance().mUpdate(memberDto);
        System.out.println(result);
        if (result){System.out.println(">>수정 완료하였습니다."); }
        else {System.out.println(">>수정 실패. 다시 입력해주세요.");}
    }

    //=======================================================회원 로그아웃 함수=======================================================
    public void logOut(){
        MemberController.getInstance().logOut();
        System.out.println(">>로그아웃 성공, 초기 화면으로 돌아갑니다.");
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
        // 메뉴 진입시 페이지 초기화
        msgCurrentPage = 1;
        System.out.println();
        System.out.println(">>쪽지 메뉴입니다.");
        while (true) {
            // "디스플레이" 시작
            System.out.println("=================== 쪽지 메뉴 "+ NormalMemberController.msgCurrentPage + " 페이지 ===================");
            System.out.println("번호         제목          보낸 날짜   답장 날짜");
            System.out.println("--------------------------------------------------------");
            //DB에서 현재 페이지 번호에 해당되는 쪽지 목록 가져와 출력
            ArrayList<MessageDto> msgList = NormalMemberController.getInstance().msgView(msgCurrentPage);
            //쪽지 출력 for문
            for (int i = 0; i < msgList.size(); i++){
                String title = msgList.get(i).getMsgTitle();
                // 제목 : maxTitleLen 글자수 넘어가면 말줄임표
                int maxTitleLen = 9;
                title = title.length() > maxTitleLen ? title.substring(0,maxTitleLen-3) + "..." : title;// 제목 : 10글자 이상이면 말줄임표
                String sentDate = msgList.get(i).getMsgDate().substring(0,10); // 날짜까지만 dateTime 표시
                String replyDate;
                if (msgList.get(i).getReplyDate() == null){
                    replyDate = "";
                } else { replyDate = msgList.get(i).getReplyDate().substring(0,10);}

                System.out.printf("%2d | %-15s | %10s | %10s\n", i+1, title, sentDate, replyDate);
            }
            System.out.println("--------------------------------------------------------");
            // "디스플레이" 끝
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
            else if (ch == 'B' || ch == 'b'){ // 메뉴 돌아가기 index()
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
        while (true) {
            try {
                // "디스플레이" 시작
                System.out.println("===================" + screenNum + "번 쪽지 상세보기 ===================");
                System.out.print("쪽지 제목 : ");
                System.out.println(msgDto.getMsgTitle());
                System.out.print("보낸 시간 : ");
                System.out.println(msgDto.getMsgDate());
                System.out.print("받은 PT 강사 회원 : ");
                System.out.println(msgDto.getReceivedMName());
                String mainContent = msgDto.getMsgContent();
                if (mainContent.length() > 35) {
                    for (int i = 0; i <= mainContent.length() / 35; i++) {
                        if (i == mainContent.length() / 35) {
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
                // "디스플레이" 끝
                System.out.print(">>1.돌아가기 2.수정하기 3.삭제하기 : ");
                int ch = scan.nextInt();
                switch (ch) {
                    case 1:
                        return;
                    case 2:
                        msgEdit(msgDto.getMessageCode());
                        break;
                    case 3:
                        msgDelete(msgDto.getMessageCode());
                        return;
                    default:
                        throw new RuntimeException();
                }
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 확인해 주세요.");
            }

        }
    }
    //쪽지 수정
    private void msgEdit(int messageCode) {
        scan.nextLine();
        System.out.println(">>새로운 제목을 입력해 주세요 : ");
        String newTitle = scan.nextLine();
        System.out.println(">>새로운 내용을 입력해 주세요 : ");
        String newContent = scan.nextLine();

        MessageDto msgDto = new MessageDto();
        msgDto.setMessageCode(messageCode);
        msgDto.setMsgTitle(newTitle);
        msgDto.setMsgContent(newContent);

        if(NormalMemberController.getInstance().msgEdit(msgDto)){
            System.out.println(">>쪽지 수정이 완료되었습니다.");
            return;
        }
        System.out.println(">>쪽지 수정에 문제가 있었습니다. 다시 시도해 주세요.");
    }

    // 현재 쪽지 삭제
    private void msgDelete(int messageCode) {
        if (NormalMemberController.getInstance().msgDelete(messageCode)){
            System.out.println(">>쪽지가 삭제되었습니다. 이전 메뉴로 돌아갑니다.");
            return;
        }
        System.out.println(">>쪽지 삭제 과정에 문제가 있었습니다. 다시 시도해 주세요.");
    }

    // 일반) 쪽지 메뉴 1 - 쪽지 보내기
    public void msgSendMessage(){
        // 회원 선택창 초기화
        msgPtMemberListPage = 1;
        while(true) {
            // 일반회원 : msgPtMemberListPage, ptMemberList
            // 강사회원 : msgMemberListPage, memberList

            ArrayList<MemberDto> memList = msgShowPtMemberList(msgPtMemberListPage);
            // 받을 PT 강사회원/ 일반회원
            System.out.println("====================== 쪽지 보내기 ======================");
            System.out.println("                       페이지 " + msgPtMemberListPage);
            System.out.println("번호          이름        번호           이름");
            System.out.println("--------------------------------------------------------");

            // 번호 1~5 회원명 | 번호 6~10 회원명
            ArrayList<MemberDto> leftList = new ArrayList<>();
            ArrayList<MemberDto> rightList = new ArrayList<>();
            // 왼쪽 오른쪽 출력할 목록 구분
            for ( int i = 0; i < 10; i++){
                if (i < 5 && i < memList.size()) {
                    leftList.add(memList.get(i));
                }
                else if (i >= 5 && i < memList.size()){
                    rightList.add(memList.get(i));
                }
            }
            // printf 한줄로 양쪽에서 한 객체씩 뽑아서 출력
            for (int i = 0; i < 5; i++) {
                if (i < rightList.size()) {
                    System.out.printf("%2d | %-15s | %2d | %-15s\n", i+1, leftList.get(i).getMemberName(), i+6, rightList.get(i).getMemberName());
                }
                else if (i < leftList.size()){
                    System.out.printf("%2d | %-15s |    |\n", i+1, leftList.get(i).getMemberName());
                }
                else {
                    System.out.println("   |                    |    |");
                }
            }
            System.out.println("--------------------------------------------------------");
            System.out.println("쪽지를 받을 강사회원의 목록을 표시합니다.");
            System.out.println("p = 이전 페이지, n = 다음 페이지, b = 돌아가기");
            System.out.print("1 ~ 10 = 해당 회원에게 쪽지 보내기 : ");
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scan.nextInt();
                scan.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scan.next().charAt(0);
                scan.nextLine();
            }
            if (ch >= 1 && ch <= 10){ // 강사 번호를 선택
                if (ch <= memList.size()) { // 강사 화면 번호와 입력 값의 유효성 검사
                    // 쪽지 제목
                    System.out.print(">>쪽지 제목을 입력해 주세요 : "); String title = scan.nextLine();
                    // 쪽지 내용
                    System.out.print(">>쪽지 내용을 입력해 주세요 : "); String content = scan.nextLine();
                    // MessageDTO 포장
                    MessageDto msgDto = new MessageDto();
                    msgDto.setSentMCode(loginMCode);
                    msgDto.setReceivedMCode(memList.get(ch-1).getMemberCode());
                    msgDto.setMsgTitle(title);
                    msgDto.setMsgContent(content);

                    if (NormalMemberController.getInstance().msgSendMessage(msgDto)){
                        System.out.println(">>쪽지 전송 완료!");
                        return;
                    } else {
                        System.out.println(">>쪽지 전송에 실패하였습니다.");
                    }

                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>해당 회원의 번호를 다시 확인해주세요.");
                    System.out.println();
                }
            } else if (ch == 'P' || ch == 'p') { // 회원 내역 이전 10개 출력
                if (msgPtMemberListPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                } else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    msgPtMemberListPage--;
                }
            } else if (ch == 'N' || ch == 'n') { // 회원 내역 다음 10개
                // 불러올 쪽지 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (NormalMemberController.getInstance().msgShowPtMemberList(msgPtMemberListPage + 1).isEmpty()) {
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                } else { // 현재 페이지 +1 및 출력
                    msgPtMemberListPage++;
                }
            }
            else if (ch == 'B' || ch == 'b'){
                break;
            }
        }
    }
    // 쪽지 보내기 - PT 강사 회원 목록 불러오기
    private ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        return NormalMemberController.getInstance().msgShowPtMemberList(msgPtMemberListPage);
    }


}



