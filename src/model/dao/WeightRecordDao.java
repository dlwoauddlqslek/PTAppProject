package model.dao;

import model.dto.WeightRecordDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class WeightRecordDao {
    // 싱글톤 패턴
    private static WeightRecordDao weightRecordDao = new WeightRecordDao();

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    private WeightRecordDao(){
        try {// -db연동
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PtAppProject","root","1234"
            );
        }catch (Exception e){System.out.println(e);}
    }

    public static WeightRecordDao getInstance(){return weightRecordDao;}
    // 싱글톤 패턴 끝


    public WeightRecordDto checkWeight(int loginMCode) {
        WeightRecordDto weightDto = new WeightRecordDto();
        try{
            String sql="select weight from weightrecord where membercode = ? order by measureTime desc limit 0, 1;";
            ps=conn.prepareStatement(sql);
            ps.setInt(1, loginMCode);
            rs=ps.executeQuery();
            if (rs.next()) {
                weightDto.setWeight(rs.getInt("weight"));
            }
        } catch (Exception e){System.out.println(e);}
        return weightDto;
    }



    // 몸무게 기록 메뉴
    public ArrayList<WeightRecordDto> weightRecordPrint(int weightRecordCurrentPage, int loginMCode) {
        ArrayList<WeightRecordDto> weightRecordList = new ArrayList<>();
        // currentpage 1 = 0, 9, 2 = 10, 19 -> x-1, 10x-1
        // limit 0, 10 -> 10, 20 ...
        try {
            String sql = "select * from weightrecord where memberCode = ? ORDER BY measureTime desc LIMIT ?, 10;";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, loginMCode);
            ps.setInt(2, (weightRecordCurrentPage-1)*10);
            rs = ps.executeQuery();
            while (rs.next()){
                WeightRecordDto weightRecordDto = new WeightRecordDto();
                weightRecordDto.setWeightCode(rs.getInt(1));
                weightRecordDto.setWeight(rs.getInt(2));
                weightRecordDto.setMeasureTime(rs.getString(3));
                weightRecordDto.setMemberCode(rs.getInt(4));
                weightRecordList.add(weightRecordDto);
            }
            return weightRecordList;
        } catch (Exception e){
            System.out.println(">>몸무게 기록 출력 DAO 오류 : " +e);
        }
        return weightRecordList;

    }   // weightRecord 함수 end

    // 몸무게 기록하는 함수
    public boolean weightRecordAdd(int weight, int loginMCode) {
        try {
            String sql = "INSERT INTO weightrecord (weight, memberCode) VALUES (?, ?);";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, weight);
            ps.setInt(2, loginMCode);
            int count = ps.executeUpdate();
            if (count == 1) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }   // weightRecordAdd 함수 end

}
