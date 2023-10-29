package edu.syr.task.repository;

import edu.syr.task.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {

    Optional<Task> findByTaskid(int taskid);
}
