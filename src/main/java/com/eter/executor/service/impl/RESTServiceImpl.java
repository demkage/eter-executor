package com.eter.executor.service.impl;

import com.eter.executor.domain.Model;
import com.eter.executor.domain.ModelStatus;
import com.eter.executor.domain.SubmissionResponse;
import com.eter.executor.service.RESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by rusifer on 5/13/17.
 */
@Service
public class RESTServiceImpl implements RESTService {
    private RestTemplate restTemplate;
    private String eterInterfaceUrl;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setEterInterfaceUrl(@Value("${eter.interface.url}") String eterInterfaceUrl) {
        this.eterInterfaceUrl = eterInterfaceUrl;
    }

    @Override
    public SubmissionResponse requireModelExecution(String modelName) {
        return restTemplate.postForObject(eterInterfaceUrl + "/application/submit/" + modelName, null,
                SubmissionResponse.class);
    }

    @Override
    public boolean checkIfModelIsReady(String modelName) {
        ModelStatus status = restTemplate.getForObject(eterInterfaceUrl + "/model/status/" + modelName, ModelStatus.class);
        if(status.isCompleted())
            return false;
        return true;
    }

    @Override
    public Model getModel(String modelName) {
        return restTemplate.getForObject(eterInterfaceUrl + "/model/" + modelName, Model.class);
    }
}
