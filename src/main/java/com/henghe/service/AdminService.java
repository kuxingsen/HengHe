package com.henghe.service;

import com.henghe.bean.Column;
import com.henghe.bean.Member;
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
    //操作admin
    public int getAdminId(String username, String oldPassword) {
        ResultSet result;
        String sql = "select id from admin where name=? and password=?";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, username);
            ps.setString(2, oldPassword);
            result = ps.executeQuery();
            if(result != null && result.next()) {
                //System.out.println(result.getInt("id"));
                return result.getInt("id");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

    //操作message
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
                    message.setIsPushed(result.getInt("is_push")==1?"是":"否");
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
    public Message updatePathInMessage(Message message, String imgPath,String filePath) {//to midify message
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
                        deleteFileOrImg(imgTmp,REAL_TITLE_IMG_PATH);
                        message.setTitleImgPath(imgPath);
                    }else {
                        message.setTitleImgPath(imgTmp);
                    }
                    if(filePath != null){
                        deleteFileOrImg(fileTmp,REAL_FILE_PATH);
                        message.setFilePath(filePath);
                    }else {
                        message.setFilePath(fileTmp);
                    }
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(message.getTitleImgPath()+":::::"+message.getFilePath());
        return message;
    }
    public int deleteMessage(List<String> messageId){
        return deleteByIds(messageId,"message");
    }
    public int getCount(String name) {
        String sql="select count(id) count from member where name like '%"+name+"%'";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            result = ps.executeQuery();
            if(result != null) {
                if(result.next()){
                    return result.getInt("count");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //操作User
    public List<Member> selectMember(String name, String index, String limit) {
        String sql = "select * from member where name like '%"+name+ "%' limit ?,?";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
//            ps.setString(1,name);
            ps.setInt(1, Integer.parseInt(index));
            ps.setInt(2, Integer.parseInt(limit));
            result = ps.executeQuery();
            if(result != null) {
                List<Member> memberList = new ArrayList<>();
                while(result.next()){
                    Member member = new Member();
                    member.setId(result.getString("id"));
                    member.setAccount(result.getString("account"));
                    member.setName(result.getString("name"));
                    member.setSex(result.getString("sex"));
                    member.setBirthday(result.getString("birthday"));
                    member.setPhone(result.getString("phone"));
                    member.setEmail(result.getString("email"));
                    member.setAddress(result.getString("address"));
                    memberList.add(member);
                }
                return memberList.size()>0?memberList:null;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int deleteUser(List<String> userId) {
        int result=0;
        int size = userId.size();
        StringBuilder sql = new StringBuilder("delete from member where id in( ?");
        for(int i = 1;i < size;i++){
            sql.append(",?");
        }
        sql.append(")");
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql.toString());
            for(int i = 1;i <= size;i++){
                ps.setInt(i, Integer.valueOf(userId.get(i-1)));
            }
            result = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //操作file/img
    public String saveFile(MultipartFile file) {
        String millis = System.currentTimeMillis() + "";
        File dir = new File(REAL_FILE_PATH);
        if(!dir.getParentFile().exists()) {
            boolean mk = dir.getParentFile().mkdirs();
            System.out.println("create: henghe_dir " + mk);
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
            System.out.println("create: henghe_dir " + mk);
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
    private boolean copyFile(MultipartFile titleImg, File uploadFile) {//to save uploadFile
        try {
            FileUtils.copyInputStreamToFile(titleImg.getInputStream(), uploadFile);
        } catch(IOException e) {
            e.printStackTrace();
            System.err.println("filePath is not save");
            return false;
        }
        return true;
    }
    private int deleteFileOrImg(String fileTmp,String path) {//to modify/delete message
        if(fileTmp != null){
            File dir = new File(path,fileTmp);
            if (dir.exists()) {
                boolean res = dir.delete();
//                System.out.println(dir.getAbsolutePath()+res);
                return res?1:0;
            }
//            System.out.println(dir.getAbsolutePath());
        }
        return 0;
    }
    public void deleteImgAndFile(List<String> messageId) {//to delete message
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
                    deleteFileOrImg(titleImg,REAL_TITLE_IMG_PATH);
                    deleteFileOrImg(file,REAL_FILE_PATH);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public int deleteOldFileOrImg(int messageId,String w) {//to modify message
        String file=null;
        String sql="select "+w+" as need from message where id = ?";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
//            ps.setString(1,w);
            ps.setInt(1,messageId);
            result = ps.executeQuery();
            if(result != null) {
                if(result.next()){
                    file = result.getString("need");
//                    System.out.println(file);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(file);
        if(file != null && !file.equals("")){
            setPathNull(w,messageId);
            if(w.equals("title_img")){
                deleteFileOrImg(file,REAL_TITLE_IMG_PATH);
            }else {
                deleteFileOrImg(file,REAL_FILE_PATH);
            }
            return 1;
        }
        return -1;//-1代表该id没有相应的title_img
    }
    private int setPathNull(String path,Integer messageId){//to modify message
        String sql="update message set "+path+"=? where id = ?";
        int result=0;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
//            ps.setString(1,w);
            ps.setString(1,null);
            ps.setInt(2,messageId);
            result = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //操作Column
    public int updateColumn(Column column){
        String sql = "update menu set name=? where id=?";
        int result=0;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, column.getName());
            ps.setInt(2, Integer.parseInt(column.getId()));
            result = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public int deleteColumn(List<String> columnIds){
        return deleteByIds(columnIds,"column");
    }

    public int insertColumn(Column column){
        int result = 0;
        String sql = "insert into column(name) values(?)";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, column.getName());
            result = ps.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    private int deleteByIds(List<String> ids,String table) {
        int result=0;
        int size = ids.size();
        StringBuilder sql = new StringBuilder("delete from "+table+" where id in( ?");
        for(int i = 1;i < size;i++){
            sql.append(",?");
        }
        sql.append(")");
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql.toString());
            for(int i = 1;i <= size;i++){
                ps.setInt(i, Integer.valueOf(ids.get(i-1)));
            }
            result = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
