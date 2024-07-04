package view;

import controller.NormalMemberController;
import model.dto.AteFoodRecordDto;

import java.util.Scanner;

public class NormalMemberView {

    public static NormalMemberView normalMemberView=new NormalMemberView();

    Scanner scan=new Scanner(System.in);

    int kcal=2400;
    public void index(){
        while(true) {
            try {
                System.out.println("1.음식칼로리계산");
                int ch = scan.nextInt();
                if (ch == 1) {
                    foodCal();
                } else {
                    System.out.println("없는 기능입니다.");
                }


            } catch (Exception e) {
                System.out.println(e);
                System.out.println("잘못된 입력입니다.");
                scan = new Scanner(System.in);
            }
        }
        }
    public  void foodCal(){
        scan.nextLine();
        System.out.print("먹은 음식을 입력해주세요: ");
        String foodName=scan.nextLine();
        int result= NormalMemberController.getInstance().foodcal(foodName);
        if(result>=0){
            System.out.println("현재 남은 칼로리: " + (kcal - result) );
        }
        else{System.out.println("다시 입력해주세요");}

    }

}



