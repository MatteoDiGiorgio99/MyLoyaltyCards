package it.matteodigiorgio.loyaltycards.adapter;

public interface ClickListener<T, E> {
    void onItemClick(T data, E dataType);
}