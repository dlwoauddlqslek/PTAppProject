package model.dto;

public class FoodDto {
    // 1. 멤버변수
    private int foodCode;
    private String foodName;
    private int foodKcal;

    // 2. 생성자
    public FoodDto() {}
    public FoodDto(int foodCode, String foodName, int foodKcal) {
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.foodKcal = foodKcal;
    }
    // --- 음식 기능 처리용 생성자
    public FoodDto(String foodName, int foodKcal) {
        this.foodName = foodName;
        this.foodKcal = foodKcal;
    }       // 회원가입용 생성자 end

    // 3. 메소드
        // 1. getter and setter
    public int getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(int foodCode) {
        this.foodCode = foodCode;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodKcal() {
        return foodKcal;
    }

    public void setFoodKcal(int foodKcal) {
        this.foodKcal = foodKcal;
    }

        // 2. toString()
    @Override
    public String toString() {
        return "FoodDto{" +
                "foodCode=" + foodCode +
                ", foodName='" + foodName + '\'' +
                ", foodKcal=" + foodKcal +
                '}';
    }

}
