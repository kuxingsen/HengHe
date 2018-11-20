package com.henghe.bean;

public class Member{
    //    id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    private String id;
    //  `account` varchar(20) COMMENT '账号',
    private String account;
    //  `password` VARCHAR(50) COMMENT '密码',
    private String password;
    //  `name` VARCHAR(20) COMMENT '姓名',
    private String name;
    //  `sex` varchar(20) COMMENT '性别',
    private String sex;
    //  `birthday` TEXT COMMENT '出生日期',
    private String birthday;
    //  `phone` VARCHAR(100) COMMENT '电话',
    private String phone;
    //  `email` tinyint COMMENT '邮箱',
    private String email;
    //  `address` INT COMMENT '地址',
    private String address;

    public Member() {
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
