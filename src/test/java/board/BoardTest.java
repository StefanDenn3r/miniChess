package board;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by std on 15.05.2017.
 */
public class BoardTest {
    @Test
    public void getPiece() throws Exception {
        Board board = new Board();
        assertEquals(board.getPiece(0, 0), 'k');
        assertEquals(board.getPiece(4, 5), 'K');
    }
}