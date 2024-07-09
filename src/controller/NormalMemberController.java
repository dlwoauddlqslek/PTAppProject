package controller;

import model.dao.MemberDao;
import model.dao.MessageDao;
import model.dao.WorkOutRecordDao;
import model.dto.AteFoodRecordDto;
import model.dto.MemberDto;
import model.dto.MessageDto;

import java.time.LocalDate;
import java.util.ArrayList;

import model.dao.AteFoodRecordDao;

import static controller.MemberController.loginMCode;
import static controller.MemberController.loginNo;

public class NormalMemberController {
    //싱글톤 패턴
    private static NormalMemberController normalMemberController = new NormalMemberController();
    private NormalMemberController(){}
    public static NormalMemberController getInstance(){ return normalMemberController; }
    // /싱글톤 패턴

    // 하루 칼로리 계산
    // 기초 대사량 : Mifflin-St Jeor Equation 공식으로
    // 기초 대사량(BMR) : 10*몸무게(kg) + 6.25*키(cm) - 5*나이 + (남성 : +5, 여성 : -161)
    // 운동습관 :
    // 1. 운동 안함 : *1.2
    // 2. 적당히 운동 : *1.4 (일주일에 3~4번)
    // 3. 운동 많이 : *1.7 (일주일에 7번 운동 ~ 격한 운동 3-4일)
    // 일일칼로리 : 기초대사량 * 운동습관, 현재 몸무게를 유지하려면 +- 0, 매일 마이너스로 끝나면 체중 감량
    // 예시) 25세 남성, 180cm/65kg, 운동습관 1 (안함) : 1986 Kcal/day
    // BMR = 650 + 1125 - 125 + 5 = 1655, 1655*1.2 = 1986
    // 칼로리 계산용 회원 정보
    public String gender = "";
    public int weight;
    public int height;
    public int age;
    public String birthDate;
    public int exHabit;
    // 로그인시 회원 정보 불러오기

    // 칼로리 계산
    public double calcDailyKcal(){
        double baseKCal = 0;
        if (gender.equals("M")){
            baseKCal = 10*weight + 6.25*height - 5*age + 5;
        } else if (gender.equals("F")){
            baseKCal = 10*weight + 6.25*height - 5*age - 161;
        }
        switch (exHabit){
            case 1 :
                baseKCal *= 1.2;
                break;
            case 2 :
                baseKCal *= 1.4;
                break;
            case 3 :
                baseKCal *= 1.7;
                break;
        }
        // 오늘 기준 먹은 음식량 (+)
        ArrayList<AteFoodRecordDto> dailyAteFoodList = getDailyFoodList(0);
        // 오늘 기준 운동량 (-)
        return baseKCal;
    }

    //오늘 + 날짜 매개변수로 ArrayList 반환
    private ArrayList<AteFoodRecordDto> getDailyFoodList(int dayModifier) {
        String today = LocalDate.now().plusDays(dayModifier).toString();
        return AteFoodRecordDao.getInstance().getDailyFoodRecord(loginMCode, today);
    }

    //
    public int foodKcalTotal(){
        //2. 로그인된회원번호 (샘플 )
        int loginMCode=2;
        int total=0;
        ArrayList<AteFoodRecordDto> ateFoodList= getDailyFoodList(0);
        // 3. 로그인한 회원이 먹은 음식코드의 칼로리 합계
        for (int i=0; i<ateFoodList.size(); i++){
            total+=ateFoodList.get(i).getFoodkcal();
        }
        return total;
    }

    public boolean foodCheck(String foodName){
        return AteFoodRecordDao.getInstance().foodCheck(foodName);
    }

    public boolean foodRecord(String foodName){
        int loginMno=2;
        return AteFoodRecordDao.getInstance().foodRecord(foodName,loginMno);
    }

    public int exKcalTotal(){
        //2. 로그인된회원번호 (샘플 )
        int loginMno=2;
        // 3. 로그인한 회원이 먹은 음식코드의 칼로리 합계
        return WorkOutRecordDao.getInstance().exKcalTotal(loginMno);
    }

    public boolean exCheck(String exName){
        return WorkOutRecordDao.getInstance().exCheck(exName);
    }

    public boolean exRecord(String exName){
        int loginMno=2;
        return WorkOutRecordDao.getInstance().exRecord(exName,loginMno);
    }


    // 쪽지 메뉴 현재 페이지 번호
    public static int msgCurrentPage = 1;
    // 쪽지 메뉴 PT 강사 목록 페이지 번호
    public static int msgPtMemberListPage = 1;
    // 쪽지 메뉴 답장 회원 목록 페이지 번호
    public static int msgMemberListPage = 1;

    // 쪽지 내역 출력
    public ArrayList<MessageDto> msgView(int msgCurrentPage){
        return MessageDao.getInstance().msgView(msgCurrentPage, loginMCode);
    }
    // 쪽지 보내기
    public boolean msgSendMessage(MessageDto msgDto){
        // 받을 PT 강사회원 쪽지 제목 쪽지 내용
        return MessageDao.getInstance().msgSendMessage(msgDto);
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

    public ArrayList<MessageDto> msgPrint(int currentPage) {
        return null;
    }
    // 쪽지 보내기 메뉴 - PT 강사 목록 불러오기
    public ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        return MemberDao.getInstance().msgShowPtMemberList(msgPtMemberListPage);
    }

    public ArrayList<MemberDto> msgShowMemberList(int msgMemberListPage) {
        return MemberDao.getInstance().msgShowMemberList(msgMemberListPage);
    }

    public MemberDto getCurrentDto() { // 회원 메뉴) 회원 메뉴에 띄울 정보를 가진 DTO 가져오기
        return MemberDao.getInstance().getCurrentDto(loginMCode);
    }
}
