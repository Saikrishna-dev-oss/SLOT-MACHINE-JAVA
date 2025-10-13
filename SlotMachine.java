package brocode;

import java.util.Random;
import java.util.Scanner;

public class SlotMachine {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

//        Declaration of variables
        int balance = 100;
        int bet,payout;
        String[] row;
        String playAgain;

        int roundsPlayed = 0;
        int biggestWin = 0;
        int totalWon = 0,totalLost = 0;


//        Welcome Message
        System.out.println("------------------------------");
        System.out.println("--  Welcome to Java Slot  --");
        System.out.println("    Symbols : 😵‍💫 😵 😱 🥶 🥰");
        System.out.println("------------------------------");

//        Checkin Conditions
        while(true) {
            System.out.println("**** Current Balance : $"+balance);

            //Checking Whether Balance is Zero Before Betting
            if(balance == 0) {
                System.out.println("Your Balance is Less Than 0");
                System.out.println("Exiting ...");
                finalSummary(roundsPlayed,balance,biggestWin,totalWon,totalLost);
                System.exit(0);
            }
            bet = handleBet(sc, balance);
            if (bet == -1)
                continue;
            balance -= bet;
            int originalBalance = balance + bet;

            roundsPlayed ++;
            System.out.println("Spinning...");
            row = spinRow();
            Thread.sleep(1000);
            printRow(row);
            payout = payOut(row,bet);
            if(payout > 0) {
                totalWon += payout;
                if(payout > biggestWin) {
                    biggestWin = payout;
                }
            } else {
                totalLost += bet;
            }

            if (bet == originalBalance && row[0].equals(row[1]) && row[1].equals(row[2])) {
                System.out.println("🎉🎉 JACKPOT! You went all-in and hit the triple!");
            }

            if (bet == originalBalance && payout > 0 ) {
                System.out.println("🎉 JACKPOT! You went all-in and won!");
                System.out.println("You Won : $"+payout);
                balance += payout;
            }
            else if(payout > 0) {
                System.out.println("You Won : $"+payout);
                balance += payout;
            }
            else {
                System.out.println("You Lost the Round");
            }
            System.out.println("----------------------------------");
            System.out.print("Do u Want to play Again (yes/no) :");
            playAgain = sc.nextLine().toUpperCase();

            if(!playAgain.equals("YES")) {
                finalSummary(roundsPlayed,balance,biggestWin,totalWon,totalLost);
                break;
            }

        }
        sc.close();
    }

    static String[] spinRow() {

        String[] symbols  = new String[]{"😵‍💫", "😵", "😱", "🥶", "🥰"};
        String[] row = new String[3];

        Random random = new Random();

//        Stores Values in Row Array
        for(int i = 0;i<3;i++) {
            row[i] = symbols[random.nextInt(symbols.length)];
        }

        return row;
    }
    static void printRow(String[] row) {
        System.out.println("*******************");
        System.out.println( "   " + String.join(" | ",row));
        System.out.println("*******************");
    }
    static int payOut(String[] row,int bet) {

        if(row[0].equals(row[1]) && row[1].equals(row[2])) {
            return switch (row[0]) {
                case "😵‍💫" -> bet * 3;
                case "😱" -> bet * 4;
                case "😵" -> bet * 7;
                case "🥶" -> bet * 10;
                case "🥰" -> bet * 20;
                default -> 0;
            };
        }

        else if(row[0].equals(row[1])) {
            return switch (row[0]) {
                case "😵‍💫" -> bet * 3;
                case "😱" -> bet * 4;
                case "😵" -> bet * 5;
                case "🥶" -> bet * 7;
                case "🥰" -> bet * 10;
                default -> 0;
            };
        }
        else if(row[1].equals(row[2])) {
            return switch (row[0]) {
                case "😵‍💫" -> bet * 4;
                case "😱" -> bet * 3;
                case "😵" -> bet * 5;
                case "🥶" -> bet * 7;
                case "🥰" -> bet * 9;
                default -> 0;
            };
        }
        return 0;
    }
    static int handleBet(Scanner sc, int balance) {
        int bet;
        System.out.print(" ---> Enter BET Amount : $");
        bet = sc.nextInt();
        sc.nextLine();  //Controlling newLine character

        if(bet > balance) {
            System.out.println("INSUFFICIENT FUNDS !!");
            return -1;
        }
        else if(bet <= 0) {
            System.out.println("Bet Must be Greater than 0");
            return -1;
        }
        if (bet == balance) {
            System.out.println("⚠️ You're betting your entire balance!");
            System.out.print("Are you sure? (yes/no) : ");
            String confirm = sc.nextLine().toUpperCase();
            if (!confirm.equalsIgnoreCase("YES")) {
                return -1;
            }
        }
        return bet;
    }
    static void finalSummary(int roundsPlayed, int balance,int biggestWin,int totalWon,int totalLost) {
        System.out.println("\n🎮 GAME SUMMARY :");
        System.out.println("* Rounds Played: " + roundsPlayed);
        System.out.println("* Final Balance: $" + balance);
        System.out.println("* Biggest Win: $" + biggestWin);
        System.out.println("* Total Won: $" + totalWon);
        System.out.println("* Total Lost: $" + totalLost);
        System.out.println("* Status: " + (balance > 100 ? "Lucky Streak!" : balance == 0 ? "Bankrupt!" : "Better luck next time"));
        System.out.println("------------------------------");
        System.out.println(" ---Thanks for playing 🎉--- ");
        System.out.println("------------------------------");
    }
}