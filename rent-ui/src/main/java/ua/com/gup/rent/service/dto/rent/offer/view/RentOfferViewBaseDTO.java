package ua.com.gup.rent.service.dto.rent.offer.view;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import ua.com.gup.rent.model.mongo.category.RentOfferCategory;
import ua.com.gup.rent.service.dto.rent.offer.RentOfferAuthorDTO;
import ua.com.gup.rent.service.dto.rent.offer.price.RentOfferPriceDTO;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

public class RentOfferViewBaseDTO implements Serializable {

    @ApiModelProperty(position = 0, example = "58ff0d6c821847a4bc8c5bff")
    private String id;

    @ApiModelProperty(position = 5)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime lastModifiedDate;

    @ApiModelProperty(position = 20, example = "58edf17a4c8e83648c2f1aa3")
    private RentOfferAuthorDTO author;

    @ApiModelProperty(position = 30)
    private LinkedList<RentOfferCategory> categories;

    @ApiModelProperty(position = 40, example = "title")
    private String title;

    @ApiModelProperty(position = 50, example = "description")
    private String description;

    @ApiModelProperty(position = 70)
    private RentOfferPriceDTO price;

    @ApiModelProperty(position = 80)
    private List<String> imageIds;

    @ApiModelProperty(position = 90, example = "prodam-toyota-rav-4-2016hod-ls")
    private String seoUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public RentOfferAuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(RentOfferAuthorDTO author) {
        this.author = author;
    }

    public LinkedList<RentOfferCategory> getCategories() {
        return categories;
    }

    public void setCategories(LinkedList<RentOfferCategory> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RentOfferPriceDTO getPrice() {
        return price;
    }

    public void setPrice(RentOfferPriceDTO price) {
        this.price = price;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }
}