package com.eter.executor.service;

import com.eter.executor.domain.Model;
import com.eter.executor.domain.SubmissionResponse;

/**
 * Created by rusifer on 5/13/17.
 */
public interface RESTService {
    SubmissionResponse requireModelExecution(String modelName);

    boolean checkIfModelIsReady(String modelName);

    Model getModel(String modelName);
}
