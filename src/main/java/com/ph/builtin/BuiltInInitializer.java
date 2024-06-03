package com.ph.builtin;

import com.ph.domain.entities.*;
import com.ph.domain.enums.KeyType;
import com.ph.domain.enums.StatusForAdvert;
import com.ph.repository.*;
import com.ph.security.role.Role;
import com.ph.utils.GeneralUtils;
import com.ph.utils.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Component
public class BuiltInInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdvertTypeRepository advertTypeRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryPropertyKeyRepository propertyKeyRepository;
    private final CityRepository cityRepository;
    private final DistrictsRepository districtsRepository;
    private final CountryRepository countriesRepository;
    private final AdvertRepository advertRepository;
    private final ImageRepository imageRepository;
    private final CategoryPropertyValueRepository categoryPropertyValueRepository;


    @Override
    public void run(String... args) throws Exception {
        // Initialize built-in data
        initializeUsers();
        initializeAdvertTypes();
        initializeCategories();
        initializePropertyKeys();
        initializeDefaultAdvert();
    }

    private void initializeUsers() {
        if (!userRepository.existsBy()) {
            User admin = User.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("admin@gmail.com")
                    .phone("(123) 456-7890")
                    .passwordHash(passwordEncoder.encode("admin123!"))
                    .role(Role.ADMIN)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(admin);

            User manager = User.builder()
                    .firstName("Jane")
                    .lastName("Doe")
                    .email("manager@gmail.com")
                    .phone("(123) 456-7891")
                    .passwordHash(passwordEncoder.encode("manager123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(manager);

            User managerUfuk = User.builder()
                    .firstName("Ufuk")
                    .lastName("Kochisar")
                    .email("kochisarufuk@gmail.com")
                    .phone("(532) 789-7890")
                    .passwordHash(passwordEncoder.encode("managerUfuk123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerUfuk);

            User managerBilal = User.builder()
                    .firstName("Bilal")
                    .lastName("Guney")
                    .email("matematikcimbilal@gmail.com")
                    .phone("(532) 789-7891")
                    .passwordHash(passwordEncoder.encode("managerBilal123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerBilal);

            User managerGulsum = User.builder()
                    .firstName("Gulsum")
                    .lastName("Satic")
                    .email("gulsum.satic@gmail.com")
                    .phone("(532) 789-7892")
                    .passwordHash(passwordEncoder.encode("managerGulsum123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerGulsum);

            User managerEbubekir = User.builder()
                    .firstName("Ebubekir")
                    .lastName("Can")
                    .email("ebubekirc442@gmail.com")
                    .phone("(532) 789-7893")
                    .passwordHash(passwordEncoder.encode("managerEbubekir123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerEbubekir);

            User managerDogan = User.builder()
                    .firstName("Dogan")
                    .lastName("Merden")
                    .email("adamd.born@gmail.com")
                    .phone("(532) 789-7894")
                    .passwordHash(passwordEncoder.encode("managerDogan123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerDogan);

            User managerZahide = User.builder()
                    .firstName("Zahide")
                    .lastName("Sarp")
                    .email("sarpzahide@gmail.com")
                    .phone("(532) 789-7895")
                    .passwordHash(passwordEncoder.encode("managerZahide123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerZahide);

            User managerZeynep = User.builder()
                    .firstName("Zeynep")
                    .lastName("Nune")
                    .email("zeynepnune@gmail.com")
                    .phone("(532) 789-7896")
                    .passwordHash(passwordEncoder.encode("managerZeynep123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerZeynep);

            User managerYasemin = User.builder()
                    .firstName("Yasemin")
                    .lastName("Dogan")
                    .email("yasemin.dn7@gmail.com")
                    .phone("(532) 789-7897")
                    .passwordHash(passwordEncoder.encode("managerYasemin123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerYasemin);

            User managerAbdurrahman = User.builder()
                    .firstName("Abdurrahman")
                    .lastName("Gocer")
                    .email("swwaque@gmail.com")
                    .phone("(532) 789-7898")
                    .passwordHash(passwordEncoder.encode("managerAbdurrahman123!"))
                    .role(Role.MANAGER)
                    .builtIn(true)
                    .enabled(true)
                    .build();
            userRepository.save(managerAbdurrahman);
        }
    }

    private void initializeAdvertTypes() {
        if (!advertTypeRepository.existsBy()) {
            advertTypeRepository.save(AdvertType.builder().title("Rent").builtIn(true).build());
            advertTypeRepository.save(AdvertType.builder().title("Sale").builtIn(true).build());
        }
    }

    private void initializeCategories() {
        if (!categoryRepository.existsBy()) {
            List<Category> categories = List.of(
                    Category.builder().title("House").icon("fa-solid fa-house").slug("house" + "-" + System.currentTimeMillis()).seq(1).active(true).builtIn(true).build(),
                    Category.builder().title("Apartment").icon("fa-solid fa-building").slug("apartment" + "-" + System.currentTimeMillis()).seq(2).active(true).builtIn(true).build(),
                    Category.builder().title("Office").icon("fa-solid fa-house-laptop").slug("office" + "-" + System.currentTimeMillis()).seq(3).active(true).builtIn(true).build(),
                    Category.builder().title("Villa").icon("fa-solid fa-place-of-worship").slug("villa" + "-" + System.currentTimeMillis()).seq(4).active(true).builtIn(true).build(),
                    Category.builder().title("Land").icon("fa-solid fa-mountain-sun").slug("land" + "-" + System.currentTimeMillis()).seq(5).active(true).builtIn(true).build(),
                    Category.builder().title("Shop").icon("fa-solid fa-store").slug("shop" + "-" + System.currentTimeMillis()).seq(6).active(true).builtIn(true).build()
            );
            categoryRepository.saveAll(categories);
        }
    }

    private void initializePropertyKeys() {
        if (!propertyKeyRepository.existsBy()) {
            //For House
            List<CategoryPropertyKey> houseProperties = List.of(
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Size").keyType(KeyType.NUMBER).prefix(null).suffix("m²").builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Bedrooms").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Bathrooms").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Garage").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Year of Build").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Furniture").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Maintenance Fee").keyType(KeyType.NUMBER).prefix("$").suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(1L).get()).name("Terrace").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build());

            propertyKeyRepository.saveAll(houseProperties);

            //For Apartment
            List<CategoryPropertyKey> apartmentProperties = List.of(
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Size").keyType(KeyType.NUMBER).prefix(null).suffix("m²").builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Bedrooms").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Bathrooms").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Parking Space").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Year of Build").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Furniture").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Maintenance Fee").keyType(KeyType.NUMBER).prefix("$").suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Balcony").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(2L).get()).name("Elevator").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build());

            propertyKeyRepository.saveAll(apartmentProperties);


            //For Office
            List<CategoryPropertyKey> officeProperties = List.of(
                    CategoryPropertyKey.builder().category(categoryRepository.findById(3L).get()).name("Size").keyType(KeyType.NUMBER).prefix(null).suffix("m²").builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(3L).get()).name("Parking Space").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(3L).get()).name("Year of Build").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(3L).get()).name("Furniture").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(3L).get()).name("Maintenance Fee").keyType(KeyType.NUMBER).prefix("$").suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(3L).get()).name("Elevator").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build());

            propertyKeyRepository.saveAll(officeProperties);

            //For Villa
            List<CategoryPropertyKey> villaProperties = List.of(
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Size").keyType(KeyType.NUMBER).prefix(null).suffix("m²").builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Bedrooms").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Bathrooms").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Garage").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Land Area").keyType(KeyType.NUMBER).prefix(null).suffix("m²").builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Year of Build").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Furniture").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("Swimming Pool").keyType(KeyType.TEXT).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(4L).get()).name("View").keyType(KeyType.TEXT).prefix(null).suffix(null).builtIn(true).build());

            propertyKeyRepository.saveAll(villaProperties);

            //For Land
            List<CategoryPropertyKey> landProperties = List.of(
                    CategoryPropertyKey.builder().category(categoryRepository.findById(5L).get()).name("Size").keyType(KeyType.NUMBER).prefix(null).suffix("m²").builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(5L).get()).name("Zoning").keyType(KeyType.TEXT).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(5L).get()).name("Access Road").keyType(KeyType.TEXT).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(5L).get()).name("Legal Status").keyType(KeyType.TEXT).prefix(null).suffix(null).builtIn(true).build());

            propertyKeyRepository.saveAll(landProperties);

            //For Shop
            List<CategoryPropertyKey> shopProperties = List.of(
                    CategoryPropertyKey.builder().category(categoryRepository.findById(6L).get()).name("Size").builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(6L).get()).name("Parking Space").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(6L).get()).name("Year of Build").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(6L).get()).name("Furniture").keyType(KeyType.BOOLEAN).prefix(null).suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(6L).get()).name("Maintenance Fee").keyType(KeyType.NUMBER).prefix("$").suffix(null).builtIn(true).build(),
                    CategoryPropertyKey.builder().category(categoryRepository.findById(6L).get()).name("Elevator").keyType(KeyType.NUMBER).prefix(null).suffix(null).builtIn(true).build());

            propertyKeyRepository.saveAll(shopProperties);
        }
    }


    private void initializeDefaultCategoryPropertyValues(Long advertId, Long categoryId) {
          if (categoryId == 1L) {
            List<CategoryPropertyValue> houseValues
                    = List.of(
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(1L).get()).value("120").build(),
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(2L).get()).value("2").build(),
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(3L).get()).value("1").build(),
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(4L).get()).value("NO").build(),
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(5L).get()).value("2010").build(),
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(6L).get()).value("YES").build(),
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(7L).get()).value("900").build(),
                    CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(8L).get()).value("NO").build()
            );
            categoryPropertyValueRepository.saveAll(houseValues);

        } else if (categoryId == 2L) {
            List<CategoryPropertyValue> apartmentValues =
                    List.of(
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(9L).get()).value("140").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(10L).get()).value("3").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(11L).get()).value("2").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(12L).get()).value("YES").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(13L).get()).value("2020").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(14L).get()).value("NO").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(15L).get()).value("1200").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(16L).get()).value("2").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(17L).get()).value("1").build()
                    );
            categoryPropertyValueRepository.saveAll(apartmentValues);


        } else if (categoryId==3L) {
            List<CategoryPropertyValue> officeValues=
                    List.of(
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(18L).get()).value("220").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(19L).get()).value("NO").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(20L).get()).value("2022").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(21L).get()).value("YES").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(22L).get()).value("2200").build(),
                            CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(23L).get()).value("YES").build()
                    );
            categoryPropertyValueRepository.saveAll(officeValues);


        } else if (categoryId==4L) {
             List<CategoryPropertyValue> villaValues=
                     List.of(
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(24L).get()).value("440").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(25L).get()).value("3").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(26L).get()).value("2").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(27L).get()).value("YES").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(28L).get()).value("500").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(29L).get()).value("2023").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(30L).get()).value("YES").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(31L).get()).value("YES").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(32L).get()).value("Lakeside").build()

                     );
             categoryPropertyValueRepository.saveAll(villaValues);


         } else if (categoryId==5L) {
             List<CategoryPropertyValue> landValues=
                     List.of(
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(33L).get()).value("1000").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(34L).get()).value("Suitable").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(35L).get()).value("Close to the road").build(),
                             CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(36L).get()).value("Farmland").build()
                     );
             categoryPropertyValueRepository.saveAll(landValues);


         } else if (categoryId==6L) {
              List<CategoryPropertyValue> shopValues=
                      List.of(
                              CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(37L).get()).value("32000").build(),
                              CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(38L).get()).value("YES").build(),
                              CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(39L).get()).value("2015").build(),
                              CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(40L).get()).value("YES").build(),
                              CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(41L).get()).value("15000").build(),
                              CategoryPropertyValue.builder().advert(advertRepository.findById(advertId).get()).categoryPropertyKey(propertyKeyRepository.findById(42L).get()).value("4").build()
                      );
              categoryPropertyValueRepository.saveAll(shopValues);

          }
    }

    private void initializeDefaultAdvert() {
        if (!advertRepository.existsBy()) {
            List<Map<String, Object>> adverts = new ArrayList<>(Arrays.asList(
                    Map.of("title", "Comfortable house in Kusadasi",
                            "description", "Beautifully designed and spacious.",
                            "price", 100000.0,
                            "ccd", new Long[]{223L, 4131L, 48448L},
                            "address", "Bayraklıdede mah. No: 15",
                            "location", new Double[]{37.8632398, 27.266873},
                            "category", 1L,
                            "advertType", 1L,
                            "image", "static/home1.jpg",
                            "user", "admin@gmail.com"),

                    Map.of("title", "Safe location in the center of Kösk",
                            "description", "Located in a prime neighborhood.",
                            "price", 200000.0,
                            "ccd", new Long[]{223L, 4131L, 48447L},
                            "address", "Soğukkuyu Mah. No: 16",
                            "location", new Double[]{37.91877855, 28.048488672658053},
                            "category", 2L,
                            "advertType", 1L,
                            "image", "static/apartment1.jpg",
                            "user", "admin@gmail.com"),

                    Map.of("title", "Modern Office",
                            "description", "Modern amenities for your comfort.",
                            "price", 300000.0,
                            "ccd", new Long[]{223L, 4131L, 48443L},
                            "address", "Acarlar Mah. No: 17",
                            "location", new Double[]{37.8537655, 27.7235028},
                            "category", 3L,
                            "advertType", 2L,
                            "image", "static/office1.jpg",
                            "user", "admin@gmail.com"),

                    Map.of("title", "Villa with sea and forest views in Didim",
                            "description", "Stunning views from every room.",
                            "price", 400000.0,
                            "ccd", new Long[]{223L, 4131L, 48441L},
                            "address", "Altınkum Mah. No:25",
                            "location", new Double[]{37.3769167, 27.2668129},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa1.jpg",
                            "user", "admin@gmail.com"),

                    Map.of("title", "Investment land close to the center",
                            "description", "Located close to the city.",
                            "price", 500000.0,
                            "ccd", new Long[]{223L, 4131L, 48438L},
                            "address", "Akçay Mah. No: 19",
                            "location", new Double[]{37.6739133, 28.3132041},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land3.jpg",
                            "user", "admin@gmail.com"),

                    Map.of("title", "Store in a busy location on the main road",
                            "description", "Our original designs and quality fabrics fit every style..",
                            "price", 600000.0,
                            "ccd", new Long[]{223L, 4131L, 48451L},
                            "address", "Atatürk Mah. No: 20",
                            "location", new Double[]{37.7519762, 27.4056294},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop1.jpg",
                            "user", "admin@gmail.com"),

                    /////////////////////////////////////////////////////

                    Map.of("title", "Scenic_Spacious_Garden_House",
                            "description", "A spacious and modern-designed house with a scenic view of nature in Çayyolu.",
                            "price", 56715.0,
                            "ccd", new Long[]{223L, 4127L, 48385L},
                            "address", "Çayyolu Mah. 456. St. No: 7",
                            "location", new Double[]{39.874185, 32.674029},
                            "category", 1L,
                            "advertType", 1L,
                            "image", "static/home2.jpg",
                            "user", "kochisarufuk@gmail.com"),

                    Map.of("title", "Modern Apartment in Central Location",
                            "description", "A renovated and modern-designed apartment in a central location in Kızılay",
                            "price", 37375.0,
                            "ccd", new Long[]{223L, 4127L, 48385L},
                            "address", "Kızılay Mah. 789. Ave. No: 10",
                            "location", new Double[]{39.919838, 32.852380},
                            "category", 2L,
                            "advertType", 2L,
                            "image", "static/apartment2.jpg",
                            "user", "kochisarufuk@gmail.com"),

                    Map.of("title", "Modern Office Space",
                            "description", "A spacious and user-friendly office space with modern infrastructure in Çankaya.",
                            "price", 43280.0,
                            "ccd", new Long[]{223L, 4127L, 48385L},
                            "address", "Çankaya Mah. 123. St. No: 5",
                            "location", new Double[]{39.8853321, 32.8554966},
                            "category", 3L,
                            "advertType", 1L,
                            "image", "static/office2.jpg",
                            "user", "kochisarufuk@gmail.com"),

                    Map.of("title", "Luxury Villa With Pool",
                            "description", "A luxurious villa with a pool, nestled in nature in Gölbaşı.",
                            "price", 550000.0,
                            "ccd", new Long[]{223L, 4127L, 48390L},
                            "address", "Sakura Cad. No: 18",
                            "location", new Double[]{39.7925289, 32.8066652},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa2.jpg",
                            "user", "kochisarufuk@gmail.com"),

                    Map.of("title", "Suitable Land for Investment",
                            "description", "A completed infrastructure land suitable for investment in Etimesgut.",
                            "price", 75715.0,
                            "ccd", new Long[]{223L, 4127L, 48388L},
                            "address", "Etimesgut Mah. 7890. St. No: 12",
                            "location", new Double[]{39.9489423, 32.6620792},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land6.jpg",
                            "user", "kochisarufuk@gmail.com"),

                    Map.of("title", "Shop on the Main Street",
                            "description", "A shop with high customer potential located on the main street in Mamak.",
                            "price", 600000.0,
                            "ccd", new Long[]{223L, 4127L, 48397L},
                            "address", "Sakura Cad. No: 20",
                            "location", new Double[]{39.9329813, 32.9082154},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop3.jpg",
                            "user", "kochisarufuk@gmail.com"),

                    ///////////////////////////////////////////////////////

                    Map.of("title", "Serene Family Home in İzmit",
                            "description", "Escape to this tranquil family home nestled in the heart of İzmit.",
                            "price", 300000.0,
                            "ccd", new Long[]{223L, 4172L, 48925L},
                            "address", "İzmit Mah. 456. St. No: 7",
                            "location", new Double[]{40.7718611, 29.9498078},
                            "category", 1L,
                            "advertType", 2L,
                            "image", "static/home6.jpg",
                            "user", "matematikcimbilal@gmail.com"),

                    Map.of("title", "Urban Oasis Stylish Apartment in Gebze",
                            "description", "Discover this stylish apartment offering an urban oasis in Gebze.",
                            "price", 200000.0,
                            "ccd", new Long[]{223L, 4172L, 48923L},
                            "address", "Gebze Mah. 789. Ave. No: 10",
                            "location", new Double[]{40.8006696, 29.431767},
                            "category", 2L,
                            "advertType", 2L,
                            "image", "static/apartment3.jpg",
                            "user", "matematikcimbilal@gmail.com"),

                    Map.of("title", "Prime Office Space in İzmit Center",
                            "description", "Experience the convenience of this prime office space located in İzmit center.",
                            "price", 350000.0,
                            "ccd", new Long[]{223L, 4172L, 48925L},
                            "address", "İzmit Mah. 123. St. No: 5",
                            "location", new Double[]{40.7718611, 29.9498078},
                            "category", 3L,
                            "advertType", 1L,
                            "image", "static/office3.jpg",
                            "user", "matematikcimbilal@gmail.com"),

                    Map.of("title", "Seaview Villa in Basiskele",
                            "description", "Enjoy luxury in this privileged villa with panoramic sea views in Başiskele.",
                            "price", 600000.0,
                            "ccd", new Long[]{223L, 4172L, 48918L},
                            "address", "Sakura Cad. No: 18",
                            "location", new Double[]{40.64592185, 29.951563320964866},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa3.jpg",
                            "user", "matematikcimbilal@gmail.com"),

                    Map.of("title", "Investment opportunity Land in Darıca",
                            "description", "Don't miss this investment opportunity with this land located in Darıca.",
                            "price", 500000.0,
                            "ccd", new Long[]{223L, 4172L, 48920L},
                            "address", "Darıca Mah. 7890. St. No: 12",
                            "location", new Double[]{40.7574799, 29.3840563},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land1.jpg",
                            "user", "matematikcimbilal@gmail.com"),

                    Map.of("title", "Shopping at Gulf commercial center",
                            "description", "Become part of the bustling commercial hub with this shop in Körfez.",
                            "price", 400000.0,
                            "ccd", new Long[]{223L, 4172L, 48929L},
                            "address", "Körfez Mah. 4567. Ave. No: 20",
                            "location", new Double[]{40.760756, 29.7839402},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop4.jpg",
                            "user", "matematikcimbilal@gmail.com"),

                    /////////////////////////////////////////////////////////

                    Map.of("title", "Calm Family House in Batman Center",
                            "description", "Escape to this serene family home located in the heart of Batman Center.",
                            "price", 250000.0,
                            "ccd", new Long[]{223L, 4134L, 48482L},
                            "address", "Yeşilova Mah. No: 10",
                            "location", new Double[]{38.3339473, 41.4202341},
                            "category", 1L,
                            "advertType", 2L,
                            "image", "static/home4.jpg",
                            "user", "gulsum.satic@gmail.com"),

                    Map.of("title", "Stylish Apartment in Besiri",
                            "description", "Experience urban comfort in this stylish apartment nestled in Beşiri.",
                            "price", 150000.0,
                            "ccd", new Long[]{223L, 4134L, 48478L},
                            "address", "Beşiri Mah. 789. Ave. No: 10",
                            "location", new Double[]{37.9162455, 41.2927679},
                            "category", 2L,
                            "advertType", 1L,
                            "image", "static/apartment4.jpg",
                            "user", "gulsum.satic@gmail.com"),

                    Map.of("title", "Modern Office in Batman Center",
                            "description", "Discover this modern office space situated at the heart of Batman Center.",
                            "price", 200000.0,
                            "ccd", new Long[]{223L, 4134L, 48477L},
                            "address", "Batman Center Mah. 123. Ave. No: 5",
                            "location", new Double[]{37.7874104, 41.2573924},
                            "category", 3L,
                            "advertType", 1L,
                            "image", "static/office4.jpg",
                            "user", "gulsum.satic@gmail.com"),

                    Map.of("title", "Secluded Villa in Gercüs",
                            "description", "Indulge in luxury with this exclusive villa offering seclusion in Gercüş.",
                            "price", 400000.0,
                            "ccd", new Long[]{223L, 4134L, 48479L},
                            "address", "Gercüş Mah. 101. St. No: 3",
                            "location", new Double[]{37.5683088, 41.3852365},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa4.jpg",
                            "user", "gulsum.satic@gmail.com"),

                    Map.of("title", "Investment Opportunity Land in Hasankeyft",
                            "description", "Don't miss this investment opportunity with this land located in Hasankeyf.",
                            "price", 300000.0,
                            "ccd", new Long[]{223L, 4134L, 48480L},
                            "address", "Hasankeyf Mah. 7890. St. No: 12",
                            "location", new Double[]{37.7304689, 41.4160863},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land2.jpg",
                            "user", "gulsum.satic@gmail.com"),

                    Map.of("title", "Shop on Main Street in Kozluk",
                            "description", "Grab this prime location shop situated on the main street in Kozluk.",
                            "price", 180000.0,
                            "ccd", new Long[]{223L, 4134L, 48481L},
                            "address", "Kozluk Mah. 4567. Ave. No: 20",
                            "location", new Double[]{38.1933398, 41.4886025},
                            "category", 6L,
                            "advertType", 1L,
                            "image", "static/shop6.jpg",
                            "user", "gulsum.satic@gmail.com"),

                    //////////////////////////////////////////////////////

                    Map.of("title", "Family Residence in Historic Kastamonu",
                            "description", "Discover peace in this charming family home located in the center of Kastamonu, surrounded by nature.",
                            "price", 280000.0,
                            "ccd", new Long[]{223L, 4166L, 48867L},
                            "address", "Kastamonu Center Mah. 456. St. No: 7",
                            "location", new Double[]{41.3680217, 33.7619177},
                            "category", 1L,
                            "advertType", 2L,
                            "image", "static/home5.jpg",
                            "user", "ebubekirc442@gmail.com"),

                    Map.of("title", "Modern Apartment in Scenic İnebolu",
                            "description", "Escape to coastal bliss in this modern apartment set amidst the scenic beauty of İnebolu.",
                            "price", 150000.0,
                            "ccd", new Long[]{223L, 4166L, 48866L},
                            "address", "İnebolu Mah. 789. Ave. No: 10",
                            "location", new Double[]{41.9785763, 33.7599031},
                            "category", 2L,
                            "advertType", 1L,
                            "image", "static/apartment5.jpg",
                            "user", "ebubekirc442@gmail.com"),

                    Map.of("title", "Contemporary Office Space in Vibrant Kastamonu",
                            "description", "Experience urban sophistication in this contemporary office space located in the vibrant heart of Kastamonu.",
                            "price", 200000.0,
                            "ccd", new Long[]{223L, 4166L, 48867L},
                            "address", "Kastamonu Center Mah. 123. Ave. No: 5",
                            "location", new Double[]{41.3680217, 33.7619177},
                            "category", 3L,
                            "advertType", 2L,
                            "image", "static/office5.jpg",
                            "user", "ebubekirc442@gmail.com"),

                    Map.of("title", "Luxurious Villa Escape in Idyllic Cide",
                            "description", "Indulge in luxury at this secluded gem, offering a luxurious villa escape in the idyllic surroundings of Cide.",
                            "price", 400000.0,
                            "ccd", new Long[]{223L, 4166L, 48859L},
                            "address", "Sakura Cad. No: 18",
                            "location", new Double[]{41.8915711, 33.003712},
                            "category", 4L,
                            "advertType", 1L,
                            "image", "static/villa6.jpg",
                            "user", "ebubekirc442@gmail.com"),

                    Map.of("title", "Vast Land in Tranquil Arac",
                            "description", "Explore investment opportunities with this vast land nestled in the tranquil surroundings of Araç.",
                            "price", 300000.0,
                            "ccd", new Long[]{223L, 4166L, 48856L},
                            "address", "Araç Mah. 7890. St. No: 12",
                            "location", new Double[]{41.2412014, 33.324933},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land4.jpg",
                            "user", "ebubekirc442@gmail.com"),

                    Map.of("title", "Bustling Shop in Central Tosya",
                            "description", "Secure your spot at the prime corner with this bustling shop located in central Tosya.",
                            "price", 180000.0,
                            "ccd", new Long[]{223L, 4166L, 48873L},
                            "address", "Tosya Mah. 4567. Ave. No: 20",
                            "location", new Double[]{41.0164957, 34.0386079},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop8.jpg",
                            "user", "ebubekirc442@gmail.com"),

                    //////////////////////////////////////////////////////

                    Map.of("title", "Exquisite Family Residence in the Heart of Bustling Izmir",
                            "description", "For those who want to escape from the busy life of Izmir, step into the world of peace intertwined with nature.",
                            "price", 320000.0,
                            "ccd", new Long[]{223L, 4161L, 48814L},
                            "address", "Izmir City Center Mah. 456. St. No: 7",
                            "location", new Double[]{38.4185042, 27.1290835},
                            "category", 1L,
                            "advertType", 2L,
                            "image", "static/home10.jpg",
                            "user", "adamd.born@gmail.com"),

                    Map.of("title", "Luxurious Coastal Apartment in Alsancak s Prestigious Enclave",
                            "description", "Enjoy the epitome of coastal living with this luxury apartment located in the heart of Alsancak's prestigious residential area.",
                            "price", 275000.0,
                            "ccd", new Long[]{223L, 4161L, 48814L},
                            "address", "Alsancak Mah. 789. Ave. No: 10",
                            "location", new Double[]{38.4185042, 27.1290835},
                            "category", 2L,
                            "advertType", 1L,
                            "image", "static/apartment6.jpg",
                            "user", "adamd.born@gmail.com"),

                    Map.of("title", "State-of-the-Art Office Space in Konak s Business District",
                            "description", "Get the opportunity to work with high efficiency and in a peaceful environment in this office located in the heart of Konak's lively business district.",
                            "price", 350000.0,
                            "ccd", new Long[]{223L, 4161L, 48814L},
                            "address", "Konak Mah. 123. Ave. No: 5",
                            "location", new Double[]{38.4185042, 27.1290835},
                            "category", 3L,
                            "advertType", 2L,
                            "image", "static/office6.jpg",
                            "user", "adamd.born@gmail.com"),

                    Map.of("title", "Secluded Villa Retreat in Urla s Idyllic Countryside",
                            "description", "Embark on an elegant escape to this secluded villa retreat set amid Urla's idyllic countryside. Surrounded by lush greenery and tranquil landscapes, this elegant retreat offers a haven of peace and tranquility.",
                            "price", 600000.0,
                            "ccd", new Long[]{223L, 4161L, 48823L},
                            "address", "Urla Mah. 101. St. No: 3",
                            "location", new Double[]{38.3228184, 26.7672998},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa5.jpg",
                            "user", "adamd.born@gmail.com"),

                    Map.of("title", "Expansive Land Parcel in Bornova s Flourishing Landscape",
                            "description", "Unlock endless possibilities with this large parcel of land located 20 km from Bornova. This first-class investment opportunity, spread over large fertile lands, offers unlimited development and growth potential. Use it as you wish.",
                            "price", 480000.0,
                            "ccd", new Long[]{223L, 4161L, 48800L},
                            "address", "Bornova Mah. 7890. St. No: 12",
                            "location", new Double[]{38.4660651, 27.2190721},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land5.jpg",
                            "user", "adamd.born@gmail.com"),

                    Map.of("title", "Prime Corner Shop in Karşıyakas Vibrant Retail Hub",
                            "description", "Position your brand for retail excellence with this exclusive corner store located in the heart of Karşıyaka's vibrant retail centre. This strategic location with high foot traffic and excellent visibility offers unparalleled visibility for your business.",
                            "price", 150000.0,
                            "ccd", new Long[]{223L, 4161L, 48810L},
                            "address", "Karşıyaka Mah. 4567. Ave. No: 20",
                            "location", new Double[]{38.503445150000005, 27.113455434284667},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop2.jpg",
                            "user", "adamd.born@gmail.com"),

                    //////////////////////////////////////////////////////

                    Map.of("title", "A house in touch with nature in Sinop",
                            "description", "Experience serene living in this elegant residence located in the heart of Sinop. With its spacious garden and modern design, this home offers a peaceful retreat integrated with the city's energy.",
                            "price", 550000.0,
                            "ccd", new Long[]{223L, 4190L, 49144L},
                            "address", "Sinop City Center Mah. 123. St. No: 4",
                            "location", new Double[]{41.6476377, 34.9560137},
                            "category", 1L,
                            "advertType", 1L,
                            "image", "static/home7.jpg",
                            "user", "sarpzahide@gmail.com"),

                    Map.of("title", "Modern Apartment Living in Boyabat",
                            "description", "Enjoy the natural radiance of Boyabat in this modern apartment. With its spacious and bright interiors, this apartment offers a serene and comfortable living space.",
                            "price", 270000.0,
                            "ccd", new Long[]{223L, 4190L, 49138L},
                            "address", "Boyabat Mah. 456. Ave. No: 12",
                            "location", new Double[]{41.4689736, 34.7672274},
                            "category", 2L,
                            "advertType", 2L,
                            "image", "static/apartment7.jpg",
                            "user", "sarpzahide@gmail.com"),

                    Map.of("title", "Modern Office Space in Gerze",
                            "description", "Elevate your business with this modern office space in Gerze. Offering superior comfort and functionality, this office provides the perfect environment for your business.",
                            "price", 390000.0,
                            "ccd", new Long[]{223L, 4190L, 49142L},
                            "address", "Gerze Mah. 7890. Ave. No: 5",
                            "location", new Double[]{41.8032697, 35.1996164},
                            "category", 3L,
                            "advertType", 1L,
                            "image", "static/office7.jpg",
                            "user", "sarpzahide@gmail.com"),

                    Map.of("title", "Villa Life in Ayancik",
                            "description", "Experience luxury villa living in the tranquil atmosphere of Ayancık. With luxurious details, this villa offers a comfortable and relaxing lifestyle amidst nature's embrace.",
                            "price", 480000.0,
                            "ccd", new Long[]{223L, 4190L, 49137L},
                            "address", "Ayancık Mah. 1234. St. No: 6",
                            "location", new Double[]{41.9465719, 34.5878729},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa7.jpg",
                            "user", "sarpzahide@gmail.com"),

                    Map.of("title", "Large Parcel of Land in Erfelek",
                            "description", "This expansive parcel of land in the developing area of Erfelek offers an ideal investment opportunity. With its strategic location, this land could be a lucrative investment in Yozgat's growing real estate market.",
                            "price", 560000.0,
                            "ccd", new Long[]{223L, 4190L, 49141L},
                            "address", "Erfelek Mah. 101. St. No: 8",
                            "location", new Double[]{41.8793624, 34.9080508},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land7.jpg",
                            "user", "sarpzahide@gmail.com"),

                    Map.of("title", "Shop on Main Street in Türkeli",
                            "description", "Located on the bustling main street of Türkeli, this shop offers the perfect location for shopping delight. With high visibility and heavy foot traffic, this shop is an ideal venue for your business.",
                            "price", 640000.0,
                            "ccd", new Long[]{223L, 4190L, 49145L},
                            "address", "Türkeli Mah. 7890. St. No: 10",
                            "location", new Double[]{41.851414500000004, 34.3721368459243},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop7.jpg",
                            "user", "sarpzahide@gmail.com"),

                    //////////////////////////////////////////////////////

                    Map.of("title", "House with Unique View of Uludag",
                            "description", "Ideal living space for nature lovers. Enjoy a peaceful and serene life surrounded by the magnificent view of Uludağ. This house equipped with modern details offers a life intertwined with nature.",
                            "price", 600000.0,
                            "ccd", new Long[]{223L, 4141L, 48543L},
                            "address", "Uludağ Mahallesi, Bursa, Turkey, No: 12",
                            "location", new Double[]{40.1982203, 29.0612098},
                            "category", 1L,
                            "advertType", 2L,
                            "image", "static/home8.jpg",
                            "user", "yasemin@gmail.com"),

                    Map.of("title", "Comfort in the Heart of the City",
                            "description", "Experience modern living in the heart of the city. Feel the energy of the city and enjoy comfort in this apartment with innovative design and stylish details.",
                            "price", 300000.0,
                            "ccd", new Long[]{223L, 4141L, 48543L},
                            "address", "Center Mahallesi, Bursa, Turkey, No: 10",
                            "location", new Double[]{40.1982203, 29.0612098},
                            "category", 2L,
                            "advertType", 1L,
                            "image", "static/apartment8.jpg",
                            "user", "yasemin@gmail.com"),

                    Map.of("title", "Modern Office in Nilüfer s Central Business Area",
                            "description", "Combine innovation and productivity. In this modern office space located in Nilüfer's bustling business center, experience working in an environment that triggers creativity and collaboration.",
                            "price", 400000.0,
                            "ccd", new Long[]{223L, 4141L, 48540L},
                            "address", "Nilüfer Mahallesi, Bursa, Turkey, No: 5",
                            "location", new Double[]{40.2129489, 28.9627863},
                            "category", 3L,
                            "advertType", 1L,
                            "image", "static/office8.jpg",
                            "user", "yasemin@gmail.com"),

                    Map.of("title", "Luxury Living in Nature s Embrace",
                            "description", "Open the doors to a peaceful life nestled in the heart of nature. In this luxurious villa with the unique view of Gölyazı, forget the fatigue of the day and enjoy the tranquility.",
                            "price", 400000.0,
                            "ccd", new Long[]{223L, 4141L, 48540L},
                            "address", "Gölyazı Mahallesi, Bursa, Turkey, No: 15",
                            "location", new Double[]{40.2129489, 28.9627863},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa8.jpg",
                            "user", "yasemin@gmail.com"),

                    Map.of("title", "Land in Yildirim s Developing Area",
                            "description", "Invest in a developing area. This land in Yıldırım's potential-filled region offers a vast opportunity for development and growth. An ideal choice to turn your vision into reality.",
                            "price", 500000.0,
                            "ccd", new Long[]{223L, 4141L, 48545L},
                            "address", "Yıldırım Mahallesi, Bursa, Turkey, No: 18",
                            "location", new Double[]{40.1877165, 29.0809205},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land8.jpg",
                            "user", "yasemin@gmail.com"),

                    Map.of("title", "Store in Gürsu s Busy Market",
                            "description", "Position your brand at the center of shopping. This corner store located in Gürsu's bustling market offers the perfect environment to showcase your products and attract customers with its strategic location and high pedestrian traffic.",
                            "price", 350000.0,
                            "ccd", new Long[]{223L, 4141L, 48545L},
                            "address", "Gürsu Mahallesi, Bursa, Turkey, No: 20",
                            "location", new Double[]{40.1877165, 29.0809205},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop5.jpg",
                            "user", "yasemin@gmail.com"),

                    //////////////////////////////////////////////////////


                    Map.of("title", "Exquisite Residence in Gaziantep s Historic Heart",
                            "description", "It is 10 km away from Gaziantep and surrounded by nature. Boasting timeless architecture and luxurious amenities, this house offers a quiet retreat in contrast to the lively energy of the city center.",
                            "price", 500000.0,
                            "ccd", new Long[]{223L, 4153L, 48698L},
                            "address", "Gaziantep City Center Mah. 456. St. No: 7",
                            "location", new Double[]{37.0727587, 37.3949769},
                            "category", 1L,
                            "advertType", 1L,
                            "image", "static/home9.jpg",
                            "user", "zeynepnune@gmail.com"),

                    Map.of("title", "Modern Apartment Living in Şahinbey s Cultural District",
                            "description", "Experience urban living at its finest in this modern apartment located in Şahinbey's vibrant cultural district. With its sleek design and unparalleled views, this residence offers a sophisticated retreat for those seeking the ultimate in city living.",
                            "price", 250000.0,
                            "ccd", new Long[]{223L, 4153L, 48697L},
                            "address", "Şahinbey Mah. 789. Ave. No: 10",
                            "location", new Double[]{37.0575903, 37.3794008},
                            "category", 2L,
                            "advertType", 2L,
                            "image", "static/apartment9.jpg",
                            "user", "zeynepnune@gmail.com"),

                    Map.of("title", "State-of-the-Art Office in Sehitkamil s Business Center",
                            "description", "Take your business to the next level in this office space located in the vibrant business center of Şehitkamil. Designed for productivity and collaboration, this workspace provides the perfect environment to fuel success in today's competitive marketplace.",
                            "price", 350000.0,
                            "ccd", new Long[]{223L, 4153L, 48698L},
                            "address", "Şehitkamil Mah. 123. Ave. No: 5",
                            "location", new Double[]{37.0727587, 37.3949769},
                            "category", 3L,
                            "advertType", 1L,
                            "image", "static/office9.jpg",
                            "user", "zeynepnune@gmail.com"),

                    Map.of("title", "Luxury Villa Retreat in Araban s Picturesque Countryside",
                            "description", "Escape to tranquility with this luxury villa retreat nestled in the picturesque countryside of Araban. Surrounded by lush landscapes and breathtaking views, this elegant villa offers a private sanctuary for relaxation and rejuvenation.",
                            "price", 600000.0,
                            "ccd", new Long[]{223L, 4153L, 48691L},
                            "address", "Araban Mah. 101. St. No: 3",
                            "location", new Double[]{37.4252863, 37.6894188},
                            "category", 4L,
                            "advertType", 2L,
                            "image", "static/villa9.jpg",
                            "user", "zeynepnune@gmail.com"),

                    Map.of("title", "Prime Parcel in Nizip s Growing Community",
                            "description", "Discover the potential of this prime parcel located in the growing community of Nizip. With its expansive size and strategic location, this land offers endless opportunities for development and investment in Gaziantep's thriving real estate market.",
                            "price", 300000.0,
                            "ccd", new Long[]{223L, 4153L, 48694L},
                            "address", "Nizip Mah. 7890. St. No: 12",
                            "location", new Double[]{37.0108864, 37.7931734},
                            "category", 5L,
                            "advertType", 1L,
                            "image", "static/land9.jpg",
                            "user", "zeynepnune@gmail.com"),

                    Map.of("title", "Corner Shop in Karkamıs s Bustling Marketplace",
                            "description", "Position your brand for success with this corner shop located in Karkamış's bustling marketplace. Offering high visibility and steady foot traffic, this retail space is the perfect venue for showcasing your products and attracting customers.",
                            "price", 150000.0,
                            "ccd", new Long[]{223L, 4153L, 48693L},
                            "address", "Karkamış Mah. 4567. Ave. No: 20",
                            "location", new Double[]{36.82947115, 38.015398741406926},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop9.jpg",
                            "user", "zeynepnune@gmail.com"),

                    //////////////////////////////////////////////////////


                    Map.of("title", "Spacious Family Home in the Heart of Osmaniye",
                            "description", "Discover peace in this spacious family home located in the lively heart of Osmaniye. With its contemporary design, this home offers the perfect blend of comfort and convenience for modern family living.",
                            "price", 170000.0,
                            "ccd", new Long[]{223L, 4184L, 49082L},
                            "address", "Osmaniye City Center Mah. 456. St. No: 7",
                            "location", new Double[]{37.2517882, 36.2993502},
                            "category", 1L,
                            "advertType", 1L,
                            "image", "static/home3.jpg",
                            "user", "abdrrahman@gmail.com"),

                    Map.of("title", "Modern Apartment Living in Düzici s Thriving Community",
                            "description", "Experience urban living at its finest in this modern apartment nestled within Düziçi's thriving community. Offering sleek design and convenient amenities, this residence provides the ideal setting for a vibrant lifestyle.",
                            "price", 80000.0,
                            "ccd", new Long[]{223L, 4184L, 49079L},
                            "address", "Düziçi Mah. 789. Ave. No: 10",
                            "location", new Double[]{37.2401223, 36.4534072},
                            "category", 2L,
                            "advertType", 2L,
                            "image", "static/apartment10.jpg",
                            "user", "abdrrahman@gmail.com"),

                    Map.of("title", "Contemporary Office in Kadirli s Business District",
                            "description", "Develop your business in this dynamic office space located in the vibrant business district of Kadirli.With its strategic location, this workspace is designed to inspire creativity and productivity.",
                            "price", 300000.0,
                            "ccd", new Long[]{223L, 4184L, 49081L},
                            "address", "Kadirli Mah. 123. Ave. No: 5",
                            "location", new Double[]{37.462024299999996, 36.17201485369948},
                            "category", 3L,
                            "advertType", 1L,
                            "image", "static/office10.jpg",
                            "user", "abdrrahman@gmail.com"),

                    Map.of("title", "Private Villa Retreat in the Scenic Countryside of Sumbas",
                            "description", "Indulge in luxury living with this private villa retreat nestled in the scenic countryside of Sumbas. Surrounded by breathtaking natural beauty, this elegant villa offers the ultimate escape for relaxation and rejuvenation.",
                            "price", 400000.0,
                            "ccd", new Long[]{223L, 4184L, 49083L},
                            "address", "Sumbas Mah. 101. St. No: 3",
                            "location", new Double[]{37.4536229, 36.0247595},
                            "category", 4L,
                            "advertType", 1L,
                            "image", "static/villa10.jpg",
                            "user", "abdrrahman@gmail.com"),

                    Map.of("title", "Expansive Land Parcel in Bahce",
                            "description", "Seize the opportunity to invest in this expansive land parcel located in the promising area of Bahçe. With its vast potential for development and growth, this land offers endless possibilities for residential or commercial projects.",
                            "price", 500000.0,
                            "ccd", new Long[]{223L, 4184L, 49078L},
                            "address", "Bahçe Mah. 7890. St. No: 12",
                            "location", new Double[]{37.2042113, 36.5820496},
                            "category", 5L,
                            "advertType", 2L,
                            "image", "static/land10.jpg",
                            "user", "abdrrahman@gmail.com"),

                    Map.of("title", "Corner Shop in Toprakkale s Bustling Marketplace",
                            "description", "Position your business for success at this corner shop located in Toprakkale's bustling market. With its high visibility and consistent foot traffic, this is ideal for retail space and attracting customers.",
                            "price", 600000.0,
                            "ccd", new Long[]{223L, 4184L,49084L},
                            "address", "Toprakkale Mah. 4567. Ave. No: 20",
                            "location", new Double[]{37.067132, 36.1460016},
                            "category", 6L,
                            "advertType", 2L,
                            "image", "static/shop10.jpg",
                            "user", "abdrrahman@gmail.com")

            ));

            for (Map<String, Object> advert : adverts) {

                String title = advert.get("title").toString();
                Image image = buildImage(advert.get("image").toString(), title);

                Advert builtInAdvert = Advert.builder()
                        .title(title)
                        .description(advert.get("description").toString())
                        .slug(GeneralUtils.generateSlug(title))
                        .price((Double) advert.get("price"))
                        .statusForAdvert(StatusForAdvert.ACTIVATED)
                        .builtIn(true)
                        .isActive(true)
                        .viewCount(0)
                        .location(new Location(((Double[]) advert.get("location"))[0], ((Double[]) advert.get("location"))[1]))
                        .advertType(advertTypeRepository.findById((Long) advert.get("advertType")).get())
                        .category(categoryRepository.findById((Long) advert.get("category")).get())
                        .country(countriesRepository.findById(((Long[]) advert.get("ccd"))[0]).get())
                        .city(cityRepository.findById(((Long[]) advert.get("ccd"))[1]).get())
                        .district(districtsRepository.findById(((Long[]) advert.get("ccd"))[2]).get())
                        .address(advert.get("address").toString())
                        .images(List.of(image))
                        .user(userRepository.findByEmail(advert.get("user").toString()).get())
                        .build();

                Advert savedAdvert = advertRepository.save(builtInAdvert);
                    initializeDefaultCategoryPropertyValues(savedAdvert.getId(), savedAdvert.getCategory().getId());

                Image savedImage = savedAdvert.getImages().stream().findFirst().orElse(null);
                if (savedImage != null) {
                    savedImage.setAdvert(savedAdvert);
                    imageRepository.save(savedImage);
                }
            }
        }
    }

    private Image buildImage(String fileName, String title) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(new ClassPathResource(fileName).getURI()));
            return Image.builder()
                    .data(ImageUtil.compressImage(data))
                    .name(title)
                    .type("image/jpg")
                    .featured(true)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}