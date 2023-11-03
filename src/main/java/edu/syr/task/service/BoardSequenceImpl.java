package edu.syr.task.service;

import edu.syr.task.model.BoardSequence;
import edu.syr.task.service.interfaces.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class BoardSequenceImpl implements SequenceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public long generateSequence(String sequenceName) {

        Query query = new Query(Criteria.where("sequenceName").is(sequenceName));

        Update update = new Update().inc("sequenceValue", 1);

        BoardSequence sequence = mongoTemplate.findAndModify(query, update, BoardSequence.class);

        if (sequence == null) {
            // If no sequence document exists with the given name, create a new sequence document with a starting value of 200
            sequence = new BoardSequence();
            sequence.setSequenceName(sequenceName);
            sequence.setSequenceValue(200);
            mongoTemplate.save(sequence);
        }

        return sequence.getSequenceValue();
    }
}
