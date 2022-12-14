package chess.domain.piece;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import chess.domain.game.state.ChessBoard;
import chess.domain.game.state.position.File;
import chess.domain.game.state.position.Position;
import chess.domain.game.state.position.Rank;
import chess.domain.piece.property.Color;

public class RookTest {

    private ChessBoard board;
    private Piece sourcePiece;

    @BeforeEach
    void initBoard() {
        board = new ChessBoard();
        sourcePiece = new Rook(Color.Black);

        board.putPiece(Position.of(File.d, Rank.Four), sourcePiece);
        board.putPiece(Position.of(File.d, Rank.Three), new Rook(Color.Black));
        board.putPiece(Position.of(File.d, Rank.Five), new Rook(Color.White));
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("invalidParameters")
    @DisplayName("룩이 이동할 수 없는 곳으로 이동")
    void rookInvalidTest(Position target, String testName) {
        Position source = Position.of(File.d, Rank.Four);

        assertThatThrownBy(() -> board.move(source, target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParameters() {
        return Stream.of(
            Arguments.of(Position.of(File.d, Rank.Three), "룩이 가려는 포지션에 같은 색의 기물이 있다면 이동할 수 없다."),
            Arguments.of(Position.of(File.e, Rank.Five), "룩은 대각선으로 이동할 수 없다."),
            Arguments.of(Position.of(File.d, Rank.One), "룩은 가려는 방향에서 기물이 있는 곳 너머로 이동할 수 없다.")
        );
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("validParameters")
    @DisplayName("룩이 이동할 수 있는 곳으로 이동")
    void rookValidTest(Position target, String testName) {
        Position source = Position.of(File.d, Rank.Four);
        board.move(source, target);

        assertThat(board.getPiece(target)).isSameAs(sourcePiece);
    }

    private static Stream<Arguments> validParameters() {
        return Stream.of(
            Arguments.of(Position.of(File.d, Rank.Five), "룩이 가려는 포지션에 다른 색의 기물이 있다면 기물을 잡고 그 위치로 이동한다."),
            Arguments.of(Position.of(File.a, Rank.Four), "가려는 방향에서 기물이 있기 전까지의 위치는 이동이 가능하다.")
        );
    }
}
