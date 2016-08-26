package ua.com.itproekt.gup.service.filestorage;

import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.web.multipart.MultipartFile;
import ua.com.itproekt.gup.server.api.rest.dto.FileUploadWrapper;

import java.util.Map;
import java.util.Set;

public interface StorageService {

    /**
     * @param serviceName
     * @param fileId
     */
    void delete(String serviceName, String fileId);

    /**
     * @param serviceName
     * @param fileIds
     */
    void delete(String serviceName, Set<String> fileIds);

    /**
     * @param serviceName
     * @param filePath
     * @param fileId
     * @return
     */
    GridFSDBFile getCachedImage(String serviceName, String filePath, String fileId);

    /**
     * Save photo in two variants: large and small
     *
     * @return - id of image
     */
    String saveCachedImageProfile(FileUploadWrapper fileUploadWrapper);


    /**
     * Save photo in several size variants.
     *
     * @return - id of image
     */
    String saveCachedImageOffer(FileUploadWrapper fileUploadWrapper);


    /**
     * Method save multiply images in different sizes (cached), and return map of <ImagesId, image order></ImagesId,>
     *
     * @param files Multipart array
     * @return Map of images id's and their order.
     */
    Map<String, String> saveCachedMultiplyImageOffer(MultipartFile[] files);
}
