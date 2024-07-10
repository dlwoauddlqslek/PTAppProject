package model.dao;

import model.dto.WeightRecordDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
