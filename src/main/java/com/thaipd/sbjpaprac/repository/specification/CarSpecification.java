package com.thaipd.sbjpaprac.repository.specification;

import com.thaipd.sbjpaprac.dto.CarSearchCriteria;
import com.thaipd.sbjpaprac.entity.Car;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CarSpecification {

    public static Specification<Car> getSpec(CarSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(criteria.getBrand())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")),
                        "%" + criteria.getBrand().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getModel())) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("model")),
                        "%" + criteria.getModel().toLowerCase() + "%"));
            }

            if (StringUtils.hasText(criteria.getColor())) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("color")),
                        criteria.getColor().toLowerCase()));
            }

            if (criteria.getMinYear() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("modelYear"), criteria.getMinYear()));
            }

            if (criteria.getMaxYear() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("modelYear"), criteria.getMaxYear()));
            }

            if (criteria.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }

            if (criteria.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
