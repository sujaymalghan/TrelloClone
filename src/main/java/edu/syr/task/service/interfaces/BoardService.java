package edu.syr.task.service.interfaces;

import edu.syr.task.exception.BoardException;
import edu.syr.task.model.Board;

import java.util.List;

public interface BoardService {

    void deleteBoardById(Long boardId);

    Board createBoard(Board board) throws BoardException;

    String getBoardById(Long id) throws BoardException;

    List<Board> getAllBoards();
}
