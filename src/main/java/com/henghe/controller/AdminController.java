package com.henghe.controller;

import com.henghe.bean.Column;
import com.henghe.bean.Member;
import com.henghe.bean.Message;
import com.henghe.dto.LayUIResult;
import com.henghe.dto.Result;
import com.henghe.service.AdminService;
import com.henghe.service.CommonService;
import com.henghe.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController{
    @Autowired
    private AdminService adminService;
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "adminLogin", method = RequestMethod.POST)
    @ResponseBody
    public Result adminLogin(String username, String password, HttpSession session) {
        int adminId;
        if((adminId = adminService.getAdminId(username, password)) != 0){
            session.setAttribute("username",username);
            session.setAttribute("adminId",adminId);
            session.setAttribute("admin_key", Md5Utils.key(String.valueOf(adminId)));
            return new Result(200);
        }
        return new Result(500, "用户名或密码错误");
    }

    @RequestMapping(value = "adminLogout")
    @ResponseBody
    public Result adminLogout( HttpSession session) {
        session.setAttribute("username",null);
        session.setAttribute("adminId",null);
        session.setAttribute("admin_key", null);
        return new Result(200);
    }

    @RequestMapping("getUsername")
    @ResponseBody
    public Result<String> getUsername(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if(username != null) {
            List<String> datas = new ArrayList<>();
            datas.add(username);
            return new Result<>(200, datas);
        }
        return new Result<>(500, "未登录");
    }

    @RequestMapping("updateAdmin")
    @ResponseBody
    public Result updateAdmin(HttpSession session, String password,String newPassword) {
        String username= (String) session.getAttribute("username");
        int adminId = (int) session.getAttribute("adminId");

        if(adminId == adminService.getAdminId(username, password)){
            System.out.println(adminId);
            int result = adminService.updateAdmin(adminId,username,newPassword);
            if(result > 0){
                return new Result(200);
            }
        }
//        System.out.println(adminId);
        return new Result(500, "更新失败");
    }

    @RequestMapping("insertMessage")
    @ResponseBody
    public Result insertMessage( MultipartFile titleImg, MultipartFile file,Message message, HttpSession session) {
//        System.out.println("insert");
        int adminId = (int) session.getAttribute("adminId");
        String imgPath = null, filePath = null;
        if(titleImg != null) {
            imgPath = adminService.saveTitleImg(titleImg);
        }
        if(file != null) {
            filePath = adminService.saveFile(file);
        }
//        System.out.println(message);
        int result = adminService.insertMessage(adminId,message,imgPath,filePath);
        if(result > 0) {
            return new Result(200);
        }
        return new Result<>(500, "插入失败");
    }

    @RequestMapping("updateMessage")
    @ResponseBody
    public Result updateMessage(@RequestParam(value = "titleImg", required = false) MultipartFile titleImg, @RequestParam(value = "file", required = false) MultipartFile file, Message message, HttpSession session) {
//        System.out.println("update111");
        int adminId = (int) session.getAttribute("adminId");
        String imgPath = null, filePath = null;
        if(titleImg != null) {
            System.out.println("titleImg is not null");
            imgPath = adminService.saveTitleImg(titleImg);
        }
        if(file != null) {
            System.out.println("file is not null");
            filePath = adminService.saveFile(file);
        }
//        System.out.println(imgPath+":::::"+filePath);
        message = adminService.updatePathInMessage(message,imgPath,filePath);
        //System.out.println(message.getTitleImgPath()+":::::"+message.getFilePath());
        int result = adminService.updateMessage(adminId,message);
        if(result > 0) {
            return new Result(200);
        }
        return new Result<>(500, "更新失败");
    }


    @RequestMapping("deleteMessage")
    @ResponseBody
    public Result deleteMessage(@RequestParam("messageId[]") List<String> messageId) {
        adminService.deleteImgAndFile(messageId);//删除相应的标题图和信息
        int result = adminService.deleteMessage(messageId);
        if(result > 0) {
            return new Result(200);
        }
        return new Result(500, "删除失败");
    }

    @RequestMapping("selectMessageByMessageId")
    @ResponseBody
    public Result<Message> selectMessageByMessageId(String messageId) {
//        System.out.println(messageId);
        String sql = "select * from message mess,menu where mess.id=? and mess.menu_id=menu.id";
        List<Message> messageList = adminService.getMessageResult(messageId, sql);
        if(messageList != null) return new Result<>(200, messageList);
        return new Result<>(500, "没有相应的记录");
    }

    @RequestMapping("selectMessageByColumnId")
    @ResponseBody
    public LayUIResult<Message> selectMessageByColumnId(String columnId, String page, String limit) {
//        System.out.println(columnId+page+""+limit);
        if((page == null) || page.equals("") || !page.matches("[1-9]\\d*")){
            page = "1";
        }
        if((limit == null) || limit.equals("") || !limit.matches("[1-9]\\d*")){
            limit = "10";
        }
        int index = Integer.valueOf(limit)*(Integer.valueOf(page)-1);
        String sql;
        if(columnId == null || columnId.equals("") || !columnId.matches("[1-9]\\d*")){
            sql = "select * from message mess,menu where 0=? and mess.menu_id=menu.id  order by mess.id desc limit "+index+","+limit;
            columnId = "0";
        }else {
            sql = "select * from message mess,menu where mess.menu_id=? and mess.menu_id=menu.id order by mess.id desc limit "+index+","+limit;
        }
        List<Message> messageList = adminService.getMessageResult(columnId, sql);
        int count = commonService.getCount(columnId);
        if(messageList != null) return new LayUIResult<>(0, count,messageList);
        return new LayUIResult<>(-1, "没有相应的记录");
    }

    @RequestMapping("selectColumn")
    @ResponseBody
    public Result<Column> selectColumn() {
        List<Column> columnList = commonService.getColumn();
        if(columnList != null) return new Result<>(200,columnList);
        return new Result<>(500, "没有相应的记录");
    }

    @RequestMapping("selectUser")
    @ResponseBody
    public LayUIResult<Member> selectUser(@RequestParam(value = "userName",defaultValue = "") String name,@RequestParam(defaultValue = "1") String page,@RequestParam(defaultValue = "10") String limit){
        if((page == null) || page.equals("") || !page.matches("[1-9]\\d*")){
            page = "1";
        }
        if((limit == null) || limit.equals("") || !limit.matches("[1-9]\\d*")){
            limit = "10";
        }
        int index = Integer.valueOf(limit)*(Integer.valueOf(page)-1);
        List<Member> memberList = adminService.selectMember(name, String.valueOf(index),limit);
        int count = adminService.getCount(name);
        if(memberList != null) return new LayUIResult<>(0,count,memberList);
        return new LayUIResult<>(500, "没有相应的记录");
    }

    @RequestMapping("deleteUser")
    @ResponseBody
    public Result deleteUser(@RequestParam("userId[]") List<String> userId) {
        int result = adminService.deleteUser(userId);
        if(result > 0) {
            return new Result(200);
        }
        return new Result(500, "删除失败");
    }

    @RequestMapping("delOldImage")
    @ResponseBody
    public Result deleteOldImage(String messageId){
        int result = adminService.deleteOldFileOrImg(Integer.parseInt(messageId),"title_img");
        if(result > 0) {
            return new Result(200);
        }
        return new Result(500, "删除失败");

    }
    @RequestMapping("delOldFile")
    @ResponseBody
    public Result deleteOldFile(String messageId){
        int result = adminService.deleteOldFileOrImg(Integer.parseInt(messageId),"file");
        if(result > 0) {
            return new Result(200);
        }
        return new Result(500, "删除失败");
    }
    @RequestMapping("updateColumn")
    @ResponseBody
    public Result updateColumn(Column column){
        System.out.println(column);
        if(column != null){
            int result = adminService.updateColumn(column);
            if(result > 0) {
                return new Result(200);
            }
        }
        return new Result(500, "删除失败");
    }
}
