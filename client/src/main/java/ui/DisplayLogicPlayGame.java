package ui;

import java.util.Scanner;

public class DisplayLogicPlayGame {
    static public void displayOptions(){
        System.out.println("""
                leave
                resign
                make move
                help
                """);
    }
    public String[] getInputPlayGame(){
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            return line.split(" ");
        }
    }
    public void displayPlayGame(){

    }
}
