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

    // 쪽지 메뉴 : 쪽지 내역 출력
    public ArrayList<MessageDto> msgView(int msgCurrentPage, int loginMCode){
        ArrayList<MessageDto> msgList = new ArrayList<>();
        // currentpage 1 = 0, 9, 2 = 10, 19 -> x-1, 10x-1
        // limit 0, 10 -> 11, 20 ...
        try {
            String sql = "select * from message where sentMCode = ? or receivedMCode = ? order by msgDate desc LIMIT ?, 10;";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, loginMCode); ps.setInt(2, loginMCode);
            ps.setInt(3, (msgCurrentPage-1)*10);
            rs = ps.executeQuery();
            ResultSet rs1;
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
                int sentMCode = rs.getInt(2); int receivedMCode = rs.getInt(3);
                // 보낸 회원 이름
                sql = "select memberName from member where memberCode = ?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, sentMCode );
                rs1 = ps.executeQuery();
                rs1.next();
                msgDto.setSentMName(rs1.getString(1));
                // 받은 강사 회원 이름
                sql = "select memberName from member where memberCode = ?;";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, receivedMCode );
                rs1 = ps.executeQuery();
                rs1.next();
                msgDto.setReceivedMName(rs1.getString(1));
                msgList.add(msgDto);
            }
        } catch (Exception e){
            System.out.println(">>쪽지 출력 DAO 오류 : " +e);
        }
        return msgList;
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
    // 쪽지 삭제
    public boolean msgDelete(int messageCode) {
        try{
            String sql = "delete from message where messageCode = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageCode);
            int result = ps.executeUpdate();
            return result==1;
        }catch (Exception e) {
            System.out.println(">>쪽지 삭제 실패 : " + e);
        }
        return false;
    }
    // 쪽지 수정
    public boolean msgEdit(MessageDto msgDto) {
        try{
            String sql = "update message set msgTitle = ?, msgContent = ? where messageCode = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, msgDto.getMsgTitle()); ps.setString(2, msgDto.getMsgContent());
            ps.setInt(3, msgDto.getMessageCode());
            int result = ps.executeUpdate();
            return result==1;
        }catch (Exception e) {
            System.out.println(">>쪽지 수정 실패 : " + e);
        }
        return false;
    }
    // PT 회원 메뉴 - 쪽지 내역 조회 - 쪽지 답장 수정
    public boolean msgReplyEdit(MessageDto msgDto) {
        try{
            String sql = "update message set replyContent = ? where messageCode = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, msgDto.getReplyContent());
            ps.setInt(2, msgDto.getMessageCode());
            int result = ps.executeUpdate();
            return result==1;
        }catch (Exception e) {
            System.out.println(">>답장 수정 실패 : " + e);
        }
        return false;
    }
    // PT 회원 메뉴 - 쪽지 내역 조회 - 쪽지 답장 수정
    public boolean msgReplyDelete(int messageCode) {
        try{
            String sql = "update message set replyContent = NULL, replyDate = NULL where messageCode = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, messageCode);
            int result = ps.executeUpdate();
            return result==1;
        }catch (Exception e) {
            System.out.println(">>답장 삭제 실패 : " + e);
        }
        return false;
    }
    // 답장을 보내지 않은 쪽지들 확인하기
    public ArrayList<MessageDto> checkPtMsgNoReply(int msgCurrentPage, int loginMCode) {
        ArrayList<MessageDto> msgList = new ArrayList<>();
        try{
            String sql = "select * from message where receivedMCode = ? and hasReply = 0 order by msgDate desc limit ?, 10;";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, loginMCode); ps.setInt(2, msgCurrentPage);
            rs = ps.executeQuery();
            while (rs.next()){
                MessageDto msgDto = new MessageDto();
                msgDto.setSentMCode(rs.getInt(2));
                msgDto.setMessageCode(rs.getInt(1));
                msgDto.setMsgTitle(rs.getString(4));
                msgDto.setMsgContent(rs.getString(5));
                msgDto.setMsgDate(rs.getString(7));
                sql = "select memberName from member where memberCode = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, rs.getInt(2));
                ResultSet rs1 = ps.executeQuery();
                rs1.next();
                msgDto.setSentMName(rs1.getString(1));
                msgList.add(msgDto);
            }
        }catch (Exception e) {
            System.out.println(">>checkPtMsgNoReply : " + e);
        }
        return msgList;
    }

    public boolean ptWriteReply(MessageDto messageDto) {
        try{
            String sql = "insert into message(replyContent) values(?) where messageCode = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, messageDto.getReplyContent()); ps.setInt(2, messageDto.getMessageCode());
            int result = ps.executeUpdate();
            if (result == 1){
                // 답글여부 O, 답글날짜 now()
                sql = "update message set hasReply = 1, replyDate = now() where messageCode = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, messageDto.getMessageCode());
                int updateResult = ps.executeUpdate();
                return updateResult==1;
            }
        }catch (Exception e) {
            System.out.println(">>답장 삭제 실패 : " + e);
        }
        return false;
    }
}
