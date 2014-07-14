package com.arvindc.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface SessionDao {
    @SqlUpdate("insert into sessions (name, screen_id) values (:name, :screenId)")
    public int create(@Bind("name")String name, @Bind("screenId")Long screenId);

    @SqlQuery("select id from sessions where name = :name")
    Long byName(@Bind("name") String name);
}
