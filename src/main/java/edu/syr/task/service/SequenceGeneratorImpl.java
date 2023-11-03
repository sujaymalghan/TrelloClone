package edu.syr.task.service;

import edu.syr.task.model.DatabaseSequence;
import edu.syr.task.service.interfaces.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SequenceGeneratorImpl implements SequenceService {

    /**
     * The MongoOperations instance for database operations.
     */
    @Autowired
    private MongoOperations mongoOperations;

    /**
     * Generates a sequence number for the provided sequence name.
     * If the sequence does not exist, it creates one and initializes the sequence to 1.
     * If the sequence exists, it increments the current value by 1.
     *
     * @param seqName The name of the sequence for which the next number is required.
     * @return The next number in the sequence.
     */
    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return counter.getSeq();
    }
}