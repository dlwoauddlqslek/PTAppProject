package model.dto;

public class MessageDto {
    // 1. 멤버변수
    private int messageCode;        // 쪽지코드
    private int sentMCode;          // 보낸회원코드
    private int receivedMCode;      // 받은회원코드
    private String msgTitle;        // 쪽지제목
    private String msgContent;      // 쪽지내용
    private int msgView;            // 쪽지조회수
    private String msgDate;         // 쪽지보낸날짜
    private String replyContent;    // 답장내용
    private String replyDate;       // 답장날짜

    public MessageDto() {
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "messageCode=" + messageCode +
                ", sentMCode=" + sentMCode +
                ", receivedMCode=" + receivedMCode +
                ", msgTitle='" + msgTitle + '\'' +
                ", msgContent='" + msgContent + '\'' +
                ", msgView=" + msgView +
                ", msgDate='" + msgDate + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", replyDate='" + replyDate + '\'' +
                '}';
    }

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

    public int getSentMCode() {
        return sentMCode;
    }

    public void setSentMCode(int sentMCode) {
        this.sentMCode = sentMCode;
    }

    public int getReceivedMCode() {
        return receivedMCode;
    }

    public void setReceivedMCode(int receivedMCode) {
        this.receivedMCode = receivedMCode;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public int getMsgView() {
        return msgView;
    }

    public void setMsgView(int msgView) {
        this.msgView = msgView;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }
}
