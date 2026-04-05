package com.asr.blogapp.helper;

import org.jsoup.Jsoup;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Helper {

    public static String getPostTitle(String postTitle) {

        String title = postTitle.trim().toLowerCase();

        String url = title.replace("\\s", "-");
        url=url.replaceAll("[^A-Za-z0-9]", "-");
        return url;
    }

    // to save file
    public static boolean saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {

        Path uploadPath = Paths.get(uploadDir);

        try(InputStream multipartFileInputStream = multipartFile.getInputStream())
        {

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path fullPath = uploadPath.resolve(fileName);
            Files.copy(multipartFileInputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String htmlSenitizer(String shortDescription) {
        return Jsoup.parse(shortDescription).text();

    }
}
