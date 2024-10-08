package model.dao;

import model.dto.ExerciseDto;
import model.dto.FoodDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class FoodDao {
    // 싱글톤
    private static FoodDao foodDao = new FoodDao();

    // 해당 클래스의 생성자를 private 하여 다른 클래스에서 new 사용하지 못하게 차단하기
    private FoodDao() {
        try {   // DB 연동
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ptappproject", "root", "1234");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // 3. 해당 객체(싱글톤)를 외부로부터 접근할 수 있도록 만들어준 간접 호출 메소드
    public static FoodDao getInstance() {
        return foodDao;
    }

    // JDBC 인터페이스
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;


    // 음식 기능1. 음식 추가 함수
    public boolean foodAdd(FoodDto foodDto) {
        String foodName = foodDto.getFoodName();
        boolean result = foodCheck(foodName);     // 추가할 음식이 DB에 이미 등록되어 있는지 확인

        if (result) {   // 삭제할 음식이 있다면 if문 실행
            try {   // 0. 예외 처리
                String sql = "INSERT INTO food(foodName, foodKcal) VALUES (?, ?);";  // 1. SQL 작성
                ps = conn.prepareStatement(sql);    // 2. sql 기재
                ps.setString(1, foodDto.getFoodName());      // 3. 기재된 sql의 매개변수 값 대입
                ps.setInt(2, foodDto.getFoodKcal());

                int count = ps.executeUpdate();     // 4. sql 실행 후 결과받기
                if (count == 1) {
                    return true;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println(">>이미 등록되어 있는 음식입니다.");
        }
        return false;

    }   // foodAdd 함수 end


    // 음식 기능2. 음식 조회 함수
    public ArrayList<FoodDto> foodListView(int foodCurrentPage) {
        ArrayList<FoodDto> foodList = new ArrayList<>();

        // currentpage 1 = 0, 9, 2 = 10, 19 -> x-1, 10x
        // limit 0, 10 -> 10, 10
        try {   // 0. 예외 처리
            String sql = "select * from food limit ?, 10;";             // 1. SQL 작성
            ps = conn.prepareStatement(sql);                            // 2. sql 기재
            ps.setInt(1, (foodCurrentPage - 1) * 10);    // 3. 기재된 sql의 매개변수 값 대입
            rs = ps.executeQuery();                                     // 4. 기재된 sql 실행 하고 결과 반환

            while (rs.next()) {
                FoodDto foodDto = new FoodDto();
                foodDto.setFoodCode(rs.getInt("foodCode"));
                foodDto.setFoodName(rs.getString("foodName"));
                foodDto.setFoodKcal(rs.getInt("foodKcal"));
                foodList.add(foodDto);
            }
            return foodList;
        } catch (Exception e) {
            System.out.println(e);
        }
        return foodList;

    }   // foodListView 함수 end


    // 음식 기능3. 음식 수정 함수
    public boolean foodUpdate(String oldFoodName, FoodDto foodDto) {
        boolean result = foodCheck(oldFoodName);     // 수정할 음식이 DB에 존재하는지 확인

        if (!result) {      // 존재하면 false를 반환받기 때문에 result 앞에 !를 써줌
            try {   // 0. 예외 처리
                String sql ="update food set foodName = ?, foodKcal = ? where foodName = ?;";   // 1. SQL 작성
                ps = conn.prepareStatement(sql);                        // 2. sql 기재
                ps.setString(1, foodDto.getFoodName());    // 3. 기재된 sql의 매개변수 값 대입
                ps.setInt(2, foodDto.getFoodKcal());
                ps.setString(3, oldFoodName);
                int count = ps.executeUpdate();                         // 4. sql 실행 후 결과받기

                if (count == 1) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println(">>DB에 존재하지 않는 음식입니다.");
        }
        return false;

    }   // foodUpdate 함수 end


    // 음식 기능4. 음식 삭제 함수
    public boolean foodDelete(String foodName) {
        boolean result = foodCheck(foodName);     // 삭제할 음식이 DB에 존재하는지 확인

        if (!result) {      // 존재하면 false를 반환받기 때문에 result 앞에 !를 써줌
            try {   // 0. 예외 처리
                String sql = "delete from food where foodName = ?;";    // 1. SQL 작성
                ps = conn.prepareStatement(sql);             // 2. sql 기재
                ps.setString(1, foodName);      // 3. 기재된 sql의 매개변수 값 대입

                int count = ps.executeUpdate();              // 4. sql 실행 후 결과 받기
                if (count == 1) {
                    return true;
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            System.out.println(">>DB에 존재하지 않는 음식입니다.");
        }
        return false;

    }   // foodDelete 함수 end

    // 음식 기능5. 음식 DB 존재 여부 확인 함수
    public boolean foodCheck(String foodName) {
        try {       // 0. 예외 처리
            String sql = "SELECT * FROM food where foodName = ?;";  // 1. SQL 작성
            ps = conn.prepareStatement(sql);             // 2. sql 기재
            ps.setString(1, foodName);      // 3. 기재된 sql의 매개변수 값 대입

            rs = ps.executeQuery();         // 4. 기재된 sql 실행 하고 결과 반환

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

    }   // foodCheck 함수 end

}   // class end
