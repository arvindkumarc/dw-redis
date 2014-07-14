package com.arvindc.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface ScreenDao {
    @SqlUpdate("insert into screens (name, location) values (:screenName, :location)")
    public int create(@Bind("screenName") String screenName, @Bind("location") String location);

    @SqlQuery("select id from screens where name = :name")
    Long byName(@Bind("name") String screenName);
}
