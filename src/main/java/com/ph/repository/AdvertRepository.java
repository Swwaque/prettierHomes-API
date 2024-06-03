package com.ph.repository;

import com.ph.domain.entities.Advert;
import com.ph.domain.enums.StatusForAdvert;
import com.ph.payload.response.AdvertCategoryResponse;
import com.ph.payload.response.AdvertCityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    boolean existsByUser_Id(Long id);

    Advert findByImages_Id(Long id);

    @Query("""
            select a from Advert a
            where (a.createdAt between ?1 and ?2) and (?3 is null or a.category.id = ?3) and (?4 is null or a.advertType.id = ?4) and (?5 is null or a.statusForAdvert = ?5)""")
    List<Advert> findForExcel(@Nullable LocalDateTime createdAtStart,
                              @Nullable LocalDateTime createdAtEnd,
                              @Nullable Long id, @Nullable Long id1,
                              @Nullable StatusForAdvert statusForAdvert);

    //Not: AÇIKLAMASI ALTTAKİ NOTTA BULUNMAKTADIR

    //    NOT: AÇIKLAMA
    //    SELECT a FROM Advert a: Bu kısım, Advert sınıfından (a takma adıyla) nesneleri seçiyoruz. Yani sorgu sonucu Advert türünden nesneleri içeren bir liste veya sayfa elde edeceğiz.

    //"(:query IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%'))) ": Bu kısım, :query parametresine dayalı bir başlık (title) filtresi ekler. Eğer query null ise, bu koşul dikkate alınmaz. Aksi takdirde, başlık içinde LIKE kullanılarak case-insensitive (LOWER fonksiyonu ile) bir arama yapılır.
    //
    //"AND (:categoryId IS NULL OR a.category.id = :categoryId)": Bu kısım, :categoryId parametresine dayalı bir kategori filtresi ekler. Eğer categoryId null ise, bu koşul dikkate alınmaz. Aksi takdirde, kategori ID'sine göre eşleşen ilanlar seçilir.
    //
    //"AND (:advertTypeId IS NULL OR a.advertType.id = :advertTypeId)": Bu kısım, :advertTypeId parametresine dayalı bir reklam türü filtresi ekler. Eğer advertTypeId null ise, bu koşul dikkate alınmaz. Aksi takdirde, reklam türü ID'sine göre eşleşen ilanlar seçilir.
    //
    //"AND (:priceStart IS NULL OR a.price >= :priceStart)": Bu kısım, :priceStart parametresine dayalı bir fiyat başlangıcı filtresi ekler. Eğer priceStart null ise, bu koşul dikkate alınmaz. Aksi takdirde, belirtilen fiyat başlangıcından yüksek veya eşit fiyatlı ilanlar seçilir.
    //
    //"AND (:priceEnd IS NULL OR a.price <= :priceEnd)": Bu kısım, :priceEnd parametresine dayalı bir fiyat bitişi filtresi ekler. Eğer priceEnd null ise, bu koşul dikkate alınmaz. Aksi takdirde, belirtilen fiyat bitişinden düşük veya eşit fiyatlı ilanlar seçilir.
    //
    //"AND (:status IS NULL OR a.status = :status)": Bu kısım, :status parametresine dayalı bir durum filtresi ekler. Eğer status null ise, bu koşul dikkate alınmaz. Aksi takdirde, belirtilen duruma sahip ilanlar seçilir.
    //
    //Pageable pageable: Sayfalama ve sıralama işlemlerini yapmak için Pageable nesnesi kullanılır.

    //    NOT: AÇIKLAMA
    //      "AND (:categoryId IS NULL OR a.category.id = :categoryId) " +
    //            "AND (:advertTypeId IS NULL OR a.advertType.id = :advertTypeId) " +
    //    BİR ÖNCEKİ HALİ

    @Query("select a from Advert a where " +
            // q parametresi boş değilse, reklamın başlığı veya açıklamasında arama yapalım
            "(:q is null or :q = '' or lower(a.title) like lower(concat('%', :q, '%')) or lower(a.description) like lower(concat('%', :q, '%'))) and " +
            // category_id parametresi boş değilse, reklamın kategorisini kontrol edelim
            "(:category is null or a.category.id = :category) and " +
            // advert_type_id parametresi boş değilse, reklamın tipini kontrol edelim
            "(:advert_type_id is null or a.advertType.id = :advert_type_id) and " +
            // price_start ve price_end parametreleri boş değilse, reklamın fiyatını kontrol edelim
            "(:price_start is null or a.price >= :price_start) and " +
            "(:price_end is null or a.price <= :price_end) and " +
            // status parametresi boş değilse, reklamın durumunu kontrol edelim
            "( a.statusForAdvert = :status) and " +
            "(:country is null or a.country.id = :country) and " +
            "(:city is null or a.city.id = :city) and " +
            "(:district is null or a.district.id = :district) and " +
            // reklamın ve kategorisinin aktif olduğunu kontrol edelim
            "(a.isActive = true and a.category.active = true)")
    Page<Advert> findForAnyms(@Param("q") String query,
                              @Param("category") Long categoryId,
                              @Param("advert_type_id") Long advertTypeId,
                              @Param("price_start") Integer priceStart,
                              @Param("price_end") Integer priceEnd,
                              @Param("status") StatusForAdvert status,
                              @Param("country") Long country,
                              @Param("city") Long city,
                              @Param("district") Long district,
                              Pageable pageable);

    @Query("select a from Advert a where " +
            // q parametresi boş değilse, reklamın başlığı veya açıklamasında arama yapalım
            "(:q is null or :q = '' or lower(a.title) like lower(concat('%', :q, '%')) or lower(a.description) like lower(concat('%', :q, '%'))) and " +
            // category_id parametresi boş değilse, reklamın kategorisini kontrol edelim
            "(:category is null or a.category.id = :category) and " +
            // advert_type_id parametresi boş değilse, reklamın tipini kontrol edelim
            "(:advert_type_id is null or a.advertType.id = :advert_type_id) and " +
            // price_start ve price_end parametreleri boş değilse, reklamın fiyatını kontrol edelim
            "(:price_start is null or a.price >= :price_start) and " +
            "(:price_end is null or a.price <= :price_end) and " +
            // status parametresi boş değilse, reklamın durumunu kontrol edelim
            "(:status is null or a.statusForAdvert = :status)")
    Page<Advert> findForAdmin(@Param("q") String query,
                              @Param("category") Long categoryId,
                              @Param("advert_type_id") Long advertTypeId,
                              @Param("price_start") Integer priceStart,
                              @Param("price_end") Integer priceEnd,
                              @Param("status") StatusForAdvert status,
                              Pageable pageable);


    //    @Query("select a from Advert a where " +
    //            // category_id parametresi boş değilse, reklamın kategorisini kontrol edelim
    //            "(:category is null or a.category.id = :category) and " +
    //            // advert_type_id parametresi boş değilse, reklamın tipini kontrol edelim
    //            "(:advert_type_id is null or a.advertType.id = :advert_type_id) and " +
    //            // price_start ve price_end parametreleri boş değilse, reklamın fiyatını kontrol edelim
    //            "(:start_date is null or a.createdAt >= :start_date) and " +
    //            "(:end_date is null or a.createdAt <= :end_date) and " +
    //       //  status parametresi boş değilse, reklamın durumunu kontrol edelim
    //            "(:status is null or a.statusForAdvert = :status)" )
    //
    //    List<Advert> findForExcel(
    //            @Param("start_date") LocalDateTime startDate,
    //            @Param("end_date") LocalDateTime endDate,
    //            @Param("status") StatusForAdvert status,
    //            @Param("advert_type_id") Long advertTypeId,
    //            @Param("category") Long categoryId
    //
    //
    //    );


    //Start: categoryService için yazıldı
    List<Advert> findByCategory_Id(Long categoryId);
    //Finish: categoryService için yazıldı


    @Query("SELECT new com.ph.payload.response.AdvertCityResponse(a.city.id, a.city.name, COUNT(a.id)) FROM Advert a WHERE a.statusForAdvert = com.ph.domain.enums.StatusForAdvert.ACTIVATED GROUP BY a.city.id, a.city.name ORDER BY COUNT(a.id) DESC LIMIT :limit")
    List<AdvertCityResponse> getAdvertsByCities(@Param("limit") Integer limit);


    @Query("SELECT new com.ph.payload.response.AdvertCategoryResponse(a.category.id,a.category.title,a.category.icon, COUNT(a.id)) FROM Advert a WHERE a.statusForAdvert = com.ph.domain.enums.StatusForAdvert.ACTIVATED GROUP BY a.category.id, a.category.title, a.category.icon")
    List<AdvertCategoryResponse> getAdvertsByCategories();

    @Query("SELECT a FROM Advert a LEFT JOIN a.tourRequests t WHERE a.statusForAdvert = com.ph.domain.enums.StatusForAdvert.ACTIVATED GROUP BY a ORDER BY (3 * COUNT(t) + a.viewCount) DESC LIMIT :limit")
     List<Advert> findPopularAdverts(@Param("limit") Integer limit);
    @Query("SELECT a FROM Advert a LEFT JOIN a.tourRequests t WHERE a.statusForAdvert = com.ph.domain.enums.StatusForAdvert.ACTIVATED and a.advertType.title=:advertType GROUP BY a ORDER BY (3 * COUNT(t) + a.viewCount) DESC LIMIT :limit")
      List<Advert> findPopularSaleOrRentAdverts(@Param("limit") Integer limit,@Param("advertType") String advertType);



    //NOT: ESKİ QUERY (EKSİK)
    //    @Query("""
    //            select a from Advert a
    //            where upper(a.title) like upper(concat('%', ?1, '%')) or upper(a.description) like upper(concat('%', ?1, '%'))""")
    //    Page<Advert> searchAdvertByPage(@Nullable String query , Pageable pageable);


    Page<Advert> findByUser_Id(Long id, Pageable pageable);

    List<Advert> findByUser_Id(Long id);

    Optional<Advert> findBySlug(String slug);

    /**
     * This  created for getting all builtIn adverts
     *
     * @param b : represent builtIn
     * @return : all builtIn adverts
     */
    @Query("select a from Advert a where a.builtIn = ?1")
    List<Advert> findAllByBuiltIn(boolean b);

    boolean existsBy();

    @Query("SELECT COUNT(a) FROM Advert a WHERE a.statusForAdvert = com.ph.domain.enums.StatusForAdvert.ACTIVATED AND a.advertType.title = 'Rent'")
    Integer countActivatedRentAdverts();


    @Query("SELECT COUNT(a) FROM Advert a WHERE a.statusForAdvert = com.ph.domain.enums.StatusForAdvert.ACTIVATED AND a.advertType.title = 'Sale'")
    Integer countActivatedSaleAdverts();


}
