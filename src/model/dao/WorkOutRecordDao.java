package model.dao;

import model.dto.AteFoodRecordDto;
import model.dto.WorkOutRecordDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class WorkOutRecordDao {
    private static WorkOutRecordDao workOutRecordDao=new WorkOutRecordDao();

    Connection conn;
    PreparedStatement ps;
    ResultSet rs;

    private WorkOutRecordDao(){
        try {// -db연동
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/PtAppProject","root","1234"
            );
        }catch (Exception e){System.out.println(e);}
    }

    public static WorkOutRecordDao getInstance(){return workOutRecordDao;}

    // 입력한 운동이 운동DB에 있는지 확인
    public boolean exCheck(String exName){
        System.out.println("exName = " + exName);
        try{
            String sql="select * from exercise where exName='"+exName+"';";
            System.out.println("sql = " + sql);
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){


                return true;
            }
        }catch (Exception e){System.out.println(e);}
        return false;
    }

    // 로그인한 회원이 수행한 운동들 칼로리 합
    public int exKcalTotal( int loginMno ){

        int total = 0;
        try{
            String sql="select exCode from member inner join workoutrecord on member.memberCode = workoutrecord.memberCode where member.memberCode = ?;";
            ps=conn.prepareStatement(sql);
            ps.setInt( 1 , loginMno );
            rs=ps.executeQuery();
            while (rs.next()){
                int excode = rs.getInt( "excode" ); System.out.println( excode );

                String sql2 = "select exKcal from exercise where exCode = ? ";
                ps = conn.prepareStatement( sql2 );
                ps.setInt( 1 , excode );
                ResultSet rs2 =  ps.executeQuery();
                if( rs2.next() ){
                    total += rs2.getInt("exKcal");
                }
            }
        }catch (Exception e){System.out.println(e);}
        return total;
    }
    // 입력한 음식 먹은음식기록에 저장
    public boolean exRecord(String exName,int loginMno){
        try{
            String sql="select * from exercise where exName='"+exName+"';";
            System.out.println("sql = " + sql);
            ps=conn.prepareStatement(sql);
            rs=ps.executeQuery();
            if(rs.next()){
                int exCode=rs.getInt("exCode");
                String sql2="insert into workoutrecord(exCode,memberCode) values(?,?);";
                ps=conn.prepareStatement(sql2);
                ps.setInt(1,exCode);
                ps.setInt(2,loginMno);
                int count=ps.executeUpdate();
                if(count==1){
                    return true;
                }
            }
        }catch (Exception e){System.out.println(e);}
        return false;
    }

    public ArrayList<WorkOutRecordDto> getDailyWorkoutList(int loginMCode, String date){
        ArrayList<WorkOutRecordDto> workoutList = new ArrayList<>();
        try{
            String sql="select * from workoutrecord inner join exercise on workoutrecord.exCode = exercise.exCode where memberCode = ? and workOutTime > ? and workOutTime < (select DATE_ADD(?, interval 1 day));";
            ps=conn.prepareStatement(sql);
            ps.setInt(1, loginMCode); ps.setString(2, date); ps.setString(3, date);
            rs=ps.executeQuery();
            while(rs.next()){
                WorkOutRecordDto workoutDto = new WorkOutRecordDto();
                workoutDto.setExName(rs.getString("exName"));
                workoutDto.setExCode(rs.getInt("exCode"));
                workoutDto.setWorkOutTime(rs.getString("workOutTime"));
                workoutDto.setExKcal(rs.getInt("exKcal"));
                workoutList.add(workoutDto);
            }
        } catch (Exception e){System.out.println(e);}
        return workoutList;
    }
}
