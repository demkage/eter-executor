package com.eter.executor.apps;

import com.eter.executor.domain.GroupStatistic;
import com.eter.executor.domain.Model;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rusifer on 5/21/17.
 */
public class KMeanGenderApplication implements Application, Serializable {
    private String sparkMaster;
    private String hdfsUrl;
    private Model model;
    private PipelineModel pipelineModel;
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

        pipelineModel = PipelineModel.load(hdfsUrl + model.getPath());

        if(pipelineModel != null) {
            isReady = true;
            return true;
        }


        isReady = false;
        return false;
    }

    public GroupStatistic predict(List<String> genders) {
        List<Row> genderRows = new ArrayList<>();

        for(String gender : genders) {
            genderRows.add(RowFactory.create(gender));
        }

        StructType schema = new StructType(new StructField[] {
                new StructField("sex", DataTypes.StringType, false, Metadata.empty())
        });

        Dataset<Row> dataset = sparkApplication.getSession().createDataFrame(genderRows, schema);

        Dataset<Row> result = pipelineModel.transform(dataset)
                .withColumnRenamed("prediction", "group");

        GroupStatistic groupStatistic = new GroupStatistic();
        Vector[] centers = ((KMeansModel) pipelineModel.stages()[2]).clusterCenters();
        groupStatistic.setGroupsCount(centers.length);

        int currentGroup = 0;
        for(Vector center : centers) {
            groupStatistic.addCenter(String.valueOf(currentGroup), center.toArray()[0]);
            ++currentGroup;
        }

        result = result.groupBy("group")
                .count();

        result.collectAsList()
                .stream()
                .forEach( row -> groupStatistic.addGroup(
                        String.valueOf(row.getInt(row.fieldIndex("group"))),
                        Double.valueOf(row.getLong(row.fieldIndex("count"))) / genders.size()));

        return groupStatistic;

    }

    public boolean isReady() {
        return isReady;
    }
}
