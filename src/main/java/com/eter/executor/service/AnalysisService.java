package com.eter.executor.service;

import com.eter.executor.domain.recomendation.ProductRating;

import java.util.List;

/**
 * Created by rusifer on 5/13/17.
 */
public interface AnalysisService {
    double predictForUserIdAndProductId(int userId, int productId);
    List<ProductRating> topProducts(int num);

    double predictGroupForAge(int age);
}
