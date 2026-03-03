import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Attacking {
    public List<Positon> pawn(Board board, Positon positon) {

        Piece pawn = board.getPiece(positon);

        if (pawn == null || pawn.getName() != PIECES.PAWN) {
            return new ArrayList<>();
        }

        List<Positon> attacking = new ArrayList<>();

        if (positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            attacking.add(new Positon(
                    pawn.getColor() == COLORS.WHITE ? positon.getRank() + 1 : positon.getRank() - 1,
                    positon.getIndex() + 1));
        }

        if (positon.getIndex() > 0) {
            attacking.add(new Positon(
                    pawn.getColor() == COLORS.WHITE ? positon.getRank() + 1 : positon.getRank() - 1,
                    positon.getIndex() - 1));
        }

        return attacking;
    }

    public List<Positon> rook(Board board, Positon positon) {
        Piece rook = board.getPiece(positon);

        if (rook == null || (rook.getName() != PIECES.ROOK && rook.getName() != PIECES.QUEEN)) {
            return new ArrayList<>();
        }

        Map<Integer, List<Square>> innerMap = board.getMap();
        List<Positon> attacking = new ArrayList<>();

        List<Square> rank = innerMap.get(positon.getRank());

        // get the squares on the right side..
        for (int i = positon.getIndex() + 1; i < Board.CHESS_CONSTANT; i++) {
            attacking.add(new Positon(positon.getRank(), i));

            if (rank.get(i).getPiece() != null && rank.get(i).getPiece().getName() == PIECES.KING
                    && rank.get(i).getPiece().getColor() != rook.getColor()) {
                continue;
            }

            if (rank.get(i).getPiece() != null) {
                break;
            }
        }

        // get the piece on the left side..
        for (int i = positon.getIndex() - 1; i >= 0; i--) {
            attacking.add(new Positon(positon.getRank(), i));

            if (rank.get(i).getPiece() != null && rank.get(i).getPiece().getName() == PIECES.KING
                    && rank.get(i).getPiece().getColor() != rook.getColor()) {
                continue;
            }

            if (rank.get(i).getPiece() != null) {
                break;
            }
        }

        // get the piece on the bottom..
        for (int i = positon.getRank() - 1; i >= 1; i--) {
            attacking.add(new Positon(i, positon.getIndex()));

            if (innerMap.get(i).get(positon.getIndex()).getPiece() != null
                    && innerMap.get(i).get(positon.getIndex()).getPiece().getName() == PIECES.KING
                    && innerMap.get(i).get(positon.getIndex()).getPiece().getColor() != rook.getColor()) {
                continue;
            }

            if (innerMap.get(i).get(positon.getIndex()).getPiece() != null) {
                break;
            }
        }

        // get the piece on the top..
        for (int i = positon.getRank() + 1; i <= Board.CHESS_CONSTANT; i++) {
            attacking.add(new Positon(i, positon.getIndex()));

            if (innerMap.get(i).get(positon.getIndex()).getPiece() != null
                    && innerMap.get(i).get(positon.getIndex()).getPiece().getName() == PIECES.KING
                    && innerMap.get(i).get(positon.getIndex()).getPiece().getColor() != rook.getColor()) {
                continue;
            }

            if (innerMap.get(i).get(positon.getIndex()).getPiece() != null) {
                break;
            }
        }

        return attacking;
    }

    public List<Positon> knight(Board board, Positon positon) throws InvalidPosition {
        Piece knight = board.getPiece(positon);

        if (knight == null || knight.getName() != PIECES.KNIGHT) {
            return new ArrayList<>();
        }

        return Utils.validKnightMoves(positon);
    }

    public List<Positon> bishop(Board board, Positon positon) {
        Piece bishop = board.getPiece(positon);

        if (bishop == null || (bishop.getName() != PIECES.BISHOP && bishop.getName() != PIECES.QUEEN)) {
            return new ArrayList<>();
        }

        Map<Integer, List<Square>> innerMap = board.getMap();
        List<Positon> attacking = new ArrayList<>();

        // right upper diagonals loop
        int j = positon.getIndex() + 1;
        for (int i = positon.getRank() + 1; i <= Board.CHESS_CONSTANT && j < Board.CHESS_CONSTANT; i++) {
            attacking.add(new Positon(i, j));

            if (innerMap.get(i).get(j).getPiece() != null && innerMap.get(i).get(j).getPiece().getName() == PIECES.KING
                    && innerMap.get(i).get(j).getPiece().getColor() != bishop.getColor()) {
                j++;
                continue;
            }

            if (innerMap.get(i).get(j++).getPiece() != null) {
                break;
            }
        }

        // left upper diagonals loop
        j = positon.getIndex() - 1;
        for (int i = positon.getRank() + 1; i <= Board.CHESS_CONSTANT && j >= 0; i++) {

            attacking.add(new Positon(i, j));

            if (innerMap.get(i).get(j).getPiece() != null && innerMap.get(i).get(j).getPiece().getName() == PIECES.KING
                    && innerMap.get(i).get(j).getPiece().getColor() != bishop.getColor()) {
                j--;
                continue;
            }

            if (innerMap.get(i).get(j--).getPiece() != null) {
                break;
            }
        }

        // right lower diagonals loop
        j = positon.getIndex() + 1;
        for (int i = positon.getRank() - 1; i > 0 && j < Board.CHESS_CONSTANT; i--) {
            attacking.add(new Positon(i, j));

            if (innerMap.get(i).get(j).getPiece() != null && innerMap.get(i).get(j).getPiece().getName() == PIECES.KING
                    && innerMap.get(i).get(j).getPiece().getColor() != bishop.getColor()) {
                j++;
                continue;
            }

            if (innerMap.get(i).get(j++).getPiece() != null) {
                break;
            }
        }

        // left lower diagonals loop
        j = positon.getIndex() - 1;
        for (int i = positon.getRank() - 1; i > 0 && j >= 0; i--) {
            attacking.add(new Positon(i, j));

            if (innerMap.get(i).get(j).getPiece() != null && innerMap.get(i).get(j).getPiece().getName() == PIECES.KING
                    && innerMap.get(i).get(j).getPiece().getColor() != bishop.getColor()) {
                j--;
                continue;
            }

            if (innerMap.get(i).get(j--).getPiece() != null) {
                break;
            }
        }

        return attacking;
    }

    public List<Positon> queen(Board board, Positon positon) {
        Piece queen = board.getPiece(positon);

        if (queen == null || queen.getName() != PIECES.QUEEN) {
            return new ArrayList<>();
        }

        List<Positon> attacking = new ArrayList<>();

        attacking.addAll(this.rook(board, positon));
        attacking.addAll(this.bishop(board, positon));

        return attacking;
    }

    public List<Positon> king(Board board, Positon positon) throws InvalidPosition {
        Piece king = board.getPiece(positon);

        if (king == null || king.getName() != PIECES.KING) {
            System.out.println("Not a king !");
            return new ArrayList<>();
        }

        List<Positon> attacking = new ArrayList<>();

        if (positon.getRank() < Board.CHESS_CONSTANT && positon.getIndex() > 0) {
            attacking.add(new Positon(positon.getRank() + 1, positon.getIndex() - 1));
        }

        if (positon.getRank() < Board.CHESS_CONSTANT) {
            attacking.add(new Positon(positon.getRank() + 1, positon.getIndex()));
        }

        if (positon.getRank() < Board.CHESS_CONSTANT && positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            attacking.add(new Positon(positon.getRank() + 1, positon.getIndex() + 1));
        }

        if (positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            attacking.add(new Positon(positon.getRank(), positon.getIndex() + 1));
        }

        if (positon.getRank() > 1 && positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            attacking.add(new Positon(positon.getRank() - 1, positon.getIndex() + 1));
        }

        if (positon.getRank() > 1) {
            attacking.add(new Positon(positon.getRank() - 1, positon.getIndex()));
        }

        if (positon.getRank() > 1 && positon.getIndex() > 0) {
            attacking.add(new Positon(positon.getRank() - 1, positon.getIndex() - 1));
        }

        if (positon.getIndex() > 0) {
            attacking.add(new Positon(positon.getRank(), positon.getIndex() - 1));
        }

        return attacking;
        
    }

    // public static Set<Positon> getAllAttackingPositions(Board board, COLORS
    // color) throws InvalidPosition {

    // Attacking attacking = new Attacking();

    // Set<Positon> allPositons = new HashSet<>();

    // for (Map.Entry<Integer, List<Square>> rank : board.getMap().entrySet()) {

    // List<Square> currentRank = rank.getValue();

    // for (int i = 0; i < currentRank.size(); i++) {
    // Piece currentPiece = currentRank.get(i).getPiece();
    // System.out.println(new Positon(rank.getKey(), i));

    // if (currentPiece != null && currentPiece.getColor() == color) {

    // if (currentPiece.getName() == PIECES.PAWN) {

    // allPositons.addAll(attacking.pawn(board, new Positon(rank.getKey(), i)));

    // } else if (currentPiece.getName() == PIECES.ROOK) {

    // allPositons.addAll(attacking.rook(board, new Positon(rank.getKey(), i)));

    // } else if (currentPiece.getName() == PIECES.KNIGHT) {

    // allPositons.addAll(attacking.knight(board, new Positon(rank.getKey(), i)));

    // } else if (currentPiece.getName() == PIECES.BISHOP) {

    // allPositons.addAll(attacking.bishop(board, new Positon(rank.getKey(), i)));

    // } else if (currentPiece.getName() == PIECES.QUEEN) {

    // allPositons.addAll(attacking.queen(board, new Positon(rank.getKey(), i)));

    // } else if (currentPiece.getName() == PIECES.KING) {

    // allPositons.addAll(attacking.king(board, new Positon(rank.getKey(), i)));

    // }
    // }
    // }
    // }
    // return allPositons;
    // }

    public static AttackingInfo getAllAttackingPositions(Board board) throws InvalidPosition {
        Set<Positon> whiteAttacking = new HashSet<>();
        Set<Positon> blackAttacking = new HashSet<>();

        Attacking attacking = new Attacking();

        for (Map.Entry<Integer, List<Square>> rank : board.getMap().entrySet()) {

            List<Square> currentRank = rank.getValue();

            for (int i = 0; i < currentRank.size(); i++) {
                Piece currentPiece = currentRank.get(i).getPiece();

                if (currentPiece != null) {
                    if (currentPiece.getName() == PIECES.PAWN) {
                        if (currentPiece.getColor() == COLORS.WHITE) {
                            whiteAttacking.addAll(attacking.pawn(board, new Positon(rank.getKey(), i)));
                        } else {
                            blackAttacking.addAll(attacking.pawn(board, new Positon(rank.getKey(), i)));
                        }
                    } else if (currentPiece.getName() == PIECES.ROOK) {
                        if (currentPiece.getColor() == COLORS.WHITE) {
                            whiteAttacking.addAll(attacking.rook(board, new Positon(rank.getKey(), i)));
                        } else {
                            blackAttacking.addAll(attacking.rook(board, new Positon(rank.getKey(), i)));
                        }
                    } else if (currentPiece.getName() == PIECES.KNIGHT) {
                        if (currentPiece.getColor() == COLORS.WHITE) {
                            whiteAttacking.addAll(attacking.knight(board, new Positon(rank.getKey(), i)));
                        } else {
                            blackAttacking.addAll(attacking.knight(board, new Positon(rank.getKey(), i)));
                        }
                    } else if (currentPiece.getName() == PIECES.BISHOP) {
                        if (currentPiece.getColor() == COLORS.WHITE) {
                            whiteAttacking.addAll(attacking.bishop(board, new Positon(rank.getKey(), i)));
                        } else {
                            blackAttacking.addAll(attacking.bishop(board, new Positon(rank.getKey(), i)));
                        }
                    } else if (currentPiece.getName() == PIECES.QUEEN) {
                        if (currentPiece.getColor() == COLORS.WHITE) {
                            whiteAttacking.addAll(attacking.queen(board, new Positon(rank.getKey(), i)));
                        } else {
                            blackAttacking.addAll(attacking.queen(board, new Positon(rank.getKey(), i)));
                        }
                    } else if (currentPiece.getName() == PIECES.KING) {
                        if (currentPiece.getColor() == COLORS.WHITE) {
                            whiteAttacking.addAll(attacking.king(board, new Positon(rank.getKey(), i)));
                        } else {
                            blackAttacking.addAll(attacking.king(board, new Positon(rank.getKey(), i)));
                        }
                    }
                }
            }
        }

        return new AttackingInfo(whiteAttacking, blackAttacking);
    }
}
