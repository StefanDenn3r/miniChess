package board;

import java.util.Arrays;

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

    char[][] getField() {
        return field;
    }

    void setField(char[][] field) {
        this.field = field;
    }

    char getPiece(int x, int y) {
        return field[y][x];
    }

    void setPiece(int x, int y, char c) {
        field[y][x] = c;
    }

    static char[][] deepCopyField(char[][] original) {
        if (original == null) {
            return null;
        }

        final char[][] result = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }
}
