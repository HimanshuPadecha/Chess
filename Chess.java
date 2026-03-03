import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Chess {
    private Board board;
    private boolean gameOver;
    Scanner scanner;
    private COLORS turn;
    static Map<Character, Integer> filesToIndices = Map.of(
            'a', 0,
            'b', 1,
            'c', 2,
            'd', 3,
            'e', 4,
            'f', 5,
            'g', 6,
            'h', 7);
    private Set<Positon> whiteAttacking;
    private Set<Positon> blackAttacking;
    private Positon whiteKingPosition;
    private Positon blackKingPositon;
    private boolean isWhiteKingInCheck;
    private boolean isBlackKingInCheck;

    public Chess() throws InvalidPosition {
        this.board = new Board();
        this.board.initBoard();
        this.scanner = new Scanner(System.in);
        this.turn = COLORS.WHITE;
        this.gameOver = false;
        // this.whiteAttacking = Attacking.getAllAttackingPositions(board,
        // COLORS.WHITE);
        // this.blackAttacking = Attacking.getAllAttackingPositions(board,
        // COLORS.BLACK);
        AttackingInfo attackingInfo = Attacking.getAllAttackingPositions(board);
        this.whiteAttacking = attackingInfo.whiteAttacking;
        this.blackAttacking = attackingInfo.blackAttacking;
        this.whiteKingPosition = Utils.convert("e1");
        this.blackKingPositon = Utils.convert("e8");
        this.isWhiteKingInCheck = false;
        this.isBlackKingInCheck = false;
    }

    enum Answer {
        yes, no
    }

    public void play() {
        System.out.print("Do you wanna start the game? (yes / no) : ");

        Answer start = null;
        try {
            start = Answer.valueOf(scanner.nextLine().trim().toLowerCase());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        if (start == null || start == Answer.no) {
            System.out.println("You aborted the game !!");
            return;
        }

        while (!this.gameOver) {
            this.board.print();

            System.out.println("Turn : " + this.turn);

            if (this.turn == COLORS.WHITE) {
                if (this.blackAttacking.contains(this.whiteKingPosition)) {
                    try {
                        this.gameOver = Utils.isChekmate(board, turn, this.whiteKingPosition,
                                new AttackingInfo(this.whiteAttacking, this.blackAttacking));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    if(this.gameOver){
                        System.out.println("Black Won !!");
                        break;
                    }
                    this.isWhiteKingInCheck = true;
                    System.out.println("It is a chek !! Do something about it !!");
                }
            } else {
                if (this.whiteAttacking.contains(this.blackKingPositon)) {
                    try {
                        this.gameOver = Utils.isChekmate(board, turn, this.blackKingPositon,
                                new AttackingInfo(this.whiteAttacking, this.blackAttacking));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    if(this.gameOver){
                        System.out.println("White Won !!");
                        break;
                    }
                    this.isBlackKingInCheck = true;
                    System.out.println("it is a chek !! Do something about it !!");
                }
            }

            System.out.print("Enter the square position to move : ");
            String squareOnBoard = scanner.nextLine().trim();

            try {
                Utils.validateSquare(squareOnBoard);
            } catch (InvalidSquare e) {
                System.out.println(e.getMessage());
                continue;
            }

            Positon current = Utils.convert(squareOnBoard);
            Piece pieceOnSquare = this.board.getPiece(current);

            if (pieceOnSquare == null) {
                System.out.println("There is no Piece on that square !! ");
                continue;
            }

            if (pieceOnSquare.getColor() != this.turn) {
                System.out.println("That is not your Piece !!");
                continue;
            }

            System.out.print("Enter where to move : ");
            String squareToMove = scanner.nextLine().trim();

            try {
                Utils.validateSquare(squareToMove);
            } catch (InvalidSquare e) {
                System.out.println(e.getMessage());
                continue;
            }

            Positon next = Utils.convert(squareToMove);

            try {
                this.board.move(board, current, next);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            Set<Positon> tempWhiteAttacking = this.whiteAttacking;
            Set<Positon> tempBlackAttakcing = this.blackAttacking;

            // after a move is done on board directly refreshing the attacking of both
            try {
                AttackingInfo attackingInfo = Attacking.getAllAttackingPositions(board);
                this.whiteAttacking = attackingInfo.whiteAttacking;
                this.blackAttacking = attackingInfo.blackAttacking;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // checking pinned piece moved or not ?
            try {
                if (this.turn == COLORS.WHITE) {
                    if (this.blackAttacking.contains(this.whiteKingPosition)) {
                        System.out.println("Illegal move !!");
                        board.informalMove(next, current);
                        this.blackAttacking = tempBlackAttakcing;
                        continue;
                    }
                } else {

                    if (this.whiteAttacking.contains(this.blackKingPositon)) {
                        System.out.println("Illegal move !!");
                        board.informalMove(next, current);
                        this.whiteAttacking = tempWhiteAttacking;
                        continue;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            if (pieceOnSquare.getName() == PIECES.KING) {
                if (this.turn == COLORS.WHITE) {
                    this.whiteKingPosition = next;
                } else {
                    this.blackKingPositon = next;
                }
            }

            try {
                if (this.turn == COLORS.WHITE && this.isWhiteKingInCheck) {

                    if (this.blackAttacking.contains(this.whiteKingPosition)) {
                        board.informalMove(next, current);
                        System.out.println("Illegal Move !!");
                        continue;
                    } else {
                        this.isWhiteKingInCheck = false;
                    }
                } else if (this.turn == COLORS.BLACK && this.isBlackKingInCheck) {

                    if (this.whiteAttacking.contains(this.blackKingPositon)) {
                        board.informalMove(next, current);
                        System.out.println("Illegal Move !!");
                        continue;
                    } else {
                        this.isBlackKingInCheck = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }

            this.turn = this.turn == COLORS.WHITE ? COLORS.BLACK : COLORS.WHITE;
        }
    }
}
