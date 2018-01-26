package ua.com.gup.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;
import ua.com.gup.common.dto.profile.ProfileDTO;
import ua.com.gup.common.model.mongo.Phone;
import ua.com.gup.mongo.composition.domain.profile.Profile;

public class EditProfileDTO extends ProfileDTO {

    @JsonProperty("mainPhone")
    private Phone mainPhone;


    public EditProfileDTO() {
    }

    public Phone getMainPhone() {
        return mainPhone;
    }

    public void setMainPhone(Phone mainPhone) {
        this.mainPhone = mainPhone;
    }

    public Profile updateModel(Profile profile) {
        profile.setFirstname(this.firstName);
        profile.setLastname(this.lastName);
        profile.setUserType(this.userType);

        //check email not Update if exists
        if (StringUtils.isEmpty(profile.getEmail()) && !StringUtils.isEmpty(this.email)) {
            profile.setEmail(this.email);
        }

        profile.setAddress(this.address);
        profile.setContactList(this.socialsList);
        profile.setStatus(this.status);
        profile.setContact(this.contact);
        profile.setMainPhone(this.mainPhone);
        return profile;
    }
}
