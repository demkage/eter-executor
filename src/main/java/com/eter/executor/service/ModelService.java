package com.eter.executor.service;

import com.eter.executor.domain.Model;

/**
 * Created by rusifer on 5/13/17.
 */
public interface ModelService {
    Model getModel(String modelName);
    boolean modelIsReady(String modelName);
    boolean requestModelExecution(String modelName);
}
