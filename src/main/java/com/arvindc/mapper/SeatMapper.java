package com.arvindc.mapper;

import com.arvindc.model.Seat;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SeatMapper implements ResultSetMapper<Seat> {
    @Override
    public Seat map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        Seat seat = new Seat();
        seat.setName(resultSet.getString("name"));
        seat.setSessionId(resultSet.getLong("session_id"));
        seat.setStatus(resultSet.getString("status"));
        return seat;
    }
}
