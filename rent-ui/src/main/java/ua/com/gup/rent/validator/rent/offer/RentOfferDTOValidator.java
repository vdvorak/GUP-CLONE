package ua.com.gup.rent.validator.rent.offer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ua.com.gup.rent.service.category.RentCategoryService;
import ua.com.gup.rent.service.category.attribute.RentCategoryAttributeService;
import ua.com.gup.rent.service.dto.rent.offer.RentOfferCreateDTO;
import ua.com.gup.rent.service.dto.rent.offer.RentOfferUpdateDTO;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RentOfferDTOValidator implements Validator {

    private final Logger log = LoggerFactory.getLogger(RentOfferDTOValidator.class);

    @Autowired
    private RentCategoryService rentCategoryService;

    @Autowired
    private RentCategoryAttributeService rentCategoryAttributeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return RentOfferUpdateDTO.class.equals(clazz) || RentOfferUpdateDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        boolean isRentOfferUpdateDTO = RentOfferUpdateDTO.class.isInstance(target);
        RentOfferCreateDTO rentOfferCreateDTO = (RentOfferCreateDTO) target;
        if (isRentOfferUpdateDTO) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "id.required");
        }
        if (!isRentOfferUpdateDTO) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.required");
        }
        if (rentOfferCreateDTO.getTitle() != null && !(2 <= rentOfferCreateDTO.getTitle().length() && rentOfferCreateDTO.getTitle().length() <= 70)) {
            errors.rejectValue("title", "title.size", null, "Title should have size in range [2;70]");
        }
        if (rentOfferCreateDTO.getDescription() != null && !(rentOfferCreateDTO.getDescription().length() <= 5000)) {
            errors.rejectValue("description", "description.size", null, "Description should have size less then 5000");
        }
        String priceType = rentOfferCreateDTO.getAttrs() == null ? null : rentOfferCreateDTO.getAttrs().get("price");
        if (rentOfferCreateDTO.getPrice() == null && "price".equals(priceType)) {
            errors.rejectValue("price", "price.required", null, "Price required for price type price.");
        }

     /*   if (rentOfferCreateDTO.getPrice() != null && "price".equals(priceType)) {
            if (rentOfferCreateDTO.getPrice().getPriceWithVAT() == null) {
                errors.rejectValue("price", "price.withWat.required", null, "Price with WAT required.");
            }
            if (rentOfferCreateDTO.getPrice().getAmount() == null) {
                errors.rejectValue("price", "price.amount.required", null, "Price amount required.");
            }
            if (rentOfferCreateDTO.getPrice().getCurrency() == null) {
                errors.rejectValue("price", "price.currency.required", null, "Price currency required.");
            }
        }
    */

        if (!isRentOfferUpdateDTO && rentOfferCreateDTO.getCategory() == null) {
            errors.rejectValue("categories", "categories.required", null, "Categories is required");
        }

        if (rentOfferCreateDTO.getCategory() != null) {
            if (!rentCategoryService.exists(rentOfferCreateDTO.getCategory())) {
                errors.rejectValue("category", "category.notexist", null, "Category " + rentOfferCreateDTO.getCategory() + " doesn't exist or has subcategory");
            } else {
                int numberOfAllAttrs = (rentOfferCreateDTO.getAttrs() == null ? 0 : rentOfferCreateDTO.getAttrs().size()) +
                        (rentOfferCreateDTO.getAttrs() == null ? 0 : rentOfferCreateDTO.getAttrs().size()) +
                        (rentOfferCreateDTO.getAttrs() == null ? 0 : rentOfferCreateDTO.getAttrs().size()) +
                        (rentOfferCreateDTO.getAttrs() == null ? 0 : rentOfferCreateDTO.getAttrs().size());
                if (!isRentOfferUpdateDTO || (isRentOfferUpdateDTO && numberOfAllAttrs > 0)) {
                    final SortedSet<CategoryAttributeDTO> categoryAttributeDTOS = rentCategoryAttributeService.findAllCategoryAttributeDTO().get(rentOfferCreateDTO.getCategory());
                    final Map<String, CategoryAttributeDTO> categoryAttributeDTOMap = categoryAttributeDTOS.stream().collect(Collectors.toMap(CategoryAttributeDTO::getKey, Function.identity()));
                    // checking values of attrs
                    if (rentOfferCreateDTO.getAttrs() != null) {
                        for (String key : rentOfferCreateDTO.getAttrs().keySet()) {
                            final CategoryAttributeDTO categoryAttributeDTO = categoryAttributeDTOMap.get(key);
                            if (categoryAttributeDTO == null) {
                                errors.rejectValue("attrs", "attrs." + key + ".unknown", null, "Unknown attribute");
                            } else {
                                CategoryAttributeValueDTO attributeValueDTO = new CategoryAttributeValueDTO();
                                attributeValueDTO.setKey(rentOfferCreateDTO.getAttrs().get(key));
                                final Set<CategoryAttributeValueDTO> valuesToCheck = new HashSet<>(categoryAttributeDTO.getValues()); // TreeSet to HashSet
                                if (!valuesToCheck.contains(attributeValueDTO)) { //
                                    errors.rejectValue("attrs", "attrs." + key + ".value.unknown", null, "Unknown value <" + rentOfferCreateDTO.getAttrs().get(key) + "> for attr <" + key + ">");
                                }
                            }
                        }
                    }
                    // checking values of multiAttrs
                    if (rentOfferCreateDTO.getMultiAttrs() != null) {
                        for (String key : rentOfferCreateDTO.getMultiAttrs().keySet()) {
                            final CategoryAttributeDTO categoryAttributeDTO = categoryAttributeDTOMap.get(key);
                            if (categoryAttributeDTO == null) {
                                errors.rejectValue("multiAttrs", "multiAttrs." + key + ".unknown", null, "Unknown attribute");
                            } else {
                                final String[] values = rentOfferCreateDTO.getMultiAttrs().get(key).split(",");
                                for (String value : values) {
                                    CategoryAttributeValueDTO attributeValueDTO = new CategoryAttributeValueDTO();
                                    attributeValueDTO.setKey(value);
                                    final Set<CategoryAttributeValueDTO> valuesToCheck = new HashSet<>(categoryAttributeDTO.getValues()); // TreeSet to HashSet
                                    if (!valuesToCheck.contains(attributeValueDTO)) { //
                                        errors.rejectValue("multiAttrs", "multiAttrs." + key + ".value.unknown", null, "Unknown value <" + value + "> for attr <" + key + ">");
                                    }
                                }
                            }
                        }
                    }
                    // checking values of numAttrs
                    if (rentOfferCreateDTO.getNumAttrs() != null) {
                        for (String key : rentOfferCreateDTO.getNumAttrs().keySet()) {
                            final CategoryAttributeDTO categoryAttributeDTO = categoryAttributeDTOMap.get(key);
                            if (categoryAttributeDTO == null) {
                                errors.rejectValue("numAttrs", "numAttrs." + key + ".unknown", null, "Unknown attribute");
                            } else {
                                final CategoryAttributeValidatorDTO validator = categoryAttributeDTO.getValidator();
                                final BigDecimal value = rentOfferCreateDTO.getNumAttrs().get(key);
                                if (validator.getMin() != null) {
                                    if (validator.getMin().compareTo(value) > 0) {
                                        errors.rejectValue("numAttrs", "numAttrs." + key + ".min", null, "The value = " + value + " is less then min value = " + validator.getMin());
                                    }
                                }
                                if (validator.getMax() != null) {
                                    if (validator.getMax().compareTo(value) < 0) {
                                        errors.rejectValue("numAttrs", "numAttrs." + key + ".max", null, "The value = " + value + " is more then max value = " + validator.getMax());
                                    }
                                }
                            }
                        }
                    }
                    // checking values of boolAttrs
                    if (rentOfferCreateDTO.getBoolAttrs() != null) {
                        for (String key : rentOfferCreateDTO.getBoolAttrs().keySet()) {
                            final CategoryAttributeDTO categoryAttributeDTO = categoryAttributeDTOMap.get(key);
                            if (categoryAttributeDTO == null) {
                                errors.rejectValue("boolAttrs", "boolAttrs." + key + ".unknown", null, "Unknown attribute");
                            }
                        }
                    }

                    // checking required attributes
                    if (categoryAttributeDTOS != null) {
                        for (CategoryAttributeDTO categoryAttributeDTO : categoryAttributeDTOS) {
                            if (categoryAttributeDTO.getType() == CategoryAttributeType.SELECT) {
                                if (rentOfferCreateDTO.getAttrs() == null || rentOfferCreateDTO.getAttrs().get(categoryAttributeDTO.getKey()) == null && categoryAttributeDTO.getValidator().isRequired()) {
                                    errors.rejectValue("attrs", "attrs." + categoryAttributeDTO.getKey() + ".required", null, "Attribute is required");
                                }
                            } else if (categoryAttributeDTO.getType() == CategoryAttributeType.MULTI_SELECT) {
                                if (rentOfferCreateDTO.getMultiAttrs() == null || rentOfferCreateDTO.getMultiAttrs().get(categoryAttributeDTO.getKey()) == null && categoryAttributeDTO.getValidator().isRequired()) {
                                    errors.rejectValue("multiAttrs", "multiAttrs." + categoryAttributeDTO.getKey() + ".required", null, "Attribute is required");
                                }
                            } else if (categoryAttributeDTO.getType() == CategoryAttributeType.NUMBER) {
                                if (rentOfferCreateDTO.getNumAttrs() == null || rentOfferCreateDTO.getNumAttrs().get(categoryAttributeDTO.getKey()) == null && categoryAttributeDTO.getValidator().isRequired()) {
                                    errors.rejectValue("numAttrs", "numAttrs." + categoryAttributeDTO.getKey() + ".required", null, "Attribute is required");
                                }
                            } else if (categoryAttributeDTO.getType() == CategoryAttributeType.BOOLEAN) {
                                if (rentOfferCreateDTO.getBoolAttrs() == null || rentOfferCreateDTO.getBoolAttrs().get(categoryAttributeDTO.getKey()) == null && categoryAttributeDTO.getValidator().isRequired()) {
                                    errors.rejectValue("boolAttrs", "boolAttrs." + categoryAttributeDTO.getKey() + ".required", null, "Attribute is required");
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!isRentOfferUpdateDTO && rentOfferCreateDTO.getContactInfo() == null) {
            errors.rejectValue("contactInfo", "contactInfo.required", null, "ContactInfo is required");
        }
        if (rentOfferCreateDTO.getContactInfo() != null) {
            if (StringUtils.isEmpty(rentOfferCreateDTO.getContactInfo().getContactName())) {
                errors.rejectValue("contactInfo.contactName", "contactInfo.contactName.required", null, "Contact name required");
            }
            if (rentOfferCreateDTO.getContactInfo().getPhoneNumbers() == null || rentOfferCreateDTO.getContactInfo().getPhoneNumbers().size() == 0) {
                errors.rejectValue("contactInfo.phoneNumbers", "contactInfo.phoneNumbers.size", null, "At least one phone number should be");
            } else {
                for (String phoneNo : rentOfferCreateDTO.getContactInfo().getPhoneNumbers()) {
                    if (phoneNo == null || !phoneNo.matches("^380[0-9]{9}$")) {
                        errors.rejectValue("contactInfo.phoneNumbers", "contactInfo.phoneNumbers.format", null, "Phone number format is ^380[0-9]{9}$");
                    }
                }
            }
        }
        if (!isRentOfferUpdateDTO && rentOfferCreateDTO.getAddress() == null) {
            errors.rejectValue("address", "address.required", null, "address required");
        }
        if (rentOfferCreateDTO.getAddress() != null) {
            final OfferAddressDTO address = rentOfferCreateDTO.getAddress();
            if (address.getLat() == null || address.getLng() == null) {
                errors.rejectValue("address.coordinates", "address.coordinates.required", null, "Coordinates required");
            }
            if (!(-90d <= address.getLat().doubleValue() && address.getLat().doubleValue() <= 90 && -180d <= address.getLng().doubleValue() && address.getLng().doubleValue() <= 180)) {
                errors.rejectValue("address.coordinates", "address.format", null, "Lat in [-90;90] Lng in [-180;180]");
            }
            if (address.getCountry() == null) {
                errors.rejectValue("address.country", "address.country.required", null, "Country required");
            }
            if (address.getDistrict() == null) {
                errors.rejectValue("address.district", "address.district.required", null, "District required");
            }
            if (address.getCity() == null) {
                errors.rejectValue("address.city", "address.city.required", null, "City required");
            }
        }
        if (rentOfferCreateDTO.getNumAttrs() != null) {
            final Map<String, BigDecimal> numAttrs = rentOfferCreateDTO.getNumAttrs();
            for (String key : numAttrs.keySet()) {
                try {
                    numAttrs.get(key).doubleValue();
                } catch (Exception e) {
                    errors.rejectValue("numAttrs", "numAttrs." + key + ".invalidFormat", null, "Invalid number format");
                }

            }
        }
    }
}
