package model.dto;

public class WorkOutRecordDto {
    private int workOutCode;
    private int exCode;
    private String workOutTime;
    private int memberCode;
    private String exName;
    private int exKcal;
    private int workOutAmount;

    public WorkOutRecordDto(){}

    public WorkOutRecordDto(int workOutCode, int exCode, String workOutTime, int memberCode) {
        this.workOutCode = workOutCode;
        this.exCode = exCode;
        this.workOutTime = workOutTime;
        this.memberCode = memberCode;
    }

    public int getExKcal() {
        return exKcal;
    }

    public void setExKcal(int exKcal) {
        this.exKcal = exKcal;
    }

    public String getExName() {
        return exName;
    }

    public void setExName(String exName) {
        this.exName = exName;
    }

    public int getWorkOutCode() {
        return workOutCode;
    }

    public void setWorkOutCode(int workOutCode) {
        this.workOutCode = workOutCode;
    }

    public int getExCode() {
        return exCode;
    }

    public void setExCode(int exCode) {
        this.exCode = exCode;
    }

    public String getWorkOutTime() {
        return workOutTime;
    }

    public void setWorkOutTime(String workOutTime) {
        this.workOutTime = workOutTime;
    }

    public int getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }

    public int getWorkOutAmount() {
        return workOutAmount;
    }

    public void setWorkOutAmount(int workOutAmount) {
        this.workOutAmount = workOutAmount;
    }

    @Override
    public String toString() {
        return "WorkOutRecordDto{" +
                "workOutCode=" + workOutCode +
                ", exCode=" + exCode +
                ", workOutTime='" + workOutTime + '\'' +
                ", memberCode=" + memberCode +
                '}';
    }

}
