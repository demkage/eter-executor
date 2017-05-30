package com.eter.executor.apps;

import com.eter.executor.domain.GroupStatistic;
import com.eter.executor.domain.Model;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by rusifer on 5/13/17.
 */
public class KMeanAgeApplication implements Application, Serializable {
    private String sparkMaster;
    private String hdfsUrl;
    private Model model;
    private KMeansModel kMeansModel;
    private boolean isReady;
    private SparkApplication sparkApplication;

    @Autowired
    public void setSparkApplication(SparkApplication sparkApplication) {
        this.sparkApplication = sparkApplication;
    }

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

        kMeansModel = KMeansModel
                .load(sparkApplication.getSession().sparkContext(), hdfsUrl + model.getPath());

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

    public GroupStatistic predict(List<Integer> ages) {
        GroupStatistic groupStatistic = new GroupStatistic();
        groupStatistic.setGroupsCount(kMeansModel.clusterCenters().length);
        Vector[] centers = kMeansModel.clusterCenters();
        int currentGroup = 0;
        for(Vector center : centers) {
            groupStatistic.addCenter("" + currentGroup, center.toArray()[0]);
            ++currentGroup;
        }

        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkApplication.getSession().sparkContext());

        JavaRDD<Integer> rddAges = javaSparkContext.parallelize(ages);
        JavaRDD<Vector> points = rddAges.map((Function<Integer, Vector>)
                v1 -> Vectors.dense(new double[] { v1.doubleValue()}));

        Map<Integer, Long> result = kMeansModel.predict(points).countByValue();

        for(Map.Entry<Integer, Long> entry : result.entrySet()) {
            groupStatistic.addGroup(entry.getKey().toString(),
                    (Long.valueOf(entry.getValue()).doubleValue() / ages.size()));
        }

        return groupStatistic;
    }

    public boolean isReady() {
        return isReady;
    }
}
