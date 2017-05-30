package com.eter.executor.service.impl;

import com.eter.executor.apps.*;
import com.eter.executor.domain.*;
import com.eter.executor.domain.recomendation.ProductRating;
import com.eter.executor.service.AnalysisService;
import com.eter.executor.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rusifer on 5/13/17.
 */
@Service
public class AnalysisServiceImpl implements AnalysisService {
    private ALSApplication alsApplication;
    private KMeanAgeApplication kMeanAgeApplication;
    private SalesAnalysis salesAnalysisApplication;
    private KMeanGenderApplication kMeanGenderApplication;
    private InventoryAnalysis inventoryAnalysis;
    private ModelService modelService;

    @Autowired
    public void setAlsApplication(ALSApplication alsApplication) {
        this.alsApplication = alsApplication;
    }

    @Autowired
    public void setkMeanAgeApplication(KMeanAgeApplication kMeanAgeApplication) {
        this.kMeanAgeApplication = kMeanAgeApplication;
    }

    @Autowired
    public void setSalesAnalysisApplication(SalesAnalysis salesAnalysisApplication) {
        this.salesAnalysisApplication = salesAnalysisApplication;
    }

    @Autowired
    public void setkMeanGenderApplication(KMeanGenderApplication kMeanGenderApplication) {
        this.kMeanGenderApplication = kMeanGenderApplication;
    }

    @Autowired
    public void setInventoryAnalysis(InventoryAnalysis inventoryAnalysis) {
        this.inventoryAnalysis = inventoryAnalysis;
    }

    @Autowired
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public double predictForUserIdAndProductId(int userId, int productId) {
        initApplication(alsApplication, "als");

        return alsApplication.predictForUserIdAndProductId(userId, productId);
    }

    @Override
    public List<ProductRating> recommendProductsForUser(int userId, int num) {
        initApplication(alsApplication, "als");

        return alsApplication.recommendProdutsForUser(userId, num);
    }

    @Override
    public List<ProductRating> topProducts(int num) {
       initApplication(alsApplication, "als");

        return alsApplication.recommendProductsForUsers(num);
    }

    @Override
    public double predictGroupForAge(int age) {
        initApplication(kMeanAgeApplication, "kmeanage");

        return kMeanAgeApplication.predictGroupFor(age);
    }

    @Override
    public GroupStatistic predictStatisticsByAge(List<Integer> ages) {
        initApplication(kMeanAgeApplication, "kmeanage");

        return kMeanAgeApplication.predict(ages);
    }

    @Override
    public GroupStatistic predictStatisticsByGender(List<String> genders) {
        initApplication(kMeanGenderApplication, "kmeangender");

        return kMeanGenderApplication.predict(genders);
    }

    @Override
    public List<SaleResult> predictSales(List<SaleData> salesData) {
        initApplication(salesAnalysisApplication, "salesanalysis");

        return salesAnalysisApplication.test(salesData);
    }

    @Override
    public List<InventoryResult> predictInventory(List<InventoryData> inventoryData) {
        initApplication(inventoryAnalysis, "inventoryanalysis");

        return inventoryAnalysis.predict(inventoryData);
    }

    private boolean initApplication(Application application, String modelName) {
        if(!application.isReady()) {
            Model model = modelService.getModel(modelName);
            application.setModel(model);
            application.load();
        }

        return application.isReady();
    }
}
