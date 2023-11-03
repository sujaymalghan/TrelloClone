package edu.syr.task.service;

import edu.syr.task.exception.BoardException;
import edu.syr.task.model.Board;
import edu.syr.task.model.Task;
import edu.syr.task.repository.BoardRepository;
import edu.syr.task.service.interfaces.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardSequenceImpl boardSequenceImpl;

    @Autowired
    private BoardRepository boardRepository;


    @Autowired
    private MongoTemplate mongoTemplate;


    @Autowired
    private TaskServiceImpl taskServiceImpl;

    /**
     * Deletes a board by its id.
     * Also deletes all tasks corresponding to the board using the deleteTask method in TaskServiceImpl.
     *
     * @param boardId The id of the board to be deleted.
     */
    public void deleteBoardById(Long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardException("Board not found with id: " + boardId));

        List<Task> tasks = board.getTaskLists();


        if (tasks != null) {
            for (Task task : tasks) {
                if (task != null && task.getTaskid() != null) {
                    taskServiceImpl.deleteTask(task.getTaskid());
                }
            }
        }


        boardRepository.delete(board);
    }

    public Board createBoard(Board board) throws BoardException {
        try {
            board.setId(boardSequenceImpl.generateSequence("board"));
            return mongoTemplate.save(board);
        } catch (Exception e) {
            throw new BoardException("Error creating board: " + e.getMessage());
        }
    }


    public String getBoardById(Long id) throws BoardException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardException("Board not found with ID: " + id));

        StringBuilder sb = new StringBuilder();
        sb.append("Board Name: ").append(board.getName()).append("\n");
        sb.append("Board Description: ").append(board.getDescription()).append("\n");

        if (board.getTaskLists() != null && !board.getTaskLists().isEmpty()) {
            sb.append("Tasks:\n");
            for (Task task : board.getTaskLists()) {
                sb.append("Task ID: ").append(task.getTaskid()).append("\n");
                sb.append("State: ").append(task.getState()).append("\n");
                sb.append("Assigned To: ").append(task.getAssignedTo()).append("\n");
                sb.append("Description: ").append(task.getDescription()).append("\n");
                sb.append("Comments: ").append(task.getComments()).append("\n");
                sb.append("Due Date: ").append(task.getDueDate()).append("\n");
                sb.append("Creation Time: ").append(task.getCreationTime()).append("\n");
                sb.append("Closed Time: ").append(task.getClosedTime()).append("\n");
                sb.append("\n");
            }
        } else {
            sb.append("No tasks available.\n");
        }

        return sb.toString();
    }

    /**
     * Fetches all boards.
     *
     * @return A list of all boards.
     */
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }
}

