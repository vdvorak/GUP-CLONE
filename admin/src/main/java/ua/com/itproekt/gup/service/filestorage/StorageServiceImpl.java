package ua.com.itproekt.gup.service.filestorage;

import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.itproekt.gup.dao.filestorage.StorageRepository;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    StorageRepository storageRepository;

    @Override
    public String save(String serviceName, InputStream inputStream, String contentType, String filename) {
        return storageRepository.save(serviceName, inputStream, contentType, filename);
    }

    @Override
    public GridFSDBFile get(String serviceName, String fileId) {
        return storageRepository.get(serviceName, fileId);
    }

    @Override
    public void delete(String serviceName, String fileId) {
        storageRepository.delete(serviceName, fileId);
    }

    @Override
    public void delete(String serviceName, Set<String> fileIds) {
        storageRepository.delete(serviceName, fileIds);
    }

    @Override
    public void cacheImage(String serviceName, String originalImageId, BufferedImage bufferedImage, String contentType, String originalFilename) throws IOException {
        storageRepository.cacheImage( serviceName, originalImageId, bufferedImage, contentType, originalFilename);
    }

    @Override
    public GridFSDBFile getCachedImage(String serviceName, String fileId) {
        return storageRepository.getCachedImage(serviceName, fileId);
    }

}