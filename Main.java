public class Main {
    public static void main(String[] args) throws InvalidPosition {
        // Chess chess = new Chess();
        // chess.play();

        Board board = new Board();

        board.place("e1", new Piece(PIECES.KING, COLORS.WHITE));
        board.place("d2", new Piece(PIECES.PAWN, COLORS.WHITE));
        board.place("f2", new Piece(PIECES.PAWN, COLORS.WHITE));

        board.place("e8", new Piece(PIECES.QUEEN, COLORS.BLACK));
        board.place("d1", new Piece(PIECES.ROOK, COLORS.WHITE));
        board.place("f1", new Piece(PIECES.BISHOP, COLORS.WHITE));
        board.place("h1", new Piece(PIECES.ROOK, COLORS.BLACK));

        board.print();

        try {
            // System.out.println(Utils.isCheckmate(board, COLORS.WHITE, Utils.convert("e1")));
            // board.move(board, Utils.convert("e7"),Utils.convert("e8"));
            System.out.println(Utils.isCheckmate(board, COLORS.WHITE, Utils.convert("e1")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        board.print();
    }
}
