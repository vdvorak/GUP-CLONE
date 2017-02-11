package com.journaldev.spring.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.journaldev.spring.CreatedObjResp;

@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    /**
     * 200 OK («хорошо»)
     * 201 Created («создано»)
     * 400 Bad Request («плохой, неверный запрос»)
     * 406 Not Acceptable («неприемлемо»)
     *
     * @param name
     * @param file
     * @return
     */
    @RequestMapping(value = "/photo/upload", method = RequestMethod.POST)
    public @ResponseBody String photoUpload(@RequestParam("name") String name,
                                            @RequestParam("file") MultipartFile file,
                                            @RequestParam(required = false, defaultValue = "false") boolean cachedImage) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File        dir = new File(rootPath + File.separator + "photos");
                if (!dir.exists()) dir.mkdirs();

                // Create the file on server
                File             serverFile = new File(dir.getAbsolutePath() + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                CreatedObjResp createdObjResp = new CreatedObjResp(name);
                logger.info("Server File Location=" + serverFile.getAbsolutePath() + " | " + createdObjResp.getId());
                return new CreatedObjResp(name).toString();
            } catch (Exception e) {
                logger.error("You failed to upload " + name + " => " + e.getMessage());
                return new CreatedObjResp().toString();
            }
        } else {
            logger.error("You failed to upload " + name + " because the file was empty.");
            return new CreatedObjResp().toString();
        }
    }

}
