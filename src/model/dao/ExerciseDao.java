package model.dao;

import model.dto.ExerciseDto;
import model.dto.FoodDto;
import model.dto.MessageDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ExerciseDao {
    // 싱글톤
    private static ExerciseDao exerDao = new ExerciseDao();

    // 해당 클래스의 생성자를 private 하여 다른 클래스에서 new 사용하지 못하게 차단하기
    private ExerciseDao() {
        try {   // DB 연동
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ptappproject", "root", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // 3. 해당 객체(싱글톤)를 외부로부터 접근할 수 있도록 만들어준 간접 호출 메소드
    public static ExerciseDao getInstance() {
        return exerDao;
    }

    // JDBC 인터페이스
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;


    //==============================================운동 선택 함수
    public ArrayList<ExerciseDto> exView( int choNum ){
        ArrayList<ExerciseDto> exList = new ArrayList<>();
        // currentpage 1 = 0, 9, 2 = 10, 19 -> x-1, 10x-1
        // limit 0, 10 -> 11, 20 ...
        try {       // 0. 예외 처리
            String sql = "select * from exercise where exIntensity = ? ";       // 1. SQL 작성
            ps = conn.prepareStatement(sql);            // 2. sql 기재
            ps.setInt(1, choNum);          // 3. 기재된 sql의 매개변수 값 대입
            rs = ps.executeQuery();                     // 4. 기재된 sql 실행 하고 결과 반환
            while (rs.next()){
                ExerciseDto exDto = new ExerciseDto();
                exDto.setExCode(rs.getInt(1));
                exDto.setExName(rs.getString(2));
                exDto.setExKcal(rs.getInt(3));
                exDto.setExIntensity(rs.getInt(4));
                exList.add(exDto);
            }
            return exList;
        } catch (Exception e){
            System.out.println(">>쪽지 출력 DAO 오류 : " +e);
        }
        return exList;
    }



    // =============================== 관리자 운동 관리 부분 =============================== //

    // 운동 기능1. 운동 추가 함수
    public boolean exerAdd(ExerciseDto exerDto) {
        String exName = exerDto.getExName();
        boolean result = exerciseCheck(exName);     // 추가할 운동이 DB에 이미 등록되어 있는지 확인

        if (result) {
            try {   // 0. 예외 처리
                String sql = "INSERT INTO exercise (exName, exKcal, exIntensity) VALUES (?, ?, ?);";  // 1. SQL 작성
                ps = conn.prepareStatement(sql);                        // 2. sql 기재
                ps.setString(1, exerDto.getExName());      // 3. 기재된 sql의 매개변수 값 대입
                ps.setInt(2, exerDto.getExKcal());
                ps.setInt(3, exerDto.getExIntensity());

                int count = ps.executeUpdate();     // 4. sql 실행 후 결과받기
                if (count == 1) {
                    return true;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println(">> 이미 등록되어 있는 운동입니다.");
        }
        return false;

    }   // exerAdd 함수 end

    // 운동 기능2. 운동 목록 조회 함수
    public ArrayList<ExerciseDto> exerListView(int exerCurrentPage) {
        ArrayList<ExerciseDto> exerList = new ArrayList<>();

        // currentpage 1 = 0, 9, 2 = 10, 19 -> x-1, 10x
        // limit 0, 10 -> 10, 10
        try {       // 0. 예외 처리
            String sql = "select * from exercise limit ?, 10;";         // 1. SQL 작성
            ps = conn.prepareStatement(sql);                            // 2. sql 기재
            ps.setInt(1, (exerCurrentPage - 1) * 10);    // 3. 기재된 sql의 매개변수 값 대입
            rs = ps.executeQuery();                                     // 4. 기재된 sql 실행 하고 결과 반환

            while (rs.next()) {
                ExerciseDto exerDto = new ExerciseDto();
                exerDto.setExCode(rs.getInt("exCode"));
                exerDto.setExName(rs.getString("exName"));
                exerDto.setExKcal(rs.getInt("exKcal"));
                exerDto.setExIntensity(rs.getInt("exIntensity"));
                exerList.add(exerDto);
            }
            return exerList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return exerList;

    }   // exerListView 함수 end

    // 운동 기능3. 운동 수정 함수
    public boolean exerUpdate(String oldExName, ExerciseDto exerDto) {
        boolean result = exerciseCheck(oldExName);

        if (!result) {
            try {       // 0. 예외 처리
                String sql = "update exercise set exName = ?, exKcal = ?, exIntensity = ? where exName = ?;";   // 1. SQL 작성
                ps = conn.prepareStatement(sql);                        // 2. sql 기재
                ps.setString(1, exerDto.getExName());       // 3. 기재된 sql의 매개변수 값 대입
                ps.setInt(2, exerDto.getExKcal());
                ps.setInt(3, exerDto.getExIntensity());
                ps.setString(4, oldExName);

                int count = ps.executeUpdate();
                if (count == 1) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println(">> DB에 존재하지 않는 운동입니다.");
        }
        return false;
    }   // exerUpdate 함수 end

    // 운동 기능4. 운동 삭제 함수
    public boolean exerDelete(String exName) {
        boolean result = exerciseCheck(exName);     // 삭제할 운동이 DB에 존재하는지 확인

        if (!result) {
            try {       // 0. 예외 처리
                String sql = "delete from exercise where exName = ?;";  // 1. SQL 작성
                ps = conn.prepareStatement(sql);                        // 2. sql 기재
                ps.setString(1, exName);                    // 3. 기재된 sql의 매개변수 값 대입
                int count = ps.executeUpdate();
                if (count == 1) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println(">> DB에 존재하지 않는 운동입니다.");
        }
        return false;

    }   // exerDelete 함수 end

    // 운동 기능5. 운동 DB 존재 여부 확인 함수
    public boolean exerciseCheck(String exName) {
        try {   // 0. 예외 처리
            String sql = "Select * from exercise where exName = ?;";  // 1. SQL 작성
            ps = conn.prepareStatement(sql);            // 2. sql 기재
            ps.setString(1, exName);      // 3. 기재된 sql의 매개변수 값 대입

            rs = ps.executeQuery();                   // 4. 기재된 sql 실행 하고 결과 반환

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

    }   // exerciseCheck 함수 end

    // ============================================================================ //

}   // class end
