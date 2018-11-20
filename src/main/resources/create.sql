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

#建立会员表
CREATE TABLE member(
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account` varchar(20) COMMENT '账号',
  `password` VARCHAR(50) COMMENT '密码',
  `name` VARCHAR(10) COMMENT '姓名',
  `sex` varchar(5) COMMENT '性别',
  `birthday` VARCHAR(20) COMMENT '出生日期',
  `phone` VARCHAR(15) COMMENT '电话',
  `email` VARCHAR(50) COMMENT '邮箱',
  `address` VARCHAR(255) COMMENT '地址',
  PRIMARY KEY (id)
)ENGINE = InnoDB CHARSET = utf8;

# 添加外键约束
ALTER TABLE message ADD CONSTRAINT fk_admin_id FOREIGN KEY (admin_id) REFERENCES admin(id);
ALTER TABLE message ADD CONSTRAINT fk_column_id FOREIGN KEY (menu_id) REFERENCES menu(id);

# 添加基本数据
INSERT INTO `henghe`.`admin` (`id`, `name`, `password`) VALUES ('1', 'asd', 'asd');

INSERT INTO `henghe`.`menu` (`id`, `name`) VALUES ('1', '公司简介');
INSERT INTO `henghe`.`menu` (`id`, `name`) VALUES ('2', '新闻中心');
INSERT INTO `henghe`.`menu` (`id`, `name`) VALUES ('3', '比赛信息');
INSERT INTO `henghe`.`menu` (`id`, `name`) VALUES ('4', '服务项目');
INSERT INTO `henghe`.`menu` (`id`, `name`) VALUES ('5', '顾问团队');

INSERT INTO `henghe`.`message` (`id`, `title`, `title_img`, `date`, `content`, `file`, `is_push`, `menu_id`, `admin_id`) VALUES ('0', '公司简介', 'company.jpg', '2018-11-14', '<p>我们为专业赛事，世界品牌，体育组织，体育部门以及体育场馆提供专业的体育营销服务。我们通过体育营销咨询，整合赞助服务赛事管理运营、媒体综合服务和体育公关活动，帮助最大化的提升客户的影响力品牌价值、销售数量和赛事规模。</p>', NULL, '0', '1', '1');
