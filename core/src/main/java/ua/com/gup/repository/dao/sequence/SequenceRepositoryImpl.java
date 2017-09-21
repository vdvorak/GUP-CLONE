package ua.com.gup.repository.dao.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ua.com.gup.domain.Sequence;


public class SequenceRepositoryImpl implements SequenceRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public long getNextSequenceValue(String sequenceId) {
        //get sequence id
        Query query = new Query(Criteria.where("_id").is(sequenceId));

        //increase sequence id by 1
        Update update = new Update();
        update.inc("value", 1);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        //this is the magic happened.
        Sequence sequence =
                mongoOperation.findAndModify(query, update, options, Sequence.class);

        //if no sequence, throws RuntimeException
        //optional, just a way to tell user when the sequence is failed to generate.
        if (sequence == null) {
            throw new RuntimeException("Unable to get sequence value for id : " + sequenceId);
        }

        return sequence.getValue();
    }
}
