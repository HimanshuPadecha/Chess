public class Piece {
    private final PIECES name;
    private final COLORS type;

    public Piece(PIECES name, COLORS type) {
        this.name = name;
        this.type = type;
    }

    public PIECES getName() {
        return this.name;
    }

    public COLORS getColor() {
        return this.type;
    }

    @Override
    public String toString(){
        return String.valueOf(this.getColor()).charAt(0) + " : " + String.valueOf(this.getName());
    }
}
