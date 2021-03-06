package board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static board.Board.deepCopyField;
import static board.Capture.*;
import static board.Color.BLACK;
import static board.Color.WHITE;
import static board.Square.convertToSquare;
import static java.lang.Character.*;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.max;
import static java.lang.Math.random;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.split;

public class State {
    private Board board;

    private int moves;
    private Color sideOnMove;
    public boolean gameOver = false;
    public Color winner;
    private static long startTime;
    private static long timeLimit;
    private static int iterator = 0;
    private static Color staticSideOnMove;


    public State() {
        getInitialState();
    }

    private void getInitialState() {
        moves = 1;
        sideOnMove = WHITE;
        board = new Board();
        timeLimit = 6000;
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
        if (squares.length != 2) {
            throw new IllegalArgumentException("Move is invalid");
        }
        return move(validateMove(new Move(convertToSquare(squares[0]), convertToSquare(squares[1]))));
    }

    private Move validateMove(Move move) {
        final Square moveToSquare = move.getToSquare();
        List<Move> legalMoves = new ArrayList<>();
        generateMoveListForPiece(legalMoves, move.getFromSquare().getX(), move.getFromSquare().getY());
        for (Move legalMove : legalMoves) {
            final Square legalMoveToSquare = legalMove.getToSquare();
            if (legalMoveToSquare.getX() == moveToSquare.getX() && legalMoveToSquare.getY() == moveToSquare.getY()) {
                return move;
            }
        }
        throw new IllegalArgumentException("Move is invalid");
    }

    public Move calculateBestWithABPruning() {
        int depth = 4;
        Move m = calculateBestMove();
        while (true) {
            Move tmpMove;
            try {
                tmpMove = calculateBestWithAb(depth);
            } catch (InterruptedException e) {
                System.out.println(depth);
                iterator = 0;
                return m;
            }
            m = tmpMove;
            depth += 1;
        }
    }

    public Move calculateBestWithNegamax(int depth) {
        final int depthForThread = depth;
        final State stateForThread = this;
        staticSideOnMove = this.sideOnMove;
        List<Move> moves = this.generateMoveList();
        List<Move> bestMoves = new ArrayList<>();
        Integer bestScore = MAX_VALUE;
        ExecutorService service = Executors.newCachedThreadPool();
        Map<Move, Future<Integer>> results = moves.stream()
                                                  .collect(toMap(moveForThread -> moveForThread,
                                                                 moveForThread -> service.submit(() -> negamax(
                                                                         stateForThread,
                                                                         depthForThread,
                                                                         moveForThread)),
                                                                 (a, b) -> b));

        int counter = results.size() - 1;
        while (counter >= 0) {
            Map<Move, Future<Integer>> copy = new HashMap<>(results);
            for (Map.Entry<Move, Future<Integer>> value : copy.entrySet()) {
                if (results.get(value.getKey()).isDone()) {
                    Integer tmpScore = null;
                    try {
                        tmpScore = results.get(value.getKey()).get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (bestScore >= tmpScore) {
                        if (bestScore.compareTo(tmpScore) == 0) {
                            bestMoves.add(value.getKey());
                        } else {
                            bestMoves.clear();
                            bestMoves.add(value.getKey());
                        }
                        bestScore = tmpScore;
                    }
                    counter = counter - 1;
                    results.remove(value.getKey());
                }
            }
        }
        return bestMoves.get((int) (bestMoves.size() * random()));
    }

    private Move calculateBestWithAb(int depth) throws InterruptedException {
        staticSideOnMove = this.sideOnMove;
        List<Move> moves = this.generateMoveList();
        List<Move> bestMoves = new ArrayList<>();
        int bestScore = MIN_VALUE;
        for (Move move : moves) {
            if (moveCapturesEnemyKing(move)) return move;
            int tmpScore = abPruning(this, move, depth, MIN_VALUE, MAX_VALUE, -1);
            if (bestScore <= tmpScore) {
                if (bestScore == tmpScore) {
                    bestMoves.add(move);
                } else {
                    bestMoves.clear();
                    bestMoves.add(move);
                }
                bestScore = tmpScore;
            }
        }
        return bestMoves.get((int) (bestMoves.size() * random()));
    }

    private int abPruning(State state, Move move, int depth, int a, int b, int color) throws InterruptedException {
        if (iterator == 0) {
            startTime = System.currentTimeMillis();
        }
        if (iterator % 10000 == 0) {
            if (System.currentTimeMillis() - startTime >= timeLimit) {
                throw new InterruptedException("time is over");
            }
        }
        iterator++;
        State tmpState = preprocessField(state, move);

        int bestScore = MIN_VALUE;

        final List<Move> moveList = tmpState.generateMoveList();
        if (depth == 0 || moveList == null || tmpState.winner != null && tmpState.winner.equals(staticSideOnMove)) {
            return tmpState.pointScore() * color;
        }

        for (Move tmpMove : moveList) {
            int tmpBestScore = -abPruning(tmpState, tmpMove, depth - 1, -b, -a, -color);
            bestScore = max(tmpBestScore, bestScore);
            a = max(a, tmpBestScore);
            if (a >= b) {
                break;
            }
        }
        return bestScore;
    }

    private Integer negamax(State state, int depth, Move move) {
        State tmpState = preprocessField(state, move);

        if (depth == 0 || tmpState.generateMoveList() == null || tmpState.winner != null && tmpState.winner.equals(
                staticSideOnMove)) {
            return tmpState.pointScore();
        }
        int bestValue = MIN_VALUE;
        for (Move tmpMove : tmpState.generateMoveList()) {
            int v = -negamax(tmpState, depth - 1, tmpMove);
            bestValue = max(bestValue, v);
        }
        return bestValue;
    }

    private State preprocessField(State state, Move move) {
        State tmpState = new State();
        tmpState.board.setField(deepCopyField(state.board.getField()));
        tmpState.sideOnMove = state.sideOnMove;
        tmpState.move(move);
        return tmpState;
    }


    public Move calculateBestMove() {
        List<Move> moves = generateMoveList();
        Move bestMove = null;
        int bestScore = MAX_VALUE;
        if (moves != null) {
            for (Move move : moves) {
                State tmpState = new State();
                tmpState.board.setField(deepCopyField(this.board.getField()));
                tmpState.sideOnMove = this.sideOnMove;
                if (toUpperCase(tmpState.board.getPiece(move.getToSquare().getX(), move.getToSquare().getY())) == 'K') {
                    return move;
                }
                tmpState.move(move);
                int tmpScore = tmpState.pointScore();
                if (bestScore > tmpScore) {
                    bestMove = move;
                    bestScore = tmpScore;
                }
            }
        }
        return bestMove;
    }

    public List<Move> generateMoveList() {
        List<Move> moves = new ArrayList<>();
        for (int y = 0; y < board.getField().length; y++) {
            for (int x = 0; x < board.getField()[y].length; x++) {
                if (isMoversPiece(x, y)) {
                    generateMoveListForPiece(moves, x, y);
                }
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
            if (!isInBounds(x, y)) {
                break;
            }
            if (isOccupied(x, y)) {
                if (isMoversPiece(x, y)) {
                    break;
                }
                if (capture.equals(FALSE)) {
                    break;
                }
                stopShort = true;
            } else if (capture.equals(ONLY)) {
                break;
            }
            moves.add(new Move(new Square(x0, y0), new Square(x, y)));
        }
        while (!stopShort);
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x <= 4 && y >= 0 && y <= 5;
    }

    private boolean isOccupied(int x, int y) {
        return board.getPiece(x, y) != '.';
    }

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
        if (sideOnMove.equals(BLACK)) {
            return -score;
        }
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
        if (!isMoversPiece(x, y)) {
            throw new IllegalStateException("Is not movers piece");
        }
    }

    private boolean isMoversPiece(int x, int y) {
        return isUpperCase(board.getPiece(x, y)) && sideOnMove == WHITE || isLowerCase(board.getPiece(x,
                                                                                                      y)) && sideOnMove == BLACK;
    }

    private Color getPieceColor(int x, int y) {
        if (isOccupied(x, y)) {
            if (isUpperCase(board.getPiece(x, y))) {
                return WHITE;
            } else {
                return BLACK;
            }
        } else {
            throw new IllegalArgumentException("Field isn't occupied");
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    private boolean moveCapturesEnemyKing(Move move) {
        if (staticSideOnMove.equals(Color.WHITE)) {
            return this.board.getPiece(move.getToSquare().getX(), move.getToSquare().getY()) == 'k';
        } else {
            return this.board.getPiece(move.getToSquare().getX(), move.getToSquare().getY()) == 'K';
        }
    }
}
