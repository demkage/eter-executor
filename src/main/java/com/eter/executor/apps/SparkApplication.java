package com.eter.executor.apps;

import org.apache.spark.sql.SparkSession;

/**
 * Created by abosii on 5/30/2017.
 */
public class SparkApplication {
    private static final String APP_NAME = "EterExecutor";
    private SparkSession sparkSession;
    private boolean ready;

    public boolean run() {
        sparkSession = new SparkSession.Builder()
                .appName(APP_NAME)
                .master("local[4]")
                .getOrCreate();

        ready = true;
        return ready;
    }

    public SparkSession getSession() {
        return sparkSession;
    }
}
