package com.eter.executor.apps;

import com.eter.executor.domain.Model;

/**
 * Created by rusifer on 5/15/17.
 */
public interface Application {
    boolean load();

    boolean isReady();

    void setModel(Model model);
}
