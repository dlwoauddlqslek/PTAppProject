package controller;

import model.dao.*;
import model.dto.*;

import java.time.LocalDate;
import java.util.ArrayList;

import static controller.MemberController.loginMCode;

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
    public static String currentGender = "";
    public static int currentHeight;
    public static int currentAge;
    public static String currentBirthDate;
    public static int currentExHabit;
    public static double baseKcal;
    // 로그인시 회원 정보 불러오기

    // 쪽지 메뉴 현재 페이지 번호
    public static int msgCurrentPage = 1;
    // 쪽지 메뉴 PT 강사 목록 페이지 번호
    public static int msgPtMemberListPage = 1;


    //===========================================운동선택 함수
    public ArrayList<ExerciseDto> exView(int choNum ){
        return ExerciseDao.getInstance().exView(choNum);
    }


    // 현재 칼로리 계산, 몸무게 기록에서 최신 기록 1개를 가져와서 계산
    public double calcDailyKcal(){
        int recentWeight = WeightRecordDao.getInstance().checkWeight(loginMCode).getWeight();
        baseKcal = 0;
        if (currentGender.equals("M")){
            baseKcal = 10*recentWeight + 6.25* currentHeight - 5* currentAge + 5;
        } else if (currentGender.equals("F")){
            baseKcal = 10*recentWeight + 6.25* currentHeight - 5* currentAge - 161;
        }
        switch (currentExHabit){
            case 1 :
                baseKcal *= 1.2;
                break;
            case 2 :
                baseKcal *= 1.4;
                break;
            case 3 :
                baseKcal *= 1.7;
                break;
        }
        // 오늘 기준 먹은 음식량 (+)
        int foodKcal = 0;
        ArrayList<AteFoodRecordDto> dailyAteFoodList = getDailyFoodList(0, 0);
        for (AteFoodRecordDto dto : dailyAteFoodList){
            foodKcal += dto.getFoodkcal();
        }
        // 오늘 기준 운동량 (-)
        int workOutKcal = 0;
        ArrayList<WorkOutRecordDto> dailyWorkOutList = getDailyWorkoutList(0, 0);
        for (WorkOutRecordDto dto : dailyWorkOutList){
            workOutKcal += (dto.getExKcal()*dto.getWorkOutAmount());
        }
        return (baseKcal + foodKcal - workOutKcal);
    }
    // 몸무게 기록 내역이 있는지 체크
    public boolean hasWeightRecord() {
        WeightRecordDto weightDto = WeightRecordDao.getInstance().checkWeight(loginMCode);
        return weightDto.getWeight() != 0;
    }

    //로그인 회원 코드 + 날짜 매개변수로 ArrayList 반환
    public ArrayList<AteFoodRecordDto> getDailyFoodList(int dayModifier, int recordNum) {
        String date = LocalDate.now().plusDays(dayModifier).toString();
        return AteFoodRecordDao.getInstance().getDailyFoodRecord(loginMCode, date, recordNum);
    }
    //로그인 회원 코드 + 날짜 매개변수로 ArrayList 반환
    public ArrayList<WorkOutRecordDto> getDailyWorkoutList(int dayModifier, int recordNum) {
        String date = LocalDate.now().plusDays(dayModifier).toString();
        return WorkOutRecordDao.getInstance().getDailyWorkoutList(loginMCode, date, recordNum);
    }

    // 음식 메뉴
    public boolean foodCheck(String foodName){
        return AteFoodRecordDao.getInstance().foodCheck(foodName);
    }
    // 음식 메뉴 추가
    public boolean foodRecord(String foodName){
        return AteFoodRecordDao.getInstance().foodRecord(foodName,loginMCode);
    }
    // 음식 메뉴 수정
    public boolean ateFoodUpdate(int ateFoodCode,String foodName){

        return AteFoodRecordDao.getInstance().ateFoodUpdate(ateFoodCode, foodName);
    }
    // 음식 메뉴 삭제
    public boolean ateFoodDelete(int ateFoodCode){
        return AteFoodRecordDao.getInstance().ateFoodDelete(ateFoodCode);
    }

    // 운동 메뉴 추가
    public boolean exRecord(int selExCode, int time){
        return WorkOutRecordDao.getInstance().exRecord(selExCode,loginMCode,time);
    }
    // 운동 메뉴 수정
    public boolean workOutRecordUpdate(int workOutCode, int exCode){
        return WorkOutRecordDao.getInstance().workOutRecordUpdate(workOutCode,exCode);
    }
    // 운동 메뉴 삭제
    public boolean workOutRecordDelete(int workOutCode){
        return WorkOutRecordDao.getInstance().workOutRecordDelete(workOutCode);
    }


    // 쪽지 내역 출력
    public ArrayList<MessageDto> msgView(int msgCurrentPage){
        return MessageDao.getInstance().msgView(msgCurrentPage, loginMCode);
    }
    // 쪽지 보내기
    public boolean msgSendMessage(MessageDto msgDto){
        // 받을 PT 강사회원 쪽지 제목 쪽지 내용
        return MessageDao.getInstance().msgSendMessage(msgDto);
    }

    // 쪽지 보내기 메뉴 - PT 강사 목록 불러오기
    public ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        return MemberDao.getInstance().msgShowPtMemberList(msgPtMemberListPage);
    }
    // 현재 회원정보 가져오기
    public MemberDto getCurrentDto() { // 회원 메뉴) 회원 메뉴에 띄울 정보를 가진 DTO 가져오기
        return MemberDao.getInstance().getCurrentDto(loginMCode);
    }

    // =============================== 몸무게 기록 부분 =============================== //

    // 몸무게 기록 메뉴 현재 페이지 번호
    public static int weightRecordCurrentPage = 1;

    // 몸무게 기록 출력
    public ArrayList<WeightRecordDto> weightRecordPrint(int weightRecordCurrentPage) {
        return WeightRecordDao.getInstance().weightRecordPrint(weightRecordCurrentPage, loginMCode);
    }   // weightRecord 함수 end

    // 몸무게 기록하는 함수
    public boolean weightRecordAdd(int weight) {
        return WeightRecordDao.getInstance().weightRecordAdd(weight, loginMCode);
    }   // weightRecordAdd 함수 end

    // 몸무게 기록 수정 함수
    public boolean weightRecordUpdate(WeightRecordDto weightRecordDto) {
        weightRecordDto.setMemberCode(loginMCode);
        return WeightRecordDao.getInstance().weightRecordUpdate(weightRecordDto);

    }   // weightRecordUpdate 함수 end

    // 몸무게 기록 삭제 함수
    public boolean weightRecordDelete(int weightCode) {
        return WeightRecordDao.getInstance().weightRecordDelete(weightCode, loginMCode);
    }   // weightRecordDelete 함수 end

    // ============================================================================ //


    //선택된 쪽지 삭제
    public boolean msgDelete(int messageCode) {
        return MessageDao.getInstance().msgDelete(messageCode);
    }
    //선택된 쪽지 수정
    public boolean msgEdit(MessageDto msgDto) {
        return MessageDao.getInstance().msgEdit(msgDto);
    }
}   // class end
