package ua.com.itproekt.gup.server.api.rest.profiles;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ua.com.itproekt.gup.bank_api.BankSession;
import ua.com.itproekt.gup.dto.ProfileInfo;
import ua.com.itproekt.gup.model.login.LoggedUser;
import ua.com.itproekt.gup.model.profiles.Profile;
import ua.com.itproekt.gup.model.profiles.ProfileFilterOptions;
import ua.com.itproekt.gup.model.profiles.UserRole;
import ua.com.itproekt.gup.server.api.login.profiles.FormLoggedUser;
import ua.com.itproekt.gup.server.api.model.profiles.CheckMainPhone;
import ua.com.itproekt.gup.service.login.UserDetailsServiceImpl;
import ua.com.itproekt.gup.service.profile.ProfilesService;
import ua.com.itproekt.gup.util.SecurityOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/rest/profilesService")
public class ProfileRestController {

    @Autowired
    private ProfilesService profilesService;

    @Autowired
    private BankSession bankSession;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Qualifier("userDetailsServiceImpl")
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /**
     * Gets profile by id.
     *
     * @param id the id
     * @return the profile by id
     */
    @CrossOrigin
    @RequestMapping(value = "/profile/read/id/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileInfo> getProfileById(@PathVariable String id) {
        ProfileInfo profileInfo = profilesService.findPrivateProfileByIdAndUpdateLastLoginDate( id );

        if (profileInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(profileInfo, HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/profile/mainPhoneViews/id/{id}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileInfo> countPhoneViewsProfileById(@PathVariable String id) {
        ProfileInfo profileInfo = profilesService.incMainPhoneViewsAtOne(id);

        if (profileInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(profileInfo, HttpStatus.OK);
    }

    /**
     * Gets profile by public-id.
     *
     * @param id the id
     * @return the profile by public-id
     */
    @CrossOrigin
    @RequestMapping(value = "/profile/read/publicid/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProfileInfo> getProfileByPublicId(@PathVariable String id) {
        ProfileInfo profileInfo = profilesService.findPrivateProfileByIdAndUpdateLastLoginDate( profilesService.findPublicProfileByPublicId(id).getProfile().getId() ); //ProfileInfo profileInfo = profilesService.findPublicProfileByPublicId(id);

        if (profileInfo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(profileInfo, HttpStatus.OK);
    }

    //TODO: add short-profile!!!


    /**
     * Gets user name by id.
     *
     * @param id - the user ID.
     * @return - the user profile.
     */
    @CrossOrigin
    @RequestMapping(value = "/profile/info/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProfileNameById(@PathVariable String id) {
        Profile profile = profilesService.findById(id);

        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(profile.getUsername(), HttpStatus.OK);
    }

    /**
     * Gets user name by public-id.
     *
     * @param id - the user ID.
     * @return - the user profile.
     */
    @CrossOrigin
    @RequestMapping(value = "/profile/publicinfo/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getProfileNameByPublicId(@PathVariable String id) {
        Profile profile = profilesService.findByPublicId(id);

        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(profile.getUsername(), HttpStatus.OK);
    }


    /**
     * Add one contact (user) to the current user contact list.
     *
     * @param profileId - the profile ID of the user, which must be added to the contact list.
     * @return - the status code Not_Found if target user was not fount,
     * status code OK if user was successfully deleted.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/id/{profileId}/myContactList/add", method = RequestMethod.POST)
    public ResponseEntity<String> addToMyContactList(@PathVariable String profileId) {

//        if (!profilesService.profileExists(profileId)) {//TODO: need make test...
        if (profilesService.profilePublicExists(profileId)) { // if (!profilesService.profilePublicExists(profileId)) {
            return new ResponseEntity<>("Target profile was not found", HttpStatus.NOT_FOUND);
        }

        String userId = SecurityOperations.getLoggedUserId();

        if( userId!=null ){
            profilesService.addSocialToSocialList(userId, profileId); //profilesService.addContactToContactList(userId, profileId); //TODO: turn...
            return new ResponseEntity<>("{\"addFrom\":\""+userId+"\", \"addTo\":\""+profileId+"\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/profile/id/{profileId}/myContactList/add", method = RequestMethod.POST)
    public ResponseEntity<String> addToMyContactList(@PathVariable String profileId, @RequestBody FormLoggedUser formLoggedUser, HttpServletResponse response) { //TODO: ???

        LoggedUser loggedUser;
        try {
            loggedUser = (LoggedUser) userDetailsService.loadUserByUsername(formLoggedUser.getEmail());
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(formLoggedUser.getPassword(), loggedUser.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        ProfileInfo profileInfo = profilesService.findPrivateProfileByEmailAndUpdateLastLoginDate(formLoggedUser.getEmail());

        if (profilesService.profilePublicExists(profileId)) {
            return new ResponseEntity<>("Target profile was not found", HttpStatus.NOT_FOUND);
        }

        String userId = profileInfo.getProfile().getId();

        if( userId!=null ){
            profilesService.addSocialToSocialList(userId, profileId);
            return new ResponseEntity<>("{\"addFrom\":\""+userId+"\", \"addTo\":\""+profileId+"\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
        }
    }







//    @CrossOrigin
//    @ResponseBody
//    @RequestMapping(value = "/profile/mainphone-check", method = RequestMethod.POST)
//
//    public ResponseEntity<ProfileInfo> login(@RequestBody FormLoggedUser formLoggedUser, HttpServletResponse response) {
//
//    public ResponseEntity<List<Profile>> idByMainPhone(@RequestBody CheckMainPhone checkMainPhone, HttpServletRequest request) {
//        List<Profile> profiles = new ArrayList<>();
//        for (String mainPhone : checkMainPhone.getMainPhones()){
//            System.err.println( mainPhone );
//            Profile profile = profilesService.findProfileByMainPhone(mainPhone);
//            if (profile != null) profiles.add(profile);
//        }
//
//        return new ResponseEntity<>(profiles, HttpStatus.OK );
//    }













    /**
     *
     * @param url
     * @return
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/id/{profileId}/mySocialList/add", method = RequestMethod.POST)
    public ResponseEntity<String> addToMySocialList(@PathVariable String url) {

        String userId = SecurityOperations.getLoggedUserId();
        profilesService.addSocialToSocialList(userId, url);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Delete one contact from user contact list.
     *
     * @param profileId - the ID of the profile which must be deleted.
     * @return - status 404 if target profile was not found, status 200 if profile was deleted from contact list.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/id/{profileId}/myContactList/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteFromMyContactList(@PathVariable String profileId) {

//        if (!profilesService.profileExists(profileId)) { //TODO: need make test...
        if (!profilesService.profilePublicExists(profileId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        profilesService.deleteFromMyContactList(profileId);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    /**
     * If User is logged in - return Profile Info, if not - return only status 200 (Ok).
     *
     * @param request - the HttpServletRequest object.
     * @return - in any case return status 200 (OK), but it will be with ProfileInfo object in the response body if
     * the user is loggedIn, and there will empty response body if the user is not loggedIn.
     */
    @CrossOrigin
    @RequestMapping(value = "/profile/read/loggedInProfile", method = RequestMethod.GET)
    public ResponseEntity<ProfileInfo> getLoggedUser(HttpServletRequest request) {

        ProfileInfo profileInfo = profilesService.getLoggedUser(request);

        if (profileInfo != null) {
            return new ResponseEntity<>(profileInfo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


    /**
     * Gets profile by username.
     *
     * @param username the username
     * @return the profile by username
     */
    @CrossOrigin
    @RequestMapping(value = "/profile/read/username/{username}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> getProfileByUsername(@PathVariable("username") String username) {
        Profile profile = profilesService.findProfileByUsername(username);
        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


    /**
     * List all profiles response entity.
     *
     * @param profileFilterOptions the profile filter options (pagination).
     *                             Use "skip" and "limit" in JSON object request body
     * @return the response entity
     */
    @CrossOrigin
    @RequestMapping(value = "/profile/read/all", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProfileInfo>> listAllProfiles(@RequestBody ProfileFilterOptions profileFilterOptions) {
        List<ProfileInfo> profiles = profilesService.findAllPublicProfilesWithOptions(profileFilterOptions);
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }


    /**
     * Check if profile with specific email exist
     *
     * @param email - the email of the target profile.
     * @return - string "NOT FOUND" or user ID.
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/profile/email-check", method = RequestMethod.POST)
    public String idByEmail(@RequestParam String email) {
        Profile profile = profilesService.findProfileByEmail(email);
        if (profile == null) {
            return "NOT FOUND";
        }
        return profile.getId();
    }

//    @CrossOrigin
//    @ResponseBody
//    @RequestMapping(value = "/profile/mainphone-check", method = RequestMethod.POST)
//    public String idByMainPhone(@RequestParam String mainPhone) {
////        Profile profile = profilesService.findProfileByMainPhone(mainPhone);
////        if (profile == null) {
////            return "NOT FOUND";
////        }
////        return profile.getId();
//
////        Stream.of(mainPhone.split(","))
////                .anyMatch( p -> { profilesService.findProfileByMainPhone(p)::equals(); } );
//        for (String p : mainPhone.split(","))
//            if (profilesService.findProfileByMainPhone(p) != null) return "IS FOUND";
//        return "NOT FOUND";
//    }
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/profile/mainphone-check", method = RequestMethod.POST)
    public ResponseEntity<List<Profile>> idByMainPhone(@RequestBody CheckMainPhone checkMainPhone, HttpServletRequest request) {
        List<Profile> profiles = new ArrayList<>();
        for (String mainPhone : checkMainPhone.getMainPhones()){
            System.err.println( mainPhone );
            Profile profile = profilesService.findProfileByMainPhone(mainPhone);
            if (profile != null) profiles.add(profile);
        }

        return new ResponseEntity<>(profiles, HttpStatus.OK );
    }


    /**
     * Update profile.
     *
     * @param newProfile the new profile with id of entity in request body
     * @return the response status, Forbidden (403) if: main email is empty for profile which has social vendor "gup.com.ua"
     * 403 can also be if someone profile already exist with new email.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public ResponseEntity<Void> updateProfile(@RequestBody Profile newProfile, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException {
        String loggedUserId = SecurityOperations.getLoggedUserId();

        newProfile.setId(loggedUserId);
        Profile oldProfile = profilesService.findById(loggedUserId);

        // we cant't allow empty email field for some cases
        if (newProfile.getEmail() == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (newProfile.getEmail().equals("")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }


        // check if someone profile already exist with new main email
        Profile foundByEmailProfile = profilesService.findProfileByEmail(newProfile.getEmail());
        if (foundByEmailProfile != null) {
            if (!foundByEmailProfile.getId().equals(loggedUserId)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }


        if (newProfile.getIdSeoWord() != null) {
            if (profilesService.isSeoWordFree(newProfile.getIdSeoWord())) {
                if (oldProfile.getId().equals(loggedUserId) || request.isUserInRole(UserRole.ROLE_ADMIN.toString())) {
                    changeUserType(newProfile, oldProfile);
                    profilesService.editProfile(newProfile);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (oldProfile.getId().equals(loggedUserId) || request.isUserInRole(UserRole.ROLE_ADMIN.toString())) {
            changeUserType(newProfile, oldProfile);
            profilesService.editProfile(newProfile);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/soc-edit", method = RequestMethod.POST)
    public ResponseEntity<Profile> updateSocProfile(@RequestBody Profile newProfile, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException {
        String loggedUserId = SecurityOperations.getLoggedUserId();

        newProfile.setId(loggedUserId);
        Profile oldProfile = profilesService.findById(loggedUserId);

        if (newProfile.getIdSeoWord() != null) { //if( !newProfile.getIdSeoWord().equals(null) ){
            if (profilesService.isSeoWordFree(newProfile.getIdSeoWord())) {
                if (oldProfile.getId().equals(loggedUserId) || request.isUserInRole(UserRole.ROLE_ADMIN.toString())) {
                    changeUserType(newProfile, oldProfile);
                    profilesService.editProfile(newProfile);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        try {
            /* Edit Profile */
            Profile profileEdit = profilesService.findPrivateProfileByUidAndUpdateLastLoginDate(newProfile.getUid(), newProfile.getSocWendor()).getProfile();
            String id = profileEdit.getId();
//            String socWendor = profileEdit.getSocWendor();
//            String username = profileEdit.getUsername();
//            String imgId = profileEdit.getImgId();
            profileEdit = newProfile;
            profileEdit.setId(id);
            profileEdit.setImgUrl("");
            profilesService.editProfile(profileEdit);
        } catch (UsernameNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    /**
     * Change profile status
     *
     * @param profile - the profile.
     * @return status 200 if ok
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/status/update", method = RequestMethod.POST)
    public ResponseEntity<Void> updateStatus(@RequestBody Profile profile) {

        String loggedUserId = SecurityOperations.getLoggedUserId();

        Profile oldProfile = profilesService.findById(loggedUserId);

        if (StringUtils.isBlank(profile.getStatus())) {
            oldProfile.setStatus("");
        } else {
            oldProfile.setStatus(profile.getStatus());
        }

        profilesService.editProfile(oldProfile);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Add or delete offer into offer favorite list.
     *
     * @param offerId - the offer ID which must be add or delete to/from offer favorite list.
     * @return - status OK in the all cases.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/updateFavoriteOffer", method = RequestMethod.POST)
    public ResponseEntity<Void> updateFavoriteOffers(@RequestParam String offerId) {
        profilesService.updateFavoriteOffers(offerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Return current users balance.
     *
     * @return - the user balance.
     */
    @CrossOrigin
    @RequestMapping(value = "/check-user-balance-by-id", method = RequestMethod.POST)
    @ResponseBody
    public Integer checkBalance() {

        String loggedUserId = SecurityOperations.getLoggedUserId();
        return bankSession.getUserBalance(loggedUserId);
    }


    /**
     * Change userType in bank
     *
     * @param newProfile - the new version of the profile.
     * @param oldProfile - the old profile.
     */
    private void changeUserType(Profile newProfile, Profile oldProfile) {
        if (!newProfile.getContact().getType().equals(oldProfile.getContact().getType())) {
            switch (newProfile.getContact().getType()) {
                case LEGAL_ENTITY:
                    bankSession.editBalanceTypeEntityOfUser(newProfile.getId(), 2);
                    break;
                case INDIVIDUAL:
                    bankSession.editBalanceTypeEntityOfUser(newProfile.getId(), 3);
                    break;
                case ENTREPRENEUR:
                    bankSession.editBalanceTypeEntityOfUser(newProfile.getId(), 4);
                    break;
            }
        }
    }


    /**
     * Find and return all financial information for current user. It is include fields: "balance", "bonusBalance" and
     * "internalTransactionList" - history of the users transactions.
     *
     * @return - the result and status 200 (OK).
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/bank/financeInfo/read", method = RequestMethod.GET)
    public ResponseEntity<String> bankTest() {

        String loggedUserId = SecurityOperations.getLoggedUserId();

        return new ResponseEntity<>(bankSession.getFinanceInfo(loggedUserId), HttpStatus.OK);
    }
}