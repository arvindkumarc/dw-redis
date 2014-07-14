package com.arvindc.resources;

import com.arvindc.dao.ScreenDao;
import com.arvindc.dao.SeatDao;
import com.arvindc.dao.SessionDao;
import lombok.AllArgsConstructor;
import org.skife.jdbi.v2.IDBI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@AllArgsConstructor
@Path("/dump")
public class DumpResource {
    private static final int SCREENS = 500;
    private static final int SESSIONS = 4;
    private static final int SEATS = 60;
    private IDBI dbi;

    @GET
    public void dumpData() {
        SessionDao sessionDao = dbi.onDemand(SessionDao.class);
        ScreenDao screenDao = dbi.onDemand(ScreenDao.class);
        SeatDao seatDao = dbi.onDemand(SeatDao.class);

        for (int screen = 0; screen < SCREENS; screen++) {
            String screenName = "screen" + screen;
            screenDao.create(screenName, "chn");
            Long screenId = screenDao.byName(screenName);
            for (int session = 0; session < SESSIONS; session++) {
                String name = "session" + session + screenId;
                sessionDao.create(name, screenId);
                Long sessionId = sessionDao.byName(name);

                for (int seat = 0; seat < SEATS; seat++) {
                    seatDao.create(getSeatNumber(seat), sessionId);
                }
            }
        }
    }

    private String getSeatNumber(int seat) {
        List<String> availableRows = newArrayList("A", "B", "C", "D", "E", "F", "G");
        String row = availableRows.get(seat % 6);
        return row + seat;
    }
}
