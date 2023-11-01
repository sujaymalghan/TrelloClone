package edu.syr.task.repository;

import edu.syr.task.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * A repository interface for the Task entity, which extends MongoRepository.
 * This repository provides custom methods to query the MongoDB Task collection.
 */
@Repository
public interface TaskRepository extends MongoRepository<Task,String> {

    /**
     * Queries the Task collection to find a task by its task id.
     *
     * @param taskid The task id to be searched.
     * @return The task associated with the provided task id.
     */
    @Query("{ 'taskid' : ?0 }")
    Task findByTaskid(int taskid);
}