package com.eter.executor.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rusifer on 5/21/17.
 */
public class GroupStatistic implements Serializable {
    private Map<String, Double> groups = new HashMap<>();
    private Map<String, Double> centers = new HashMap<>();
    private int groupsCount;

    public Map<String, Double> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Double> groups) {
        this.groups = groups;
    }

    public int getGroupsCount() {
        return groupsCount;
    }

    public void setGroupsCount(int groupsCount) {
        this.groupsCount = groupsCount;
    }

    public Map<String, Double> getCenters() {
        return centers;
    }

    public void setCenters(Map<String, Double> centers) {
        this.centers = centers;
    }

    public void addGroup(String tag, Double value) {
        groups.put(tag, value);
    }

    public void addCenter(String tag, Double value) {
        centers.put(tag, value);
    }
}
