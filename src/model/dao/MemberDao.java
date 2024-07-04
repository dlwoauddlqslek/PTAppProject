package model.dao;

import model.dto.MemberDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MemberDao {
    //싱글톤 패턴
    private static MemberDao memberDao = new MemberDao();
    private MemberDao(){};

    // JDBC 인터페이스들
    static Connection conn; //.prepareStatement(string)
    PreparedStatement ps; // .executeQuery() .executeUpdate()
    ResultSet rs;

    //.getInstance()
    public static MemberDao getInstance(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ptappproject", "root", "1234"
            );
        }catch (Exception e){
            System.out.println(">>MessageDAO.getInstance() 오류 : " +e);
        }
        return memberDao;
    };

    public ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        ArrayList<MemberDto> ptMemberList = new ArrayList<>();
        try {
            String sql = "select * from member where accCategory = 3 LIMIT ?, ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, (msgPtMemberListPage-1)*10); ps.setInt(2, msgPtMemberListPage*10-1);
            rs = ps.executeQuery();
            while (rs.next()) {
                MemberDto memberDto = new MemberDto();

                //private int memberCode;            //회원코드
                //    private String ID;                 //회원ID
                //    private String PW;                 //회원비밀번호
                //    private String memberName;         //회원명
                //    private int height;                //회원 키
                //    private int exHabit;               //회원 운동습관 (저조-> 우수 1,2,3)
                //    private char gender;               //성별 (M,F)
                //    private String birthDate;          //생년월일
                //    private String contact;            //연락처
                //    private int accCategory;
                memberDto.setMemberCode(); memberDto.setID(); memberDto.setPW(); memberDto.setMemberName();
                memberDto.setHeight(); memberDto.setExHabit(); memberDto.setGender(); memberDto.setBirthDate();
                memberDto.setContact(); memberDto.setAccCategory();
            }
        } catch (Exception e){
            System.out.println(">>MemberDao 오류 : " +e);
        }
        return ptMemberList;
    }
}
