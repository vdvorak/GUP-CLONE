package ua.com.gup.service.event;

import org.springframework.context.ApplicationEvent;
import ua.com.gup.model.profiles.Profile;

public class OnForgetPasswordEvent extends ApplicationEvent {

    public OnForgetPasswordEvent(Profile profile) {
        super(profile);
    }
}
