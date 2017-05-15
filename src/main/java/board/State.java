package board;

import java.util.ArrayList;
import java.util.List;

import static board.Capture.FALSE;
import static board.Capture.ONLY;
import static board.Capture.TRUE;
import static board.Color.BLACK;
import static board.Color.WHITE;
import static board.Square.convertToSquare;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;
import static java.lang.Character.toLowerCase;
import static org.apache.commons.lang3.StringUtils.split;

public class State {
    private static Board board;
    private static int moves;
    private static Color sideOnMove;

    public State() {
        getInitialState();
    }

    private void getInitialState() {
        moves = 1;
        sideOnMove = WHITE;
        board = new Board();
    }

    public void printCurrentBoard() {
        if (board == null) {
            getInitialState();
        }
        System.out.println(moves + " " + sideOnMove);
        System.out.println(board.toString());
    }

    public State move(Move move) {
        moveIsValid(move);

        final Square fromSquare = move.getFromSquare();
        final int fromSquareX = fromSquare.getX();
        final int fromSquareY = fromSquare.getY();
        char tmp = board.getPiece(fromSquareX, fromSquareY);
        board.setPiece(fromSquareX, fromSquareY, '.');

        final Square toSquare = move.getToSquare();
        final int toSquareX = toSquare.getX();
        final int toSquareY = toSquare.getY();
        board.setPiece(toSquareX, toSquareY, tmp);

        changeSideOnMove();
        return this;
    }

    public State move(String value) {
        String[] squares = split(value, '-');
        if (squares.length != 2)
            throw new IllegalArgumentException("Move is invalid");
        return move(new Move(convertToSquare(squares[0]), convertToSquare(squares[1])));
    }

    public List<Move> generateMoveList(int x, int y) {
        List<Move> moves = new ArrayList<Move>();
        char p = toLowerCase(board.getPiece(x, y));
        switch (p) {
            case 'q': {
                moves.addAll(symmscan(x, y, 0, 1, false, TRUE));
                moves.addAll(symmscan(x, y, 1, 1, false, TRUE));
                return moves;
            }
            case 'k': {
                moves.addAll(symmscan(x, y, 0, 1, true, TRUE));
                moves.addAll(symmscan(x, y, 1, 1, true, TRUE));
                return moves;
            }
            case 'r': {
                moves.addAll(symmscan(x, y, 0, 1, false, TRUE));
                return moves;
            }
            case 'b': {
                moves.addAll(symmscan(x, y, 0, 1, false, TRUE));
                moves.addAll(symmscan(x, y, 1, 1, false, TRUE));
                return moves;
            }
            case 'n': {
                moves.addAll(symmscan(x, y, 1, 2, true, TRUE));
                moves.addAll(symmscan(x, y, -1, 2, true, TRUE));
                return moves;
            }
            case 'p': {
                int dir = getPieceColor(x, y).equals(BLACK) ? -1 : 1;
                moves.addAll(scan(x, y, -1, dir, true, ONLY));
                moves.addAll(scan(x, y, 1, dir, true, ONLY));
                moves.addAll(scan(x, y, 0, dir, true, FALSE));
                return moves;
            }
            default:
                return moves;
        }
    }

    public List<Move> symmscan(int x, int y, int dx, int dy, boolean stopShort, Capture capture) {
        List<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < 4; i++) {
            moves.addAll(scan(x, y, dx, dy, stopShort, capture));
            int tmp = dx;
            dx = dy;
            dy = -tmp;
        }
        return moves;
    }

    public List<Move> scan(int x, int y, int dx, int dy, boolean stopShort, Capture capture) {
        List<Move> moves = new ArrayList<Move>();
        int x0 = x;
        int y0 = y;
        do {
            x = x + dx;
            y = y + dy;
            if (!isInBounds(x, y))
                break;
            if (isOccupied(x, y)) {
                if (isMoversPiece(x, y))
                    break;
                if (capture.equals(FALSE))
                    break;
                stopShort = true;
            } else if (capture.equals(ONLY))
                break;
            moves.add(new Move(new Square(x0, y0), new Square(x, y)));
        } while (stopShort);
        return null;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x <= 4 && y >= 0 && y <= 5;
    }

    public boolean isOccupied(int x, int y) {
        return board.getPiece(x, y) != '.';
    }


    private void changeSideOnMove() {
        if (sideOnMove.equals(WHITE)) {
            sideOnMove = BLACK;
        } else {
            sideOnMove = WHITE;
            moves++;
        }
    }

    public void moveIsValid(Move move) {
        final Square fromSquare = move.getFromSquare();
        final int x = fromSquare.getX();
        final int y = fromSquare.getY();
        if (!isMoversPiece(x, y))
            throw new IllegalStateException("Move is invalid");
    }

    private boolean isMoversPiece(int x, int y) {
        return isUpperCase(board.getPiece(x, y)) && sideOnMove == WHITE || isLowerCase(board.getPiece(x, y)) && sideOnMove == BLACK;
    }

    private Color getPieceColor(int x, int y) {
        if (isOccupied(x, y)) {
            if (isUpperCase(board.getPiece(x, y)))
                return WHITE;
            else return BLACK;
        } else throw new IllegalArgumentException("Field isn't occupied");
    }
}
