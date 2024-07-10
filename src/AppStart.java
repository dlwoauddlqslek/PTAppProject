import view.AdminView;
//import view.NormalMemberView;


import view.MemberView;
import view.NormalMemberView;

public class AppStart {
    public static void main(String[] args) {
        MemberView.getInstance().index();
    }
}