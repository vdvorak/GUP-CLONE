package ua.com.gup.repository.custom.impl;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;
import ua.com.gup.domain.Offer;
import ua.com.gup.domain.enumeration.Currency;
import ua.com.gup.domain.enumeration.OfferStatus;
import ua.com.gup.domain.filter.*;
import ua.com.gup.repository.custom.OfferRepositoryCustom;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OfferRepositoryImpl implements OfferRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(OfferRepositoryImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void createIndex() {
        TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .named(Offer.COLLECTION_NAME + "_TextIndex")
                .onField("$**")
                .withDefaultLanguage("russian")
                .build();
        mongoTemplate.indexOps(Offer.class).ensureIndex(textIndex);
    }

    @Override
    public List<Offer> findByFilter(OfferFilter offerFilter, OfferStatus offerStatus, Pageable pageable) {
        return findByFilter(offerFilter, Arrays.asList(offerStatus), pageable);
    }

    @Override
    public List<Offer> findByFilter(OfferFilter offerFilter, OfferStatus offerStatus, String excludedId, Pageable pageable) {
        return findByFilter(offerFilter, Arrays.asList(offerStatus), Arrays.asList(excludedId), pageable);
    }

    @Override
    public List<Offer> findByFilter(OfferFilter offerFilter, List<OfferStatus> offerStatuses, Pageable pageable) {
        return findByFilter(offerFilter, offerStatuses, null, pageable);
    }

    @Override
    public List<Offer> findByFilter(OfferFilter offerFilter, List<OfferStatus> offerStatuses, Collection<String> excludedIds, Pageable pageable) {
        return createQueryAndFind(offerFilter, offerStatuses, excludedIds, pageable);
    }

    @Override
    public void incrementViews(String id) {
        incrementStatistic(new Query(Criteria.where("_id").is(id)), "views");
    }

    @Override
    public void incrementViewsBySeoUrl(String seoUrl) {
        incrementStatistic(new Query(Criteria.where("seoUrl").is(seoUrl)), "views");
    }

    @Override
    public void incrementPhoneViews(String id) {
        incrementStatistic(new Query(Criteria.where("_id").is(id)), "phoneViews");
    }

    @Override
    public void incrementFavorites(String id) {
        incrementStatistic(new Query(Criteria.where("_id").is(id)), "favorites");
    }

    private void incrementStatistic(Query query, String field) {
        Update update = new Update();
        update.inc("statistic." + field, 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(false);
        mongoTemplate.findAndModify(query, update, options, Offer.class);
    }

    public void updateBasePriceByExchangeRate(OfferStatus status, Currency currency, Currency baseCurrency, double exchangeRate) {
        BasicDBObject query = new BasicDBObject();
        query.put("status", status.name());
        query.put("price.currency", currency.name());
        BasicDBObject projection = new BasicDBObject();
        projection.put("_id", 1);
        projection.put("price.amount", 1);
        final DBCollection collection = mongoTemplate.getCollection(Offer.COLLECTION_NAME);
        DBCursor cursor = collection.find(query, projection);
        try {
            while (cursor.hasNext()) {
                final DBObject doc = cursor.next();
                try {
                    final DBObject price = (DBObject) doc.get("price");
                    final BasicDBObject fields = new BasicDBObject();
                    fields.put("price.baseAmount", exchangeRate * Double.valueOf("" + price.get("amount")));
                    fields.put("price.baseCurrency", baseCurrency.name());
                    fields.put("price.last_modified_date", new Date());
                    collection.update(new BasicDBObject("_id", doc.get("_id")), new BasicDBObject("$set", fields));
                } catch (Exception e) {
                    log.error("Error price update doc = {}", doc, e);
                }
            }
        } catch (Exception e) {
            log.error("ForEach update with currency = {}, baseCurrency = {}, exchangeRate = {} failed: ", currency, baseCurrency, exchangeRate, e);
        } finally {
            cursor.close();
        }
    }

    private List<Offer> createQueryAndFind(OfferFilter offerFilter, List<OfferStatus> statusList, Collection<String> excludedIds, Pageable pageable) {
        Query query = new Query();
        if (!StringUtils.isEmpty(offerFilter.getQuery())) {
            TextCriteria textCriteria = TextCriteria
                    .forLanguage("russian");
            if (pageable != null && pageable.getSort() != null) {
                textCriteria.matchingPhrase(offerFilter.getQuery());
            } else {
                textCriteria.matching(offerFilter.getQuery());
            }
            query.addCriteria(textCriteria);
        }
        if (excludedIds != null && excludedIds.size() > 0) {
            query.addCriteria(Criteria.where("_id").nin(excludedIds));
        }
        if (offerFilter.getDate() != null) {
            final DateFilter dateFilter = offerFilter.getDate();
            if (dateFilter.getFrom() != null && dateFilter.getTo() != null) {
                query.addCriteria(Criteria.where("lastModifiedDate").gte(dateFilter.getFrom()).lte(dateFilter.getTo()));
            } else {
                if (dateFilter.getFrom() != null) {
                    query.addCriteria(Criteria.where("lastModifiedDate").gte(dateFilter.getFrom()));
                }
                if (dateFilter.getTo() != null) {
                    query.addCriteria(Criteria.where("lastModifiedDate").lte(dateFilter.getTo()));
                }
            }
        }
        if (statusList != null && statusList.size() > 0) {
            if (statusList.size() == 1) {
                query.addCriteria(Criteria.where("status").is(statusList.get(0).toString()));
            } else {
                query.addCriteria(Criteria.where("status").in(
                        statusList
                                .stream()
                                .map(OfferStatus::toString)
                                .collect(Collectors.toList())));
            }
        } else {
            query.addCriteria(Criteria.where("status").is(OfferStatus.ACTIVE));
        }
        if (offerFilter.getCategories() != null) {
            String regex = "^" + offerFilter.getCategories().replace(",", "/");
            if (!regex.endsWith("/")) {
                regex = regex + "/";
            }
            query.addCriteria(Criteria.where("categoriesRegExp").regex(regex, "i"));
        }
        if (offerFilter.getAddress() != null) {
            AddressFilter addressFilter = offerFilter.getAddress();
            if (addressFilter.getCountries() != null) {
                query.addCriteria(Criteria.where("address.country.code").in(addressFilter.getCountries().split(",")));
            }
            if (addressFilter.getDistricts() != null) {
                query.addCriteria(Criteria.where("address.district.code").in(addressFilter.getDistricts().split(",")));
            }
            if (addressFilter.getCities() != null) {
                query.addCriteria(Criteria.where("address.city.code").in(addressFilter.getCities().split(",")));
            }
        }
        if (offerFilter.getPrice() != null) {
            MoneyFilter price = offerFilter.getPrice();
            if (price.getCurrency() != null) {
                if (price.getFrom() != null && price.getTo() != null) {
                    query.addCriteria(Criteria.where("price.baseAmount").gte(price.getFrom()).lte(price.getTo()));
                } else {
                    if (price.getFrom() != null) {
                        query.addCriteria(Criteria.where("price.baseAmount").gte(price.getFrom()));
                    }
                    if (price.getTo() != null) {
                        query.addCriteria(Criteria.where("price.baseAmount").lte(price.getTo()));
                    }
                }
            }
        }
        if (offerFilter.getAttrs() != null) {
            for (AttributeFilter attrFilter : offerFilter.getAttrs()) {
                query.addCriteria(Criteria.where("attrs." + attrFilter.getKey() + ".selected.key").in(attrFilter.getVals().split(",")));
            }
        }
        if (offerFilter.getMultiAttrs() != null) {
            for (AttributeFilter attrFilter : offerFilter.getMultiAttrs()) {
                query.addCriteria(Criteria.where("multiAttrs." + attrFilter.getKey() + ".selected").elemMatch(Criteria.where("key").in(attrFilter.getVals().split(","))));
            }
        }
        if (offerFilter.getNumAttrs() != null) {
            for (NumericAttributeFilter filter : offerFilter.getNumAttrs()) {
                if (filter.getFrom() != null && filter.getTo() != null) {
                    query.addCriteria(Criteria.where("numAttrs." + filter.getKey() + ".selectedDouble").gte(filter.getFrom()).lte(filter.getTo()));
                } else {
                    if (filter.getFrom() != null) {
                        query.addCriteria(Criteria.where("numAttrs." + filter.getKey() + ".selectedDouble").gte(filter.getFrom()));
                    }
                    if (filter.getTo() != null) {
                        query.addCriteria(Criteria.where("numAttrs." + filter.getKey() + ".selectedDouble").lte(filter.getTo()));
                    }
                }
            }
        }
        if (offerFilter.getBoolAttrs() != null) {
            for (BooleanAttributeFilter filter : offerFilter.getBoolAttrs()) {
                query.addCriteria(Criteria.where("boolAttrs." + filter.getKey() + ".selected").is(filter.getVal()));
            }
        }
        query.with(pageable);
        return mongoTemplate.find(query, Offer.class);
    }
}





