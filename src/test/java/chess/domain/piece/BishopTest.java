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

public class BishopTest {

    private ChessBoard board;
    private Piece sourcePiece;

    @BeforeEach
    void initBoard() {
        board = new ChessBoard();
        sourcePiece = new Bishop(Color.Black);

        board.putPiece(Position.of(File.d, Rank.Four), sourcePiece);
        board.putPiece(Position.of(File.c, Rank.Five), new Bishop(Color.White));
        board.putPiece(Position.of(File.c, Rank.Three), new Bishop(Color.Black));
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("invalidParameters")
    @DisplayName("비숍이 이동할 수 없는 곳으로 이동")
    void bishopInvalidTest(Position target, String testName) {
        Position source = Position.of(File.d, Rank.Four);

        assertThatThrownBy(() -> board.move(source, target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> invalidParameters() {
        return Stream.of(
            Arguments.of(Position.of(File.e, Rank.Four), "비숍은 상하좌우로 이동할 수 없다."),
            Arguments.of(Position.of(File.b, Rank.Six), "비숍은 기물을 뛰어넘을 수 없다."),
            Arguments.of(Position.of(File.c, Rank.Three), "비숍은 이동하려는 칸에 같은 색의 기물이 있을경우 이동할 수 없다.")
        );
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("validParameters")
    @DisplayName("비숍이 이동할 수 있는 곳으로 이동")
    void bishopValidTest(Position target, String testName) {
        Position source = Position.of(File.d, Rank.Four);
        board.move(source, target);

        assertThat(board.getPiece(target)).isSameAs(sourcePiece);
    }

    private static Stream<Arguments> validParameters() {
        return Stream.of(
            Arguments.of(Position.of(File.e, Rank.Five), "비숍은 대각선으로 이동할 수 있다."),
            Arguments.of(Position.of(File.c, Rank.Five), "비숍은 이동하려는 칸에 다른 색의 기물이 있을경우 기물을 잡고 그 위치로 이동한다.")
        );
    }
}
