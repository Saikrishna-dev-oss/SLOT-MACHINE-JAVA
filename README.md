# 🎰 SLOT MACHINE JAVA (CLI)

A command-line slot machine game built in Java — featuring emoji-based reels, clean OOP structure, betting logic, and a dynamic summary board that tracks your gameplay stats.

---

## 🧠 Features
- 🎞️ Emoji-based slot reels (`😵‍💫 😵 😱 🥶 🥰`) managed by a dedicated Java `Enum`.
- 💸 Betting system with balance tracking
- ⚠️ All-in confirmation prompt to prevent accidental large bets.
- 🎉 Jackpot detection for triple matches
- 📊 Summary board with:
  - Rounds played
  - Final balance
  - Biggest win
  - Total won and lost
  - Status message (Lucky Streak / Bankrupt / Better luck next time)
- 🛡️ Input validation for non-numeric bets

---

## 🚀 How to Run

### Prerequisites:
- Java installed (`javac` and `java` available in terminal)

### Steps:
```bash
git clone https://github.com/Saikrishna-dev-oss/SLOT-MACHINE-JAVA.git
cd SLOT-MACHINE-JAVA
javac SlotMachine.java
java brocode.SlotMachine
```

---

## 📸 Sample Output

```
----------------------------------
---  Welcome to Java Slot Pro  ---
    Symbols: 😵‍💫 😱 😵 🥶 🥰 
----------------------------------

**** Current Balance: $100
 ---> Enter BET Amount ($1 - $100): $20
Spinning...
*******************
   😱 | 🥶 | 😱
*******************
✨ DOUBLE MATCH! (😱) Payout: x4
✅ YOU WON: $80 (Net: $60)
----------------------------------
Do you want to play again? (yes/no) : NO

==============================
     🎮 FINAL GAME SUMMARY     
==============================
* Rounds Played: 1
* Final Balance: $160
* Biggest Net Win: $60
* Total Net Won: $60
* Total Lost (Stakes): $0
* Status: Lucky Streak! You finished up.
------------------------------
--- Thanks for playing 🎉 ---
------------------------------
```

---

## 🛠️ Future Enhancements
- 🖼️ GUI version using JavaFX or Swing
- 👥 Multiplayer mode
- 🧪 Unit tests and code coverage
- 🌐 Web-based version with leaderboard

---

## 🤝 Contributing
Pull requests are welcome! For major changes, please open an issue first to discuss what you’d like to change.

---

## 📄 License
This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
