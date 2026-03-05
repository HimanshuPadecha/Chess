# Chess ♟️

A two-player chess game built in Java that implements the complete rules of chess with a focus on clean object-oriented design and rule validation. The project manages board state, piece movement, and game flow while enforcing complex mechanics such as check, checkmate, stalemate, castling, en passant, and pawn promotion, all executed through a terminal-based interface. ♟️

## 🚀 Getting Started

This section guides you through installing and running chess for the first time. We'll use Java 21.0+ as the example environment.

### 📋 Prerequisites

Before diving in, ensure you have the following installed:

- [Language/Runtime]: [Java 21.0+](https://www.oracle.com/in/java/technologies/downloads/) (check with `java --version`).
- [Other Tools]:
  - [Git](https://git-scm.com/).
  
If you're missing anything, follow the linked install guides—they're quick.

### 📥 Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/HimanshuPadecha/Chess
   cd Chess
   ```

2. **Compile and Run**:
   ```bash
   javac Main.java
   java Main
   ```

## 🏗️ Architecture

### 📊 Board Representation

The chess board is represented as an 8×8 structure of squares. ♟️  
Each square contains a position and optionally a chess piece.

```
Board
 └── Map<Rank, List<Square>>
        └── Square
              ├── Position (rank, file)
              └── Piece (optional)
```

### 📚 Important Classes

| File                  | Responsibility                          |
|-----------------------|-----------------------------------------|
| `Main.java`           | Entry point of the application          |
| `Chess.java`          | Controls game flow and turn management  |
| `Board.java`          | Represents and manages the chess board  |
| `Square.java`         | Represents a single board square        |
| `Positon.java`        | Stores rank and file coordinates        |
| `Piece.java`          | Base abstraction for chess pieces       |
| `PIECES.java`         | Enum representing piece types           |
| `COLORS.java`         | Enum representing piece colors          |
| `ValidateMoves.java`  | Validates legal moves                   |
| `Attacking.java`      | Calculates attacking squares            |
| `AttackingInfo.java`  | Stores attack information               |
| `PrevMove.java`       | Tracks previous move information        |
| `Utils.java`          | Utility helper methods                  |
| `IllegalMove.java`    | Exception for illegal moves             |
| `InvalidPosition.java`| Exception for invalid positions         |
| `InvalidSquare.java`  | Exception for invalid square input      |

## 🧮 Some Algorithms

Kindly check this PDF: 📄 [Detailed Algorithms](https://raw.githubusercontent.com/HimanshuPadecha/Chess/main/algorithms.pdf)