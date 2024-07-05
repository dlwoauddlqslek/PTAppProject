package model.dto;

public class WorkOutRecordDto {
    private int workOutCode;
    private int exCode;
    private String workOutTime;
    private int memberCode;

    public WorkOutRecordDto(){}

    public WorkOutRecordDto(int workOutCode, int exCode, String workOutTime, int memberCode) {
        this.workOutCode = workOutCode;
        this.exCode = exCode;
        this.workOutTime = workOutTime;
        this.memberCode = memberCode;
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
