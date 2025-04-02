package ru.askar.common.exception;

public class CollectionIsEmptyException extends Exception {
    public CollectionIsEmptyException() {
        super("Коллекция пуста");
    }
}
