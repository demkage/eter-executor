package com.eter.executor.service;

import com.eter.executor.domain.*;
import com.eter.executor.domain.recomendation.ProductRating;

import java.util.List;

/**
 * Created by rusifer on 5/13/17.
 */
public interface AnalysisService {
    double predictForUserIdAndProductId(int userId, int productId);

    List<ProductRating> recommendProductsForUser(int userId, int num);

    List<ProductRating> topProducts(int num);

    double predictGroupForAge(int age);

    GroupStatistic predictStatisticsByAge(List<Integer> ages);

    GroupStatistic predictStatisticsByGender(List<String> genders);

    List<SaleResult> predictSales(List<SaleData> salesData);

    List<InventoryResult> predictInventory(List<InventoryData> inventoryData);
}
