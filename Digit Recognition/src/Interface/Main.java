package Interface;
import recognition.*;
import serial.Utils;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int choice = 0;
        boolean ok = false;
        System.out.println("1. Learn the network");
        System.out.println("2. Guess a number");
        Scanner scan = new Scanner(System.in);

        while (!ok) {
            try {
                ok = true;
                choice = scan.nextInt();
                if (choice < 1 || choice > 2) {
                    System.out.println("Please enter a valid option");
                    ok = false;
                }
            } catch (Exception e) {
                System.out.println("Your choice must be only in numbers");
            }
        }

        System.out.println("Your choice: " + choice);
        Utils serial = new Utils("s1.0");
        switch (choice) {
            case 1 :
                System.out.println("Learning. . .");
                Treino novoTreino = new Treino(300);
                System.out.print("Done! ");
                System.out.println(novoTreino.getPesos()[0][0]);
                Guess learnedNetwork = new Guess (novoTreino.getPesos());
                serial.serialize(learnedNetwork);
                System.out.println("Saved to file!");
                break ;

            case 2 :
                 Guess palpite = (Guess) serial.deserialize();
                 palpite.start();
                // Imprime o numero chutado.
                 System.out.println("This number is: " + palpite.getNumGuessed());
                 break;

        }
    }
}
