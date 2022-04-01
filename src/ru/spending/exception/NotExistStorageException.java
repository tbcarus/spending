package ru.spending.exception;

public class NotExistStorageException extends StorageException{
    public NotExistStorageException(String uuid) {
        super("Запись " + uuid + " не найдена", uuid);
    }
}
