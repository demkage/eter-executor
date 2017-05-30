package com.eter.executor.apps;

import com.eter.executor.domain.*;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abosii on 5/30/2017.
 */
public class InventoryAnalysis implements Application {
    private boolean isReady;
    private Model model;
    private PipelineModel pipelineModel;
    private String sparkMaster;
    private String hdfsUrl;
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

    @Override
    public boolean load() {
        pipelineModel = PipelineModel.load(hdfsUrl + model.getPath());

        if (pipelineModel != null) {
            isReady = true;
            return true;
        }

        return false;
    }


    public List<InventoryResult> predict(List<InventoryData> inventories) {
        List<Row> inventoryRows = new ArrayList<>();

        for (InventoryData data : inventories) {
            inventoryRows.add(RowFactory.create(data.getProductId(), data.getLasSaleDayAgo(), data.getSalesCount()));
        }

        StructType schema = new StructType(new StructField[]{
                new StructField("productid", DataTypes.LongType, false, Metadata.empty()),
                new StructField("lastsale", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("sales", DataTypes.IntegerType, false, Metadata.empty())
        });

        Dataset<Row> inventoryData = sparkApplication.getSession().createDataFrame(inventoryRows, schema);

        Dataset<Row> result = pipelineModel.transform(inventoryData)
                .withColumnRenamed("prediction", "score");

        result.show();

        final List<InventoryResult> invetoryResult = new ArrayList<>();
        result.toJavaRDD().collect()
                .stream()
                .forEach(
                        row -> invetoryResult.add(
                                new InventoryResult(row.getLong(0), row.getDouble(6))
                        )
                );

        System.out.println("Count: " + invetoryResult.size());

        return invetoryResult;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }
}
