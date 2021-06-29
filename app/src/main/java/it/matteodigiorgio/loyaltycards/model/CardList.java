package it.matteodigiorgio.loyaltycards.model;

import java.io.Serializable;
import java.util.ArrayList;

import it.matteodigiorgio.loyaltycards.model.entity.LoyaltyCard;

public class CardList implements Serializable {
    private ArrayList<LoyaltyCard> _cards;

    public CardList() {
        this._cards = new ArrayList<LoyaltyCard>();
    }

    public int size() {
        return this._cards.size();
    }

    public ArrayList<LoyaltyCard> getAll() {
        return this._cards;
    }

    public LoyaltyCard getAtIndex(int index) {
        return this._cards.get(index);
    }

    public int indexOf(LoyaltyCard card) { return this._cards.indexOf(card); }

    public void add(LoyaltyCard card) {
        this._cards.add(card);
    }

    public void update(int index, LoyaltyCard card) { this._cards.set(index, card); }

    public void remove(LoyaltyCard card) {
        this._cards.remove(card);
    }
}
