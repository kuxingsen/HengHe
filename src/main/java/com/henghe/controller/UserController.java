package com.henghe.controller;

import com.henghe.bean.Banner;
import com.henghe.bean.Column;
import com.henghe.bean.Message;
import com.henghe.dto.Result;
import com.henghe.service.CommonService;
import com.henghe.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.henghe.bean.Content.REAL_FILE_PATH;

/**
 * User 为未注册用户
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CommonService commonService;

    @RequestMapping("selectColumn")
    @ResponseBody
    public Result<Column> selectColumn() {
        List<Column> columnList = commonService.getColumn();
        if(columnList != null) return new Result<>(200,columnList);
        return new Result<>(500, "没有相应的记录");
    }
    @RequestMapping("columnName")
    @ResponseBody
    public Result<String> columnName(Integer columnId) {
        String name = commonService.getColumnName(columnId);
        if(name != null){
            List<String> s = new ArrayList<>();
            s.add(name);
            return new Result<>(200,s);
        }
        return new Result<>(500, "没有相应的记录");
    }

    @RequestMapping("selectBanner")
    @ResponseBody
    public Result<Banner> selectBanner(@RequestParam(defaultValue = "5") Integer num) {
        List<Banner> bannerList = commonService.selectBanner(num);
        if(bannerList != null){
            return new Result<>(200, bannerList);
        }
        return new Result<>(500, "没有相应的记录");
    }

    @RequestMapping("selectMessageByMessageId")
    @ResponseBody
    public Result<Message> selectMessageByMessageId(String messageId) {
        List<Message> messageList = userService.selectMessageByMessageId(messageId);
        if(messageList != null) return new Result<>(200, messageList);
        return new Result<>(500, "没有相应的记录");
    }

    @RequestMapping("selectMessageByColumnId")
    @ResponseBody
    public Result<Message> selectMessageByColumnId(String columnId, @RequestParam(defaultValue = "1") String page, @RequestParam(defaultValue = "5") String count) {
//        int count = 10;
        if((page == null) || page.equals("") || !page.matches("[1-9]\\d*")){
            page = "1";
        }
        if((count == null) || count.equals("") || !count.matches("[1-9]\\d*")){
            count = "5";
        }
        int index = Integer.valueOf(count)*(Integer.valueOf(page)-1);
        List<Message> messageList = userService.selectMessageByColumnId(columnId,index,Integer.valueOf(count));

        int totalNum = commonService.getCount(columnId);
        int totalPage = (int)Math.ceil(1.0*totalNum/Integer.valueOf(count));
        if(messageList != null) return new Result<>(200,Integer.valueOf(page),totalNum,totalPage,messageList);
        return new Result<>(500, "没有相应的记录");
    }

    @RequestMapping("downFile")
    public ResponseEntity<byte[]> downFile(String messageId) throws IOException {
        String fileName = commonService.getFileName(messageId);
        if(fileName != null) {
            File downFile = new File(REAL_FILE_PATH, fileName);
            if (!downFile.getParentFile().exists()) {
                boolean mk=downFile.getParentFile().mkdirs();
                System.err.println("create:"+mk);
            }

            //System.out.println(fileName);
            HttpHeaders headers = new HttpHeaders();

            //下载显示的文件名，解决中文名称乱码问题
            String downloadFielName = new String(fileName.substring(fileName.indexOf("_")).getBytes(StandardCharsets.UTF_8),
                    StandardCharsets.ISO_8859_1);
            //通知浏览器以attachment（下载方式）
            headers.setContentDispositionFormData("attachment", downloadFielName);
            //application/octet-stream ： 二进制流数据（最常见的文件下载）。
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            /*
              解决IE不能下载文件问题
             */
            return new ResponseEntity<>(FileUtils.readFileToByteArray(downFile),
                    headers, HttpStatus.OK);
        }
        return null;
    }


}
