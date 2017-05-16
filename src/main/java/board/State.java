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
import static java.lang.Character.toUpperCase;
import static org.apache.commons.lang3.StringUtils.split;

public class State {
    private static Board board;
    private static int moves;
    private static Color sideOnMove;
    public static boolean gameOver = false;

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

    public void move(Move move) {

        if (toUpperCase(board.getPiece(move.getToSquare().getX(), move.getToSquare().getY())) == 'K') {
            System.out.println(sideOnMove.toString() + " loses");
            finishedGame();
            return;
        }
        if (moves >= 40) {
            System.out.println("Game ends in draw");
            finishedGame();
        }
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
    }

    private void finishedGame() {
        printCurrentBoard();
        gameOver = true;
        return;
    }

    public void move(String value) {
        String[] squares = split(value, '-');
        if (squares.length != 2)
            throw new IllegalArgumentException("Move is invalid");
        move(validateMove(new Move(convertToSquare(squares[0]), convertToSquare(squares[1]))));
    }

    private Move validateMove(Move move) {
        List<Move> legalMoves = new ArrayList<Move>();
        generateMoveListForPiece(legalMoves, move.getFromSquare().getX(), move.getFromSquare().getY());
        for (Move legalMove : legalMoves) {
            final Square legalMoveToSquare = legalMove.getToSquare();
            final Square moveToSquare = move.getToSquare();
            if (legalMoveToSquare.getX() == moveToSquare.getX() && legalMoveToSquare.getY() == moveToSquare.getY())
                return move;
        }
        throw new IllegalArgumentException("Invalid move");

    }

    public List<Move> generateMoveList() {
        List<Move> moves = new ArrayList<Move>();
        for (int y = 0; y < board.getField().length; y++) {
            for (int x = 0; x < board.getField()[y].length; x++) {
                if (isMoversPiece(x, y))
                    generateMoveListForPiece(moves, x, y);
            }
        }
        return moves;
    }

    public void generateMoveListForPiece(List<Move> moves, int x, int y) {
        char p = toLowerCase(board.getPiece(x, y));
        switch (p) {
            case 'q': {
                symmscan(moves, x, y, 0, 1, false, TRUE);
                symmscan(moves, x, y, 1, 1, false, TRUE);
                break;
            }
            case 'k': {
                symmscan(moves, x, y, 0, 1, true, TRUE);
                symmscan(moves, x, y, 1, 1, true, TRUE);
                break;
            }
            case 'r': {
                symmscan(moves, x, y, 0, 1, false, TRUE);
                break;
            }
            case 'b': {
                symmscan(moves, x, y, 0, 1, true, FALSE);
                symmscan(moves, x, y, 1, 1, false, TRUE);
                break;
            }
            case 'n': {
                symmscan(moves, x, y, 1, 2, true, TRUE);
                symmscan(moves, x, y, -1, 2, true, TRUE);
                break;
            }
            case 'p': {
                int dir = getPieceColor(x, y).equals(BLACK) ? -1 : 1;
                scan(moves, x, y, -1, dir, true, ONLY);
                scan(moves, x, y, 1, dir, true, ONLY);
                scan(moves, x, y, 0, dir, true, FALSE);
                break;
            }
            default:
                break;
        }
    }

    public void symmscan(List<Move> moves, int x, int y, int dx, int dy, boolean stopShort, Capture capture) {
        for (int i = 0; i < 4; i++) {
            scan(moves, x, y, dx, dy, stopShort, capture);
            int tmp = dx;
            dx = dy;
            dy = -tmp;
        }
    }

    public void scan(List<Move> moves, int x, int y, int dx, int dy, boolean stopShort, Capture capture) {
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
        } while (!stopShort);
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

    public static Board getBoard() {
        return board;
    }

    public static int getMoves() {
        return moves;
    }

    public static Color getSideOnMove() {
        return sideOnMove;
    }
}
