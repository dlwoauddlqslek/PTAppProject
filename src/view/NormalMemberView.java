package view;

import java.util.ArrayList;
import java.util.Scanner;

import controller.NormalMemberController;
import controller.MemberController;
import model.dto.ExerciseDto;
import model.dto.MessageDto;
import model.dto.MemberDto;
import model.dto.WeightRecordDto;
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
        // 메뉴 처음 진입시 페이지 초기화
        msgCurrentPage = 1;

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
            // 번호 1~5 회원명 | 번호 6~10 회원명
            ArrayList<MemberDto> leftList = new ArrayList<>();
            ArrayList<MemberDto> rightList = new ArrayList<>();
//            // 왼쪽 오른쪽 출력할 목록 구분
//            for ( int i = 0; i < 10; i++){
//                if (i < 5 && i < memList.size()) {
//                    leftList.add(memList.get(i));
//                }
//                else if (i >= 5 && i < memList.size()){
//                    rightList.add(memList.get(i));
//                }
//            }
//            // printf 한줄로 양쪽에서 한 객체씩 뽑아서 출력
//            for (int i = 0; i < 5; i++) {
//                if (i < rightList.size()) {
//                    System.out.printf("%2d | %-15s | %2d | %-10s\n", i+1, leftList.get(i).getMemberName(), i+6, rightList.get(i).getMemberName());
//                }
//                else if (i < leftList.size()){
//                    System.out.printf("%2d | %-15s |    |\n", i+1, leftList.get(i).getMemberName());
//                }
//                else {
//                    System.out.println("   |                    |    |");
//                }
//            }
            // "디스플레이" 끝
            try {
                System.out.println(">>원하시는 메뉴를 선택해 주세요.");
                System.out.print(">>1.몸무게기록 2.음식기록 3.운동기록 4.쪽지메뉴 5.회원정보수정 6.로그아웃 : 7.회원탈퇴 ");
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
                } else if (ch == 7) {
                    removeMem();
                } else {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 시도해 주세요.");
                scan = new Scanner(System.in);
            }
        }   // while end
    }   // index() end


    // =============================== 몸무게 기록 부분 =============================== //

    // 몸무게 기록이 있는지 확인
    private boolean hasWeightRecord() {
        return NormalMemberController.getInstance().hasWeightRecord();
    }

//    private void weightRecord() { // 몸무게 등록 메뉴
//        System.out.println(">>몸무게 기록 관리 메뉴입니다.");
//    }
    // 몸무게 기록 메뉴
    private void weightRecord() {
        while (true) {
            System.out.println("================== 몸무게 기록 메뉴 "+ NormalMemberController.weightRecordCurrentPage + " 페이지 ==================");
            System.out.println("번호         몸무게                측정시간");
            System.out.println("-----------------------------------------------------------");
            // DB에서 현재 페이지 번호에 해당되는 몸무게 기록 목록 가져와 출력
            ArrayList<WeightRecordDto> weightRecordList = NormalMemberController.getInstance().weightRecordPrint(NormalMemberController.weightRecordCurrentPage);
            // 몸무게 기록 출력 for문
            for (int i = 0; i < weightRecordList.size(); i++){

                int weightCode = weightRecordList.get(i).getWeightCode();
                int weight = weightRecordList.get(i).getWeight();
                String measureTime = weightRecordList.get(i).getMeasureTime();       //.substring(0,10); // 날짜까지만 dateTime 표시

                System.out.printf(" %-12d %-12d %s \n", i + 1, weight, measureTime);
            }
            System.out.println("------------------------------------------------------------");
            System.out.println("1 ~ 10 = 해당 몸무게 기록 보기, p = 이전 페이지, n = 다음 페이지");
            System.out.print("r = 몸무게 기록하기, b = 돌아가기 : ");
            int ch;
            try { //알파벳 입력과 숫자 1~10 입력을 같이 받기
                ch = scan.nextInt();
                scan.nextLine();
            } catch (Exception e) { // 정수 입력이 아닐 때 char 입력으로 전환
                ch = scan.next().charAt(0);
                scan.nextLine();
            }
            if (ch >= 1 && ch <= 10){ // 몸무게 기록 상세 내용 보기
                if (ch <= weightRecordList.size()) { // 몸무게 기록 화면 번호와 입력 값의 유효성 검사
                    weightRecordContent(ch, weightRecordList.get(ch - 1));
                } else { // 표시되지 않은 번호를 입력
                    System.out.println(">>몸무게 기록 번호를 다시 확인해주세요.");
                    System.out.println();
                }
            }
            else if (ch == 'P' || ch == 'p'){ // 몸무게 기록 내역 이전 10개 출력
                if (NormalMemberController.weightRecordCurrentPage == 1) { // 첫 번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    NormalMemberController.weightRecordCurrentPage--;
                }
            }
            else if (ch == 'N' || ch == 'n'){ // 몸무게 기록 내역 다음 10개
                // 불러올 몸무게 기록 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (NormalMemberController.getInstance().weightRecordPrint(NormalMemberController.weightRecordCurrentPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    NormalMemberController.weightRecordCurrentPage++;
                }
            }
            else if (ch == 'R' || ch == 'r'){ // 몸무게 기록하기
                weightRecordAdd();   // 몸무게 기록하기 함수
            }
            else if (ch == 'B' || ch == 'b'){ // 메뉴 돌아가기
                System.out.println(">>이전 메뉴로 돌아갑니다.");
                NormalMemberController.weightRecordCurrentPage = 1;
                break;
            }
            else { // 메뉴 입력값이 이상하다
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }
        }

    }   // weightRecord() end

    // 1~10, 몸무게 기록 상세 내용 보기
    public void weightRecordContent(int screenNum, WeightRecordDto weightRecordDto){ // 화면 몸무게 기록 번호 ch, 해당 DTO객체 msgDTO
        while (true) {
            System.out.println("=================== " + screenNum + "번 몸무게 기록 상세보기 ===================");
            System.out.print("몸무게  ㅤ: ");
            System.out.println(weightRecordDto.getWeight());
            System.out.print("측정 시간 : ");
            System.out.println(weightRecordDto.getMeasureTime());
            System.out.println("------------------------------------------------------------");
            System.out.print(">>u = 몸무게 기록 수정, d = 몸무게 기록 삭제, b = 돌아가기 : ");
            char ch = scan.next().charAt(0);    // 메뉴 입력 받기
            if (ch == 'u') {            // 몸무게 기록 수정 함수 호출
                weightRecordUpdate(weightRecordDto.getWeightCode());
                return;
            } else if (ch == 'd') {     // 몸무게 기록 삭제 함수 호출
                weightRecordDelete(weightRecordDto.getWeightCode());
                return;
            } else if (ch == 'b') {     // 돌아가기
                return;
            } else {
                System.out.println(">>입력이 잘못되었습니다.");
                System.out.println();
                scan = new Scanner(System.in); // 새 scanner 객체 부여
            }
        }
    }   // weightRecordContent 함수 end

    // 몸무게 기록하는 함수
    private void weightRecordAdd() {
        while (true) {
            try {
                // 기록할 몸무게 입력
                System.out.print("몸무게를 입력해주세요 : ");
                int weight = scan.nextInt();
                boolean result = NormalMemberController.getInstance().weightRecordAdd(weight);

                if (result) {
                    System.out.println("몸무게 기록이 완료되었습니다.\n");
                } else {
                    System.out.println("몸무게를 기록하는 데에 실패하였습니다.\n");
                }
                return;
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 입력해 주세요. " + e);
                scan.nextLine();
            }
        }

    }   // weightRecordAdd 함수 end

    // 몸무게 기록 수정 함수
    private void weightRecordUpdate(int weightCode) {
        while (true) {
            try {
                // 수정할 몸무게 입력
                System.out.print("수정할 몸무게를 입력해주세요 : ");
                int weight = scan.nextInt();
//                System.out.print("수정할 측정시간을 입력해주세요. 예) 2000-00-00 00:00:00 : ");
//                scan.nextLine();
//                String measureTime = scan.nextLine();

                WeightRecordDto weightRecordDto = new WeightRecordDto();
                weightRecordDto.setWeightCode(weightCode);
                weightRecordDto.setWeight(weight);
//                weightRecordDto.setMeasureTime(measureTime);

                boolean result = NormalMemberController.getInstance().weightRecordUpdate(weightRecordDto);

                if (result) {
                    System.out.println("몸무게 기록 수정이 완료되었습니다.\n");
                    return;
                } else {
                    System.out.println("몸무게 기록을 수정하는 데에 실패하였습니다. 다시 입력해 주세요.\n");
                }
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 입력해 주세요. " + e);
                scan.nextLine();
            }
        }
    }   // weightRecordUpdate 함수 end

    // 몸무게 기록 삭제 함수
    private void weightRecordDelete(int weightCode) {
        boolean result = NormalMemberController.getInstance().weightRecordDelete(weightCode);

        if (result) {
            System.out.println("몸무게 기록 삭제가 완료되었습니다.\n");
        } else {
            System.out.println("몸무게를 기록을 삭제하는 데에 실패하였습니다.\n");
        }
    }   // weightRecordDelete 함수 end

    // ============================================================================ //


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
        System.out.println("========================운동선택메뉴======================");
        System.out.print("1.초급운동(Level)    2.중급운동(Level)    3.고급운동(Level)  :  ");
        int choNum = scan.nextInt();
        if (choNum == 1 ) {
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println();
            System.out.println("번호=======운동강도========운동내용==========칼로리========");
            for (int i = 0; i < result.size() ; i++) {
                System.out.println("번호 "+(i+1)+"       "+result.get(i).getExIntensity()+"             "+result.get(i).getExName()+"              "+result.get(i).getExKcal());
            }
            int ch = scan.nextInt();
            int selExCode = result.get(ch-1).getExCode();
            // System.out.println(selExCode); 운동코드 확인용
        }
        else if (choNum == 2){
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println();
            System.out.println("번호=======운동강도========운동내용==========칼로리========");
            for (int i = 0; i < result.size() ; i++) {
                System.out.println("번호 "+(i+1)+"       "+result.get(i).getExIntensity()+"             "+result.get(i).getExName()+"              "+result.get(i).getExKcal());
            }
            int ch = scan.nextInt();
            int selExCode = result.get(ch-1).getExCode();
            // System.out.println(selExCode); 운동코드 확인용
        }
        else if (choNum == 3){
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println();
            System.out.println("번호=======운동강도========운동내용============칼로리======");
            for (int i = 0; i < result.size() ; i++) {
                System.out.println("번호 "+(i+1)+"       "+result.get(i).getExIntensity()+"         "+result.get(i).getExName()+"         "+result.get(i).getExKcal());
            }
            int ch = scan.nextInt();
            int selExCode = result.get(ch-1).getExCode();
            // System.out.println(selExCode); 운동코드 확인용
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
        System.out.print("변경할 키 : "); int height = scan.nextInt();
        System.out.print("변경할 운동습관(1 ~ 3) : "); int habit = scan.nextInt();
        System.out.print("변경할 연락처 : "); String mphone = scan.next();
        MemberDto memberDto = new MemberDto();
        memberDto.setHeight(height);
        memberDto.setExHabit(habit);
        memberDto.setContact(mphone);
        boolean result = MemberController.getInstance().mUpdate(memberDto);
        System.out.println(result);
        if (result){System.out.println("수정성공"); }
        else {System.out.println("수정실패. 다시입력해주세요");}
    }

    //=======================================================회원 로그아웃 함수=======================================================
    public void logOut(){
        MemberController.getInstance().logOut();
        System.out.println("로그아웃 성공");
    }

    //=====================================================회원 탈퇴 함수===============================================================
    public void removeMem(){
        System.out.println(">>회원탈퇴 메뉴입니다.");
        System.out.print(">>회원탈퇴할 계정의 아이디를 입력해 주세요 : ");
        String removeId = scan.next();
        System.out.print(">>회원탈퇴할 계정의 비밀번호를 입력해 주세요 : ");
        String removePw = scan.next();
        MemberDto memberDto = new MemberDto();
        memberDto.setID(removeId);memberDto.setPW(removePw);
        boolean result = MemberController.getInstance().removeMem(memberDto);
        if (result){
            System.out.println("회원탈퇴 성공입니다."); MemberView.getInstance().index();
        }
        else {
            System.out.println();
            System.out.println("회원탈퇴 실패입니다. 다시 입력해주세요");
            System.out.println();
        }
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
        System.out.println();
        System.out.println(">>쪽지 메뉴입니다.");
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
            // 일반회원 : msgPtMemberListPage, ptMemberList
            // 강사회원 : msgMemberListPage, memberList
            int msgListPage = 1;
            ArrayList<MemberDto> memList = null;
            // 로그인된 회원 분류 코드에 따라 내용 변경
            if (loginAccCode == 2){ //일반회원
                msgListPage = msgPtMemberListPage;
                memList = msgShowPtMemberList(msgPtMemberListPage);
            } else if (loginAccCode == 3){ // PT강사회원
                msgListPage = msgMemberListPage;
                memList = msgShowMemberList(msgMemberListPage);
            }
            // 받을 PT 강사회원/ 일반회원
            System.out.println("====================== 쪽지 보내기 ======================");
            System.out.println("                       페이지 "+msgListPage);
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
                    System.out.printf("%2d | %-15s | %2d | %-10s\n", i+1, leftList.get(i).getMemberName(), i+6, rightList.get(i).getMemberName());
                }
                else if (i < leftList.size()){
                    System.out.printf("%2d | %-15s |    |\n", i+1, leftList.get(i).getMemberName());
                }
                else {
                    System.out.println("   |                    |    |");
                }
            }
            System.out.println("--------------------------------------------------------");
            if (loginAccCode == 2){ //일반회원
                System.out.println("쪽지를 받을 강사회원의 목록을 표시합니다.");
            } else if (loginAccCode == 3){ // PT강사회원
                System.out.println("답장을 보낼 수 있는 회원의 목록을 표시합니다.");
            }
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
            }
            else if (ch == 'P' || ch == 'p'){ // 회원 내역 이전 10개 출력
                if (msgListPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    if (loginAccCode == 2){ //일반회원
                        msgPtMemberListPage--;
                    } else if (loginAccCode == 3){ // PT강사회원
                        msgMemberListPage--;
                    }

                }
            }
            else if (ch == 'N' || ch == 'n'){ // 회원 내역 다음 10개
                // 불러올 쪽지 목록이 없다 : 다음 페이지가 비어있다 > 현재 페이지가 마지막 페이지라고 알린다
                if (NormalMemberController.getInstance().msgShowPtMemberList(msgListPage+1).isEmpty()){
                    System.out.println(">>마지막 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 +1 및 출력
                    System.out.println();
                    if (loginAccCode == 2){ //일반회원
                        msgPtMemberListPage++;
                    } else if (loginAccCode == 3){ // PT강사회원
                        msgMemberListPage++;
                    }
                }
            }
            else if (ch == 'B' || ch == 'b'){
                break;
            }
        }
    }

    private ArrayList<MemberDto> msgShowMemberList(int msgMemberListPage) {
        return NormalMemberController.getInstance().msgShowMemberList(msgMemberListPage);
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



