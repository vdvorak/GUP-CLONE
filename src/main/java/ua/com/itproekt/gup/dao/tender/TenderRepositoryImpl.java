package ua.com.itproekt.gup.dao.tender;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ua.com.itproekt.gup.model.profiles.Profile;
import ua.com.itproekt.gup.model.tender.Tender;
import ua.com.itproekt.gup.model.tender.TenderFilterOptions;
import ua.com.itproekt.gup.model.tender.TenderType;
import ua.com.itproekt.gup.util.EntityPage;
import ua.com.itproekt.gup.util.MongoTemplateOperations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Repository
public class TenderRepositoryImpl implements TenderRepository {
    private static long LUST_CHECK = 0L;

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Autowired
//    TenderMongoRepository tenderMongoRepository;

    //            query.fields().exclude("fieldName");

    @Override
    public void createTender(Tender tender) {
        mongoTemplate.save(tender);
    }

    @Override
    public Tender findById(String id) {
//        return tenderMongoRepository.findByIdWholeProfile(id);
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Tender.class);
    }

    @Override
    public Tender findTenderAndUpdate(Tender tender) {
        return MongoTemplateOperations.updateFieldsAndReturnUpdatedObj(tender);
    }

    @Override
    public void update(Tender tender) {
        mongoTemplate.save(tender);
    }

    @Override
    public int deleteTenderById(String id) {
//        return tenderMongoRepository.deleteById(id);
        Query query = new Query(Criteria.where("id").is(id));
        WriteResult result = mongoTemplate.remove(query, Tender.class);
        return result.getN();
    }

    @Override
    public boolean tenderExists(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.exists(query, Tender.class);
    }

    @Override
    public EntityPage<Tender> findWihOptions(TenderFilterOptions tenderFilterOptions, Profile currUser) {

//        Фильтр тендеров
//        По КВЭД !  совпадение со списком (хотябы один елемент совпадает) и по ИД елем матч
//        По автору !
//        По участнику !
//        По адресу !
//        По заголовку !
//        По дате начала и конца !
//        По типу (при выборе закрытых, показывает пользователю все закрытые тендеры в которых он участвует)

        Query query = new Query();
        if (tenderFilterOptions.getAuthorId() != null) {
            query.addCriteria(Criteria.where("authorId").all(tenderFilterOptions.getAuthorId()));
        }

        if (tenderFilterOptions.getMembers() != null && tenderFilterOptions.getMembers().size() > 0) {
            query.addCriteria(Criteria.where("members").elemMatch(Criteria.where("id").is(tenderFilterOptions.getMemberId())));
        }

        if (tenderFilterOptions.getAddress() != null) {
            if (tenderFilterOptions.getAddress().getArea() != null) {
                query.addCriteria(Criteria.where("address.area").is(tenderFilterOptions.getAddress().getArea()));
            }
            if (tenderFilterOptions.getAddress().getCity() != null) {
                query.addCriteria(Criteria.where("address.city").is(tenderFilterOptions.getAddress().getCity()));
            }
            if (tenderFilterOptions.getAddress().getGoogleMapKey() != null) {
                query.addCriteria(Criteria.where("address.googleMapKey").is(tenderFilterOptions.getAddress().getGoogleMapKey()));
            }
        }


        if (tenderFilterOptions.getSearchField() != null) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("title").regex("(?i:.*" + tenderFilterOptions.getSearchField() + ".*)"),
                    Criteria.where("body").regex("(?i:.*" + tenderFilterOptions.getSearchField() + ".*)")));
        }


        if (tenderFilterOptions.getNaceIds() != null) {
            query.addCriteria(Criteria.where("naceId").elemMatch(Criteria.where("id").in(tenderFilterOptions.getNaceIds())));
        }

        if (tenderFilterOptions.getBegin() != -1) {
            query.addCriteria(Criteria.where("begin").gte(tenderFilterOptions.getBegin()));
        }

        if (tenderFilterOptions.getEnd() != -1) {
            query.addCriteria(Criteria.where("end").lte(tenderFilterOptions.getEnd()));
        }

        if (tenderFilterOptions.getSortDirection() != null && tenderFilterOptions.getSortField() != null) {
            query.with(new Sort(Sort.Direction.fromString(tenderFilterOptions.getSortDirection()), tenderFilterOptions.getSortField()));
        }


        if (tenderFilterOptions.getMinPrice() != null && tenderFilterOptions.getMaxPrice() != null) {
            query.addCriteria(Criteria.where("expectedPrice")
                    .gte(tenderFilterOptions.getMinPrice())
                    .lte(tenderFilterOptions.getMaxPrice()));
        }

        if (tenderFilterOptions.getMaxPrice() != null) {
            query.addCriteria(Criteria.where("expectedPrice").gte(0).lte(tenderFilterOptions.getMaxPrice()));
        }

        if (tenderFilterOptions.getMinPrice() != null) {
            query.addCriteria(Criteria.where("expectedPrice").gte(tenderFilterOptions.getMinPrice()));
        }


        Criteria closed = new Criteria().andOperator(Criteria.where("type").is("CLOSE"), new Criteria().orOperator(
                Criteria.where("members").elemMatch(Criteria.where("id").is(currUser.getId())),
                Criteria.where("authorId").is(currUser.getId())));
        Criteria opened = new Criteria().where("type").is("OPEN");
        if (tenderFilterOptions.getType() == TenderType.CLOSE) {
            query.addCriteria(closed);
        } else if (tenderFilterOptions.getType() == TenderType.OPEN) {
            query.addCriteria(opened);
        } else {
            //ToDo this must work. But doesn't.
//            query.addCriteria(new Criteria().orOperator(opened, closed));
        }

//            if(currUser != null && currUser.getContact() != null && currUser.getContact().getNaceId() != null) {
//                query.addCriteria(Criteria.where("naceId").in(currUser.getContact().getNaceId()));
//            }

        query.skip(tenderFilterOptions.getSkip());
        query.limit(tenderFilterOptions.getLimit());
        return new EntityPage<>(mongoTemplate.count(query, Tender.class),
                mongoTemplate.find(query, Tender.class));
    }

    public List<Tender> getTodayEndTenders() {
        long now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        DBObject queryObj = new BasicDBObject();
        queryObj.put("end", new BasicDBObject("$gte", LUST_CHECK));
        queryObj.put("end", new BasicDBObject("$lte", now));
        LUST_CHECK = now;
        return mongoTemplate.find(new BasicQuery(queryObj), Tender.class);
    }

    @Override
    public Set<String> getMatchedNames(String name) {
        String searchFieldRegex = "(?i:.*" + name + ".*)";
        Query query = new Query();

        query.addCriteria(new Criteria().orOperator(Criteria.where("title").regex(searchFieldRegex)));

        query.fields().include("title");
        query.skip(0);
        query.limit(10);

        return mongoTemplate.find(query, Tender.class).stream().map(Tender::getTitle).collect(Collectors.toSet());

    }

    @Override
    public Set<String> getMatchedTenderNumber(String tenderNumb) {
        String searchFieldRegex = "(?i:.*" + tenderNumb + ".*)";
        Query query = new Query();

        query.addCriteria(new Criteria().orOperator(Criteria.where("tenderNumber").regex(searchFieldRegex)));

        query.fields().include("tenderNumber");
        query.skip(0);
        query.limit(10);

        return mongoTemplate.find(query, Tender.class).stream().map(Tender::getTenderNumber).collect(Collectors.toSet());

    }


}