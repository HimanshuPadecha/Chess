import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidateMoves {

    public void pawn(Board board, Positon from, Positon to) throws IllegalMove {
        Piece pawn = board.getPiece(from);

        if (pawn == null || pawn.getName() != PIECES.PAWN) {
            System.out.println("Not a pawn");
            return;
        }

        // index validation
        if (Math.abs(from.getIndex() - to.getIndex()) > 1) {
            throw new IllegalMove("Index invalid");
        }

        // rank validations

        // to will always be greater then from : done
        if (pawn.getColor() == COLORS.WHITE ? from.getRank() >= to.getRank() : from.getRank() <= to.getRank()) {
            throw new IllegalMove("Cannot Move downwards or parallel !!");
        }

        // cannot move pawn more then 2 squares : done
        if (Math.abs(to.getRank() - from.getRank()) > 2) {
            throw new IllegalMove("Cannot move that many steps !!");
        }

        // if moving two square and it is not first move of the pawn : done
        if (Math.abs(from.getRank() - to.getRank()) == 2
                && from.getRank() != (pawn.getColor() == COLORS.WHITE ? Board.WHITE_PAWN_RANK
                        : Board.BLACK_PAWN_RANK)) {
            throw new IllegalMove("Cannot move that many square !!");
        }

        // if moving straight : no pieces should on that square
        if (from.getIndex() == to.getIndex()) { // done
            if (board.getPiece(to) != null) { // done
                throw new IllegalMove("Already occupied square !!");
            }

            // if moving two square checking if there is something in the middle
            if (Math.abs(to.getRank() - from.getRank()) == 2) { // done
                if (board.getPiece(
                        new Positon(pawn.getColor() == COLORS.WHITE ? from.getRank() + 1 : from.getIndex() - 1,
                                from.getIndex())) != null) {// done
                    throw new IllegalMove("Something is in the middle ! cannot move !!");
                }
            }
        } else {
            // if not moving straight : must be a capture or en passant
            if (board.getPiece(to) == null
                    && board.getPiece(new Positon(from.getRank(), to.getIndex())) == null) {
                throw new IllegalMove("Illegal move !!"); // done
            }

            // if there is no black piece then pawn cannot go diagonally
            if (board.getPiece(to) != null) {
                if (board.getPiece(to).getColor() != (pawn.getColor() == COLORS.WHITE ? COLORS.BLACK : COLORS.WHITE)) {
                    throw new IllegalMove("Cannot capture !!");
                } // done

            } else {
                // if its en passent then pawn must be on fifth rank
                if (from.getRank() != (pawn.getColor() == COLORS.WHITE ? 5 : 4)
                        || board.getPiece(new Positon(from.getRank(), to.getIndex()))
                                .getColor() != (pawn.getColor() == COLORS.WHITE ? COLORS.BLACK : COLORS.WHITE)
                        || board.getPiece(new Positon(from.getRank(), to.getIndex())).getName() != PIECES.PAWN ||
                        Chess.prevMove.getTo() != new Positon(from.getRank(), to.getIndex()) ||
                        Chess.prevMove.getPiece() != PIECES.PAWN) {

                    throw new IllegalMove("Illegal en passent !!"); // done

                }
            }
        }

        if (from.getRank() == (pawn.getColor() == COLORS.WHITE ? 5 : 4) && board.getPiece(to) == null
                && Math.abs(from.getIndex() - to.getIndex()) == 1
                && board.getPiece(new Positon(from.getRank(), to.getIndex()))
                        .getColor() == (pawn.getColor() == COLORS.WHITE ? COLORS.BLACK : COLORS.WHITE)
                && board.getPiece(new Positon(from.getRank(), to.getIndex())).getName() == PIECES.PAWN) {

            board.getMap().get(from.getRank()).get(to.getIndex()).setPiece(null);// done
        }
    }

    // This function will return all the valid moves of the pawn
    public List<Positon> pawn(Board board, Positon positon) {

        Piece pawn = board.getPiece(positon);
        List<Positon> validMoves = new ArrayList<>();

        if (pawn == null || pawn.getName() != PIECES.PAWN) {
            System.out.println("Not a pawn");
            return validMoves;
        }

        // try + 1, + 2, and captures and en passent

        try {
            this.pawn(board, positon,
                    new Positon(pawn.getColor() == COLORS.WHITE ? positon.getRank() + 1 : positon.getRank() - 1,
                            positon.getIndex()));
            validMoves.add(
                    new Positon(pawn.getColor() == COLORS.WHITE ? positon.getRank() + 1 : positon.getRank() - 1,
                            positon.getIndex()));
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }
        try {
            this.pawn(board, positon,
                    new Positon(pawn.getColor() == COLORS.WHITE ? positon.getRank() + 2 : positon.getRank() - 2,
                            positon.getIndex()));
            validMoves.add(
                    new Positon(pawn.getColor() == COLORS.WHITE ? positon.getRank() + 2 : positon.getRank() - 2,
                            positon.getIndex()));
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }
        try {

            // left side move
            if (positon.getIndex() > 0) {
                Positon left = new Positon(
                        pawn.getColor() == COLORS.WHITE ? positon.getRank() + 1 : positon.getRank() - 1,
                        positon.getIndex() - 1);
                this.pawn(board, positon, left);
                validMoves.add(left);
            }
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }

        try {
            // right side move
            if (positon.getIndex() < Board.CHESS_CONSTANT - 1) {
                Positon right = new Positon(
                        pawn.getColor() == COLORS.WHITE ? positon.getRank() + 1 : positon.getRank() - 1,
                        positon.getIndex() + 1);
                this.pawn(board, positon, right);
                validMoves.add(right);
            }
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }

        return validMoves;
    }

    public void rook(Board board, Positon from, Positon to) throws IllegalMove {
        Piece rook = board.getPiece(from);

        if (rook == null || (rook.getName() != PIECES.ROOK && rook.getName() != PIECES.QUEEN)) {
            System.out.println("Not a Rook");
            return;
        }

        if (from.equals(to)) {
            throw new IllegalMove("Cannot move to same square !!");
        }

        // it must be the same rank or file
        if (from.getIndex() != to.getIndex() && from.getRank() != to.getRank()) {
            throw new IllegalMove("Illegal move.");
        }

        // if to have some piece then it must be of opposite color
        if (board.getPiece(to) != null) {
            if (rook.getColor() == board.getPiece(to).getColor()) {
                throw new IllegalMove("Cannot capture own pieces");
            }
        }

        Map<Integer, List<Square>> innerMap = board.getMap();

        // checking if moving horizontal or vertical ??
        if (from.getIndex() == to.getIndex()) {
            // checking if going down to up
            if (from.getRank() < to.getRank()) {
                for (int i = from.getRank() + 1; i < to.getRank(); i++) {
                    Square currentSquare = innerMap.get(i).get(from.getIndex());
                    if (currentSquare.getPiece() != null) {
                        System.out.println(currentSquare);
                        throw new IllegalMove("Cannot jump over pieces !!");
                    }
                }
            } else {
                // going up to down
                for (int i = from.getRank() - 1; i > to.getRank(); i--) {
                    Square currentSquare = innerMap.get(i).get(from.getIndex());
                    if (currentSquare.getPiece() != null) {
                        throw new IllegalMove("Cannot jump over pieces !!");
                    }
                }
            }
        } else if (from.getRank() == to.getRank()) {
            List<Square> rank = innerMap.get(from.getRank());

            // going left to right
            if (from.getIndex() < to.getIndex()) {

                for (int i = from.getIndex() + 1; i < to.getIndex(); i++) {
                    if (rank.get(i).getPiece() != null) {
                        throw new IllegalMove("Cannot jump over pieces !!");
                    }
                }

            } else {
                for (int i = from.getIndex() - 1; i > to.getIndex(); i--) {
                    if (rank.get(i).getPiece() != null) {
                        throw new IllegalMove("Cannot jump over piece !!");
                    }
                }
            }
        }
    }

    public void knight(Board board, Positon from, Positon to) throws IllegalMove, InvalidPosition {
        Piece knight = board.getPiece(from);

        if (knight == null || knight.getName() != PIECES.KNIGHT) {
            System.out.println("Not a night or not valid from !!");
            return;
        }

        List<Positon> validMoves = Utils.validKnightMoves(from);

        if (!validMoves.contains(to)) {
            throw new IllegalMove("Knight can only move in L shape !!");
        }

        if (board.getPiece(to) != null && board.getPiece(to).getColor() == knight.getColor()) {
            throw new IllegalMove("Square already occupied !!");
        }
    }

    public void bishop(Board board, Positon from, Positon to) throws IllegalMove, InvalidPosition {
        Piece bishop = board.getPiece(from);

        if (bishop == null || (bishop.getName() != PIECES.BISHOP && bishop.getName() != PIECES.QUEEN)) {
            System.out.println("Not a bishop");
            return;
        }

        // cannot go to same square
        if (from.equals(to)) {
            throw new IllegalMove("Cannot go to same square !!");
        }

        List<Positon> validMoves = null;

        if (bishop.getName() == PIECES.BISHOP) {
            validMoves = Utils.validBishopMoves(from);
        }

        if (validMoves != null) {
            if (!validMoves.contains(to)) {
                throw new IllegalMove("Illegal square !!");
            }
        }
        // checking if the to is valid ?

        // cannot capture our own pieces
        if (board.getPiece(to) != null && bishop.getColor() == board.getPiece(to).getColor()) {
            throw new IllegalMove("Cannot capture own Pieces !!");
        }

        Map<Integer, List<Square>> innerMap = board.getMap();

        // going upper right
        if (from.getIndex() < to.getIndex() && from.getRank() < to.getRank()) {

            int j = from.getIndex() + 1;
            for (int i = from.getRank() + 1; i < to.getRank() && j < to.getIndex(); i++) {

                Square currentSquare = innerMap.get(i).get(j++);
                if (currentSquare.getPiece() != null) {
                    throw new IllegalMove("Cannot jump pieces !!");
                }
            }
        } else if (from.getRank() < to.getRank() && from.getIndex() > to.getIndex()) {
            // going upper left
            int j = from.getIndex() - 1;
            for (int i = from.getRank() + 1; i < to.getRank() && j > to.getIndex(); i++) {
                Square currnetSquare = innerMap.get(i).get(j--);

                if (currnetSquare.getPiece() != null) {
                    throw new IllegalMove("Cannot jump pieces !!");
                }
            }
        } else if (from.getRank() > to.getRank() && from.getIndex() < to.getIndex()) {
            // going lower right
            int j = from.getIndex() + 1;
            for (int i = from.getRank() - 1; i > to.getRank() && j < to.getIndex(); i--) {
                Square currnetSquare = innerMap.get(i).get(j++);

                if (currnetSquare.getPiece() != null) {
                    throw new IllegalMove("Cannot jump pieces !!");
                }
            }
        } else if (from.getRank() > to.getRank() && from.getIndex() > to.getIndex()) {
            // going lower left
            int j = from.getIndex() - 1;
            for (int i = from.getRank() - 1; i > to.getRank() && j > to.getIndex(); i--) {
                Square currentSquare = innerMap.get(i).get(j--);

                if (currentSquare.getPiece() != null) {
                    throw new IllegalMove("Cannot jump this pieces !!");
                }
            }
        }
    }

    public void queen(Board board, Positon from, Positon to) throws IllegalMove, InvalidPosition {
        Piece queen = board.getPiece(from);

        if (queen == null || queen.getName() != PIECES.QUEEN) {
            System.out.println("Not a queen");
            return;
        }

        List<Positon> validBishopMoves = Utils.validBishopMoves(from);

        if (validBishopMoves.contains(to)) {
            this.bishop(board, from, to);
        } else {
            this.rook(board, from, to);
        }
    }

    public void king(Board board, Positon from, Positon to) throws InvalidPosition, IllegalMove {
        Piece king = board.getPiece(from);

        if (king == null || king.getName() != PIECES.KING) {
            System.out.println("Not a king !!");
            return;
        }

        List<Positon> validMoves = Utils.validKingMoves(board, from);

        if (!validMoves.contains(to)) {
            throw new IllegalMove("Illegal king move !!");
        }
    }

    public static void validate(Board board, Positon from, Positon to) throws IllegalMove, InvalidPosition {
        if (board.getPiece(from) == null) {
            System.out.println("No piece of from position");
            return;
        }

        ValidateMoves validateMoves = new ValidateMoves();
        Piece piece = board.getPiece(from);

        if (piece.getName() == PIECES.PAWN) {
            validateMoves.pawn(board, from, to);
        } else if (piece.getName() == PIECES.ROOK) {
            validateMoves.rook(board, from, to);
        } else if (piece.getName() == PIECES.KNIGHT) {
            validateMoves.knight(board, from, to);
        } else if (piece.getName() == PIECES.BISHOP) {
            validateMoves.bishop(board, from, to);
        } else if (piece.getName() == PIECES.QUEEN) {
            validateMoves.queen(board, from, to);
        } else if (piece.getName() == PIECES.KING) {
            validateMoves.king(board, from, to);
        }
    }
}
