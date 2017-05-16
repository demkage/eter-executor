package com.eter.executor.apps;

import com.eter.executor.domain.Model;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by rusifer on 5/13/17.
 */
public class KMeanAgeApplication implements Application {
    private String sparkMaster;
    private String hdfsUrl;
    private Model model;
    private KMeansModel kMeansModel;
    private boolean isReady;

    @Autowired
    public void setSparkMaster(@Value("${eter.spark.master}") String sparkMaster) {
        this.sparkMaster = sparkMaster;
    }

    @Autowired
    public void setHdfsUrl(@Value("${eter.hdfs.url}") String hdfsUrl) {
        this.hdfsUrl = hdfsUrl;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public boolean load() {
        SparkSession sparkSession = new SparkSession.Builder()
                .appName("KMean-Age-executor")
                .master("local")
                .getOrCreate();

        kMeansModel = KMeansModel.load(sparkSession.sparkContext(), hdfsUrl + model.getPath());

        if(kMeansModel != null) {
            isReady = true;
            return true;
        }


        isReady = false;
        return false;
    }

    public double predictGroupFor(int age) {
       return kMeansModel.predict(Vectors.dense(age));
    }

    public boolean isReady() {
        return isReady;
    }
}
