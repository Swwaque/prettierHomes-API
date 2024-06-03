package com.ph.payload.mapper;

import com.ph.domain.entities.Category;
import com.ph.domain.entities.CategoryPropertyKey;
import com.ph.domain.entities.CategoryPropertyValue;
import com.ph.payload.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CategoryMapper {


    public CategoryWithoutPropertiesResponse mapToCategoryWithoutPropertyResponse(Category category) {
        return CategoryWithoutPropertiesResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .slug(category.getSlug())
                .icon(category.getIcon())
                .seq(category.getSeq())
                .builtIn(category.isBuiltIn())
                .active(category.isActive())
                .build();
    }

    public CategoryResponse mapToCategoryResponsewithPropertyKeys(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .title(category.getTitle())
                .slug(category.getSlug())
                .icon(category.getIcon())
                .seq(category.getSeq())
                .builtIn(category.isBuiltIn())
                .active(category.isActive())
                .categoryPropertyKeysResponse(category.getCategoryPropertyKeys().stream().map(this::mapToCategoryPropertyKeyResponse).toList())
                // we'l get an exception if we set cotegoryPropertyKeys object that's why we usegetCategoryPropertyKeysResponse
                .build();
    }


    public CategoryResponseForFavorite toCategoryResponseForFavorite(Category category) {
        return CategoryResponseForFavorite.builder()
                .id(category.getId())
                .title(category.getTitle())
                .build();
    }


    public CategoryPropertyKeyResponse mapToCategoryPropertyKeyResponse(CategoryPropertyKey categoryPropertyKey) {

        return CategoryPropertyKeyResponse.builder()
                .id(categoryPropertyKey.getId())
                .name(categoryPropertyKey.getName())
                .keyType(categoryPropertyKey.getKeyType())
                .prefix(categoryPropertyKey.getPrefix())
                .suffix(categoryPropertyKey.getSuffix())
                .builtIn(categoryPropertyKey.isBuiltIn())
                .build();
    }


    public List<PropertyValueResponse> entityToResponse(List<CategoryPropertyValue> entities) {
         entities.sort(Comparator.comparing(entity -> entity.getCategoryPropertyKey().getId()));
        return entities.stream()
                .map(entity -> PropertyValueResponse.builder()
                        .id(entity.getId())
                        .value(entity.getValue())
                        .build())
                .collect(Collectors.toList());
    }

}
