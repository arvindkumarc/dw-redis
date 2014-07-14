package com.arvindc.dao;

import com.arvindc.mapper.SeatMapper;
import com.arvindc.model.Seat;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

import java.util.List;

public interface SeatDao {
    @SqlQuery("select * from seats where session_id = :sessionId")
    @Mapper(SeatMapper.class)
    List<Seat> forSession(@Bind("sessionId") Long sessionId);

    @SqlUpdate("update seats set status = :status where name = :seatNumber and session_id = :sessionId")
    void updateSeat(@Bind("status") String status, @Bind("seatNumber") String seatNumber, @Bind("sessionId") Long sessionId);

    @SqlUpdate("insert into seats (name, session_id) values (:name, :sessionId)")
    int create(@Bind("name")String name, @Bind("sessionId")Long sessionId);
}
