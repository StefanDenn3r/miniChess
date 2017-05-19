package board;

import org.junit.Test;

import static org.junit.Assert.*;


public class BoardTest {
    @Test
    public void getPiece() throws Exception {
        Board board = new Board();
        assertEquals(board.getPiece(0, 0), 'R');
        assertEquals(board.getPiece(4, 5), 'r');
    }
}