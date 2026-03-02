import java.util.Objects;

public class Positon {
    private Integer rank;
    private Integer index;

    public Positon(int rank, int index) {
        this.rank = rank;
        this.index = index;
    }

    public int getRank() {
        return this.rank;
    }

    public int getIndex() {
        return this.index;
    }

    public void validate() throws InvalidPosition {
        if (this.index < 0 || this.index > Board.CHESS_CONSTANT - 1) {
            throw new InvalidPosition("Invalid Position");
        }

        if (this.rank < 1 || this.rank > Board.CHESS_CONSTANT) {
            throw new InvalidPosition("Invalid Position");
        }
    }

    @Override
    public boolean equals(Object positon) {
        if (positon == null)
            return false;

        if (!(positon instanceof Positon))
            return false;

        Positon pos = (Positon) positon;

        return Objects.equals(this.rank, pos.getRank()) &&
                Objects.equals(this.index, pos.getIndex());
    }

    @Override
    public String toString() {
        try {
            return Utils.convertToSquareName(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.rank, this.index);
    }
}
