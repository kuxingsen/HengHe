package com.henghe.service;

import com.henghe.bean.Message;
import com.henghe.dto.Result;
import com.henghe.utils.DateUtils;
import com.henghe.utils.DbUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.henghe.bean.Content.REAL_FILE_PATH;
import static com.henghe.bean.Content.REAL_TITLE_IMG_PATH;
import static com.henghe.bean.Content.TITLE_IMG_PATH;

@Service
public class AdminService{

    public int getAdminId(String username, String oldPassword) {
        ResultSet result;
        String sql = "select id from admin where name=? and password=?";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, username);
            ps.setString(2, oldPassword);
            result = ps.executeQuery();
            if(result != null && result.next()) {
                System.out.println(result.getInt("id"));
                return result.getInt("id");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Message> getMessageResult(String searchId, String sql) {
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setInt(1, Integer.parseInt(searchId));
            result = ps.executeQuery();
            if(result != null) {
                List<Message> messageList = new ArrayList<>();
                while(result.next()) {
                    Message message = new Message();
                    message.setId(result.getString("id"));
                    message.setTitle(result.getString("title"));
                    if(result.getString("title_img") != null){
                        message.setTitleImgPath(TITLE_IMG_PATH + File.separator+result.getString("title_img"));
                    }
                    message.setDate(result.getString("date"));
                    message.setColumnId(String.valueOf(result.getInt("menu_id")));
                    message.setColumnName(result.getString("name"));
                    message.setContent(result.getString("content"));
                    message.setIsPushed(result.getInt("is_push")==1?"ÊÇ":"·ñ");
                    String filePath = result.getString("file");
                    if(filePath!= null){
                        filePath = filePath.substring(filePath.indexOf("_")+1);
                    }
                    message.setFilePath(filePath);

                    messageList.add(message);
                }
                return messageList.size()>0?messageList:null;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int updateAdmin(Integer adminId,String username,String newPassword) {
        int result = 0;
        String sql = "update admin set name=?,password=? where id=?";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, username);
            ps.setString(2, newPassword);
            ps.setInt(3, adminId);
            result = ps.executeUpdate();
//            System.out.println(result);
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int insertMessage(Integer adminId,Message message,String imgPath,String filePath){
        int result = 0;
        String sql = "insert into message(title,title_img,date,content,file,is_push,menu_id,admin_id) values(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, message.getTitle());
            ps.setString(2, imgPath);
            ps.setString(3, DateUtils.today("yyyy-MM-dd"));
            ps.setString(4, message.getContent());
            ps.setString(5, filePath);
            ps.setInt(6, Integer.parseInt(message.getIsPushed()));
            ps.setInt(7, Integer.parseInt(message.getColumnId()));
            ps.setInt(8, adminId);
            result = ps.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public int updateMessage(Integer adminId,Message message){
        int result = 0;
        String sql = "update message set title=?,title_img=?,content=?,file=?,is_push=?,menu_id=?,admin_id=? where id=?";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, message.getTitle());
            ps.setString(2, message.getTitleImgPath());
            ps.setString(3, message.getContent());
            ps.setString(4, message.getFilePath());
            ps.setInt(5, Integer.parseInt(message.getIsPushed()));
            ps.setInt(6, Integer.parseInt(message.getColumnId()));
            ps.setInt(7, adminId);
            ps.setString(8, message.getId());
            result = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String saveFile(MultipartFile file) {
        String millis = System.currentTimeMillis() + "";
        File dir = new File(REAL_FILE_PATH);
        if(!dir.getParentFile().exists()) {
            boolean mk = dir.getParentFile().mkdirs();
            System.out.println("create: yundao_dir " + mk);
        }
        if(!dir.exists()) {
            boolean mk = dir.mkdirs();
            System.out.println("create file_path_dir:" + mk);
        }
        String fileName = millis + "_" + file.getOriginalFilename();
        File uploadFile = new File(REAL_FILE_PATH, fileName);
        if(copyFile(file, uploadFile)) return fileName;
        return null;
    }

    public String saveTitleImg(MultipartFile titleImg) {
        String millis = System.currentTimeMillis() + "";
        File dir = new File(REAL_TITLE_IMG_PATH);
        if(!dir.getParentFile().exists()) {
            boolean mk = dir.getParentFile().mkdirs();
            System.out.println("create: yundao_dir " + mk);
        }
        if(!dir.exists()) {
            boolean mk = dir.mkdirs();
            System.out.println("create title_img_path_dir:" + mk);
        }
        String originalFileName = titleImg.getOriginalFilename();
        String fileName = millis + originalFileName.substring(originalFileName.lastIndexOf("."));
        File uploadFile = new File(REAL_TITLE_IMG_PATH, fileName);
//        System.out.println(fileName);
        if(copyFile(titleImg, uploadFile)) return fileName;
        return null;
    }

    private boolean copyFile(MultipartFile titleImg, File uploadFile) {
        try {
            FileUtils.copyInputStreamToFile(titleImg.getInputStream(), uploadFile);
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("filePath is not save");
            return false;
        }
        return true;
    }

    public Message updatePathInMessage(Message message, String imgPath,String filePath) {
        ResultSet result1;
        String sql = "select title_img,file from message where id =?";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setInt(1, Integer.valueOf(message.getId()));
            result1 = ps.executeQuery();
            if(result1 != null) {
                while(result1.next()){
                    String imgTmp = result1.getString("title_img");
                    String fileTmp = result1.getString("file");
                    if(imgPath != null){
                        deleteImg(imgTmp);
                        message.setTitleImgPath(imgPath);
                    }else {
                        message.setTitleImgPath(imgTmp);
                    }
                    if(filePath != null){
                        deleteFile(fileTmp);
                        message.setFilePath(filePath);
                    }else {
                        message.setFilePath(fileTmp);
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        System.out.println(message.getTitleImgPath()+":::::"+message.getFilePath());
        return message;
    }

    private void deleteImg(String imgTmp) {
        if(imgTmp != null){
            File dir = new File(REAL_TITLE_IMG_PATH,imgTmp);
            if (dir.exists()) {
                boolean res = dir.delete();
                System.out.println("delete img: "+res);
            }
        }
    }
    private void deleteFile(String fileTmp) {
        if(fileTmp != null){
            File dir = new File(REAL_FILE_PATH,fileTmp);
            if (dir.exists()) {
                boolean res = dir.delete();
                System.out.println("delete file: "+res);
            }
        }
    }

    public int deleteMessage(List<String> messageId){
        int result=0;
        int size = messageId.size();
        StringBuilder sql = new StringBuilder("delete from message where id in( ?");
        for(int i = 1;i < size;i++){
            sql.append(",?");
        }
        sql.append(")");
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql.toString());
            for(int i = 1;i <= size;i++){
                ps.setInt(i, Integer.valueOf(messageId.get(i-1)));
            }
            result = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void deleteImgAndFile(List<String> messageId) {
        int size = messageId.size();
        ResultSet result;
        StringBuilder sql = new StringBuilder("select title_img,file from message where id in( ?");
        for(int i = 1;i < size;i++){
            sql.append(",?");
        }
        sql.append(")");
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql.toString());
            for(int i = 1;i <= size;i++){
                ps.setInt(i, Integer.valueOf(messageId.get(i-1)));
            }
            result = ps.executeQuery();
            if(result != null) {
                while(result.next()){
                    String titleImg = result.getString("title_img");
                    String file = result.getString("file");
                    deleteImg(titleImg);
                    deleteFile(file);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
