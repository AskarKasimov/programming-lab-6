package ru.askar.common.exception;

public class NoSuchKeyException extends Exception {
    public NoSuchKeyException() {
        super("Нет элемента с таким ключом");
    }
}
