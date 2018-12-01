package com.henghe.service;

import com.henghe.bean.Member;
import com.henghe.utils.DateUtils;
import com.henghe.utils.DbUtil;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class MemberService{

    public Member login(String account, String password) {
        ResultSet result;
        String sql = "select * from member where account=? and password=?";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, account);
            ps.setString(2, password);
            result = ps.executeQuery();
            if(result != null && result.next()) {
                Member member = new Member();
                member.setId(result.getString("id"));
                member.setAccount(result.getString("account"));
                member.setName(result.getString("name"));
                member.setSex(result.getString("sex"));
                member.setBirthday(result.getString("birthday"));
                member.setPhone(result.getString("phone"));
                member.setEmail(result.getString("email"));
                member.setAddress(result.getString("address"));

                return member;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int accountRepeat(String account) {
        String sql="select id from member where account=?";
        ResultSet result;
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1,account);
            result = ps.executeQuery();
            if(result != null) {
                if(result.next()){
                    return 1;
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int insertMember(Member member) {
        int result = 0;
        String sql = "insert into member(id,account,password,name,sex,birthday,phone,email,address) values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, null);
            ps.setString(2, member.getAccount());
            ps.setString(3, member.getPassword());
            ps.setString(4, member.getName());
            ps.setString(5, member.getSex());
            ps.setString(6, member.getBirthday());
            ps.setString(7, member.getPhone());
            ps.setString(8, member.getEmail());
            ps.setString(9, member.getAddress());
            result = ps.executeUpdate();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int updateMember(Member member) {
        int result = 0;
        String sql = "update member set password = ?,name=?,sex=?,birthday=?,phone=?,email=?,address=?where id=?";
        try {
            PreparedStatement ps = DbUtil.executePreparedStatement(sql);
            ps.setString(1, member.getPassword());
            ps.setString(2, member.getName());
            ps.setString(3, member.getSex());
            ps.setString(4, member.getBirthday());
            ps.setString(5, member.getPhone());
            ps.setString(6, member.getEmail());
            ps.setString(7, member.getAddress());
            ps.setString(8, member.getId());
            result = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int check(Member member) {
        //todo 数据校验需完善
        String account = member.getAccount();
        if(account == null || account.length()>20 || account.length() < 1){
           return -1;
        }else if( accountRepeat(account) == 1){
            return -2;
        }
        String password = member.getPassword();
        if(password == null || password.length()>20 || password.length() < 1){
            return -1;
        }
        String name = member.getName();
        if(name == null || name.length()>20 || name.length() < 1){
            return -1;
        }
        String sex = member.getSex();
        if(sex == null || sex.length()>20 || sex.length() < 1 ){
            return -1;
        }
        String birthday = member.getBirthday();
        if(birthday == null || birthday.length()>20 || birthday.length() < 1){
            return -1;
        }
        String phone = member.getPhone();
        if(phone == null || phone.length()>20 || phone.length() < 1 || !phone.matches("1[34578]\\d{9}$")){
            return -1;
        }
        String email = member.getEmail();
        if(email == null || email.length()>20 || email.length() < 1 || !email.matches("^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$")){
            return -1;
        }
        String address = member.getAddress();
        if(address == null || address.length()>20 || address.length() < 1){
            return -1;
        }
        return 1;
    }
}
