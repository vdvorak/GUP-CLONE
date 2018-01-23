package ua.com.gup.server.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.com.gup.common.dto.profile.ProfileDTO;
import ua.com.gup.dto.profile.CreateProfileDTO;
import ua.com.gup.dto.profile.EditProfileDTO;

@Component
public class ProfileDTOValidator implements Validator {

    private final Logger log = LoggerFactory.getLogger(ProfileDTOValidator.class);

    @Override
    public boolean supports(Class<?> clazz) {
        return CreateProfileDTO.class.equals(clazz) || EditProfileDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        boolean isUpdateDTO = EditProfileDTO.class.isInstance(target);
        ProfileDTO profileDTO = (ProfileDTO) target;

         //FOR update required
        /*if (isUpdateDTO) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "id.required");
        }*/
       /* if (!isUpdateDTO) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.required");
        }*/

        if (profileDTO.getContact() != null) {

            if (profileDTO.getContact().getContactPhones() == null || profileDTO.getContact().getContactPhones().size() == 0) {
                errors.rejectValue("contact.contactPhones", "contact.contactPhones.size", null, "At least one phone number should be");
            } else {
                for (String phoneNo : profileDTO.getContact().getContactPhones()) {
                    if (phoneNo == null || !phoneNo.matches("^380[0-9]{9}$")) {
                        errors.rejectValue("contact.contactPhones", "contact.contactPhones.format", null, "Phone number format is ^380[0-9]{9}$");
                    }
                }
            }
        }


    }
}

