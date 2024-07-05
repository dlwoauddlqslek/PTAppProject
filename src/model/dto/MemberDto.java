package model.dto;

public class MemberDto {
    // 1. 멤버변수
    private int memberCode;            //회원코드
    private String ID;                 //회원ID
    private String PW;                 //회원비밀번호
    private String memberName;         //회원명
    private int height;                //회원 키
    private int exHabit;               //회원 운동습관 (저조-> 우수 1,2,3)
    private String gender;               //성별 (M,F)
    private String birthDate;          //생년월일
    private String contact;            //연락처
    private int accCategory;           //회원분류코드 (관리자 = 1, 일반회원 = 2, PT강사회원 = 3)

    // 2. 생성자
    public MemberDto() {}


    // 3. 메서드
    @Override
    public String toString() {
        return "MemberDto{" +
                "memberCode=" + memberCode +
                ", ID='" + ID + '\'' +
                ", PW='" + PW + '\'' +
                ", memberName='" + memberName + '\'' +
                ", height=" + height +
                ", exHabit=" + exHabit +
                ", gender=" + gender +
                ", birthDate='" + birthDate + '\'' +
                ", contact='" + contact + '\'' +
                ", accCategory=" + accCategory +
                '}';
    }

    public int getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(int memberCode) {
        this.memberCode = memberCode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getExHabit() {
        return exHabit;
    }

    public void setExHabit(int exHabit) {
        this.exHabit = exHabit;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getAccCategory() {
        return accCategory;
    }

    public void setAccCategory(int accCategory) {
        this.accCategory = accCategory;
    }
}
