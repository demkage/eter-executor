package com.eter.executor.apps;

import com.eter.executor.domain.Model;
import com.eter.executor.domain.recomendation.ProductRating;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;
import org.apache.spark.mllib.recommendation.Rating;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rusifer on 5/13/17.
 */
public class ALSApplication implements Application {
    private String sparkMaster;
    private String hdfsUrl;
    private Model model;
    private MatrixFactorizationModel factorizationModel;
    private SparkApplication sparkApplication;
    private boolean isReady;

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

        factorizationModel = MatrixFactorizationModel
                .load(sparkApplication.getSession().sparkContext(), hdfsUrl + model.getPath());

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

    public List<ProductRating> recommendProdutsForUser(int userId, int num) {
        List<ProductRating> products = new ArrayList<>();
        for(Rating rating : factorizationModel.recommendProducts(userId, num)) {
            products.add(new ProductRating(rating.product(), rating.rating()));
        }

        return products;
    }

    public List<ProductRating> recommendProductsForUsers(int num) {
        return factorizationModel.recommendProductsForUsers(num).toJavaRDD().map((value) ->
            new ProductRating(value._2()[0].product(), value._2()[0].rating())
        ).take(num);
    }

    public boolean isReady() {
        return isReady;
    }
}
