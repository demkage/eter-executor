package com.eter.executor.domain;

/**
 * Created by rusifer on 5/13/17.
 */
public class ModelStatus {
    private String name;
    private String status;
    private boolean completed;

    public ModelStatus() {
        status = "Unknown Model";
    }

    public static ModelStatus transfromFromModel(Model model) {
        if (model == null) {
            return new ModelStatus();
        } else {
            ModelStatus status = new ModelStatus();
            status.setStatus(model.getStatus());
            status.setCompleted(model.getComplete());
            status.setName(model.getName());

            return status;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}