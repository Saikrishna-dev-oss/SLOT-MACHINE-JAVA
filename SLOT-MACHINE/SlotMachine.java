

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SlotMachineCLI {

    // 1. Using an Enum to centralize symbols and their payout multipliers
    public enum Symbol {
        // Emoji | Two-of-a-kind Multiplier | Three-of-a-kind Multiplier

        DIZZY_FACE("😵‍💫", 3, 7),
        SCREAM("😱", 4, 8),
        CONFUSED("😵", 5, 10),
        COLD_FACE("🥶", 6, 15),
        HEART_EYES("🥰", 10, 25); // Highest paying symbol

        public final String emoji;
        public final int doubleMultiplier;
        public final int tripleMultiplier;

        Symbol(String emoji, int doubleMultiplier, int tripleMultiplier) {
            this.emoji = emoji;
            this.doubleMultiplier = doubleMultiplier;
            this.tripleMultiplier = tripleMultiplier;
        }

        @Override
        public String toString() {
            return emoji;
        }

        // Utility to get all emojis as a simple array for the spinner
        public static String[] getEmojis() {
            Symbol[] values = Symbol.values();
            String[] emojis = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                emojis[i] = values[i].emoji;
            }
            return emojis;
        }

        // Utility to map an emoji string back to an Enum object
        public static Symbol fromEmoji(String emoji) {
            for (Symbol symbol : Symbol.values()) {
                if (symbol.emoji.equals(emoji)) {
                    return symbol;
                }
            }
            // Should not happen if symbol list is correct
            throw new IllegalArgumentException("Unknown symbol: " + emoji);
        }
    }

    // --- Main Game Class (Refactored for better OOP) ---

    private int balance = 100;
    private int roundsPlayed = 0;
    private int biggestWin = 0;
    private int totalWon = 0;
    private int totalLost = 0;
    private final Scanner sc;
    private final Random random;

    public SlotMachineCLI(Scanner sc) {
        this.sc = sc;
        this.random = new Random();
    }

    public void runGame() throws InterruptedException {
        // Welcome Message
        System.out.println("----------------------------------");
        System.out.println("---  Welcome to Java Slot Pro  ---");
        System.out.print("    Symbols: ");
        for (Symbol s : Symbol.values()) {
            System.out.print(s.emoji + " ");
        }
        System.out.println("\n----------------------------------");

        // Main game loop
        while (true) {
            System.out.println("\n**** Current Balance: $" + balance);

            if (balance <= 0) {
                System.out.println("Your balance is zero. Game over.");
                break;
            }

            int bet = handleBet();
            if (bet == 0) { // Check for exit condition or invalid input after retries
                continue;
            }

            int originalBet = bet;
            balance -= bet;
            roundsPlayed++;

            System.out.println("Spinning...");
            String[] row = spinRow();
            TimeUnit.SECONDS.sleep(1);
            printRow(row);

            int payout = calculatePayout(row, originalBet);
            processRoundResult(payout, originalBet);

            System.out.println("----------------------------------");
            if (!askToPlayAgain()) {
                break;
            }
        }

        finalSummary();
    }

    // --- Utility Methods ---

    /**
     * Handles user input for the bet amount, validating it against the balance.
     * Retries until a valid bet or exit is chosen.
     * @return The valid bet amount, or 0 if the user chooses to retry or exit.
     */
    private int handleBet() {
        int bet = 0;
        boolean validBet = false;
        while (!validBet) {
            System.out.print(" ---> Enter BET Amount ($1 - $" + balance + "): $");
            try {
                bet = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("🚫 Invalid input! Please enter a whole number.");
                continue;
            }

            if (bet > balance) {
                System.out.println("INSUFFICIENT FUNDS! Your balance is only $" + balance);
            } else if (bet <= 0) {
                System.out.println("Bet must be greater than $0.");
            } else {
                validBet = true;

                // All-in confirmation
                if (bet == balance) {
                    System.out.println("⚠️ You're betting your entire balance! Are you sure? (yes/no) ");
                    String confirm = sc.nextLine().trim();
                    if (!confirm.equalsIgnoreCase("YES")) {
                        validBet = false; // Force a retry of the bet
                    }
                }
            }
        }
        return bet;
    }

    /**
     * Spins the row and generates three random symbols.
     * @return An array of 3 symbol strings.
     */
    private String[] spinRow() {
        String[] symbols = Symbol.getEmojis();
        String[] row = new String[3];

        for(int i = 0; i < 3; i++) {
            row[i] = symbols[random.nextInt(symbols.length)];
        }
        return row;
    }

   
     //Prints the current row in a clear format.
     
    private void printRow(String[] row) {
        System.out.println("*******************");
        System.out.println( "   " + String.join(" | ", row));
        System.out.println("*******************");
    }

    /**
     * Calculates the payout based on the symbols in the row.
     * return The total win amount (bet * multiplier), or 0 for a loss.
     */
    private int calculatePayout(String[] row, int bet) {
        String s0 = row[0];
        String s1 = row[1];
        String s2 = row[2];

        // 3-of-a-kind
        if (s0.equals(s1) && s1.equals(s2)) {
            Symbol winningSymbol = Symbol.fromEmoji(s0);
            System.out.println("🏆 TRIPLE MATCH! (" + winningSymbol.emoji + ") Payout: x" + winningSymbol.tripleMultiplier);
            return bet * winningSymbol.tripleMultiplier;
        }

        // Two-of-a-kind (Harmonized to check all three possible pairs)
        int multiplier = 0;
        Symbol winningSymbol = null;

        // Check (0, 1) or (1, 2) or (0, 2)
        if (s0.equals(s1)) {
            winningSymbol = Symbol.fromEmoji(s0);
            multiplier = winningSymbol.doubleMultiplier;
        } else if (s1.equals(s2)) {
            winningSymbol = Symbol.fromEmoji(s1);
            multiplier = winningSymbol.doubleMultiplier;
        } else if (s0.equals(s2)) { // Added missing (0, 2) check
            winningSymbol = Symbol.fromEmoji(s0);
            multiplier = winningSymbol.doubleMultiplier;
        }

        if (multiplier > 0 && winningSymbol != null) {
            System.out.println("✨ DOUBLE MATCH! (" + winningSymbol.emoji + ") Payout: x" + multiplier);
            return bet * multiplier;
        }

        return 0; // No match
    }

    /**
     * Updates statistics and balance based on the round outcome.
     */
    private void processRoundResult(int payout, int originalBet) {
        if (payout > 0) {
            int netWin = payout - originalBet;
            System.out.println("✅ YOU WON: $" + payout + " (Net: $" + netWin + ")");
            balance += payout;
            totalWon += netWin;

            if (netWin > biggestWin) {
                biggestWin = netWin;
            }
        } else {
            System.out.println("❌ You Lost the Round.");
            totalLost += originalBet;
        }
    }

    /**
     * Asks the user if they want to play again, ensuring valid input.
     * @return true if the user enters 'yes', false otherwise.
     */
    private boolean askToPlayAgain() {
        String playAgain;
        do {
            System.out.print("Do you want to play again? (yes/no) :");
            playAgain = sc.nextLine().trim().toUpperCase();
        } while (!playAgain.equals("YES") && !playAgain.equals("NO"));

        return playAgain.equals("YES");
    }

    /**
     * Prints the final game summary and statistics.
     */
    private void finalSummary() {
        System.out.println("\n==============================");
        System.out.println("     🎮 FINAL GAME SUMMARY     ");
        System.out.println("==============================");
        System.out.println("* Rounds Played: " + roundsPlayed);
        System.out.println("* Final Balance: $" + balance);
        System.out.println("* Biggest Net Win: $" + biggestWin);
        System.out.println("* Total Net Won: $" + totalWon);
        System.out.println("* Total Lost (Stakes): $" + totalLost);

        String status;
        if (balance > 100) {
            status = "Lucky Streak! You finished up.";
        } else if (balance == 100) {
            status = "Broke Even! Back to the start.";
        } else if (balance == 0) {
            status = "Bankrupt! Better luck next time.";
        } else {
            status = "Finished down. Maybe one more spin?";
        }
        System.out.println("* Status: " + status);
        System.out.println("------------------------------");
        System.out.println("--- Thanks for playing 🎉 ---");
        System.out.println("------------------------------");
    }

    // Main execution point
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            SlotMachineCLI game = new SlotMachineCLI(sc);
            game.runGame();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Game interrupted.");
        } finally {
            sc.close();
        }
    }
}
