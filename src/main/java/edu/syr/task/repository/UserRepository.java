package edu.syr.task.repository;

import edu.syr.task.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    @Query("{ 'name' : { $regex: '^?0$', $options: 'i' } }")
    List<User> existsByName(String assignedUser);

    List<User> findByTasksTaskid(Integer taskid);

    @Query("{ 'name' : { $regex: '^?0', $options: 'i' } }")
    List<User> findUsersByStartingLetter(String letter);

}
