package ua.com.gup.rent.filter;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApiModel(description = "Offer filter model")
public class OfferFilter implements Serializable {

    private static final long serialVersionUID = 1232825578694716871L;

    @ApiModelProperty(value = "String for full text search", position = 0)
    private String query;

    @ApiModelProperty(value = "Field form object Profile", position = 5)
    private RentOfferAuthorFilter rentOfferAuthorFilter;

    @ApiModelProperty(value = "Categories with ',' delimiter", position = 10)
    private Integer[] categories;

    @ApiModelProperty(value = "Filter for creation date [from; to] optional", position = 20)
    private DateFilter date;

    @ApiModelProperty(value = "Filter for address", position = 30)
    private RentOfferAddressFilter address;

    @ApiModelProperty(value = "Coordinates for address", position = 30)
    private CoordinatesFilter coordinates;

    @ApiModelProperty(value = "Filter for price", position = 40)
    private MoneyFilter price;

    @ApiModelProperty(value = "Filters for attributes", position = 50)
    private List<RentOfferAttributeFilter> attrs = new ArrayList<>();

    @ApiModelProperty(value = "Filters for attributes", position = 55)
    private List<RentOfferAttributeFilter> multiAttrs = new ArrayList<>();

    @ApiModelProperty(value = "Filters for numeric attributes", position = 60)
    private List<NumericAttributeFilter> numAttrs = new ArrayList<>();

    @ApiModelProperty(value = "Filters for boolean attributes", position = 70)
    private List<BooleanAttributeFilter> boolAttrs = new ArrayList<>();

    @ApiModelProperty(value = "Seo url's with ',' delimiter", position = 80)
    private String[] seoUrls;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Integer[] getCategories() {
        return categories;
    }

    public void setCategories(Integer[] categories) {
        this.categories = categories;
    }

    public DateFilter getDate() {
        return date;
    }

    public void setDate(DateFilter date) {
        this.date = date;
    }

    public RentOfferAddressFilter getAddress() {
        return address;
    }

    public void setAddress(RentOfferAddressFilter address) {
        this.address = address;
    }

    public MoneyFilter getPrice() {
        return price;
    }

    public void setPrice(MoneyFilter price) {
        this.price = price;
    }

    public List<RentOfferAttributeFilter> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<RentOfferAttributeFilter> attrs) {
        this.attrs = attrs;
    }

    public List<RentOfferAttributeFilter> getMultiAttrs() {
        return multiAttrs;
    }

    public void setMultiAttrs(List<RentOfferAttributeFilter> multiAttrs) {
        this.multiAttrs = multiAttrs;
    }

    public List<NumericAttributeFilter> getNumAttrs() {
        return numAttrs;
    }

    public void setNumAttrs(List<NumericAttributeFilter> numAttrs) {
        this.numAttrs = numAttrs;
    }

    public List<BooleanAttributeFilter> getBoolAttrs() {
        return boolAttrs;
    }

    public void setBoolAttrs(List<BooleanAttributeFilter> boolAttrs) {
        this.boolAttrs = boolAttrs;
    }

    public RentOfferAuthorFilter getRentOfferAuthorFilter() {
        return rentOfferAuthorFilter;
    }

    public void setRentOfferAuthorFilter(RentOfferAuthorFilter rentOfferAuthorFilter) {
        this.rentOfferAuthorFilter = rentOfferAuthorFilter;
    }


    public String[] getSeoUrls() {
        return seoUrls;
    }

    public void setSeoUrls(String[] seoUrls) {
        this.seoUrls = seoUrls;
    }

    public CoordinatesFilter getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesFilter coordinates) {
        this.coordinates = coordinates;

    }

    @Override
    public String toString() {
        return "OfferFilter{" +
                "query='" + query + '\'' +
                ", rentOfferAuthorFilter=" + rentOfferAuthorFilter +
                ", categories=" + Arrays.toString(categories) +
                ", date=" + date +
                ", address=" + address +
                ", price=" + price +
                ", attrs=" + attrs +
                ", multiAttrs=" + multiAttrs +
                ", numAttrs=" + numAttrs +
                ", boolAttrs=" + boolAttrs +
                '}';
    }
}