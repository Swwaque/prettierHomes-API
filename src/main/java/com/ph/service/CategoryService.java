package com.ph.service;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.Category;
import com.ph.exception.customs.*;
import com.ph.payload.mapper.CategoryMapper;
import com.ph.payload.request.CategoryRequest;
import com.ph.payload.response.CategoryResponse;
import com.ph.payload.response.CategoryWithoutPropertiesResponse;
import com.ph.repository.CategoryRepository;
import com.ph.utils.GeneralUtils;
import com.ph.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final AdvertService advertService;
    private final MessageUtil messageUtil;


    //NOT: saveCategory **************************************************  C04

    /**
     * !!!
     * <p>
     * This method create an category.
     *
     * @param categoryRequest : represent category containing specific information
     * @return : ResponseEntity with created category
     */
    @CacheEvict(value = "category", allEntries = true)
    public ResponseEntity<?> save(CategoryRequest categoryRequest) {


        Category category = categoryRequest.get();

        // generate slug
        category.setSlug(GeneralUtils.generateSlug(category.getTitle()));

        // check title constraint
        checkTitle(categoryRequest.getTitle());

        //Eski hali
        // check title for duplicate if there is same title in database then throw ConflictException
/*        if (categoryRepository.existsByTitle(categoryRequest.getTitle()) &&
                !category.getTitle().equalsIgnoreCase(categoryRequest.getTitle())) {
            throw new ConflictException(messageUtil.getMessage("error.category.save.exist"));
        }*/

        //Yeni hali
        if (categoryRepository.existsByTitleIgnoreCase(categoryRequest.getTitle())) {
            throw new ConflictException(messageUtil.getMessage("error.category.save.exist"));
        }

        // save category in database
        Category saved = categoryRepository.save(category);
        // return created category
        return ResponseEntity.ok(categoryMapper.mapToCategoryWithoutPropertyResponse(saved));

    }

    /**
     * this method created for checking title
     *
     * @param title : represent  title of category
     */
    public void checkTitle(String title) {

        // if the title contains character except a-zA-Z then throw ConflictException
        if (!title.matches("^[a-zA-Z ]+$")) {
            throw new ConflictException(messageUtil.getMessage("error.category.save.not-alphabetic"));
        }

    }

    /**
     * This method created for check duplicate slug
     *
     * @param slug : represent customized title of category
     */
    public void checkDuplicateForSlug(String slug) {
        if (categoryRepository.existsBySlug(slug)) {
            throw new ConflictException(messageUtil.getMessage("error.category.save.duplicate.slug"));
        }
    }

    //NOT: getAllCategoryWithPage *****************************************  C01

//    /**
//     * This method created for getting all categories
//     * @param page : represent page number
//     * @param size : represent page size
//     * @param sort : represent sort type
//     * @param type : represent type of sort
//     * @return : all categories with pageable type
//     */
//    public Page<CategoryResponse> getAllCategoryWithPage(int page, int size, String sort, String type) {
//
//        Pageable pageable = PageRequest.of(page,size, Sort.by(sort).ascending());
//        if(Objects.equals(type, "desc")) {
//            pageable = PageRequest.of(page,size,Sort.by(sort).descending());
//        }
//
//        return categoryRepository.findAll(pageable).map(categoryMapper::mapToCategoryResponse);
//    }

    //NOT: getAllCategoryWithList **************************************************C01

    /**
     * This method created for getting all categories which active field is true
     *
     * @return : all categories without properties
     */
    public ResponseEntity<List<CategoryWithoutPropertiesResponse>> getAllCategory() {

        // added: 19.11.2023 ->  get all categories where active field is true
        List<Category> categories = categoryRepository.findAll()
                .stream()
                .filter(Category::isActive)
                .toList();

        // convert categories to categoryResponses
        List<CategoryWithoutPropertiesResponse> categoryResponses = categories
                .stream()
                .map(categoryMapper::mapToCategoryWithoutPropertyResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(categoryResponses);
    }


    //NOT: getAllCategoryWithPageAndWithAdmin **************************************************C02

    /**
     * This method created for getting all categories with page for admin and manager
     *
     * @param page  represent page number
     * @param size  represent page size
     * @param sort  represent sort type
     * @param type  represent type of sort
     * @param query represent search query
     * @return all categories with pageable type
     */

    public Page<CategoryWithoutPropertiesResponse>
    getAllCategoryWithPageAndWithAdmin(int page, int size, String sort, String type, String query) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (type.equals("desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        // if query is null or empty then return all categories
//        if (query == null || query.isEmpty()) {
//            return categoryRepository.findAll(pageable).map(categoryMapper::mapToCategoryWithoutPropertyResponse);
//        }

        // if query is't null then return list of filtered categories which contains query
        return categoryRepository.findAllWithQuery(query, pageable).map(categoryMapper::mapToCategoryWithoutPropertyResponse);

    }


    //Not: deleteCategory *************************************************************** C06

    /**
     * this method created for deleting the category
     *
     * @param categoryId : represent the category id
     * @return : ResponseEntity with deleted category
     */
    @CacheEvict(value = "category", key = "#categoryId")
    public ResponseEntity<?> deleteCategory(Long categoryId) {

        //  check if category exists
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new ResourceNotFoundException(messageUtil.getMessage("error.category.not-found"));
        }
        // check if category is built in
        if (category.get().isBuiltIn()) { // we used get() method because of Optional
            throw new NonDeletableException(messageUtil.getMessage("error.category.delete.built-in"));
        }

        // if there is any advert related to the category then it cannot be deleted
        List<Advert> adverts = advertService.getAdvertsOfCategory(categoryId);
        if (!adverts.isEmpty()) {
            throw new RelatedFieldException(messageUtil.getMessage("error.category.delete.advert"));
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageUtil.getMessage("error.category.delete.advert"));
        }
        // delete category
        categoryRepository.deleteById(categoryId);

        // return deleted category
        return ResponseEntity.ok(categoryMapper.mapToCategoryWithoutPropertyResponse(category.get()));

    }


    //Not: getById  ******************************************************************* CO3

    /**
     * This method created for getting category by id with category property keys
     *
     * @param categoryId : represent category id
     * @return : ResponseEntity with category that is asked
     */
    @Cacheable(value = "category", key = "#categoryId")
    public ResponseEntity<CategoryResponse> getById(Long categoryId) {

        // check if category exists and return
        Category category = categoryRepository.findById(categoryId).orElseThrow(()
                -> new ResourceNotFoundException(messageUtil.getMessage("error.category.not-found")));

        return ResponseEntity.ok(categoryMapper.mapToCategoryResponsewithPropertyKeys(category));

    }


    //Not: updateById ************************************************************************ C05

    /**
     * this method created for updating category
     *
     * @param categoryId      : represent category id
     * @param categoryRequest : represent category request object
     * @return : ResponseEntity with updated category
     */

    @CacheEvict(value = "category", key = "#categoryId")
    public ResponseEntity<?> updateById(Long categoryId, CategoryRequest categoryRequest) {
        // Check if the category with the given ID exists in the database
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException(messageUtil.getMessage("error.category.not-found"))
        );

        // check the category is built_in
        if (category.isBuiltIn()) {
            throw new NonUpdatableException(messageUtil.getMessage("error.category.update.built-in"));
        }

        // check title for duplicate if there is same title in database then throw ConflictException
        if (categoryRepository.existsByTitle(categoryRequest.getTitle()) &&
                !category.getTitle().equalsIgnoreCase(categoryRequest.getTitle())) {
            throw new ConflictException(messageUtil.getMessage("error.category.save.exist"));
        }

        // check title  constraint
        checkTitle(categoryRequest.getTitle());


        // Update the existing category with the new values
        category.setTitle(categoryRequest.getTitle());
        category.setIcon(categoryRequest.getIcon());
        category.setSeq(categoryRequest.getSeq());
        category.setActive(categoryRequest.getActive());
        category.setSlug(GeneralUtils.generateSlug(categoryRequest.getTitle()));

        // Save the updated category in the database
        Category saved = categoryRepository.save(category);

        // Return the updated category
        return ResponseEntity.ok(categoryMapper.mapToCategoryWithoutPropertyResponse(saved));
    }


    /**
     * this method created for getting category object for category property key service
     *
     * @param categoryId : represent category id
     * @return : Category object
     */
    public Category getCategoryById(Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException(messageUtil.getMessage("error.category.not-found")));
        return category;
    }

}



