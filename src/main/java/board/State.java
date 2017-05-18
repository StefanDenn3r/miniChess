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
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.random;
import static org.apache.commons.lang3.StringUtils.split;

public class State {
    private Board board;

    private int moves;
    private Color sideOnMove;
    public boolean gameOver = false;
    public Color winner;
    private boolean isDraw;
    private static long startTime;
    public static long timeLimit;
    public static int iterator = 0;
    public static Color staticSideOnMove;


    public State() {
        getInitialState();
    }

    private void getInitialState() {
        moves = 1;
        sideOnMove = WHITE;
        board = new Board();
        timeLimit = 3000;
    }

    public void printCurrentBoard() {
        if (board == null) {
            getInitialState();
        }
        System.out.println(moves + " " + sideOnMove);
        System.out.println(board.toString());
    }

    public Move move(Move move) {

        if (toUpperCase(board.getPiece(move.getToSquare().getX(), move.getToSquare().getY())) == 'K') {
            gameOver = true;
            winner = sideOnMove;
        } else if (moves >= 40) {
            printCurrentBoard();
            gameOver = true;
            isDraw = true;
            System.out.println("Game ends in draw");
        }
        moveIsValid(move);

        final Square fromSquare = move.getFromSquare();
        final int fromSquareX = fromSquare.getX();
        final int fromSquareY = fromSquare.getY();
        char tmp = board.getPiece(fromSquareX, fromSquareY);

        final Square toSquare = move.getToSquare();
        final int toSquareX = toSquare.getX();
        final int toSquareY = toSquare.getY();
        if (toUpperCase(board.getPiece(fromSquareX, fromSquareY)) == 'P' && (toSquareY == 0 || toSquareY == 5)) {
            board.setPiece(toSquareX, toSquareY, (char) (tmp + 1));
        } else {
            board.setPiece(toSquareX, toSquareY, tmp);
        }
        board.setPiece(fromSquareX, fromSquareY, '.');

        changeSideOnMove();
        return move;
    }

    public Move move(String value) {
        String[] squares = split(value, '-');
        if (squares.length != 2)
            throw new IllegalArgumentException("Move is invalid");
        return move(validateMove(new Move(convertToSquare(squares[0]), convertToSquare(squares[1]))));
    }

    private Move validateMove(Move move) {
        final Square moveToSquare = move.getToSquare();
        List<Move> legalMoves = new ArrayList<Move>();
        generateMoveListForPiece(legalMoves, move.getFromSquare().getX(), move.getFromSquare().getY());
        for (Move legalMove : legalMoves) {
            final Square legalMoveToSquare = legalMove.getToSquare();
            if (legalMoveToSquare.getX() == moveToSquare.getX() && legalMoveToSquare.getY() == moveToSquare.getY())
                return move;
        }
        throw new IllegalArgumentException("Move is invalid");
    }

    public Move iterativeDeepening() {
//        if (moves <= 4){
//            timeLimit = 5000;
//        } else if(moves <= 12){
//            timeLimit = 8000;
//        } else if(moves <= 30){
//            timeLimit = 12000;
//        } else if(moves <= 40){
//            timeLimit = 5000;
//        }
        //TODO: UMBEDINGT ÄNDERN!
        int depth = 6;
        Move m = calculateBestMove();
        while (true) {
            Move tmpMove;
            try {
                tmpMove = calculateBest(depth);
            } catch (InterruptedException e) {
                System.out.println(depth);
                iterator = 0;
                return m;
            }
            m = tmpMove;
            depth += 1;
        }
    }

    public Move calculateBest(int depth) throws InterruptedException {
        staticSideOnMove = this.sideOnMove;
        List<Move> bestMoves = new ArrayList<Move>();
        Integer bestScore = MIN_VALUE;
        for (Move move : this.generateMoveList()) {
            int tmpScore = -abPruning(this, depth, move, MIN_VALUE, MAX_VALUE);
            if (bestScore <= tmpScore) {
                if (bestScore == tmpScore)
                    bestMoves.add(move);
                else {
                    bestMoves.clear();
                    bestMoves.add(move);
                }
                bestScore = tmpScore;
            }
        }
        return bestMoves.get((int) (bestMoves.size() * random()));
    }

    public int abPruning(State state, int depth, Move move, int a, int b) throws InterruptedException {
        if (iterator == 0) {
            startTime = System.currentTimeMillis();
        }
        if (iterator % 10000 == 0) {
            if (System.currentTimeMillis() - startTime >= timeLimit)
                throw new InterruptedException("time is over");
        }
        iterator++;

        State tmpState = new State();
        tmpState.board.setField(state.board.deepCopyField(state.board.getField()));
        tmpState.sideOnMove = state.sideOnMove;
        tmpState.move(move);
        List<Move> generatedMoveList = tmpState.generateMoveList();
        if (depth == 0 || tmpState.winner != null || generatedMoveList == null) {
            return tmpState.pointScore();
        }
        int s;
        int bestScore = MIN_VALUE;
        for (Move tmpMove : generatedMoveList) {
            if (tmpState.winner != null) {
                s = tmpState.pointScore();
            } else s = -abPruning(tmpState, depth - 1, tmpMove, -b, -a);
            if (s >= b) return s;
            if (s > a) a = s;
            if (s > bestScore) bestScore = s;
        }
        return bestScore;
    }

    private int negamax(State state, int depth, Move move) throws InterruptedException {
        State tmpState = new State();
        tmpState.board.setField(state.board.deepCopyField(state.board.getField()));
        tmpState.sideOnMove = state.sideOnMove;
        tmpState.move(move);

        if (depth == 0 || tmpState.winner != null && tmpState.winner.equals(staticSideOnMove)) {
            return tmpState.pointScore();
        }
        Integer bestValue = MIN_VALUE;
        for (Move tmpMove : tmpState.generateMoveList()) {
            int v = -negamax(tmpState, depth - 1, tmpMove);
            bestValue = max(bestValue, v);
        }
        return bestValue;
    }


    public Move calculateBestMove() {
        List<Move> moves = generateMoveList();
        Move bestMove = null;
        int bestScore = MIN_VALUE;
        if (moves != null) {
            for (Move move : moves) {
                State tmpState = new State();
                tmpState.board.setField(this.board.deepCopyField(this.board.getField()));
                tmpState.sideOnMove = this.sideOnMove;
                if (toUpperCase(tmpState.board.getPiece(move.getToSquare().getX(), move.getToSquare().getY())) == 'K') {
                    return move;
                }
                tmpState.move(move);
                int tmpScore = tmpState.pointScore();
                if (bestScore < tmpScore) {
                    bestMove = move;
                    bestScore = tmpScore;
                }
            }
        }
        return bestMove;
    }

    public List<Move> generateMoveList() {
        List<Move> moves = new ArrayList<Move>();
        for (int y = 0; y < board.getField().length; y++) {
            for (int x = 0; x < board.getField()[y].length; x++) {
                if (isMoversPiece(x, y))
                    generateMoveListForPiece(moves, x, y);
            }
        }
        if (moves.isEmpty()) {
            return null;
        }
        return moves;
    }

    void generateMoveListForPiece(List<Move> moves, int x, int y) {
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

    private void symmscan(List<Move> moves, int x, int y, int dx, int dy, boolean stopShort, Capture capture) {
        for (int i = 0; i < 4; i++) {
            scan(moves, x, y, dx, dy, stopShort, capture);
            int tmp = dx;
            dx = dy;
            dy = -tmp;
        }
    }

    private void scan(List<Move> moves, int x, int y, int dx, int dy, boolean stopShort, Capture capture) {
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

    private boolean isOccupied(int x, int y) {
        return board.getPiece(x, y) != '.';
    }

    /**
     * positiv = advantage for side on move
     *
     * @return
     */
    int pointScore() {
        int score = 0;
        for (char[] row : board.getField()) {
            for (char c : row) {
                if (isUpperCase(c)) {
                    // If its white add to score
                    score += getPieceScore(toLowerCase(c));
                }
                if (isLowerCase(c)) {
                    // If its black sub from score
                    score -= getPieceScore(c);
                }
            }
        }
        if (sideOnMove.equals(BLACK))
            return -score;
        return score;
    }

    private int getPieceScore(char c) {
        switch (c) {
            case 'p': {
                return 10;
            }
            case 'n':
            case 'b': {
                return 30;
            }
            case 'r': {
                return 50;
            }
            case 'q': {
                return 90;
            }
            case 'k': {
                return 1000;
            }
            default:
                return 0;
        }
    }

    private void changeSideOnMove() {
        if (sideOnMove.equals(WHITE)) {
            sideOnMove = BLACK;
        } else {
            sideOnMove = WHITE;
            moves++;
        }
    }


    private void moveIsValid(Move move) {
        final Square fromSquare = move.getFromSquare();
        final int x = fromSquare.getX();
        final int y = fromSquare.getY();
        if (!isMoversPiece(x, y))

            throw new IllegalStateException("Is not movers piece");
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

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getMoves() {
        return moves;
    }

    public Color getSideOnMove() {
        return sideOnMove;
    }
}
