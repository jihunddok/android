package com.project.trello1;

public class BoardItem {

    String boardCode;
    String boardName;

    BoardItem(String _boardCode, String _boardName) {
        boardCode = _boardCode;
        boardName = _boardName;
    }

    public String getBoardCode() {
        return boardCode;
    }

    public String getBoardName() {
        return boardName;
    }
}