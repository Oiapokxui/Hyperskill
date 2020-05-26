package recognition;
import network.Guess;
import network.Treino;
import serial.Utils;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int choice = 0;
        boolean ok = false;
        System.out.println("1. Learn the network");
        System.out.println("2. Guess a number");
        System.out.println("3. Learn it and test it.");
        System.out.println("4. Just Test it");
        Scanner scan = new Scanner(System.in);

        while (!ok) {
            try {
                ok = true;
                choice = scan.nextInt();
                if (choice < 1 || choice > 4) {
                    System.out.println("Please enter a valid option");
                    ok = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Your choice must be only in whole numbers");
                scan.next();
            }
        }
        System.out.println("Your choice: " + choice);
        Utils serial = new Utils("s1.0");
        //Guess palpite = (Guess) serial.deserialize();
        //Guess palpite = new Guess(aiai);
        String in;
        switch (choice) {
            case 3 :
                System.out.println("Learning. . .");
                Treino nT = new Treino();
                System.out.print("Done, ");
                System.out.printf("in %d generations! ", nT.getTotalGens());
                System.out.println("Saved to file!");
                Guess lN = new Guess (nT.getPesos());
                serial.serialize(lN);
                for (int num = 0; num < 10; num++) {
                    System.out.println("Esperado: " + (num + 1)%10);
                    lN.test(nT.idealNeurons[num]);
                    System.out.println("This number is: " + lN.getNumGuessed());
                }
                ok = false;
                while(!ok) {
                    lN.start();
                    // Imprime o numero chutado.
                    System.out.println("This number is: " + lN.getNumGuessed());
                    System.out.println("Do you wish to stop? Y/N");
                    in = scan.nextLine();
                    if (in == "Y") ok = true;
                }
                break;
            case 1:
                System.out.println("Learning. . .");
                Treino novoTreino = new Treino();
                System.out.print("Done, ");
                System.out.printf("in %d generations! ", novoTreino.getTotalGens());
                Guess learnedNetwork = new Guess (novoTreino.getPesos());
                serial.serialize(learnedNetwork);
                System.out.println("Saved to file!");
                break ;
            case 2 :
                Guess palpite = (Guess) serial.deserialize();
                ok = false;
                while(!ok) {
                    palpite.start();
                    // Imprime o numero chutado.
                    System.out.println("This number is: " + palpite.getNumGuessed());
                    System.out.println("Do you wish to stop? Y/N");
                    in = scan.nextLine();
                    if (in == "Y") ok = true;
                }
                break;
            case 4 :
                Guess p = (Guess) serial.deserialize();
                Treino t = new Treino();
                for (int num = 0; num < 10; num++) {
                    System.out.println("Esperado: " + (num + 1)%10);
                    p.test(t.getInput(num));
                    System.out.println("This number is: " + p.getNumGuessed());
                }
                ok = false;
                while(!ok) {
                    p.start();
                    // Imprime o numero chutado.
                    System.out.println("This number is: " + p.getNumGuessed());
                    System.out.println("Do you wish to stop? Y/N");
                    in = scan.nextLine();
                    if (in == "Y") ok = true;
                }
                break;

        }
    }
}

