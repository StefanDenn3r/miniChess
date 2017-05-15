package board;

/**
 * Created by std on 15.05.2017.
 */
public class Board {
    char[][] field = {
            {'k', 'q', 'b', 'n', 'r' },
            {'p', 'p', 'p', 'p', 'p' },
            {'.', '.', '.', '.', '.' },
            {'.', '.', '.', '.', '.' },
            {'P', 'P', 'P', 'P', 'P' },
            {'R', 'N', 'B', 'Q', 'K' }
    };

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (char[] row : field) {
            for (char coordinate : row) {
                string.append(coordinate).append(" ");
            }
            string.append('\n');
        }
        return string.toString();
    }
}
