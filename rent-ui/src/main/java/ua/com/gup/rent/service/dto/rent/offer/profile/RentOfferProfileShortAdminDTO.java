package ua.com.gup.rent.service.dto.rent.offer.profile;

import ua.com.gup.rent.model.mongo.user.RentOfferProfile;

import java.util.Set;
import java.util.stream.Collectors;

public class RentOfferProfileShortAdminDTO {
    private String firstname;
    private String lastname;
    private Set<String> userRoles;
    private String publicId;
    private String imageIdLarge;
    private String imageIdSmall;
    private Long createdDate;
    private Long lastLogin;
    private String socWendor;
    private Boolean active;
    private String email;
    private Boolean ban;


    public RentOfferProfileShortAdminDTO(RentOfferProfile profile) {
        this.firstname = profile.getFirstname();
        this.lastname = profile.getLastname();
        this.userRoles = profile.getUserRoles().stream().map(cur -> cur.toString()).collect(Collectors.toSet());
        this.publicId = profile.getPublicId();
        if (profile.getImageLarge() != null) {
            this.imageIdLarge = profile.getImageLarge().getS3id();
        }
        if (profile.getImageSmall() != null) {
            this.imageIdSmall = profile.getImageSmall().getS3id();
        }
        this.createdDate = profile.getCreatedDate();
        this.lastLogin = profile.getLastLoginDate();
        this.socWendor = profile.getSocWendor();
        this.active = profile.getActive();
        this.ban = profile.getBan();
        this.email = profile.getEmail();
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<String> userRoles) {
        this.userRoles = userRoles;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getImageIdLarge() {
        return imageIdLarge;
    }

    public void setImageIdLarge(String imageIdLarge) {
        this.imageIdLarge = imageIdLarge;
    }

    public String getImageIdSmall() {
        return imageIdSmall;
    }

    public void setImageIdSmall(String imageIdSmall) {
        this.imageIdSmall = imageIdSmall;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getSocWendor() {
        return socWendor;
    }

    public void setSocWendor(String socWendor) {
        this.socWendor = socWendor;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getBan() {
        return ban;
    }

    public void setBan(Boolean ban) {
        this.ban = ban;
    }
}