package model.dao;

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

    // 입력한 음식 먹은음식기록에 저장
    public boolean exRecord(int selExCode,int loginMno,int time){
        try{

                String sql2="insert into workoutrecord(exCode,memberCode,workOutAmount) values(?,?,?);";
                ps=conn.prepareStatement(sql2);
                ps.setInt(1,selExCode);
                ps.setInt(2,loginMno);
                ps.setInt(3,time);
                int count=ps.executeUpdate();
                if(count==1){
                    return true;
                }

        }catch (Exception e){System.out.println(e);}
        return false;
    }

    public ArrayList<WorkOutRecordDto> getDailyWorkoutList(int loginMCode, String date, int recordNum){
        ArrayList<WorkOutRecordDto> workoutList = new ArrayList<>();
        try{
            String sql="select * from workoutrecord inner join exercise on workoutrecord.exCode = exercise.exCode where memberCode = ? and workOutTime > ? and workOutTime < (select DATE_ADD(?, interval 1 day)) order by workOutTime desc;";
            if (recordNum != 0) {sql = sql.replace(";", " limit 0," + recordNum + ";");}
            ps=conn.prepareStatement(sql);
            ps.setInt(1, loginMCode); ps.setString(2, date); ps.setString(3, date);
            rs=ps.executeQuery();
            while(rs.next()){
                WorkOutRecordDto workoutDto = new WorkOutRecordDto();
                workoutDto.setExName(rs.getString("exName"));
                workoutDto.setExCode(rs.getInt("exCode"));
                workoutDto.setWorkOutTime(rs.getString("workOutTime"));
                workoutDto.setExKcal(rs.getInt("exKcal"));
                workoutDto.setWorkOutCode(rs.getInt("workoutcode"));
                workoutDto.setWorkOutAmount(rs.getInt("workoutamount"));
                workoutList.add(workoutDto);
            }
        } catch (Exception e){System.out.println(e);}
        return workoutList;
    }

    public boolean workOutRecordUpdate(int workOutCode, int exCode){
        try{



                String sql2="update workoutrecord set excode=? where workoutcode=?;";
                System.out.println("sql2 = " + sql2);
                ps=conn.prepareStatement(sql2);
                ps.setInt(1,exCode);
                ps.setInt(2,workOutCode);
                int count=ps.executeUpdate();
                if(count==1){
                    return true;
                }
            } catch (Exception e){System.out.println(e);}
        return false;
    }

    public boolean workOutRecordDelete(int workOutCode){
        try{
            String sql2="delete from workoutrecord where workoutcode=?;";
            System.out.println("sql2 = " + sql2);
            ps=conn.prepareStatement(sql2);
            ps.setInt(1,workOutCode);
            int count=ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){System.out.println(e);}
        return false;
    }
}
