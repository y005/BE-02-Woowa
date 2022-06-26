package com.example.woowa.order.review.converter;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.order.review.dto.ReviewCreateRequest;
import com.example.woowa.order.review.dto.ReviewFindResponse;
import com.example.woowa.order.review.entity.Review;
import com.example.woowa.order.review.enums.ScoreType;

public class ReviewConverter {
    public static ReviewFindResponse toReviewDto(Review review) {
        return new ReviewFindResponse(review.getId(), review.getContent(), review.getScoreType());
    }

    public static Review toReview(ReviewCreateRequest reviewCreateRequest, Customer customer, Order order) {
        validateReview(reviewCreateRequest.getContent(),
            reviewCreateRequest.getScoreType(), customer, order);
        return new Review(reviewCreateRequest.getContent(), ScoreType.of(
            reviewCreateRequest.getScoreType()), customer, order);
    }

    private static void validateReview(String content, Integer score, Customer customer, Order order) {
        assert content.length() >= 10 && content.length() <= 500;
        assert score >= 1 && score <= 5;
        assert customer != null;
    }
}
