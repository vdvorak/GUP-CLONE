package ua.com.gup.domain;



import ua.com.gup.domain.enumeration.OfferModifiedField;
import ua.com.gup.domain.enumeration.OfferRefusalReason;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;


public class ModerationReport {

    String moderatorId; // moderator ID whose last modified profile

    ZonedDateTime lastModifiedDate; // last modified date when moderator modified profile

    List<OfferRefusalReason> offerRefusalReasons; // the reason why moderator refuse offer

    Set<OfferModifiedField> offerModifiedFieldList; // the list of the last modified fields

    public String getModeratorId() {
        return moderatorId;
    }

    public void setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<OfferRefusalReason> getOfferRefusalReasons() {
        return offerRefusalReasons;
    }

    public void setOfferRefusalReasons(List<OfferRefusalReason> offerRefusalReasons) {
        this.offerRefusalReasons = offerRefusalReasons;
    }

    public Set<OfferModifiedField> getOfferModifiedFieldList() {
        return offerModifiedFieldList;
    }

    public void setOfferModifiedFieldList(Set<OfferModifiedField> offerModifiedFieldList) {
        this.offerModifiedFieldList = offerModifiedFieldList;
    }
}
