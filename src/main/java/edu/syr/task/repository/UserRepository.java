package edu.syr.task.repository;

import edu.syr.task.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * A repository interface for the User entity, which extends MongoRepository.
 * This repository provides custom methods to query the MongoDB User collection.
 */
@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

    /**
     * Queries the User collection to find users whose name exactly matches the provided string, ignoring case.
     *
     * @param assignedUser The name to be matched.
     * @return A list of users whose name matches the provided string.
     */
    @Query("{ 'name' : { $regex: '^?0$', $options: 'i' } }")
    List<User> existsByName(String assignedUser);

    /**
     * Finds users by the task id associated with them.
     *
     * @param taskid The task id to be searched.
     * @return A list of users associated with the provided task id.
     */
    List<User> findByTasksTaskid(Integer taskid);

    /**
     * Queries the User collection to find users whose name starts with the provided letter, ignoring case.
     *
     * @param letter The starting letter of the name to be matched.
     * @return A list of users whose name starts with the provided letter.
     */
    @Query("{ 'name' : { $regex: '^?0', $options: 'i' } }")
    List<User> findUsersByStartingLetter(String letter);
}
