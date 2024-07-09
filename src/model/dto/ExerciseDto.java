package model.dto;

public class ExerciseDto {
    // 1. 멤버변수
    private int exCode;         // 운동 코드
    private String exName;      // 운동 이름
    private int exKcal;         // 운동 칼로리
    private int exIntensity;    // 운동 강도

    // 2. 생성자
    public ExerciseDto() {}
    public ExerciseDto(int exCode, String exName, int exKcal, int exIntensity) {
        this.exCode = exCode;
        this.exName = exName;
        this.exKcal = exKcal;
        this.exIntensity = exIntensity;
    }
    // --- 운동 기능 처리용 생성자
    public ExerciseDto(String exName, int exKcal, int exIntensity) {
        this.exName = exName;
        this.exKcal = exKcal;
        this.exIntensity = exIntensity;
    }

    // 3. 메소드
        // 1. getter and setter
    public int getExCode() {
        return exCode;
    }

    public void setExCode(int exCode) {
        this.exCode = exCode;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public int getExKcal() {
        return exKcal;
    }

    public void setExKcal(int exKcal) {
        this.exKcal = exKcal;
    }

    public int getExIntensity() {
        return exIntensity;
    }

    public void setExIntensity(int exIntensity) {
        this.exIntensity = exIntensity;
    }

        // 2. toString()
    @Override
    public String toString() {
        return "ExerciseDto{" +
                "exCode=" + exCode +
                ", exName='" + exName + '\'' +
                ", exKcal=" + exKcal +
                ", exIntensity=" + exIntensity +
                '}';
    }
}
