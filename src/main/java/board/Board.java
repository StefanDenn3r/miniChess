package board;

public class Board {
    private char[][] field = {
            {'R', 'N', 'B', 'Q', 'K'},
            {'P', 'P', 'P', 'P', 'P'},
            {'.', '.', '.', '.', '.'},
            {'.', '.', '.', '.', '.'},
            {'p', 'p', 'p', 'p', 'p'},
            {'k', 'q', 'b', 'n', 'r'}
    };

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = field.length - 1; i >= 0; i--) {
            for (char coordinate : field[i]) {
                string.append(coordinate).append(" ");
            }
            string.append('\n');
        }
        return string.toString();
    }

    public char getPiece(int x, int y) {
        return field[y][x];
    }

    public void setPiece(int x, int y, char c) {
        field[y][x] = c;
    }
}
