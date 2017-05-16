package com.eter.executor.service.impl;

import com.eter.executor.apps.ALSApplication;
import com.eter.executor.apps.Application;
import com.eter.executor.apps.KMeanAgeApplication;
import com.eter.executor.domain.Model;
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
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public double predictForUserIdAndProductId(int userId, int productId) {
        initApplication(alsApplication, "als");

        return alsApplication.predictForUserIdAndProductId(userId, productId);
    }

    @Override
    public List<ProductRating> topProducts(int num) {
       initApplication(alsApplication, "als");

        return alsApplication.recommendProductsForUser(num);
    }

    @Override
    public double predictGroupForAge(int age) {
        initApplication(kMeanAgeApplication, "kmeanage");

        return kMeanAgeApplication.predictGroupFor(age);
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
