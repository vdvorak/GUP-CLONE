package ua.com.gup.rent.model.mongo.bonus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.com.gup.common.GupLoggedUser;
import ua.com.gup.common.model.enumeration.CommonBonusScenarios;
import ua.com.gup.common.model.mongo.bonus.CommonBonus;
import ua.com.gup.common.model.object.ObjectType;

import java.time.LocalDateTime;

/**
 * @author Victor Dvorak
 **/
@Document(collection = ObjectType.BONUS_SCENARIOS)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
@EqualsAndHashCode(of = {"name", "code"})
public class RentOfferBonus extends CommonBonus {
    @Id
    String id;
    @Indexed(unique = true)
    private String name;
    @Indexed(unique = true)
    private String code;
    @CreatedBy
    private GupLoggedUser createdByUser;

    public RentOfferBonus() {
    }

    public RentOfferBonus(String name, String code, CommonBonusScenarios scenarios, LocalDateTime startDate, LocalDateTime endDate, Integer countUse, Boolean active) {
        super(active, LocalDateTime.now(), startDate, endDate, countUse, scenarios);
        this.name = name;
        this.code = code;
    }

}