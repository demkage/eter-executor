package com.eter.executor.domain;

import java.io.Serializable;

/**
 * Created by abosii on 5/30/2017.
 */
public class InventoryResult implements Serializable {
    private long productId;
    private double score;

    public InventoryResult() {
    }

    public InventoryResult(long productId, double score) {
        this.productId = productId;
        this.score = score;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoryResult that = (InventoryResult) o;

        return getProductId() == that.getProductId();

    }

    @Override
    public int hashCode() {
        return (int) (getProductId() ^ (getProductId() >>> 32));
    }
}
