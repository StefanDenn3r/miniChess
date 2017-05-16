package board;


public class Move {
    private Square fromSquare;
    private Square toSquare;

    public Move(Square fromSquare, Square toSquare) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    @Override
    public String toString() {
        return fromSquare.toString() + "-" + toSquare.toString();
    }

    public Square getToSquare() {
        return toSquare;
    }

    public void setToSquare(Square toSquare) {
        this.toSquare = toSquare;
    }

    public Square getFromSquare() {
        return fromSquare;
    }

    public void setFromSquare(Square fromSquare) {
        this.fromSquare = fromSquare;
    }
}
