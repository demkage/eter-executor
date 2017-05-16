package com.eter.executor.apps;

import com.eter.executor.domain.Model;
import com.eter.executor.domain.recomendation.ProductRating;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created by rusifer on 5/13/17.
 */
public class ALSApplication implements Application {
    private String sparkMaster;
    private String hdfsUrl;
    private Model model;
    private MatrixFactorizationModel factorizationModel;
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
                .appName("ALS-executor")
                .master("local")
                .getOrCreate();

        factorizationModel = MatrixFactorizationModel.load(sparkSession.sparkContext(),
                hdfsUrl + model.getPath());

        if(factorizationModel != null) {
            isReady = true;
            return true;
        }

        isReady = false;
        return false;
    }

    public double predictForUserIdAndProductId(int userId, int productId) {
        return factorizationModel.predict(userId, productId);
    }

    public List<ProductRating> recommendProductsForUser(int num) {
        return factorizationModel.recommendProductsForUsers(num).toJavaRDD().map((value) ->
            new ProductRating(value._2()[0].product(), value._2()[0].rating())
        ).take(num);
    }

    public boolean isReady() {
        return isReady;
    }
}
