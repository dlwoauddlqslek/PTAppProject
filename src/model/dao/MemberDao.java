package model.dao;

import model.dto.MemberDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDao {

    private static MemberDao Mdao = new MemberDao();
    //생성자 만들고 안에다가 DB연동
    public MemberDao(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PtAppProject", "root","1234"  );
        }catch (Exception e){
            System.out.println("연동실패"+e);
        }
    }
    public static MemberDao getInstance(){return Mdao;}

    Connection conn; PreparedStatement ps; ResultSet rs;

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
    public int login(MemberDto memberDto){
        try {
            String sql = "select * from member where ID = ? and PW = ? ; ";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getID());
            ps.setString(2,memberDto.getPW());
            rs=ps.executeQuery();
            if (rs.next()){return rs.getInt("memberCode");}
        }catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }

    public int loginCate(MemberDto memberDto){
        try {
            String sql = "select * from member where ID = ? and PW = ? ; ";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getID());
            ps.setString(2,memberDto.getPW());
            rs=ps.executeQuery();
            if (rs.next()){ int loginCate = rs.getInt("accCategory"); return loginCate;  }
        }catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }

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


}
