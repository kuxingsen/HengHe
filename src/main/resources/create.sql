# 建立数据库
CREATE DATABASE henghe;

USE henghe;
# 建立管理员表
CREATE TABLE admin(
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` VARCHAR(20) COMMENT '用户名',
  `password` VARCHAR(20) COMMENT '用户密码',
  PRIMARY KEY (id)
)ENGINE = InnoDB CHARSET = utf8;

# 建立栏目表
CREATE TABLE menu(
  `id` int NOT NULL AUTO_INCREMENT COMMENT '栏目id',
  `name` VARCHAR(20) COMMENT '栏目名',
  PRIMARY KEY (id)
)ENGINE = InnoDB CHARSET = utf8;

# 建立信息表
CREATE TABLE message(
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` VARCHAR(50) COMMENT '标题',
  `title_img` VARCHAR(20) COMMENT '标题图路径',
  `date` varchar(20) COMMENT '发布日期',
  `content` TEXT COMMENT '富文本内容',
  `file` VARCHAR(100) COMMENT '附件路径',
  `is_push` tinyint COMMENT '是否推送',
  `menu_id` INT COMMENT '栏目id',
  `admin_id` INT COMMENT '发布人的姓名',
  PRIMARY KEY (id)
)ENGINE = InnoDB CHARSET = utf8;

# 添加外键约束
ALTER TABLE message ADD CONSTRAINT fk_admin_id FOREIGN KEY (admin_id) REFERENCES admin(id);
ALTER TABLE message ADD CONSTRAINT fk_column_id FOREIGN KEY (menu_id) REFERENCES menu(id);