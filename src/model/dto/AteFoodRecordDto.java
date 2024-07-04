package model.dto;

public class AteFoodRecordDto {
    private int ateFoodCode;
    private int foodCode;
    private String ateTime;
    private int memberCode;

    private int foodkcal;

    public int getFoodkcal() {
        return foodkcal;
    }

    public void setFoodkcal(int foodkcal) {
        this.foodkcal = foodkcal;
    }

    public AteFoodRecordDto(){}

    public AteFoodRecordDto(int ateFoodCode, int foodCode, String ateTime, int memberCode) {
        this.ateFoodCode = ateFoodCode;
        this.foodCode = foodCode;
        this.ateTime = ateTime;
        this.memberCode = memberCode;
    }

    public int getAteFoodCode() {
        return ateFoodCode;
    }

    public void setAteFoodCode(int ateFoodCode) {
        this.ateFoodCode = ateFoodCode;
    }

    public int getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(int foodCode) {
        this.foodCode = foodCode;
    }

    public String getAteTime() {
        return ateTime;
    }

    public void setAteTime(String ateTime) {
        this.ateTime = ateTime;
    }

    public int getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }
}
