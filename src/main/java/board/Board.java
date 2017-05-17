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

    public char[][] getField() {
        return field;
    }

    public void setField(char[][] field) {
        this.field = field;
    }

    public char getPiece(int x, int y) {
        return field[y][x];
    }

    public void setPiece(int x, int y, char c) {
        field[y][x] = c;
    }

    @Deprecated
    public static char[][] deepCopyField(char[][] original) {
        if (original == null) {
            return null;
        }

        final char[][] result = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
            // For Java versions prior to Java 6 use the next:
            // System.arraycopy(original[i], 0, result[i], 0, original[i].length);
        }
        return result;
    }
}
