package com.inditex.infrastructure.out.persistence;

import com.inditex.infrastructure.out.entity.PricePersistence;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;

public class PriceSpecifications {
    private PriceSpecifications() {}

    public static Specification<PricePersistence> hasProductId(Long productId) {
        return (root, query, cb) ->
                cb.equal(root.get("productId"), productId);
    }

    public static Specification<PricePersistence> hasBrandId(Long brandId) {
        return (root, query, cb) ->
                cb.equal(root.get("brandId"), brandId);
    }

    public static Specification<PricePersistence> applicableAt(LocalDateTime date) {
        return (root, query, cb) -> cb.and(
                cb.lessThanOrEqualTo(root.get("startDate"), date),
                cb.greaterThanOrEqualTo(root.get("endDate"), date)
        );
    }

    public static Specification<PricePersistence> orderByPriorityDesc() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("priority")));
            return cb.conjunction();
        };
    }

    public static Specification<PricePersistence> applicablePrices(
            Long productId, Long brandId, LocalDateTime applicationDate) {

        return Specification
                .where(hasProductId(productId))
                .and(hasBrandId(brandId))
                .and(applicableAt(applicationDate))
                .and(orderByPriorityDesc());
    }
}
