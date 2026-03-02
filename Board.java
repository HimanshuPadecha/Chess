import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<Integer, List<Square>> board;
    static final int CHESS_CONSTANT = 8;
    static final int WHITE_PAWN_RANK = 2;
    static final int BLACK_PAWN_RANK = 7;
    private static final int WHITE_MAJOR_PIECE_RANK = 1;
    private static final int BLACK_MAJOR_PIECE_RANK = Board.CHESS_CONSTANT;

    public Board() {
        this.board = new HashMap<>();
        this.initEmptyBoard();
    }

    public final void initBoard() {

        for (int i = 1; i <= Board.CHESS_CONSTANT; i++) {

            if (i == Board.WHITE_PAWN_RANK) {

                this.board.put(i, this.initPawns(COLORS.WHITE));

            } else if (i == Board.BLACK_PAWN_RANK) {

                this.board.put(i, this.initPawns(COLORS.BLACK));

            } else if (i == Board.WHITE_MAJOR_PIECE_RANK) {

                this.board.put(i, this.initMinorAndMajorPieces(COLORS.WHITE));

            } else if (i == Board.BLACK_MAJOR_PIECE_RANK) {

                this.board.put(i, this.initMinorAndMajorPieces(COLORS.BLACK));

            } else {
                List<Square> rank = new ArrayList<>();

                for (int j = 1; j <= Board.CHESS_CONSTANT; j++) {
                    rank.add(new Square());
                }

                this.board.put(i, rank);
            }
        }
    }

    private final List<Square> initPawns(COLORS color) {
        List<Square> rank = new ArrayList<>();

        for (int i = 1; i <= Board.CHESS_CONSTANT; i++) {
            Square square = new Square(new Piece(PIECES.PAWN, color));
            rank.add(square);
        }

        return rank;
    }

    public final void initEmptyBoard() {
        for (int i = 1; i <= Board.CHESS_CONSTANT; i++) {
            List<Square> rank = new ArrayList<>();

            for (int j = 0; j < Board.CHESS_CONSTANT; j++) {
                rank.add(new Square());
            }

            this.board.put(i, rank);
        }
    }

    private final List<Square> initMinorAndMajorPieces(COLORS color) {
        return List.of(
                new Square(new Piece(PIECES.ROOK, color)),
                new Square(new Piece(PIECES.KNIGHT, color)),
                new Square(new Piece(PIECES.BISHOP, color)),
                new Square(new Piece(PIECES.QUEEN, color)),
                new Square(new Piece(PIECES.KING, color)),
                new Square(new Piece(PIECES.BISHOP, color)),
                new Square(new Piece(PIECES.KNIGHT, color)),
                new Square(new Piece(PIECES.ROOK, color)));
    }

    public final Piece getPiece(Positon positon) {
        return this.board.get(positon.getRank()).get(positon.getIndex()).getPiece();
    }

    public final void move(Board board, Positon from, Positon to) throws InvalidPosition, IllegalMove {

        from.validate();
        to.validate();

        ValidateMoves.validate(board, from, to);

        Piece piece = this.getPiece(from);
        this.board.get(from.getRank()).get(from.getIndex()).setPiece(null);
        this.board.get(to.getRank()).get(to.getIndex()).setPiece(piece); 

        if (piece.getName() == PIECES.PAWN) {
            if (piece.getColor() == COLORS.WHITE && to.getRank() == Board.CHESS_CONSTANT) {
                Utils.promotion(board, to);
            } else if (piece.getColor() == COLORS.WHITE && to.getRank() == 1) {
                Utils.promotion(board, to);
            }
        }
    }

    public Map<Integer, List<Square>> getMap() {
        return this.board;
    }

    public final void print() {
        System.out.println();
        System.out.println();
        for (int i = Board.CHESS_CONSTANT; i >= 1; i--) {
            List<Square> rank = this.board.get(i);
            System.out.print(i + "   |  ");

            for (int j = 0; j < Board.CHESS_CONSTANT; j++) {
                System.out.printf("%-15s", rank.get(j));
            }

            System.out.println();
            System.out.println("    |   ");
        }

        System.out.print(0 + "   |  ");

        char ch = 'a';
        for (int j = 0; j < Board.CHESS_CONSTANT; j++) {
            System.out.printf("%-15s", " " + ch++ + " ");
        }

        System.out.println();
        System.out.println();
    }

    public void place(String squareName, Piece piece) {

        try {
            Utils.validateSquare(squareName);
        } catch (InvalidSquare e) {
            System.out.println(e.getMessage());
            return;
        }

        Positon positon = Utils.convert(squareName);

        this.board.get(positon.getRank()).get(positon.getIndex()).setPiece(piece);
    }

    public void informalMove(Positon from, Positon to) {
        Piece piece = this.getPiece(from);
        this.board.get(from.getRank()).get(from.getIndex()).setPiece(null);
        this.board.get(to.getRank()).get(to.getIndex()).setPiece(piece);
    }
}
