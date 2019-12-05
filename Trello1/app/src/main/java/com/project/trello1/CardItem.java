package com.project.trello1;

public class CardItem {

    String cardCode;
    String cardName;
    String finish;

    CardItem(String _cardCode, String _cardName, String _finish) {
        cardCode = _cardCode;
        cardName = _cardName;
        finish = _finish;
    }

    public String getCardCode() {
        return cardCode;
    }

    public String getCardName() {
        return cardName;
    }

    public String isFinish() {
        return finish;
    }

}