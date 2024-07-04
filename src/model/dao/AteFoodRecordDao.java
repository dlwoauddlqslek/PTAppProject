package model.dao;

import model.dto.AteFoodRecordDto;
import model.dto.FoodDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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


    public AteFoodRecordDto foodCal(String foodName){
        System.out.println("foodName = " + foodName);
        try{
            String sql="select * from food where foodName='"+foodName+"';";
            System.out.println("sql = " + sql);
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                int foodKcal=rs.getInt("foodKcal");
                int foodCode=rs.getInt("foodCode");

                AteFoodRecordDto ateFoodRecordDto=new AteFoodRecordDto();
                ateFoodRecordDto.setFoodCode(foodCode);
                ateFoodRecordDto.setFoodkcal(foodKcal);

                return ateFoodRecordDto;
            }
        }catch (Exception e){System.out.println(e);}
        return null;
    }

    //
    public int aaaaa( int loginMno ){

        int max = 0;
         try{
            String sql="select foodCode from member inner join atefoodrecord on member.memberCode = atefoodrecord.memberCode where member.memberCode = ?;";
            ps=conn.prepareStatement(sql);
            ps.setInt( 1 , loginMno );
            rs=ps.executeQuery();
            while (rs.next()){
                int foodCode = rs.getInt( "foodCode" ); System.out.println( foodCode );

                String sql2 = "select foodKcal from food where foodCode = ? ";
                ps = conn.prepareStatement( sql2 );
                ps.setInt( 1 , foodCode );
                ResultSet rs2 =  ps.executeQuery();
                if( rs2.next() ){
                    max += rs2.getInt("foodKcal");
                }
            }
        }catch (Exception e){System.out.println(e);}
        return max;
    }

}















