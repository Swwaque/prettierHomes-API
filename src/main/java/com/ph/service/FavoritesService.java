package com.ph.service;

import com.ph.domain.entities.Advert;
import com.ph.domain.entities.Favorite;
import com.ph.domain.entities.User;
import com.ph.exception.customs.ResourceNotFoundException;
import com.ph.payload.mapper.AdvertMapper;
import com.ph.payload.response.AdvertResponseForFavorite;
import com.ph.payload.response.FavoriteCountResponse;
import com.ph.repository.FavoritesRepository;
import com.ph.utils.MessageUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final UserService userService;
    private final MessageUtil messageUtil;
    private final AdvertService advertService;
    private final AdvertMapper advertMapper;

    // Not :K01 - GetFavoritesByCustomer() ***************************************************

    /**
     * Retrieves the list of favorite adverts for a customer.
     *
     * @param userDetails the details of the customer
     * @return the ResponseEntity containing the list of favorite adverts
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public ResponseEntity<List<AdvertResponseForFavorite>> getFavoritesByCustomer(UserDetails userDetails) {
        User user = (User) userDetails;
        List<Favorite> favorites = favoritesRepository.findByUser_Id(user.getId());
        List<AdvertResponseForFavorite> response = favorites.stream()
                .map((fav) -> advertMapper.toAdvertResponseForFavorite(fav.getId(), fav.getAdvert()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Not :K02 - GetByUserIdFavoritesByManagerAndAdmin() ***************************************************

    /**
     * Retrieves a list of favorite adverts for a manager or admin based on user ID.
     *
     * @param id The ID of the user
     * @return The list of favorite adverts as a ResponseEntity
     */

    public ResponseEntity<List<AdvertResponseForFavorite>> getByUserIdFavoritesForManagerAndAdmin(Long id) {
        // Retrieve the list of favorites for the user ID
        List<Favorite> favorites = favoritesRepository.findByUser_Id(id);
        List<AdvertResponseForFavorite> response = favorites.stream()
                .map((fav) -> advertMapper.toAdvertResponseForFavorite(fav.getId(), fav.getAdvert()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }


    // Not :K03 - FavoriteSave() ***************************************************************************

    /**
     * Add the specified advert to the user's favorites.
     *
     * @param advertId    the ID of the advert to add to favorites
     * @param userDetails the details of the user making the request
     * @return the response entity containing the advert response for the favorite
     * @throws ResourceNotFoundException if the user is not found
     */
    @Transactional
    public ResponseEntity<?> addToFavorites(Long advertId, UserDetails userDetails) {
        // Get the user based on the provided user details
        User user = (User) userDetails;
        // Check if the favorite already exists
        Optional<Favorite> favorite = favoritesRepository.findByUser_IdAndAdvert_Id(user.getId(), advertId);

        if (favorite.isPresent()) {
            // If the favorite exists, delete it and return the response entity
            favoritesRepository.delete(favorite.get());
            return ResponseEntity.ok().body(messageUtil.getMessage("success.favorites.successfully.deleted"));
        } else {
            // If the favorite does not exist, create a new favorite and save it
            Advert advert = advertService.getById(advertId);
            Favorite newFavorite = Favorite.builder()
                    .user(user)
                    .advert(advert)
                    .build();

            Favorite savedFavorite = favoritesRepository.save(newFavorite);

            return ResponseEntity.ok(advertMapper.toAdvertResponseForFavorite(savedFavorite.getId(), savedFavorite.getAdvert()));
        }
    }

    // Not :K04 - DeleteFavoriteByCustomer() ***************************************************************************

    /**
     * Deletes all favorites of a user.
     *
     * @param userDetails the user details of the user
     * @return a ResponseEntity with a success message
     * @throws ResourceNotFoundException if the user is not found
     */
    public ResponseEntity<String> deleteFavoriteByCustomer(UserDetails userDetails) {
        // Get the user by email
        User user = (User) userDetails;
        // Delete all favorites by their IDs
        favoritesRepository.deleteAllById(user.getFavorites().stream().map(Favorite::getId).collect(Collectors.toList()));
        // Return a success response with a message
        return ResponseEntity.ok().body(messageUtil.getMessage("success.favorites.successfully.deleted"));
    }

    // Not :K05 - DeleteFavoriteIdByAdminAndManager() *********************************************************

    /**
     * Deletes all favorites associated with a user by an admin or manager.
     *
     * @param userId the ID of the user
     * @return a ResponseEntity with a success message
     */
    public ResponseEntity<String> deleteFavoritesByAdminAndManager(Long userId) {
        // Get the user by ID
        User user = userService.getOneUserById(userId);
        // Delete all favorites associated with the user
        favoritesRepository.deleteAllById(user.getFavorites().stream().map(Favorite::getId).collect(Collectors.toList()));
        // Return a ResponseEntity with a success message
        return ResponseEntity.ok().body(messageUtil.getMessage("success.favorites.successfully.deleted"));
    }

    // Not :K06 - DeleteFavoriteIdByAdminAndManager() *********************************************************

    /**
     * Deletes a favorite item by admin and manager.
     *
     * @param favoriteId The ID of the favorite item to be deleted.
     * @return A response entity with a success message.
     */
    public ResponseEntity<String> deleteFavoriteIdByAdminAndManager(Long favoriteId) {
        favoritesRepository.deleteById(favoriteId);
        return ResponseEntity.ok().body(messageUtil.getMessage("success.favorite.successfully.deleted"));
    }


    // Not: getFavCountForAdvert

    /**
     * Retrieves the favorite count for a specific advert.
     *
     * @param advertId    the ID of the advert
     * @param userDetails the user details of the user
     * @return a ResponseEntity with the favorite count
     * @throws ResourceNotFoundException if the user is not found
     */
    public ResponseEntity<Long> getFavCountForAdvert(Long advertId, UserDetails userDetails) {
        // Get the user by email
        User user = (User) userDetails;

        // Retrieve the favorite count for the advert
        Long favCount = favoritesRepository.countByUser_IdAndAdvert_Id(user.getId(), advertId);

        // Return the favorite count as a ResponseEntity
        return ResponseEntity.ok(favCount);
    }

    // Not: deleteFavorite
    public ResponseEntity<String> deleteFavorite(Long favoriteId, UserDetails userDetails) {
        Favorite favorite = favoritesRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException(messageUtil.getMessage("error.favorite.not.found")));
        // If the favorite exists, delete it and return the response message
        favoritesRepository.delete(favorite);
        return ResponseEntity.ok().body(messageUtil.getMessage("success.favorite.successfully.deleted"));
    }

    // Not: getAllFavoritesByUserId
    @Transactional
    public ResponseEntity<Page<AdvertResponseForFavorite>> getAllFavorites(Long userId, int page, String sort, int size, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<Favorite> favorites = favoritesRepository.findByUser_Id(userId, pageable);
        Page<AdvertResponseForFavorite> response = favorites
                .map((fav) -> advertMapper.toAdvertResponseForFavorite(fav.getId(), fav.getAdvert()));
        return ResponseEntity.ok(response);
    }

    public List<FavoriteCountResponse> getFavCountForAdvertCustomer(UserDetails userDetails) {
        User user = (User) userDetails;
        List<Long> advertIds = advertService.getAllAdvertsByUserId(user.getId()).stream().map(Advert::getId).toList();
        return favoritesRepository.getfavoriteCountsForCustomer(advertIds);
    }

    public List<FavoriteCountResponse> getFavCountForAdvertAdmin() {
        return favoritesRepository.getfavoriteCountsForAdmin();
    }
}

