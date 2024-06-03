package com.ph.service;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.CategoryPropertyKey;
import com.ph.domain.entities.CategoryPropertyValue;
import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.repository.CategoryPropertyValueRepository;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CategoryPropertyValueService {

    private final CategoryPropertyValueRepository repository;
    private final MessageUtil messageUtil;


    public CategoryPropertyValue saveValue(CategoryPropertyKey key, String value, Advert advert) {

        CategoryPropertyValue categoryPropertyValue = CategoryPropertyValue.builder()
                .advert(advert)
                .categoryPropertyKey(key)
                .value(value)
                .build();

        return repository.save(categoryPropertyValue);

    }

    public void updateValue(String newValue, Long propValueId) {

        CategoryPropertyValue categoryPropertyValue = repository.findById(propValueId).orElseThrow(() ->
                new ResourceNotFoundException(String.format(messageUtil.getMessage("error.category.property.value.not.found"), propValueId)));


        categoryPropertyValue.setValue(newValue);


        CategoryPropertyValue save = repository.save(categoryPropertyValue);
        System.err.println(save);

    }

    public void deleteValue(List<CategoryPropertyValue> propertyValues) {
        repository.deleteAllById(propertyValues
                .stream()
                .map(CategoryPropertyValue::getId)
                .collect(Collectors
                        .toList()));


    }

}
