package chess.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chess.web.dto.PieceDto;
import chess.web.utils.DBConnector;

public class PieceDao {
    private final Connection connection = DBConnector.getConnection();

    public List<PieceDto> findAllByRoomId(int roomId) throws SQLException {
        final String sql = "select position, name, imagePath, state from piece where roomId = ?";
        List<PieceDto> pieces = new ArrayList<>();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, roomId);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            PieceDto pieceDto = PieceDto.of(
                resultSet.getString("position"),
                resultSet.getString("name"),
                resultSet.getString("imagePath"),
                resultSet.getString("state")
            );
            pieces.add(pieceDto);
        }
        return pieces;
    }

    public int save(PieceDto pieceDto, int roomId) throws SQLException {
        final String sql = "insert into piece (position, name, imagePath, state, roomId) values (?, ?, ?, ?, ?)";
        final PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, pieceDto.getPosition());
        statement.setString(2, pieceDto.getName());
        statement.setString(3, pieceDto.getImageName());
        statement.setString(4, pieceDto.getState());
        statement.setInt(5, roomId);
        statement.executeUpdate();
        ResultSet result = statement.getGeneratedKeys();

        if (result.next()) {
            return result.getInt(1);
        }

        return 0;
    }

    public List<Integer> saveAll(List<PieceDto> pieces, int roomId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        for (PieceDto pieceDto : pieces) {
            ids.add(save(pieceDto, roomId));
        }
        return Collections.unmodifiableList(ids);
    }

    public void removeAllByRoomId(int roomId) throws SQLException {
        final String sql = "delete from piece where roomId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, roomId);
        statement.executeUpdate();
    }

    public void deleteByPosition(String position) throws SQLException {
        final String sql = "delete from piece where position = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, position);
        statement.executeUpdate();
    }

    public void updatePosition(String from, String to) throws SQLException {
        final String sql = "update piece set position = ? where position = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, to);
        statement.setString(2, from);
        statement.executeUpdate();
    }

    public void deleteByRoomId(int roomId) throws SQLException {
        final String sql = "delete from piece where roomId = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, roomId);
        statement.executeUpdate();
    }

    public void updatePawnState(String position) throws SQLException {
        if (isStartedPawn(position)) {
            final String sql = "update piece set state = ? where position = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "Moved");
            statement.setString(2, position);
            statement.executeUpdate();
        }
    }

    private boolean isStartedPawn(String position) throws SQLException {
        final String sql = "select name, state from piece where position = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, position);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        String name = resultSet.getString("name") + resultSet.getString("state");
        return name.equalsIgnoreCase("p");
    }
}
