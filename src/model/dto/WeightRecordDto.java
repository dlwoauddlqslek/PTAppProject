package model.dto;

public class WeightRecordDto {
    int weightCode;
    int weight;
    String measureTime;
    int memberCode;

    public int getWeightCode() {
        return weightCode;
    }

    public void setWeightCode(int weightCode) {
        this.weightCode = weightCode;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public void setMeasureTime(String measureTime) {
        this.measureTime = measureTime;
    }

    public int getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }
}
