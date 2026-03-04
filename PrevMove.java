public class PrevMove {
    private PIECES piece;
    private Positon to;

    PrevMove(PIECES pieces, Positon to) {
        this.piece = pieces;
        this.to = to;
    }

    public void setTo(Positon to){
        this.to = to;
    }
    public void setPiece(PIECES piece){
        this.piece = piece;
    }

    public Positon getTo(){
        return this.to;
    }

    public PIECES getPiece(){
        return this.piece;
    }

}
