package model.dao;

import model.dto.MemberDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static controller.MemberController.loginMCode;

public class MemberDao {

    static Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    private static MemberDao mDao = new MemberDao();
    //생성자 만들고 안에다가 DB연동
    private MemberDao(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PtAppProject", "root","1234"  );
        }catch (Exception e){
            System.out.println("연동실패"+e);
        }
    }
    public static MemberDao getInstance(){return mDao;}



    // 회원가입 함수
    public boolean signup (MemberDto memberDto){
        try {
            String sql = "INSERT INTO member ( ID, PW, memberName, height, exHabit, gender, birthDate, contact, accCategory) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getID());
            ps.setString(2,memberDto.getPW());
            ps.setString(3,memberDto.getMemberName());
            ps.setInt(4,memberDto.getHeight());
            ps.setInt(5,memberDto.getExHabit());
            ps.setString(6,memberDto.getGender());
            ps.setString(7,memberDto.getBirthDate());
            ps.setString(8,memberDto.getContact());
            ps.setInt(9,memberDto.getAccCategory());
            int count = ps.executeUpdate();
            if (count == 1 ){return true;}
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    //로그인 함수
    public MemberDto login(MemberDto memberDto){

        MemberDto memberDto1 = new MemberDto();
        try {
            String sql = "select * from member where ID = ? and PW = ? ; ";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getID());
            ps.setString(2,memberDto.getPW());
            rs=ps.executeQuery();

            if (rs.next()){
            memberDto1.setMemberCode(rs.getInt("memberCode"));
            memberDto1.setHeight(rs.getInt("height"));
            memberDto1.setGender(rs.getString("gender"));
            memberDto1.setExHabit(rs.getInt("exHabit"));
            memberDto1.setBirthDate(rs.getString("birthDate"));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return memberDto1;
    }

//    public int loginCate(MemberDto memberDto){
//        try {
//            String sql = "select * from member where ID = ? and PW = ? ; ";
//            ps = conn.prepareStatement(sql);
//            ps.setString(1,memberDto.getID());
//            ps.setString(2,memberDto.getPW());
//            rs=ps.executeQuery();
//            if (rs.next()){ int loginCate = rs.getInt("accCategory"); return loginCate;  }
//        }catch (Exception e){
//            System.out.println(e);
//        }
//        return 0;
//    }

    //아이디 찾기 함수
    public String findId(MemberDto memberDto){
        try {
            String sql = "select * from member where memberName = ? and contact = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getMemberName());
            ps.setString(2,memberDto.getContact());
            rs = ps.executeQuery();
            if (rs.next()){ String findId = rs.getString("ID"); return findId;  }
        }catch (Exception e){
            System.out.println(e);
        } return null;
    }

    //비밀번호 찾기 함수
    public String findPw(MemberDto memberDto){
        try {
            String sql = "select * from member where ID = ? and memberName = ? and birthDate = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getID());
            ps.setString(2,memberDto.getMemberName());
            ps.setString(3,memberDto.getBirthDate());
            rs = ps.executeQuery();
            if (rs.next()){ String findPw = rs.getString("PW"); return findPw;  }
        }catch (Exception e){
            System.out.println(e);
        } return null;
    }

    // 강사 회원 목록 조회 함수
    public ArrayList<MemberDto> msgShowPtMemberList(int msgPtMemberListPage) {
        ArrayList<MemberDto> ptMemberList = new ArrayList<>();
        try {
            String sql = "select * from member where accCategory = 3 LIMIT ?, ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, (msgPtMemberListPage-1)*10); ps.setInt(2, msgPtMemberListPage*10-1);
            rs = ps.executeQuery();
            while (rs.next()) {
                MemberDto memberDto = new MemberDto();
                // 회원코드, 회원명
                memberDto.setMemberCode(rs.getInt(1)); memberDto.setMemberName(rs.getString(4));
                ptMemberList.add(memberDto);
            }
        } catch (Exception e){
            System.out.println(">>MemberDao 오류 : " +e);
        }
        return ptMemberList;
    }

    public ArrayList<MemberDto> msgShowMemberList(int msgMemberListPage) {
        ArrayList<MemberDto> memberList = new ArrayList<>();
        try {
            String sql = "select * from member where accCategory = 2 and receivedMCode = ? LIMIT ?, ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, loginMCode);
            ps.setInt(2, (msgMemberListPage-1)*10); ps.setInt(3, msgMemberListPage*10-1);
            rs = ps.executeQuery();
            while (rs.next()) {
                MemberDto memberDto = new MemberDto();
                // 회원코드, 회원명
                memberDto.setMemberCode(rs.getInt(1)); memberDto.setMemberName(rs.getString(4));
                memberList.add(memberDto);
            }
        } catch (Exception e){
            System.out.println(">>MemberDao 오류 : " +e);
        }
        return memberList;
    }

    public boolean mUpdate(MemberDto memberDto){
        try {
            System.out.println(memberDto+"dao");
            String sql = "update member set height = ? , exHabit = ?, contact=? where memberCode = ?;\n";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,memberDto.getHeight());
            ps.setInt(2,memberDto.getExHabit());
            ps.setString(3,memberDto.getContact());
            ps.setInt(4,memberDto.getMemberCode());
            int count = ps.executeUpdate();
            if (count == 1 ){return true;}
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }



}
