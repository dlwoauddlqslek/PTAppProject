package model.dao;

import model.dto.AteFoodRecordDto;
import model.dto.FoodDto;
import model.dto.WeightRecordDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class AteFoodRecordDao {
    private static AteFoodRecordDao ateFoodRecordDao=new AteFoodRecordDao();

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    private AteFoodRecordDao(){
        try {// -db연동
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PtAppProject","root","1234"
            );
        }catch (Exception e){System.out.println(e);}
    }

    public static AteFoodRecordDao getInstance(){return ateFoodRecordDao;}

    // 입력한 음식이 음식DB에 있는지 확인
    public boolean foodCheck(String foodName){
        System.out.println("foodName = " + foodName);
        try{
            String sql="select * from food where foodName='"+foodName+"';";
            System.out.println("sql = " + sql);
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){


                return true;
            }
        }catch (Exception e){System.out.println(e);}
        return false;
    }


    // 입력한 음식 먹은음식기록에 저장
    public boolean foodRecord(String foodName,int loginMno){
        try{
            String sql="select * from food where foodName='"+foodName+"';";
            System.out.println("sql = " + sql);
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                int foodCode=rs.getInt("foodCode");

                String sql2="insert into atefoodrecord(foodCode,memberCode) values(?,?);";
                ps=conn.prepareStatement(sql2);
                ps.setInt(1,foodCode);
                ps.setInt(2,loginMno);
                int count=ps.executeUpdate();
                if(count==1){
                    return true;
                }
            }

        }catch (Exception e){System.out.println(e);}
        return false;
    }
    // 오늘 먹은 음식 기록 불러오기
    // 회원코드, 음식(이름으로) 먹은 시간 (하루 기준)
    public ArrayList<AteFoodRecordDto> getDailyFoodRecord(int loginMCode, String date, int recordNum) {
        ArrayList<AteFoodRecordDto> dailyFoodList = new ArrayList<>();
        try{
            String sql="select * from atefoodrecord inner join food on atefoodrecord.foodCode = food.foodCode where memberCode = ? and ateTime > ? and ateTime < (select DATE_ADD(?, interval 1 day)) order by atetime desc;";
            if (recordNum != 0) {sql = sql.replace(";", " limit 0," + recordNum + ";");}
            ps=conn.prepareStatement(sql);
            ps.setInt(1, loginMCode); ps.setString(2, date); ps.setString(3, date);
            rs=ps.executeQuery();
            while(rs.next()){
                AteFoodRecordDto foodRecordDto = new AteFoodRecordDto();
                foodRecordDto.setFoodName(rs.getString("foodName"));
                foodRecordDto.setFoodCode(rs.getInt("foodCode"));
                foodRecordDto.setAteTime(rs.getString("ateTime"));
                foodRecordDto.setFoodkcal(rs.getInt("foodKcal"));
                dailyFoodList.add(foodRecordDto);
            }

        } catch (Exception e){System.out.println(e);}
        return dailyFoodList;
    }


}















