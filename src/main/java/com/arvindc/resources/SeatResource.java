package com.arvindc.resources;

import com.arvindc.dao.RedisClient;
import com.arvindc.dao.SeatDao;
import com.arvindc.model.Seat;
import com.arvindc.request.BlockSeat;
import com.arvindc.request.ConfirmSeat;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static javax.ws.rs.core.Response.ok;

@AllArgsConstructor
@Path("/seat")
public class SeatResource {

    private static long TOTALTIME = 0;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response seatLayoutForSession(@QueryParam("sessionId") String sessionId) {
        JSONObject json = new JSONObject();
        SeatDao seatDao = dbi.onDemand(SeatDao.class);
        List<Seat> seats = seatDao.forSession(Long.valueOf(sessionId));
        for(Seat seat : seats) {
            json.put(seat.getName(), seat);
        }
        return ok(json).build();
    }

    @POST
    @Path("/confirm")
    public Response confirmSeats(ConfirmSeat confirmSeat) {
        List<String> seatNumbers = confirmSeat.getSeatNumbers();
        SeatDao seatDao = dbi.onDemand(SeatDao.class);
        for (String seatNumber: seatNumbers) {
//            seatDao.updateSeat(SeatStatus.CONFIRM.name(), seatNumber, confirmSeat.getSession());
        }
        return ok().build();
    }

    @POST
    @Path("/block")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response blockSeats(BlockSeat blockSeat) throws InterruptedException {
        long start = System.nanoTime();
        List<String> seatNumbers = blockSeat.getSeatNumbers();
//        SeatDao seatDao = dbi.onDemand(SeatDao.class);
        ExecutorService executorService = Executors.newFixedThreadPool(seatNumbers.size());
        for (final String seatNumber: seatNumbers) {
//            seatDao.updateSeat(SeatStatus.BLOCK.name(), seatNumber, blockSeat.getSession());
            LatchedThread latchedThread = new LatchedThread(seatNumber);
            executorService.execute(latchedThread);
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {

        }

        System.out.println(System.nanoTime() - start);
        return ok().build();
    }

    public class LatchedThread extends Thread {
        private String seatNumber;

        public LatchedThread(String seatNumber) {
            this.seatNumber = seatNumber;
        }

        public void run() {
//            try {
//                Thread.sleep(1, 0);
//            } catch (InterruptedException e) {
//
//            }
            redisClient.BlockSeat(seatNumber);
        }
    }

    private DBI dbi;
    private RedisClient redisClient;
}
