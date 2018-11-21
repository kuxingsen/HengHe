package com.henghe.bean;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class Content{
    public static final String REAL_TITLE_IMG_PATH ;
    public static final String REAL_FILE_PATH;
    public static final String TITLE_IMG_PATH ;
    public static final String FILE_PATH;


    public static final String URL;
    public static final String USER;
    public static final String PASSWORD;
    public static final String DRIVER;


    static {
        Resource resource = new ClassPathResource("content.properties");
        Properties properties;
        try {
            //System.out.println("?????????");
            properties = PropertiesLoaderUtils.loadProperties(resource);
            //System.out.println("?????????");
        } catch (IOException e) {
            throw new RuntimeException("content.properties import exception");
        }
        REAL_TITLE_IMG_PATH = properties.getProperty("real_title_img_path");
        REAL_FILE_PATH = properties.getProperty("real_file_path");
        TITLE_IMG_PATH = properties.getProperty("title_img_path");
        FILE_PATH = properties.getProperty("file_path");
        URL = properties.getProperty("jdbc_url");
        USER = properties.getProperty("jdbc_user");
        PASSWORD = properties.getProperty("jdbc_password");
        DRIVER = properties.getProperty("jdbc_driver");
    }
}
