package model.dto;

public class FoodDto {
    private int foodCode;
    private String  foodName;
    private int foodkcal;

    public FoodDto(){}

    public FoodDto(int foodCode, String foodName, int foodkcal) {
        this.foodCode = foodCode;
        this.foodName = foodName;
        this.foodkcal = foodkcal;
    }

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

    public int getFoodkcal() {
        return foodkcal;
    }

    public void setFoodkcal(int foodkcal) {
        this.foodkcal = foodkcal;
    }
}
