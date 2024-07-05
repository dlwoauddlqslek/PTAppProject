package model.dao;

import model.dto.MessageDto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MessageDao {
    // 싱글톤 패턴
    private static MessageDao msgDao = new MessageDao();
    private MessageDao(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ptappproject", "root", "1234"
            );
        }catch (Exception e){
            System.out.println(">>MessageDAO.getInstance() 오류 : " +e);
        }
    };

    // JDBC 인터페이스들
    static Connection conn; //.prepareStatement(string)
    PreparedStatement ps; // .executeQuery() .executeUpdate()
    ResultSet rs;

    //.getInstance()
    public static MessageDao getInstance(){return msgDao;};

    // 쪽지 메뉴 & 쪽지 내역 출력
    public ArrayList<MessageDto> msgView(int msgCurrentPage, int loginMCode){
        ArrayList<MessageDto> msgList = new ArrayList<>();
        // currentpage 1 = 0, 9, 2 = 10, 19 -> x-1, 10x-1
        // limit 0, 10 -> 11, 20 ...
        try {
            String sql = "select * from message where sentMCode = ? or receivedMCode = ? LIMIT ?, ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, loginMCode); ps.setInt(2, loginMCode);
            ps.setInt(3, (msgCurrentPage-1)*10); ps.setInt(4, msgCurrentPage*10-1);
            rs = ps.executeQuery();
            while (rs.next()){
                // 쪽지 메뉴 번호 \ msgTitle \ msgView \ msgDate \ replyContent 여부 확인?
                // 1 \ 제목1 \ 0 \ 2024-07-03 \ 답장이 아직 없습니다
                MessageDto msgDto = new MessageDto();
                msgDto.setMessageCode(rs.getInt(1));
                msgDto.setSentMCode(rs.getInt(2));
                msgDto.setReceivedMCode(rs.getInt(3));
                msgDto.setMsgTitle(rs.getString(4));
                msgDto.setMsgContent(rs.getString(5));
                msgDto.setHasReply(rs.getInt(6));
                msgDto.setMsgDate(rs.getString(7)); 
                msgDto.setReplyContent(rs.getString(8));
                msgDto.setReplyDate(rs.getString(9));
                msgList.add(msgDto);
            }
            return msgList;
        } catch (Exception e){
            System.out.println(">>쪽지 출력 DAO 오류 : " +e);
        }
        return msgList;
    }

    public void msgPrint(int accCode, int loginMCode, int msgCurrentPage){
        String sql = "";
        if (accCode == 1){ //일반회원 보낸 쪽지들
            sql = "select * from message where ";
        }
        else if (accCode == 2){ // PT강사회원 받은 쪽지들

        }
        // 2 = 일반회원 : 1 쪽지 보내기, 2 답장 확인하기, 3 쪽지 내역 보기
        // 3 = 강사회원 : 1 받은 쪽지 확인하기, 2 답장 보내기, 3 쪽지 보낸 회원의 키, 몸무게, 음식기록, 운동기록 확인하기, 4 쪽지 내역 보기
    }
    // 쪽지 보내기
    public boolean msgSendMessage(MessageDto msgDto){
        try{
            String sql = "insert into message(sentMCode, receivedMCode, msgTitle, msgContent) values (?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, msgDto.getSentMCode()); ps.setInt(2, msgDto.getReceivedMCode());
            ps.setString(3, msgDto.getMsgTitle()); ps.setString(4, msgDto.getMsgContent());
            int result = ps.executeUpdate();
            return result==1;
        }catch (Exception e) {
            System.out.println(">>메시지 전송 실패 : " + e);
        }
        return false;
    }
    // 일반) 쪽지 메뉴 2 - 답장 확인하기
    public ArrayList<MessageDto> msgCheckReply(){

        return null;
    }
    // 일반 & 강사) 쪽지 메뉴 3 & 4 - 쪽지 송신 내역 보기
    public void msgCheckHistory(){
        // accCode와 memberCode를 받아 쪽지 데이터 가져오기
    }
    // 강사) 쪽지 메뉴 - 받은 쪽지 확인하기
    public void msgCheckMessage(){
        //
    }
    // 강사) 쪽지 메뉴 - 받은 쪽지 답장 보내기
    public void msgSendReply(){

    }

    // 강사) 쪽지 메뉴 - 쪽지 보낸 회원 정보(키, 몸무게, 음식기록, 운동기록) 확인하기
    public void msgCheckMemberStat(){
        // 현재 memberCode를 보내 쪽지 기록이 있는 회원 코드와 이름을 불러오기
        // 1. 아무개 ... <- 번호를 고르면 키와 몸무게가 뜨고 1.음식기록 2.운동기록 3.뒤로가기
        // 1/2를 고르면 오늘 날짜 기준으로 기록을 가져온다. 1.전날 2.다음날 3.돌아가기
    }
}
