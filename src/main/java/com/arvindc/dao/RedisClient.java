package com.arvindc.dao;

import lombok.AllArgsConstructor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@AllArgsConstructor
public class RedisClient {
    private JedisPool pool;

    public void BlockSeat(String seatkey) {
        Jedis redis = null;
//        System.out.println("Obtained connection from pool.");
        try {
            redis = pool.getResource();
            if (!redis.exists(seatkey)) {
                System.out.println("No seat with key: "+ seatkey);
                return;
            }

            redis.watch(seatkey);
            String status = redis.get(seatkey);
//            System.out.println("Seat "+ seatkey+", Status: " +status);

            if (status.equalsIgnoreCase("free")) {
                Transaction transaction = redis.multi();
                transaction.set(seatkey, SeatStatus.FREE.name().toLowerCase());
                transaction.exec();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            if (redis != null) {
                pool.returnResource(redis);
            }
//            System.out.println("Returned connection to pool." + pool.toString());
        }
    }
}
