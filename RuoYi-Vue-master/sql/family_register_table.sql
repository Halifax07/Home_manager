-- 创建家庭注册表
-- 用于存储用户注册信息，等待家庭管理员审批

DROP TABLE IF EXISTS `family_register`;
CREATE TABLE `family_register` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '注册ID',
  `account` varchar(50) NOT NULL COMMENT '注册账号',
  `password_hash` varchar(255) NOT NULL COMMENT '密码哈希',
  `name` varchar(50) NOT NULL COMMENT '真实姓名',
  `gender` char(1) DEFAULT NULL COMMENT '性别（男/女）',
  `kinship` varchar(20) DEFAULT NULL COMMENT '家庭关系（如：父亲/母亲/子女等）',
  `occupation` varchar(50) DEFAULT NULL COMMENT '职业',
  `user_type` varchar(20) NOT NULL COMMENT '用户类型（admin-家庭管理员/member-普通成员）',
  `family_name` varchar(100) DEFAULT NULL COMMENT '申请加入的家庭名称',
  `status` char(1) DEFAULT '0' COMMENT '审批状态（0-待审批 1-已通过 2-已拒绝）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `approve_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approve_by` varchar(50) DEFAULT NULL COMMENT '审批人',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='家庭注册申请表';

-- 插入测试数据
INSERT INTO `family_register` (`account`, `password_hash`, `name`, `gender`, `kinship`, `occupation`, `user_type`, `family_name`, `status`) VALUES
('test_admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '张三', '男', '父亲', '工程师', 'admin', '张家', '0'),
('test_member', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '李四', '女', '母亲', '教师', 'member', '张家', '0');
