package model.dao;

import model.dto.MemberDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static controller.MemberController.loginMCode;

public class MemberDao {
    //싱글톤 패턴
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
    // 싱글톤 패턴 끝

    // 회원가입 함수
    public boolean signUp(MemberDto memberDto){
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
            String sql = "select * from member where ID = ? and PW = ? ;";
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
            memberDto1.setAccCategory(rs.getInt("accCategory"));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return memberDto1;
    }

    //아이디 찾기 함수
    public String findId(MemberDto memberDto){
        try {
            String sql = "select id from member where memberName = ? and contact = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getMemberName());
            ps.setString(2,memberDto.getContact());
            rs = ps.executeQuery();
            if (rs.next()){return rs.getString("ID");}
        }catch (Exception e){
            System.out.println(e);
        } return null;
    }

    //비밀번호 찾기 함수
    public String findPw(MemberDto memberDto){
        try {
            String sql = "select pw from member where ID = ? and memberName = ? and birthDate = ?;";
            ps = conn.prepareStatement(sql);
            ps.setString(1,memberDto.getID());
            ps.setString(2,memberDto.getMemberName());
            ps.setString(3,memberDto.getBirthDate());
            rs = ps.executeQuery();
            if (rs.next()){return rs.getString("PW");}
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
            ps.setInt(1, (msgPtMemberListPage-1)*10); ps.setInt(2, msgPtMemberListPage*10);
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
            ps.setInt(2, (msgMemberListPage-1)*10); ps.setInt(3, msgMemberListPage*10);
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




    // =============================== 관리자 회원 관리 부분 =============================== //

    // 회원 관리 기능1. 회원 목록 조회 함수
    public ArrayList<MemberDto> memberListView(int memberCurrentPage) {
        ArrayList<MemberDto> memberList = new ArrayList<>();

        // currentpage 1 = 0, 9, 2 = 10, 19 -> x-1, 10x
        // limit 0, 10 -> 10, 10
        try {   // 0. 예외 처리
            String sql = "select * from member limit ?, 10;";               // 1. SQL 작성
            ps = conn.prepareStatement(sql);                                // 2. sql 기재
            ps.setInt(1, (memberCurrentPage - 1) * 10);      // 3. 기재된 sql의 매개변수 값 대입
            rs = ps.executeQuery();                                         // 4. 기재된 sql 실행 하고 결과 반환

            while (rs.next()) {
                MemberDto memberDto = new MemberDto();
                memberDto.setMemberCode(rs.getInt("memberCode"));
                memberDto.setID(rs.getString("ID"));
                memberDto.setPW(rs.getString("PW"));
                memberDto.setMemberName(rs.getString("memberName"));
                memberDto.setHeight(rs.getInt("height"));
                memberDto.setExHabit(rs.getInt("exHabit"));
                memberDto.setGender(rs.getString("gender"));
                memberDto.setBirthDate(rs.getString("birthDate"));
                memberDto.setContact(rs.getString("contact"));
                memberDto.setAccCategory(rs.getInt("accCategory"));
                memberList.add(memberDto);
            }
            return memberList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return memberList;

    }

    // 회원 관리 기능2. 회원 탈퇴 함수
    public boolean memberWithdraw(String ID) {
        boolean result = memberCheck(ID);     // 삭제할 운동이 DB에 존재하는지 확인

        if (!result) {
            try {   // 0. 예외 처리
                String sql = "delete from member where ID = ?;";    // 1. SQL 작성
                ps = conn.prepareStatement(sql);                    // 2. sql 기재
                ps.setString(1, ID);                    // 3. 기재된 sql의 매개변수 값 대입
                int count = ps.executeUpdate();                      // 4. sql 실행 후 결과받기
                if (count == 1) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println(">>존재하지 않는 회원입니다.");
        }
        return false;

    }

    // 회원 관리 기능3. 회원 DB 존재 여부 확인 함수
    public boolean memberCheck(String ID) {
        try {   // 0. 예외 처리
            String sql = "Select * from member where ID = ?;";  // 1. SQL 작성
            ps = conn.prepareStatement(sql);                    // 2. sql 기재
            ps.setString(1, ID);                   // 3. 기재된 sql의 매개변수 값 대입
            rs = ps.executeQuery();                             // 4. 기재된 sql 실행 하고 결과 반환

            int count = 0;
            while (rs.next()) {
                count++;    // 이미 등록되어 있다면 count 1 증가
            }

            //System.out.println(count);
            if (count > 0) {    // count가 0보다 크다면 false 반환
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return true;

    }

    // ============================================================================ //


    public MemberDto getCurrentDto(int loginMCode) { // 회원 메뉴) 회원 메뉴에 띄울 정보를 가진 DTO 가져오기
        MemberDto memberDto = new MemberDto();
        try {
            String sql = "select * from member where memberCode = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, loginMCode);
            rs = ps.executeQuery();
            if (rs.next()){
                memberDto.setMemberName(rs.getString("memberName"));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return memberDto;
    }
}
