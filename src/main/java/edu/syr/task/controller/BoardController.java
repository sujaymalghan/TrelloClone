package edu.syr.task.controller;


import edu.syr.task.exception.BoardException;
import edu.syr.task.model.Board;
import edu.syr.task.service.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;

    @Autowired
    public BoardController(BoardServiceImpl boardServiceImpl) {
        this.boardServiceImpl = boardServiceImpl;
    }

    /**
     * Creates a new board with the given description.
     *
     * @param name and description The description of the board.
     * @return A ResponseEntity containing the ID of the created board.
     */
    @PostMapping("/create")
    public ResponseEntity<Long> createBoard(@RequestParam String name, @RequestParam String description) {
        try {
            Board board = new Board();
            board.setDescription(description);
            board.setName(name);
            Board createdBoard = boardServiceImpl.createBoard(board);
            return ResponseEntity.ok(createdBoard.getId());
        } catch (BoardException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(-1L);
        }
    }


    /**
     * Fetches a board with the given ID.
     *
     * @param id The ID of the board to fetch.
     * @return A ResponseEntity containing the fetched board.
     * If the board is not found, returns a ResponseEntity with a 404 Not Found status.
     */
    @GetMapping("/showboard/{id}")
    public ResponseEntity<String> getBoard(@PathVariable Long id) {
        try {
            String boardDetails = boardServiceImpl.getBoardById(id);
            return ResponseEntity.ok(boardDetails);
        } catch (BoardException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/showallboards")
    public ResponseEntity<List<Board>> getAllBoards() {
        try {
            List<Board> boards = boardServiceImpl.getAllBoards();
            return ResponseEntity.ok(boards);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }


    }


    /**
     * Deletes a board by its ID, along with all tasks associated with the board.
     *
     * @param boardId The ID of the board to be deleted.
     * @return A ResponseEntity indicating the outcome of the operation.
     */
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<String> deleteBoardById(@PathVariable Long boardId) {
        try {
            boardServiceImpl.deleteBoardById(boardId);
            return ResponseEntity.ok("Deleted board with ID " + boardId + " successfully");
        } catch (BoardException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}







