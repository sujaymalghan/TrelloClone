package edu.syr.task.repository;

import edu.syr.task.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
    @Query("{ 'taskid' : ?0 }")
    Task findByTaskid(int taskid);
}
