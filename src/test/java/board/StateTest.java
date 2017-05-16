package board;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;

public class StateTest {
    @Rule
    public ExpectedException expectedException = none();

    @Test
    public void printCurrentBoard() throws Exception {
        State state = new State();
        state.printCurrentBoard();
    }

    @Test
    public void testMoveIsValid() throws Exception {
        State state = new State();
        state.move(new Move(new Square(4, 1), new Square(1, 2)));
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Move is invalid");
        state.move(new Move(new Square(4, 1), new Square(1, 2)));
    }

    @Test
    public void testMove() throws Exception {
        State state = new State();
        state.move("a2-a3");
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Move is invalid");
        state.move("a2-a3");
    }

    @Test
    public void testMoveTranslator() throws Exception {
        State state = new State();
        state.move("a2-a3");
        state.printCurrentBoard();
        state.move("a5-a4");
        state.printCurrentBoard();
        state.move("b2-b3");
        state.printCurrentBoard();
        state.move("b5-b4");
        state.printCurrentBoard();
    }

    @Test
    public void testMoveKing() throws Exception {
        State state = new State();
        List<Move> moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 4, 0);
        Assert.assertEquals(true, moves.isEmpty());
    }

    @Test
    public void testMovePawn() throws Exception {
        State state = new State();
        List<Move> moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 4, 1);
        Assert.assertEquals(1, moves.size());
    }

    @Test
    public void testMoveKnight() throws Exception {
        State state = new State();
        List<Move> moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 1, 0);
        Assert.assertEquals(2, moves.size());
    }

    @Test
    public void testMoveRook() throws Exception {
        State state = new State();
        List<Move> moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 0, 0);
        Assert.assertEquals(0, moves.size());
    }

    @Test
    public void testPlayChess() throws Exception {
        State state = new State();
        List<Move> moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 1,0);
        state.move(moves.get(0));
        state.printCurrentBoard();
        moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 0, 4);
        state.move(moves.get(0));
        state.printCurrentBoard();
        moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 0, 0);
        Assert.assertEquals(1, moves.size());
        moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 2, 2);
        Assert.assertEquals(5, moves.size());
        state.move(moves.get(1));
        state.printCurrentBoard();
        state.move("a4-a3");
        state.printCurrentBoard();
    }

    @Test
    public void testMoveGenerator(){
        State state = new State();
        assertEquals(7, state.generateMoveList().size());
    }

    @Test
    public void convertPawnToQueen(){
        State state = new State();
        char[][] field = {
                {'.', '.', 'B', 'Q', 'K'},
                {'.', 'p', 'P', 'P', 'P'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'p', '.', 'p', 'p', 'p'},
                {'k', 'q', 'b', 'n', 'r'}
        };
        state.getBoard().setField(field);
        state.printCurrentBoard();
        state.move("c2-c3");
        state.printCurrentBoard();
        System.out.println("test");
        state.move("b2-b1");
        state.printCurrentBoard();
    }

    @Test
    public void testConvertColorOfBishop(){
        State state = new State();
        char[][] field = {
                {'.', '.', 'B', 'Q', 'K'},
                {'.', 'p', 'P', 'P', 'P'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'p', '.', 'p', 'p', 'p'},
                {'k', 'q', 'b', 'n', 'r'}
        };
        state.getBoard().setField(field);
        state.printCurrentBoard();
        state.move("c1-b1");
        state.printCurrentBoard();
    }

    @Test
    public void testUnableToMove(){
        State state = new State();
        char[][] field = {
                {'.', '.', '.', '.', '.'},
                {'.', 'p', '.', '.', '.'},
                {'.', 'P', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'},
                {'.', '.', '.', '.', '.'}
        };
        state.getBoard().setField(field);
        List<Move> moves = new ArrayList<Move>();
        state.generateMoveListForPiece(moves, 1, 3);
        Assert.assertEquals(0, moves.size());
    }

    @Test
    public void testSimulateSkirmishError(){
        State state = new State();
        char[][] field = {
                {'R', 'N', '.', 'Q', 'K'},
                {'.', 'B', '.', '.', 'P'},
                {'.', 'P', '.', 'P', '.'},
                {'b', 'p', 'P', 'p', '.'},
                {'p', '.', 'p', '.', 'p'},
                {'k', 'q', 'r', 'n', '.'}
        };
        state.getBoard().setField(field);
        state.printCurrentBoard();
        state.move("d1-c2");
        state.printCurrentBoard();
        state.move("d6-b5");
        state.printCurrentBoard();
    }
}