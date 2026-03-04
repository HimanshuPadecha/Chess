import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

enum castles {
    whiteKingSide, whiteQueenSide, blackKingSide, blackQueenSide
}

public class Utils {

    final static Map<Integer, Character> indicesTofiles = Map.of(
            0, 'a',
            1, 'b',
            2, 'c',
            3, 'd',
            4, 'e',
            5, 'f',
            6, 'g',
            7, 'h');

    final static Map<castles, List<Positon>> castlingSquares = Map.of(
            castles.whiteKingSide, List.of(Utils.convert("f1"), Utils.convert("g1")),
            castles.whiteQueenSide, List.of(Utils.convert("d1"), Utils.convert("c1"), Utils.convert("b1")),

            castles.blackKingSide, List.of(Utils.convert("f8"), Utils.convert("g8")),
            castles.blackQueenSide, List.of(Utils.convert("d8"), Utils.convert("c8"), Utils.convert("b8")));

    public static Positon convert(String squareName) {
        return new Positon(squareName.charAt(1) - '0', Chess.filesToIndices.get(squareName.charAt(0)));
    }

    public static String convertToSquareName(Positon positon) throws InvalidPosition {
        positon.validate();

        return String.valueOf(indicesTofiles.get(positon.getIndex())) + String.valueOf(positon.getRank());
    }

    public static void validateSquare(String position) throws InvalidSquare {
        if (position.trim().length() != 2) {
            throw new InvalidSquare("Length of the square must be 2 !!");
        }

        char ch = Character.toLowerCase(position.charAt(0));

        if (ch < 'a' || ch > 'h') {
            throw new InvalidSquare("Invalid char in the position !!");
        }

        if (!Character.isDigit(position.charAt(1))) {
            throw new InvalidSquare("Invalid Number in the position !!");
        }

        int num = position.charAt(1) - '0';

        if (num < 1 || num > Board.CHESS_CONSTANT) {
            throw new InvalidSquare("Invalid number given !!");
        }
    }

    public static List<Positon> validKnightMoves(Positon positon) throws InvalidPosition {
        positon.validate();

        List<Positon> validMoves = new ArrayList<>();

        // upper four moves
        if (positon.getRank() < Board.CHESS_CONSTANT - 1 && positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            validMoves.add(new Positon(positon.getRank() + 2, positon.getIndex() + 1));
        }

        if (positon.getRank() < Board.CHESS_CONSTANT && positon.getIndex() < Board.CHESS_CONSTANT - 2) {
            validMoves.add(new Positon(positon.getRank() + 1, positon.getIndex() + 2));
        }

        if (positon.getRank() < Board.CHESS_CONSTANT - 1 && positon.getIndex() > 0) {
            validMoves.add(new Positon(positon.getRank() + 2, positon.getIndex() - 1));
        }

        if (positon.getRank() < Board.CHESS_CONSTANT && positon.getIndex() > 1) {
            validMoves.add(new Positon(positon.getRank() + 1, positon.getIndex() - 2));
        }

        // lower four moves
        if (positon.getRank() > 2 && positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            validMoves.add(new Positon(positon.getRank() - 2, positon.getIndex() + 1));
        }

        if (positon.getRank() > 1 && positon.getIndex() < Board.CHESS_CONSTANT - 2) {
            validMoves.add(new Positon(positon.getRank() - 1, positon.getIndex() + 2));
        }

        if (positon.getRank() > 2 && positon.getIndex() > 0) {
            validMoves.add(new Positon(positon.getRank() - 2, positon.getIndex() - 1));
        }

        if (positon.getRank() > 1 && positon.getIndex() > 1) {
            validMoves.add(new Positon(positon.getRank() - 1, positon.getIndex() - 2));
        }

        return validMoves;
    }

    public static List<Positon> validBishopMoves(Positon positon) throws InvalidPosition {
        positon.validate();
        List<Positon> validMoves = new ArrayList<>();

        // right upper diagonals loop
        int j = positon.getIndex() + 1;
        for (int i = positon.getRank() + 1; i <= Board.CHESS_CONSTANT && j < Board.CHESS_CONSTANT; i++) {
            validMoves.add(new Positon(i, j++));
        }

        // left upper diagonals loop
        j = positon.getIndex() - 1;
        for (int i = positon.getRank() + 1; i <= Board.CHESS_CONSTANT && j >= 0; i++) {
            validMoves.add(new Positon(i, j--));
        }

        // right lower diagonals loop
        j = positon.getIndex() + 1;
        for (int i = positon.getRank() - 1; i > 0 && j < Board.CHESS_CONSTANT; i--) {
            validMoves.add(new Positon(i, j++));
        }

        // left lower diagonals loop
        j = positon.getIndex() - 1;
        for (int i = positon.getRank() - 1; i > 0 && j >= 0; i--) {
            validMoves.add(new Positon(i, j--));
        }

        return validMoves;
    }

    public static List<Positon> validKingMoves(Board board, Positon positon)
            throws InvalidPosition {
        positon.validate();
        Piece king = board.getPiece(positon);
        List<Positon> validMoves = new ArrayList<>();

        AttackingInfo attackingInfo = Attacking.getAllAttackingPositions(board);

        Set<Positon> enemyAttacking = king.getColor() == COLORS.WHITE ? attackingInfo.blackAttacking
                : attackingInfo.whiteAttacking;

        // top left square
        if (positon.getRank() < Board.CHESS_CONSTANT && positon.getIndex() > 0) {
            Positon current = new Positon(positon.getRank() + 1, positon.getIndex() - 1);

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        // top top square
        if (positon.getRank() < Board.CHESS_CONSTANT) {

            Positon current = new Positon(positon.getRank() + 1, positon.getIndex());

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        // top right square
        if (positon.getRank() < Board.CHESS_CONSTANT && positon.getIndex() < Board.CHESS_CONSTANT - 1) {

            Positon current = new Positon(positon.getRank() + 1, positon.getIndex() + 1);

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        // right right square
        if (positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            Positon current = new Positon(positon.getRank(), positon.getIndex() + 1);

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        // bottom right square
        if (positon.getRank() > 1 && positon.getIndex() < Board.CHESS_CONSTANT - 1) {
            Positon current = new Positon(positon.getRank() - 1, positon.getIndex() + 1);

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        // bottom bottom square
        if (positon.getRank() > 1) {
            Positon current = new Positon(positon.getRank() - 1, positon.getIndex());

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        // bottom left square
        if (positon.getRank() > 1 && positon.getIndex() > 0) {
            Positon current = new Positon(positon.getRank() - 1, positon.getIndex() - 1);

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        // left left square
        if (positon.getIndex() > 0) {
            Positon current = new Positon(positon.getRank(), positon.getIndex() - 1);

            if (board.getPiece(current) == null) {
                if (!enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            } else {
                if (board.getPiece(current).getColor() != king.getColor() && !enemyAttacking.contains(current)) {
                    validMoves.add(current);
                }
            }
        }

        return validMoves;
    }

    public static boolean isChekmate(Board board, COLORS color, Positon kingPositon, AttackingInfo attackingInfo)
            throws InvalidPosition, IllegalMove {
        // find the piece that have given chek

        if (board.getPiece(kingPositon) == null || board.getPiece(kingPositon).getName() != PIECES.KING
                || board.getPiece(kingPositon).getColor() != color) {
            System.out.println("Incorrect input in checkmate function !!");
            return false;
        }

        List<Positon> checkingPositions = Utils.getChekingPieces(board, color, kingPositon);
        List<Positon> kingValidPositons = Utils.validKingMoves(board, kingPositon);

        Set<Positon> currentAttackings = color == COLORS.WHITE ? attackingInfo.whiteAttacking
                : attackingInfo.blackAttacking;

        if (checkingPositions.isEmpty()) {
            return false;
        }

        Positon checkFrom = checkingPositions.get(0);

        if (checkingPositions.size() == 1) {

            if (board.getPiece(checkFrom).getName() == PIECES.PAWN
                    || board.getPiece(checkFrom).getName() == PIECES.KNIGHT && kingValidPositons.isEmpty()
                            && !currentAttackings.contains(checkFrom)) {
                return true;

            } else if (board.getPiece(checkFrom).getName() == PIECES.ROOK) {
                List<Positon> path = Utils.rookChekPath(board, checkFrom, kingPositon);

                List<Positon> piecesToComeBetween = Utils.piecesToComeBetween(board, kingPositon, path, color);

                if (piecesToComeBetween.isEmpty() && !currentAttackings.contains(checkFrom)
                        && kingValidPositons.isEmpty()) {
                    return true;
                }
            } else if (board.getPiece(checkFrom).getName() == PIECES.BISHOP) {
                List<Positon> path = Utils.bishopChekPath(board, checkFrom, kingPositon);

                List<Positon> piecesToComeBetween = Utils.piecesToComeBetween(board, kingPositon, path, color);

                if (piecesToComeBetween.isEmpty() && !currentAttackings.contains(checkFrom)
                        && kingValidPositons.isEmpty()) {
                    return true;
                }
            } else if (board.getPiece(checkFrom).getName() == PIECES.QUEEN) {

                List<Positon> validBishopMoves = Utils.validBishopMoves(checkFrom);
                List<Positon> path;

                if (validBishopMoves.contains(kingPositon)) {
                    path = Utils.bishopChekPath(board, checkFrom, kingPositon);
                } else {
                    path = Utils.rookChekPath(board, checkFrom, kingPositon);
                }

                List<Positon> piecesToComeBetween = Utils.piecesToComeBetween(board, kingPositon, path, color);

                if (piecesToComeBetween.isEmpty() && !currentAttackings.contains(checkFrom)
                        && kingValidPositons.isEmpty()) {
                    return true;
                }
            }
        } else {
            if (kingValidPositons.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static List<Positon> getChekingPieces(Board board, COLORS color, Positon kingPositon)
            throws InvalidPosition {

        List<Positon> chekingPositons = new ArrayList<>();
        Attacking attacking = new Attacking();

        for (Map.Entry<Integer, List<Square>> rank : board.getMap().entrySet()) {
            List<Square> currentRank = rank.getValue();

            for (int i = 0; i < currentRank.size(); i++) {
                Square currentSquare = currentRank.get(i);

                if (currentSquare.getPiece() != null && currentSquare.getPiece().getColor() != color) {

                    if (currentSquare.getPiece().getName() == PIECES.PAWN) {
                        List<Positon> pawnAttacking = attacking.pawn(board, new Positon(rank.getKey(), i));

                        if (pawnAttacking.contains(kingPositon)) {
                            chekingPositons.add(new Positon(rank.getKey(), i));
                        }
                    } else if (currentSquare.getPiece().getName() == PIECES.ROOK) {
                        List<Positon> rookAttacking = attacking.rook(board, new Positon(rank.getKey(), i));

                        if (rookAttacking.contains(kingPositon)) {
                            chekingPositons.add(new Positon(rank.getKey(), i));
                        }
                    } else if (currentSquare.getPiece().getName() == PIECES.KNIGHT) {
                        List<Positon> knightAttacking = attacking.knight(board, new Positon(rank.getKey(), i));

                        if (knightAttacking.contains(kingPositon)) {
                            chekingPositons.add(new Positon(rank.getKey(), i));
                        }
                    } else if (currentSquare.getPiece().getName() == PIECES.BISHOP) {
                        List<Positon> bishopAttacking = attacking.bishop(board, new Positon(rank.getKey(), i));

                        if (bishopAttacking.contains(kingPositon)) {
                            chekingPositons.add(new Positon(rank.getKey(), i));
                        }
                    } else if (currentSquare.getPiece().getName() == PIECES.QUEEN) {
                        List<Positon> queenAttacking = attacking.queen(board, new Positon(rank.getKey(), i));

                        if (queenAttacking.contains(kingPositon)) {
                            chekingPositons.add(new Positon(rank.getKey(), i));
                        }
                    }
                }
            }
        }

        return chekingPositons;
    }

    public static List<Positon> rookChekPath(Board board, Positon rookPositon, Positon kingPositon) {

        List<Positon> path = new ArrayList<>();

        // checking if moving horizontal or vertical ??
        if (rookPositon.getIndex() == kingPositon.getIndex()) {
            // checking if going down to up
            if (rookPositon.getRank() < kingPositon.getRank()) {
                for (int i = rookPositon.getRank() + 1; i < kingPositon.getRank(); i++) {
                    path.add(new Positon(i, rookPositon.getIndex()));
                }
            } else {
                // going up to down
                for (int i = rookPositon.getRank() - 1; i > kingPositon.getRank(); i--) {
                    path.add(new Positon(i, rookPositon.getIndex()));
                }
            }
        } else if (rookPositon.getRank() == kingPositon.getRank()) {
            // going left to right
            if (rookPositon.getIndex() < kingPositon.getIndex()) {

                for (int i = rookPositon.getIndex() + 1; i < kingPositon.getIndex(); i++) {
                    path.add(new Positon(rookPositon.getRank(), i));
                }

            } else {
                for (int i = rookPositon.getIndex() - 1; i > kingPositon.getIndex(); i--) {
                    path.add(new Positon(rookPositon.getRank(), i));
                }
            }
        }

        return path;
    }

    public static List<Positon> bishopChekPath(Board board, Positon bishopPositon, Positon kingPositon) {
        List<Positon> path = new ArrayList<>();

        // going upper right
        if (bishopPositon.getIndex() < kingPositon.getIndex() && bishopPositon.getRank() < kingPositon.getRank()) {

            int j = bishopPositon.getIndex() + 1;
            for (int i = bishopPositon.getRank() + 1; i < kingPositon.getRank() && j < kingPositon.getIndex(); i++) {
                path.add(new Positon(i, j++));
            }
        } else if (bishopPositon.getRank() < kingPositon.getRank()
                && bishopPositon.getIndex() > kingPositon.getIndex()) {
            // going upper left
            int j = bishopPositon.getIndex() - 1;
            for (int i = bishopPositon.getRank() + 1; i < kingPositon.getRank() && j > kingPositon.getIndex(); i++) {
                path.add(new Positon(i, j--));
            }
        } else if (bishopPositon.getRank() > kingPositon.getRank()
                && bishopPositon.getIndex() < kingPositon.getIndex()) {
            // going lower right
            int j = bishopPositon.getIndex() + 1;
            for (int i = bishopPositon.getRank() - 1; i > kingPositon.getRank() && j < kingPositon.getIndex(); i--) {
                path.add(new Positon(i, j++));
            }
        } else if (bishopPositon.getRank() > kingPositon.getRank()
                && bishopPositon.getIndex() > kingPositon.getIndex()) {
            // going lower left
            int j = bishopPositon.getIndex() - 1;
            for (int i = bishopPositon.getRank() - 1; i > kingPositon.getRank() && j > kingPositon.getIndex(); i--) {
                path.add(new Positon(i, j--));
            }
        }

        return path;
    }

    enum promotion {
        queen, knight, bishop, rook
    }

    public static void promotion(Board board, Positon promoteOn) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Piece to promote ( queen / knight / bishop / rook ) : ");
        promotion piece = null;

        try {
            piece = promotion.valueOf(scanner.nextLine().trim().toLowerCase());
        } catch (Exception e) {
            System.out.println("Invalid promotion piece name !!");
            Utils.promotion(board, promoteOn);
        }

        scanner.close();
        COLORS current = board.getPiece(promoteOn).getColor();

        try {

            if (piece == promotion.queen) {
                board.place(Utils.convertToSquareName(promoteOn), new Piece(PIECES.QUEEN, current));
            } else if (piece == promotion.bishop) {
                board.place(Utils.convertToSquareName(promoteOn), new Piece(PIECES.BISHOP, current));
            } else if (piece == promotion.knight) {
                board.place(Utils.convertToSquareName(promoteOn), new Piece(PIECES.KNIGHT, current));
            } else if (piece == promotion.rook) {
                board.place(Utils.convertToSquareName(promoteOn), new Piece(PIECES.ROOK, current));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // when in chek This function returns the pieces that can come between and
    // remove the chek
    public static List<Positon> piecesToComeBetween(Board board, Positon kingPositon,
            List<Positon> chekPath, COLORS color) throws InvalidPosition, IllegalMove {

        List<Positon> positons = new ArrayList<>();
        Attacking attacking = new Attacking();

        Map<Integer, List<Square>> innerMap = board.getMap();

        for (Map.Entry<Integer, List<Square>> rank : innerMap.entrySet()) {
            List<Square> currentRank = rank.getValue();

            for (int i = 0; i < currentRank.size(); i++) {
                Piece currentPiece = currentRank.get(i).getPiece();

                if (currentPiece != null && currentPiece.getColor() == color) {
                    if (currentPiece.getName() == PIECES.PAWN) {

                        ValidateMoves validateMoves = new ValidateMoves();
                        List<Positon> pawnPositons = validateMoves.pawn(board, new Positon(rank.getKey(), i));

                        boolean canComeBetween = false;
                        Positon pawnPositon = null;

                        for (Positon positon : pawnPositons) {
                            if (chekPath.contains(positon)) {
                                canComeBetween = true;
                                pawnPositon = positon;
                                break;
                            }
                        }

                        if (canComeBetween) {
                            board.move(board, new Positon(rank.getKey(), i), pawnPositon);
                            AttackingInfo newattackingInfo = Attacking.getAllAttackingPositions(board);
                            Set<Positon> opponentAttacking = color == COLORS.WHITE ? newattackingInfo.blackAttacking
                                    : newattackingInfo.whiteAttacking;

                            if (!opponentAttacking.contains(kingPositon)) {
                                positons.add(new Positon(rank.getKey(), i));
                            }

                            board.informalMove(pawnPositon, new Positon(rank.getKey(), i));
                        }

                    } else if (currentPiece.getName() == PIECES.ROOK) {
                        List<Positon> rookAttacking = attacking.rook(board, new Positon(rank.getKey(), i));

                        boolean canComeBetween = false;
                        Positon rookPositon = null;

                        for (Positon positon : rookAttacking) {
                            if (chekPath.contains(positon)) {
                                canComeBetween = true;
                                rookPositon = positon;
                                break;
                            }
                        }

                        if (canComeBetween) {
                            board.move(board, new Positon(rank.getKey(), i), rookPositon);
                            AttackingInfo newattackingInfo = Attacking.getAllAttackingPositions(board);
                            Set<Positon> opponentAttacking = color == COLORS.WHITE ? newattackingInfo.blackAttacking
                                    : newattackingInfo.whiteAttacking;

                            if (!opponentAttacking.contains(kingPositon)) {
                                positons.add(new Positon(rank.getKey(), i));
                            }

                            board.informalMove(rookPositon, new Positon(rank.getKey(), i));
                        }
                    } else if (currentPiece.getName() == PIECES.BISHOP) {
                        List<Positon> bishopAttacking = attacking.bishop(board, new Positon(rank.getKey(), i));
                        boolean canComeBetween = false;
                        Positon bishopPositon = null;

                        for (Positon positon : bishopAttacking) {
                            if (chekPath.contains(positon)) {
                                canComeBetween = true;
                                bishopPositon = positon;
                                break;
                            }
                        }

                        if (canComeBetween) {
                            board.move(board, new Positon(rank.getKey(), i), bishopPositon);
                            AttackingInfo newattackingInfo = Attacking.getAllAttackingPositions(board);
                            Set<Positon> opponentAttacking = color == COLORS.WHITE ? newattackingInfo.blackAttacking
                                    : newattackingInfo.whiteAttacking;

                            if (!opponentAttacking.contains(kingPositon)) {
                                positons.add(new Positon(rank.getKey(), i));
                            }

                            board.informalMove(bishopPositon, new Positon(rank.getKey(), i));
                        }
                    } else if (currentPiece.getName() == PIECES.KNIGHT) {
                        List<Positon> knightPositons = attacking.knight(board, new Positon(rank.getKey(), i));
                        boolean canComeBetween = false;
                        Positon knightPositon = null;

                        for (Positon positon : knightPositons) {
                            if (chekPath.contains(positon)) {
                                canComeBetween = true;
                                knightPositon = positon;
                                break;
                            }
                        }

                        if (canComeBetween) {
                            board.move(board, new Positon(rank.getKey(), i), knightPositon);
                            AttackingInfo newattackingInfo = Attacking.getAllAttackingPositions(board);
                            Set<Positon> opponentAttacking = color == COLORS.WHITE ? newattackingInfo.blackAttacking
                                    : newattackingInfo.whiteAttacking;

                            if (!opponentAttacking.contains(kingPositon)) {
                                positons.add(new Positon(rank.getKey(), i));
                            }

                            board.informalMove(knightPositon, new Positon(rank.getKey(), i));
                        }
                    } else if (currentPiece.getName() == PIECES.QUEEN) {
                        List<Positon> queenPositons = attacking.queen(board, new Positon(rank.getKey(), i));
                        boolean canComeBetween = false;
                        Positon queenPositon = null;

                        for (Positon positon : queenPositons) {
                            if (chekPath.contains(positon)) {
                                canComeBetween = true;
                                queenPositon = positon;
                                break;
                            }
                        }

                        if (canComeBetween) {
                            board.move(board, new Positon(rank.getKey(), i), queenPositon);
                            AttackingInfo newattackingInfo = Attacking.getAllAttackingPositions(board);
                            Set<Positon> opponentAttacking = color == COLORS.WHITE ? newattackingInfo.blackAttacking
                                    : newattackingInfo.whiteAttacking;

                            if (!opponentAttacking.contains(kingPositon)) {
                                positons.add(new Positon(rank.getKey(), i));
                            }

                            board.informalMove(queenPositon, new Positon(rank.getKey(), i));
                        }
                    }
                }
            }
        }

        return positons;
    }
}
