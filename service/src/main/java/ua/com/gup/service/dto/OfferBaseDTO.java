package ua.com.gup.service.dto;



import ua.com.gup.domain.Attribute;
import ua.com.gup.domain.OfferCategory;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class OfferBaseDTO implements Serializable {

    private LinkedList<OfferCategory> categories;

    @Size(max = 5000, message = "The length of field 'description' should be less then 5000")
    private String description;

    private LinkedHashSet<OfferImageDTO> images;

    private PriceDTO price;

    @Pattern(regexp = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*", message = "Url format exception")
    private String videoUrl;

    private Set<Attribute<String>> attrs = new HashSet<>();

    private Set<Attribute<Long>> numAttrs = new HashSet<>();

    private Set<Attribute<Boolean>> boolAttrs = new HashSet<>();

    public LinkedList<OfferCategory> getCategories() {
        return categories;
    }

    public void setCategories(LinkedList<OfferCategory> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LinkedHashSet<OfferImageDTO> getImages() {
        return images;
    }

    public void setImages(LinkedHashSet<OfferImageDTO> images) {
        this.images = images;
    }

    public PriceDTO getPrice() {
        return price;
    }

    public void setPrice(PriceDTO price) {
        this.price = price;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Set<Attribute<String>> getAttrs() {
        return attrs;
    }

    public void setAttrs(Set<Attribute<String>> attrs) {
        this.attrs = attrs;
    }

    public Set<Attribute<Long>> getNumAttrs() {
        return numAttrs;
    }

    public void setNumAttrs(Set<Attribute<Long>> numAttrs) {
        this.numAttrs = numAttrs;
    }

    public Set<Attribute<Boolean>> getBoolAttrs() {
        return boolAttrs;
    }

    public void setBoolAttrs(Set<Attribute<Boolean>> boolAttrs) {
        this.boolAttrs = boolAttrs;
    }
}
