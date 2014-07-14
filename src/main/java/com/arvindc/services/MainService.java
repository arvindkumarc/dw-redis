package com.arvindc.services;

import com.arvindc.configurations.BookingConfiguration;
import com.arvindc.dao.RedisClient;
import com.arvindc.resources.DumpResource;
import com.arvindc.resources.SeatResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.jdbi.DBIFactory;
import com.yammer.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import com.yammer.dropwizard.views.ViewBundle;
import org.skife.jdbi.v2.DBI;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MainService extends Service<BookingConfiguration> {

    public static void main(String[] args) throws Exception {
        MainService service = new MainService();
        service.run(args);
    }

    @Override
    public void initialize(Bootstrap<BookingConfiguration> bootstrap) {
        bootstrap.setName("Booking engine service");
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new DBIExceptionsBundle());
    }

    @Override
    public void run(BookingConfiguration configuration, Environment environment) throws Exception {
        DBIFactory factory = new DBIFactory();
        DBI dbi = factory.build(environment, configuration.getDatabaseConfiguration(), "postgresql");
        JedisPool pool = new JedisPool(jedisConfig(), "localhost", 6379);

        RedisClient client = new RedisClient(pool);

        environment.addResource(new SeatResource(dbi, client));
        environment.addResource(new DumpResource(dbi));
    }

    private JedisPoolConfig jedisConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(20);
        poolConfig.setMaxWait(1000);
        poolConfig.setMinIdle(10);
        poolConfig.setMaxActive(1000);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setNumTestsPerEvictionRun(60000);
//        poolConfig.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
        return poolConfig;
    }
}
