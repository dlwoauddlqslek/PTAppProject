import view.MemberView;
import view.NormalMemberView;

import java.time.LocalDate;

public class AppStart {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        System.out.println("today = " + today);
        NormalMemberView.getInstance().msgView();

    }
}