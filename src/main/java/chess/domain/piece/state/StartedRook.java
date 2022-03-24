package chess.domain.piece.state;

import java.util.ArrayList;
import java.util.List;

import chess.domain.ChessBoard;
import chess.domain.piece.position.Position;
import chess.domain.piece.position.Direction;
import chess.domain.piece.Piece;

public class StartedRook implements State{

    @Override
    public List<Position> getMovablePaths(Position source, ChessBoard board) {

        List<Position> list = new ArrayList<>();
        list.addAll(getPositions(source, board, Direction.Up));
        list.addAll(getPositions(source, board, Direction.Right));
        list.addAll(getPositions(source, board, Direction.Down));
        list.addAll(getPositions(source, board, Direction.Left));

        return list;
    }

    private List<Position> getPositions(Position source, ChessBoard board, Direction direction) {
        List<Position> list = new ArrayList<>();

        Position currentPosition = source;
        Position nextPosition = currentPosition.getNext(direction); //움직인것

        while ((currentPosition != nextPosition) && (!board.isFilled(nextPosition))) {
            list.add(nextPosition);
            currentPosition = nextPosition;
            nextPosition = currentPosition.getNext(direction);
        }

        if (canKill(source, nextPosition, board)) {
            list.add(nextPosition);
        }

        return list;
    }

    private boolean canKill(Position source, Position target, ChessBoard board) {
        return board.isFilled(target) && isDifferentColor(board.getPiece(source), board.getPiece(target));
    }

    private boolean isDifferentColor(Piece sourcePiece, Piece targetPiece) {
        return !sourcePiece.isSameColor(targetPiece);
    }

    @Override
    public State killed() {
        return new Dead();
    }

    @Override
    public State updateState() {
        return new StartedRook();
    }
}
