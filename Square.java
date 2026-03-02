public class Square {
    Piece piece;
    // String position;

    public Square() {
        this.piece = null;
    }

    public Square(Piece piece) {
        this.piece = piece;
    }

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    public Piece getPiece(){
        return this.piece;
    }

    @Override
    public String toString() {
        return this.piece == null ? "   .   "
                : String.valueOf(this.piece.getColor()).charAt(0) + " : " + String.valueOf(this.piece.getName());
    }
}
