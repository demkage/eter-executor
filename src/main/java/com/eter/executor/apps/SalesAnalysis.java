package com.eter.executor.apps;

import com.eter.executor.domain.Model;
import com.eter.executor.domain.SaleData;
import com.eter.executor.domain.SaleResult;
import org.apache.spark.ml.PipelineModel;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rusifer on 5/20/17.
 */
public class SalesAnalysis implements Application {
    private String sparkMaster;
    private String hdfsUrl;
    private boolean isReady;
    private Model model;
    private SparkSession sparkSession;
    private PipelineModel pipelineModel;

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
        sparkSession = new SparkSession.Builder()
                .appName("Sales Analysis Executor")
                .master("local")
                .getOrCreate();

        pipelineModel = PipelineModel.load(hdfsUrl + model.getPath());
        if (pipelineModel != null) {
            isReady = true;
            return true;
        }

        return false;
    }

    @Override
    public boolean isReady() {
        return isReady;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    public List<SaleResult> test(List<SaleData> salesdata) {
        List<Row> salesRows = new ArrayList<>();

        for (SaleData data : salesdata) {
            salesRows.add(RowFactory.create(data.getDayOfMonth(), data.getDayOfWeek(), data.isPromo()));
        }

        StructType schema = new StructType(new StructField[]{
                new StructField("dayOfMonth", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("dayOfWeek", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("promo", DataTypes.BooleanType, false, Metadata.empty())
        });

        Dataset<Row> saleDataset = sparkSession.createDataFrame(salesRows, schema);

        Dataset<Row> result = pipelineModel.transform(saleDataset)
                .withColumnRenamed("prediction", "sales");

        final List<SaleResult> saleResults = new ArrayList<>();
        result.toJavaRDD().collect()
                .stream()
                .forEach(
                        row -> saleResults.add(
                                new SaleResult(row.getInt(1), row.getInt(0), row.getDouble(6))
                        )
                );

        System.out.println("Count: " + saleResults.size());

        return saleResults;
    }
}
