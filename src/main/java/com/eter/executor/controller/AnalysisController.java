package com.eter.executor.controller;

import com.eter.executor.domain.recomendation.ProductRating;
import com.eter.executor.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rusifer on 5/13/17.
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    private AnalysisService analysisService;

    @Autowired
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @RequestMapping("/als/{userId}/{productId}")
    public double alsForUserIdAndProductId(@PathVariable("userId") int userId,
                                           @PathVariable("productId") int productId) {
        return analysisService.predictForUserIdAndProductId(userId, productId);
    }

    @RequestMapping("/als/top/{num}")
    public List<ProductRating> topProducts(@PathVariable("num") int num) {
        return analysisService.topProducts(num);
    }

    @RequestMapping("/kmean/age/group/{age}")
    public double kmeanAgeGroupForAge(@PathVariable("age") int age) {
        return analysisService.predictGroupForAge(age);
    }
}
