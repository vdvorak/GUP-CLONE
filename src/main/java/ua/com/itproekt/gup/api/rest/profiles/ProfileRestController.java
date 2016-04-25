package ua.com.itproekt.gup.api.rest.profiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.com.itproekt.gup.bank_api.BankSession;
import ua.com.itproekt.gup.model.profiles.Profile;
import ua.com.itproekt.gup.model.profiles.ProfileFilterOptions;
import ua.com.itproekt.gup.model.profiles.UserRole;
import ua.com.itproekt.gup.service.profile.ProfilesService;
import ua.com.itproekt.gup.service.profile.VerificationTokenService;
import ua.com.itproekt.gup.util.CreatedObjResp;
import ua.com.itproekt.gup.util.EntityPage;
import ua.com.itproekt.gup.util.SecurityOperations;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;


/**
 * The type Profile rest controller.
 */
@RestController
@RequestMapping("/api/rest/profilesService")
public class ProfileRestController {
    /**
     * The Profiles service.
     */
    @Autowired
    ProfilesService profilesService;

    @Autowired
    VerificationTokenService verificationTokenService;

    @Autowired
    BankSession bankSession;

    /**
     * Create profile.
     *
     * @param profile JSON object in request body
     * @return the response status
     */
    @RequestMapping(value = "/profile/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedObjResp> createProfile(@RequestBody Profile profile) {
        if (profilesService.profileExistsWithEmail(profile.getEmail())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        profilesService.createProfile(profile);
        verificationTokenService.sendEmailRegistrationToken(profile.getId());

        CreatedObjResp createdObjResp = new CreatedObjResp(profile.getId());

        return new ResponseEntity<>(createdObjResp, HttpStatus.CREATED);
    }

    /**
     * Gets profile by id.
     *
     * @param id the id
     * @return the profile by id
     */
    @RequestMapping(value = "/profile/read/id/{id}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> getProfileById(@PathVariable String id) {
        Profile profile = profilesService.findById(id);
        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        if (SecurityOperations.isUserLoggedIn() && profile.getId().equals(SecurityOperations.getLoggedUserId())) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else {
            profile.setContactList(null);
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/read/id/{profileId}/wholeProfile", method = RequestMethod.POST)
    public ResponseEntity<Profile> readUserProfile(@PathVariable String profileId, HttpServletRequest request) {
        Profile profile = profilesService.findWholeProfileById(profileId);
        if (profile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String loggedUserId = SecurityOperations.getLoggedUserId();
        if (profile.getId().equals(loggedUserId) || request.isUserInRole(UserRole.ROLE_ADMIN.toString())) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/read/loggedInProfile", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> getLoggedUser() {
        Profile profile = profilesService.findWholeProfileById(SecurityOperations.getLoggedUserId());

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    /**
     * Gets profile by username.
     *
     * @param username the username
     * @return the profile by username
     */
    @RequestMapping(value = "/profile/read/username/{username}", method = RequestMethod.POST,
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
    @RequestMapping(value = "/profile/read/all", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityPage<Profile>> listAllProfiles(@RequestBody ProfileFilterOptions profileFilterOptions) {
        EntityPage<Profile> profiles = profilesService.findAllProfiles(profileFilterOptions);
        if (profiles.getEntities().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(profiles, HttpStatus.OK);
    }

    /**
     * Update profile response entity.
     *
     * @param newProfile the new profile with id of entity in request body
     * @return the response status
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile newProfile, HttpServletRequest request) {
        if (!profilesService.profileExists(newProfile.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Profile profile = profilesService.findById(newProfile.getId());
        String loggedUserId = SecurityOperations.getLoggedUserId();
        if (profile.getId().equals(loggedUserId) || request.isUserInRole(UserRole.ROLE_ADMIN.toString())) {
            profilesService.editProfile(newProfile);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Add friend response entity.
     *
     * @param friendID the friend's profile id
     * @return the response status
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/friends/addFriend/{friendID}/", method = RequestMethod.POST)
    public ResponseEntity<Void> addFriend(@PathVariable String friendID) {
        if (!profilesService.profileExists(friendID)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String profileId = SecurityOperations.getLoggedUserId();
        profilesService.addFriend(profileId, friendID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/profile/email-check", method = RequestMethod.POST)
    public String idByEmail(@RequestParam String email) {
        Profile profile = profilesService.findProfileByEmail(email);
        if (profile == null) {
            return "NOT FOUND";
        }
        return profile.getId();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/id/{profileId}/myContactList/add", method = RequestMethod.POST)
    public ResponseEntity<Void> addToMyContactList(@PathVariable String profileId) {

        if (!profilesService.profileExists(profileId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//        contactId
//        ownerContactListId
        String userId = SecurityOperations.getLoggedUserId();
        profilesService.addContactToContactList(userId, profileId);

//        String authorId = blogPostService.findComment(blogPostId, commentId).getComments().iterator().next().getFromId();
//        activityFeedService.createEvent(new Event(authorId, EventType.BLOG_POST_COMMENT_LIKE, blogPostId, userId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/join-organization", method = RequestMethod.POST)
    public ResponseEntity<String> joinToOrganization() {

        String userId = SecurityOperations.getLoggedUserId();
        Profile profile = profilesService.findById(userId);

        if (profile.getContact().isMember()) {
            return new ResponseEntity<>("1", HttpStatus.OK);
        } else {
            Integer userBalance = bankSession.getUserBalance(userId);
            if (userBalance >= 50) {
                bankSession.investInOrganization(5555, userId, 50L, 11, "Success");
                profile.getContact().setMember(true);
                profilesService.editProfile(profile);
                return new ResponseEntity<>("2", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("3", HttpStatus.OK);
            }
        }
    }


//    @PreAuthorize("isAuthenticated()")
//    @RequestMapping(value = "/offer-reservation", method = RequestMethod.POST)
//    public ResponseEntity<Void> offerReservation() {
//
//        String userId = SecurityOperations.getLoggedUserId();
//
//            Integer userBalance = bankSession.getUserBalance(userId);
//            if (userBalance >= 5) {
//                bankSession.investInOrganization(5555, userId, 5L, 30, "Success");
//                return new ResponseEntity<>(HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//            }
//    }


    @RequestMapping(value = "/check-user-balance-by-id", method = RequestMethod.POST)
    @ResponseBody
    public Integer checkBalance(@RequestParam("userId") String userId) {

        if (profilesService.profileExists(userId)) {
            return bankSession.getUserBalance(userId);
        }
        return 0;
    }
}