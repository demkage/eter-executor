package com.eter.executor.domain;

import java.io.Serializable;

/**
 * Created by abosii on 5/30/2017.
 */
public class InventoryData implements Serializable {
    private int lasSaleDayAgo;
    private int salesCount;
    private long productId;

    public int getLasSaleDayAgo() {
        return lasSaleDayAgo;
    }

    public void setLasSaleDayAgo(int lasSaleDayAgo) {
        this.lasSaleDayAgo = lasSaleDayAgo;
    }

    public int getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(int salesCount) {
        this.salesCount = salesCount;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoryData that = (InventoryData) o;

        if (getLasSaleDayAgo() != that.getLasSaleDayAgo()) return false;
        if (getSalesCount() != that.getSalesCount()) return false;
        return getProductId() == that.getProductId();

    }

    @Override
    public int hashCode() {
        int result = getLasSaleDayAgo();
        result = 31 * result + getSalesCount();
        result = 31 * result + (int) (getProductId() ^ (getProductId() >>> 32));
        return result;
    }
}
