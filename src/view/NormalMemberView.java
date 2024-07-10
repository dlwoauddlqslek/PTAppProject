package view;

import java.util.ArrayList;
import java.util.Scanner;

import controller.NormalMemberController;
import controller.MemberController;
import model.dto.*;

import static controller.MemberController.loginMCode;
import static controller.NormalMemberController.msgCurrentPage;
import static controller.NormalMemberController.msgPtMemberListPage;


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
    public void index(){
        System.out.println();
        System.out.println(">>일반 회원 메뉴입니다.");
        while(true) {
            // "디스플레이" 시작
            // 현재 회원 MemberDto 가져오기
            MemberDto currentDto = NormalMemberController.getInstance().getCurrentDto();
            System.out.println("================ 안녕하세요, " + currentDto.getMemberName() +"님 ==================");
            // 몸무게 측정 기록 여부에 따라 안내 메시지 출력
            if (NormalMemberController.getInstance().hasWeightRecord()){
                double dailyKcal = NormalMemberController.getInstance().calcDailyKcal();
                System.out.println("오늘의 남은 칼로리 : " + dailyKcal + " Kcal");
            } else {
                System.out.println("몸무게를 등록해서 오늘의 남은 칼로리를 알아보세요.");
            }
            System.out.println("    오늘 먹은 음식              오늘 한 운동");
            System.out.println("--------------------------------------------------------");
            System.out.println(" 번호         이름        번호           이름");
            System.out.println("--------------------------------------------------------");

            // 날짜 기준 최근 운동 5개, 먹은 음식 5개
            // 번호 1~5 음식 | 번호 1~5 운동
            ArrayList<AteFoodRecordDto> foodList = NormalMemberController.getInstance().getDailyFoodList(0, 5);
            ArrayList<WorkOutRecordDto> workOutList = NormalMemberController.getInstance().getDailyWorkoutList(0, 5);

            // printf 한줄로 양쪽에서 한 객체씩 뽑아서 출력
            for (int i = 0; i < 5; i++) {
                if (i < foodList.size() && i < workOutList.size()) {
                    System.out.printf("%2d | %-16s | %2d | %-16s\n", i+1, foodList.get(i).getFoodName(), i+1, workOutList.get(i).getExName());
                }
                else if (i < foodList.size() && i >= workOutList.size()){
                    System.out.printf("%2d | %-16s |    |\n", i+1, foodList.get(i).getFoodName());
                }
                else if (i >= foodList.size() && i < workOutList.size()){
                    System.out.printf("   |                    | %2d | %-16s", i+1, workOutList.get(i).getExName());
                }
                else {
                    System.out.println("   |                    |    |");
                }
            }
            System.out.println("--------------------------------------------------------");
            // "디스플레이" 끝
            try {
                System.out.println(">>원하시는 메뉴를 선택해 주세요.");
                System.out.print(">>1.몸무게기록 2.음식기록 3.운동기록 4.쪽지메뉴 5.회원정보수정 6.로그아웃 7.회원탈퇴 : ");
                int ch = scan.nextInt();
                if (ch == 1) {
                    weightRecord();
                } else if (ch == 2) {
                    foodMenu();
                } else if (ch == 3) {
                    exMenu();
                } else if (ch == 4) {
                    msgView();
                } else if (ch == 5) {
                    mUpdate();
                } else if (ch == 6) {
                    logOut();
                    return;
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
            System.out.println("-----------------------------------------------------------");
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


    public void foodMenu() {
        while (true) {
            try {
                System.out.println(">>원하시는 메뉴를 선택해 주세요.");
                System.out.print(">>1.먹은음식 등록 2.수정 3.삭제 4.돌아가기 : ");
                int ch = scan.nextInt();
                if (ch == 1) {
                    foodCal();
                } else if (ch == 2) {
                    ateFoodUpdate();
                } else if (ch == 3) {
                    ateFoodDelete();
                } else if (ch == 4) {
                    return;
                } else {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 시도해 주세요.");
                scan = new Scanner(System.in);
            }
        }
    }
    // 음식 입력-> 먹은 음식 레코드 저장 ->권장 칼로리에서 로그인한 회원이 먹은 음식들 칼로리합을 차감
    public void foodCal(){
        scan.nextLine();
        System.out.print(">>먹은 음식을 입력해주세요: ");
        String foodName=scan.nextLine();
        if(NormalMemberController.getInstance().foodCheck(foodName)) {
            NormalMemberController.getInstance().foodRecord(foodName);
        }
        else{System.out.println(">>다시 입력해주세요");}
    }

    public ArrayList<AteFoodRecordDto> ateFoodPrint(){
        ArrayList<AteFoodRecordDto> ateFoodList=NormalMemberController.getInstance().getDailyFoodList(0,0);
        System.out.println("번호     음식     칼로리     시간");
        for (int i = 0; i < ateFoodList.size() ; i++) {
            System.out.println((i+1)+"       "+ateFoodList.get(i).getFoodName()+"             "+ateFoodList.get(i).getFoodkcal()+"              "+ateFoodList.get(i).getAteTime());
        }
        return ateFoodList;
    }

    public void ateFoodUpdate(){
        ArrayList<AteFoodRecordDto> ateFoodList=ateFoodPrint();
        System.out.print("수정할 음식번호를 입력해주세요");
        int ch = scan.nextInt();
        int ateFoodCode = ateFoodList.get(ch-1).getAteFoodCode();

        System.out.println(ateFoodCode);
        System.out.print("수정할 음식명을 입력해주세요");
        scan.nextLine();
        String foodName=scan.nextLine();
        boolean result1=NormalMemberController.getInstance().ateFoodUpdate(ateFoodCode,foodName);
        if (result1){
            System.out.println("수정 성공");
        }
        else {
            System.out.println("다시 입력해주세요");
        }
    }

    public void ateFoodDelete(){
        ArrayList<AteFoodRecordDto> ateFoodList=ateFoodPrint();
        System.out.print("삭제할 음식번호를 입력해주세요");
        int ch = scan.nextInt();
        int ateFoodCode = ateFoodList.get(ch-1).getAteFoodCode();
        boolean result1=NormalMemberController.getInstance().ateFoodDelete(ateFoodCode);
        if (result1){System.out.println("삭제 성공");}
        else {
            System.out.println("다시 입력해주세요");
        }
    }

    public void exMenu(){
        while (true) {
            try {
                System.out.println(">>원하시는 메뉴를 선택해 주세요.");
                System.out.print(">>1.수행한 운동 등록 2.수정 3.삭제 4.돌아가기 : ");
                int ch = scan.nextInt();
                if (ch == 1) {
                    exCal2();
                } else if (ch == 2) {
                    workOutRecordUpdate();
                } else if (ch == 3) {
                    workOutRecordDelete();
                } else if (ch == 4) {
                    return;
                } else {
                    throw new RuntimeException();
                }
            } catch (Exception e) {
                System.out.println(">>입력이 잘못되었습니다. 다시 시도해 주세요.");
                scan = new Scanner(System.in);
            }
        }
    }
    //운동 추가 메뉴
    public void exCal2(){
        System.out.println("======================== 운동선택메뉴 ======================");
        System.out.print("1.초급운동(Level)    2.중급운동(Level)    3.고급운동(Level)  :  ");
        int choNum = scan.nextInt();
        if (choNum == 1 ) {
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println("번호 ======= 운동강도 ======== 운동내용 ========== 칼로리 ========");
            for (int i = 0; i < result.size() ; i++) {
                System.out.println("번호 "+(i+1)+"       "+result.get(i).getExIntensity()+"             "+result.get(i).getExName()+"              "+result.get(i).getExKcal());
            }
            int ch = scan.nextInt();
            int selExCode = result.get(ch-1).getExCode();
            System.out.print(">>운동 시간을 10분 단위로 입력해 주세요. 예시) 30분 운동 = 3 :"); int time = scan.nextInt();
            if(NormalMemberController.getInstance().exRecord(selExCode, time)){
                System.out.println(">>운동 추가 성공.");
            }
            // System.out.println(selExCode); 운동코드 확인용
        }
        else if (choNum == 2){
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println("번호 ======= 운동강도 ======== 운동내용 ========== 칼로리 ========");
            for (int i = 0; i < result.size() ; i++) {
                System.out.println("번호 "+(i+1)+"       "+result.get(i).getExIntensity()+"             "+result.get(i).getExName()+"              "+result.get(i).getExKcal());
            }
            int ch = scan.nextInt();
            int selExCode = result.get(ch-1).getExCode();
            System.out.print(">>운동 시간을 10분 단위로 입력해 주세요. 예시) 30분 운동 = 3 :"); int time = scan.nextInt();
            if(NormalMemberController.getInstance().exRecord(selExCode, time)){
                System.out.println(">>운동 추가 성공.");
            }
            // System.out.println(selExCode); 운동코드 확인용
        }
        else if (choNum == 3){
            ArrayList<ExerciseDto> result = NormalMemberController.getInstance().exView(choNum);
            System.out.println("번호 ======= 운동강도 ======== 운동내용 ========== 칼로리 ========");
            for (int i = 0; i < result.size() ; i++) {
                System.out.println("번호 "+(i+1)+"       "+result.get(i).getExIntensity()+"         "+result.get(i).getExName()+"         "+result.get(i).getExKcal());
            }
            int ch = scan.nextInt();
            int selExCode = result.get(ch-1).getExCode();
            System.out.print(">>운동 시간을 10분 단위로 입력해 주세요. 예시) 30분 운동 = 3 :"); int time = scan.nextInt();
            if(NormalMemberController.getInstance().exRecord(selExCode, time)){
                System.out.println(">>운동 추가 성공.");
            }
             //System.out.println(selExCode);
        }
    }// 운동 고르기 함수 종료

    public ArrayList<WorkOutRecordDto> workOutPrint(){
        ArrayList<WorkOutRecordDto> workOutList=NormalMemberController.getInstance().getDailyWorkoutList(0,0);
        System.out.println("번호     운동     칼로리     운동량         시간");
        for (int i = 0; i < workOutList.size() ; i++) {
            System.out.println((i+1)+"       "+workOutList.get(i).getExName()+"             "+workOutList.get(i).getExKcal()+"              "+workOutList.get(i).getWorkOutAmount()+"0분              "+workOutList.get(i).getWorkOutTime());
        }
        return workOutList;
    }

    public void workOutRecordUpdate(){
        ArrayList<WorkOutRecordDto> workOutList=workOutPrint();
        System.out.print("수정할 운동번호를 입력해주세요");
        int ch = scan.nextInt();
        int workOutCode = workOutList.get(ch-1).getWorkOutCode();
        int exCode=workOutList.get(ch-1).getExCode();
        boolean result1=NormalMemberController.getInstance().workOutRecordUpdate(workOutCode,exCode);
        if (result1){
            System.out.println("수정 성공");
        }
        else {
            System.out.println("다시 입력해주세요");
        }
    }

    public void workOutRecordDelete(){
        ArrayList<WorkOutRecordDto> workOutList=workOutPrint();
        System.out.print("삭제할 운동번호를 입력해주세요");
        int ch = scan.nextInt();
        int workOutCode = workOutList.get(ch-1).getWorkOutCode();
        boolean result1=NormalMemberController.getInstance().workOutRecordDelete(workOutCode);
        if (result1){
            System.out.println("삭제 성공");
        }
        else {
            System.out.println("다시 입력해주세요");
        }
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


    // ====================== 쪽지 메뉴 ===================================== //
    public void msgView(){
        // 메뉴 진입시 페이지 초기화
        msgCurrentPage = 1;
        System.out.println();
        System.out.println(">>쪽지 메뉴입니다.");
        while (true) {
            // "디스플레이" 시작
            System.out.println("=================== 쪽지 메뉴 "+ msgCurrentPage + " 페이지 ===================");
            System.out.println("번호         제목          보낸 날짜   답장 날짜");
            System.out.println("--------------------------------------------------------");
            //DB에서 현재 페이지 번호에 해당되는 쪽지 목록 가져와 출력
            ArrayList<MessageDto> msgList = NormalMemberController.getInstance().msgView(msgCurrentPage);
            //쪽지 출력 for문
            for (int i = 0; i < msgList.size(); i++){
                String title = msgList.get(i).getMsgTitle();
                // 제목 : 15글자를 넘어가면 말줄임표, 미만이면 빈칸 추가
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
                if (msgCurrentPage == 1) { // 첫번째 페이지일 때
                    System.out.println(">>이미 첫 번째 페이지입니다!");
                    System.out.println();
                }
                else { // 현재 페이지 -1 하고 출력
                    System.out.println();
                    msgCurrentPage--;
                }
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
                System.out.println(msgDto);
                System.out.println("=================== " + screenNum + "번 쪽지 상세보기 ===================");
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
                } else {
                    System.out.println(mainContent);
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



