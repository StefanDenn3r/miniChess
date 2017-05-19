package board;

public class Square {
    private int x;
    private int y;

    Square(int x, int y) {
        if (x >= 0 && x <= 4 && y >= 0 && y <= 5) {
            this.x = x;
            this.y = y;
        } else
            throw new IllegalArgumentException("x or y not a valid number");
    }

    static Square convertToSquare(String string) {
        return new Square(string.charAt(0) - 97, string.charAt(1) - 49);
    }

    @Override
    public String toString() {
        return (char) (x + 97) + "" + (y + 1);
    }

    int getX() {
        return x;
    }


    int getY() {
        return y;
    }
}
