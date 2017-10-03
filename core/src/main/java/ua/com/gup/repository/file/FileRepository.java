package ua.com.gup.repository.file;


import ua.com.gup.model.file.FileWrapper;
import ua.com.gup.model.offer.Image;

public interface FileRepository {

    String save(FileWrapper fileWrapper);

    FileWrapper findOne(String bucket, String id);

    void delete(String bucket, Image id);
}