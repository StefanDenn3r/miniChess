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

    Square getToSquare() {
        return toSquare;
    }

    Square getFromSquare() {
        return fromSquare;
    }
}
