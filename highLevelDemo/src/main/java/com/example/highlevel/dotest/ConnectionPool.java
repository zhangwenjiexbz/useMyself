package com.example.highlevel.dotest;

import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.util.LinkedList;

public class ConnectionPool {
    
    private final LinkedList<Connection> pool = new LinkedList<>();
    
    public ConnectionPool(int initSize) {
        if (initSize > 0) {
            for (int i = 0; i < initSize; i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }
    
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (pool) {
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }
    
    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool) {
            if (mills <= 0) {
                while(pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else {
                long feature = System.currentTimeMillis() + mills;
                long remaining = mills;
                while (CollectionUtils.isEmpty(pool) && remaining > 0) {
                    pool.wait(remaining);
                    remaining = feature - System.currentTimeMillis();
                }
                Connection result = null;
                if (!CollectionUtils.isEmpty(pool)) {
                    result =  pool.removeFirst();
                }
                return result;
            }
        }
    }
}
