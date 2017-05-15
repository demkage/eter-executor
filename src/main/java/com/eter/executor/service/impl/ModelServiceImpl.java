package com.eter.executor.service.impl;

import com.eter.executor.domain.Model;
import com.eter.executor.domain.SubmissionResponse;
import com.eter.executor.service.ModelService;
import com.eter.executor.service.RESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rusifer on 5/13/17.
 */
@Service
public class ModelServiceImpl implements ModelService {
    private RESTService restService;

    @Autowired
    public void setRestService(RESTService restService) {
        this.restService = restService;
    }

    @Override
    public Model getModel(String modelName) {
        return restService.getModel(modelName);
    }

    @Override
    public boolean modelIsReady(String modelName) {
        return restService.checkIfModelIsReady(modelName);
    }

    @Override
    public boolean requestModelExecution(String modelName) {
        SubmissionResponse response = restService.requireModelExecution(modelName);
        if(response.isSuccess())
            return true;

        return false;
    }
}
