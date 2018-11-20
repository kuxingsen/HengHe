package com.henghe.service;

import com.henghe.bean.Banner;
import com.henghe.bean.Column;
import com.henghe.bean.Message;
import com.henghe.dto.Result;
import com.henghe.utils.DbUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.henghe.bean.Content.TITLE_IMG_PATH;

@Service
public class CommonService{

    public List<Column> getColumn() {
        String sql = "select * from menu";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            result = ps.executeQuery();
            System.out.println(result);
            if(result != null) {
                List<Column> columnList = new ArrayList<>();
                while(result.next()) {
                    Column column = new Column();
                    column.setId(result.getString("id"));
                    column.setName(result.getString("name"));

                    columnList.add(column);
                }
                return columnList;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getColumnName(Integer columnId) {
        String sql = "select name from menu where id = ?";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setInt(1,columnId);
            result = ps.executeQuery();
            if(result != null) {
                if(result.next()){
                    return result.getString("name");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int getCount(String searchId) {
        String sql;
        if(searchId.equals("0")){
            sql = "select count(*) count from message where 0 = ?";
        }else {
            sql= "select count(*) count from message where menu_id = ?";
        }
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setInt(1, Integer.parseInt(searchId));
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

    public List<Banner> selectBanner(int num) {
        String sql = "select id,title,title_img from message where is_push = 1 order by id desc limit 0,?";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setInt(1, num);
            result = ps.executeQuery();
//        System.out.println(result);
            if(result != null) {
                List<Banner> bannerList = new ArrayList<>();
                while(result.next()) {
                    Banner banner = new Banner();
                    banner.setId(result.getString("id"));
                    banner.setTitle(result.getString("title"));
                    String titleImg = result.getString("title_img");
                    if(titleImg != null) {
                        banner.setTitleImg(TITLE_IMG_PATH + File.separator + titleImg);
                    }
                    bannerList.add(banner);
                }
                return bannerList;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileName(String messageId) {
        String sql = "select file from message where id=?";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, messageId);
            result = ps.executeQuery();
            if(result != null) {
                if(result.next()) {
                    return result.getString("file");
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
