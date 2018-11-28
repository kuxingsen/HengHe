package com.henghe.controller;

import com.henghe.bean.Member;
import com.henghe.dto.Result;
import com.henghe.service.CommonService;
import com.henghe.service.MemberService;
import com.henghe.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sun.plugin.javascript.navig.Link;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
@RestController
public class MemberController{
    @Autowired
    private MemberService memberService;
    @Autowired
    private CommonService commonService;
    @RequestMapping("login")
    public Result login(String account, String password, HttpSession session){

        Member member = memberService.login(account,password);
        if(member != null){
            session.setAttribute("member",member);
            session.setAttribute("member_id",member.getId());
            session.setAttribute("member_key", Md5Utils.key(member.getAccount()));
            return new Result(200);
        }
        return new Result(500,"ÕËºÅ»òÃÜÂë³ö´í");
    }

    @RequestMapping("logout")
    public Result logout(HttpSession session){
        session.setAttribute("member",null);
        session.setAttribute("member_key", null);
        session.setAttribute("member_id", null);
        return new Result(200);
    }

    @RequestMapping("isLogin")
    public Result isLogin(HttpSession session){
        if(session.getAttribute("member_key") != null){
            return new Result(200);
        }
        return new Result(500,"Î´µÇÂ¼");
    }

    @RequestMapping("selectMember")
    public Result<Member> selectMember(HttpSession session){
        Member member = (Member) session.getAttribute("member");
        if(member != null){
            List<Member> memberList = new ArrayList<>();
            memberList.add(member);
            return new Result<>(200,memberList);
        }
        return new Result<>(500,"Î´µÇÂ¼");
    }

    @RequestMapping("accountRepeat")
    public Result accountRepeat(String account){
        int r = memberService.accountRepeat(account);
        if(r > 0){
            return new Result(200);
        }
        return new Result(500,"ÕËºÅÖØ¸´");
    }

    @RequestMapping("insertMember")
    public Result insertMember(Member member){
        int r1 = memberService.check(member);
        if(r1 > 0){
            int r = memberService.insertMember(member);
            if(r > 0){
                return new Result(200);
            }
        }
        return new Result(500,"ÕËºÅÐÅÏ¢³ö´í");
    }
    @RequestMapping("updateMember")
    public Result updateMember(Member member,HttpSession session){
        String key = (String) session.getAttribute("member_key");
        if(key.equals(Md5Utils.key(member.getAccount()))){
            member.setId((String) session.getAttribute("member_id"));
            int r1 = memberService.check(member);
            if(r1 > 0) {
                int r = memberService.updateMember(member);
                if(r > 0) {
                    member.setPassword(null);
                    session.setAttribute("member",member);
                    session.setAttribute("member_id",member.getId());
                    session.setAttribute("member_key", Md5Utils.key(member.getAccount()));
                    return new Result(200);
                }
            }
        }
        return new Result(500,"ÕËºÅÐÅÏ¢³ö´í");
    }
}
