/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : msg_center

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 27/09/2022 17:06:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for channel_account
-- ----------------------------
DROP TABLE IF EXISTS `channel_account`;
CREATE TABLE `channel_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '账号名称',
  `send_channel` tinyint(4) NOT NULL DEFAULT 0 COMMENT '消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序 70.企业微信 80.钉钉机器人 90.钉钉工作通知 100.企业微信机器人 110.飞书机器人 110. 飞书应用消息 ',
  `account_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '账号配置',
  `created` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `updated` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除：0.不删除 1.删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_send_channel`(`send_channel`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '渠道账号信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '标题',
  `audit_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '当前消息审核状态： 10.待审核 20.审核成功 30.被拒绝',
  `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工单ID',
  `msg_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '当前消息状态：10.新建 20.停用 30.启用 40.等待发送 50.发送中 60.发送成功 70.发送失败',
  `cron_task_id` bigint(20) NULL DEFAULT NULL COMMENT '定时任务Id (xxl-job-admin返回)',
  `cron_crowd_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '定时发送人群的文件路径',
  `expect_push_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '期望发送时间：0:立即发送 定时任务以及周期任务:cron表达式',
  `id_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '消息的发送ID类型：10. userId 20.did 30.手机号 40.openId 50.email 60.企业微信userId',
  `send_channel` tinyint(4) NOT NULL DEFAULT 0 COMMENT '消息发送渠道：10.IM 20.Push 30.短信 40.Email 50.公众号 60.小程序 70.企业微信',
  `template_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '10.运营类 20.技术类接口调用',
  `msg_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '10.通知类消息 20.营销类消息 30.验证码类消息',
  `shield_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '10.夜间不屏蔽 20.夜间屏蔽 30.夜间屏蔽(次日早上9点发送)',
  `msg_content` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消息内容 占位符用{$var}表示',
  `send_account` tinyint(4) NOT NULL DEFAULT 0 COMMENT '发送账号 一个渠道下可存在多个账号',
  `creator` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `auditor` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核人',
  `team` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务方团队',
  `proposer` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '业务方',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除：0.不删除 1.删除',
  `created` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `updated` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_channel`(`send_channel`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息模板信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_template
-- ----------------------------
INSERT INTO `message_template` VALUES (2, '邮件信息', 20, '', 30, NULL, '', '', 50, 40, 20, 10, 0, '{\"content\":\"{$content}\",\"title\":\"{$title}\"}', 66, 'langao', 'langao', 'langao', 'langao', '兰皋', 0, 1646274195, 1646274195);
INSERT INTO `message_template` VALUES (3, '验证码通知', 20, '', 30, NULL, '', '', 30, 30, 20, 10, 0, '{\"content\":\"{$content}\",\"url\":\"{$url}\"}', 66, 'langao', 'langao', 'langao', 'langao', '兰皋', 0, 1646275213, 1646275213);
INSERT INTO `message_template` VALUES (5, '定时任务', 20, '', 20, 2, 'e:/upload/2022-07-06/test.csv', '* * * * * ? *', 50, 40, 10, 10, 0, '{\"content\":\"{$content}\",\"title\":\"{$title}\"}', 66, 'imwj', 'imwj', 'imwj', 'imwj', '测试', 0, 1653559314, 1661915639);
INSERT INTO `message_template` VALUES (6, '公众号模板消息', 20, '', 30, NULL, '', '', 40, 50, 20, 10, 0, '{\"content\":\"{$content}\",\"url\":\"{$url}\"}', 66, 'imwj', 'imwj', 'imwj', 'imwj', '公众号', 0, 1657856424, 0);
INSERT INTO `message_template` VALUES (7, '企业微信', 20, '', 30, NULL, '', '', 60, 70, 20, 10, 30, '{\"content\":\"{$content}\",\"sendType\":\"{$sendType}\",\"mediaId\":\"{$mediaId}\"}', 66, 'imwj', 'imwj', 'imwj', 'imwj', '企业微信', 0, 1658816856, 0);
INSERT INTO `message_template` VALUES (8, '微信小程序', 20, '', 30, NULL, '', '', 40, 60, 20, 10, 30, '{\"content\":\"{$content}\",\"pagel\":\"{$page}\"}', 66, 'imwj', 'imwj', 'imwj', 'imwj', '微信小程序', 0, 1658816856, 0);
INSERT INTO `message_template` VALUES (9, 'app个推', 20, '', 30, NULL, '', '', 80, 20, 20, 10, 30, '{\"content\":\"{$content}\",\"title\":\"{$title}\",\"url\":\"{$url}\"}', 66, 'imwj', 'imwj', 'imwj', 'imwj', '微信小程序', 0, 1658816856, 0);

-- ----------------------------
-- Table structure for sms_record
-- ----------------------------
DROP TABLE IF EXISTS `sms_record`;
CREATE TABLE `sms_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message_template_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '消息模板ID',
  `phone` bigint(20) NOT NULL DEFAULT 0 COMMENT '手机号',
  `supplier_id` tinyint(4) NOT NULL DEFAULT 0 COMMENT '发送短信渠道商的ID',
  `supplier_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '发送短信渠道商的名称',
  `msg_content` varchar(600) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '短信发送的内容',
  `series_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '下发批次的ID',
  `charging_num` tinyint(4) NOT NULL DEFAULT 0 COMMENT '计费条数',
  `report_content` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '回执内容',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '短信状态： 10.发送 20.成功 30.失败',
  `send_date` int(11) NOT NULL DEFAULT 0 COMMENT '发送日期：20211112',
  `created` int(11) NOT NULL DEFAULT 0 COMMENT '创建时间',
  `updated` int(11) NOT NULL DEFAULT 0 COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_send_date`(`send_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '短信记录信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_record
-- ----------------------------
INSERT INTO `sms_record` VALUES (1, 1, 15200985202, 10, '腾讯云', '', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220401, 1648794127, 1648794127);
INSERT INTO `sms_record` VALUES (2, 1, 15200985202, 10, '腾讯云', '', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220401, 1648794520, 1648794520);
INSERT INTO `sms_record` VALUES (3, 1, 15200985202, 10, '腾讯云', '', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220507, 1651889308, 1651889308);
INSERT INTO `sms_record` VALUES (4, 1, 15200985202, 10, '腾讯云', '6666', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220511, 1652248910, 1652248910);
INSERT INTO `sms_record` VALUES (5, 1, 15200985202, 10, '腾讯云', '666677777', '', 0, 'InvalidParameterValue.TemplateParameterFormatError', 10, 20220511, 1652248995, 1652248995);
INSERT INTO `sms_record` VALUES (6, 3, 15200985202, 10, '腾讯云', '666677777', '', 0, 'InvalidParameterValue.TemplateParameterFormatError', 10, 20220719, 1658210625, 1658210625);
INSERT INTO `sms_record` VALUES (7, 3, 15200985202, 10, '腾讯云', '666677777 http://blog.imwj.club/?track_code_bid=2000000320220726', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220726, 1658817403, 1658817403);
INSERT INTO `sms_record` VALUES (8, 3, 15200985202, 10, '腾讯云', '666677777 http://blog.imwj.club/?track_code_bid=2000000320220726', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220726, 1658817504, 1658817504);
INSERT INTO `sms_record` VALUES (9, 3, 15200985202, 10, '腾讯云', '666677777 http://blog.imwj.club/?track_code_bid=2000000320220726', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220726, 1658817515, 1658817515);
INSERT INTO `sms_record` VALUES (10, 3, 15200985202, 10, '腾讯云', '666677777', '', 0, 'InvalidParameterValue.TemplateParameterFormatError', 10, 20220726, 1658817553, 1658817553);
INSERT INTO `sms_record` VALUES (11, 3, 15200985202, 10, '腾讯云', '666677777', '', 0, 'InvalidParameterValue.TemplateParameterFormatError', 10, 20220830, 1661850823, 1661850823);
INSERT INTO `sms_record` VALUES (12, 3, 15200985202, 10, '腾讯云', '666677777 123?track_code_bid=2000000320220830', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220830, 1661850861, 1661850861);
INSERT INTO `sms_record` VALUES (13, 3, 15200985202, 10, '腾讯云', '123456 123?track_code_bid=2000000320220830', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220830, 1661850924, 1661850924);
INSERT INTO `sms_record` VALUES (14, 3, 15200985202, 10, '腾讯云', '123 123?track_code_bid=2000000320220830', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220830, 1661850951, 1661850951);
INSERT INTO `sms_record` VALUES (15, 3, 15200985202, 10, '腾讯云', '123 1?track_code_bid=2000000320220830', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220830, 1661851088, 1661851088);
INSERT INTO `sms_record` VALUES (16, 3, 15200985202, 10, '腾讯云', '1', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220830, 1661851143, 1661851143);
INSERT INTO `sms_record` VALUES (17, 3, 15200985202, 10, '腾讯云', '1 1?track_code_bid=2000000320220830', '', 0, 'InvalidParameterValue.TemplateParameterLengthLimit', 10, 20220830, 1661851187, 1661851187);
INSERT INTO `sms_record` VALUES (18, 3, 15200985202, 10, '腾讯云', '1', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220830, 1661851202, 1661851202);
INSERT INTO `sms_record` VALUES (19, 3, 15200985202, 10, '腾讯云', '1', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220901, 1662001459, 1662001459);
INSERT INTO `sms_record` VALUES (20, 3, 15200985202, 10, '腾讯云', '1', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220901, 1662010783, 1662010783);
INSERT INTO `sms_record` VALUES (21, 3, 15200985202, 10, '腾讯云', '1', '', 0, 'LimitExceeded.PhoneNumberThirtySecondLimit', 10, 20220901, 1662010793, 1662010793);
INSERT INTO `sms_record` VALUES (22, 3, 15200985202, 10, '腾讯云', '1', '', 0, 'FailedOperation.InsufficientBalanceInSmsPackage', 10, 20220901, 1662010801, 1662010801);
INSERT INTO `sms_record` VALUES (23, 3, 15200985202, 10, '腾讯云', '1', '', 0, 'LimitExceeded.PhoneNumberThirtySecondLimit', 10, 20220901, 1662010804, 1662010804);

SET FOREIGN_KEY_CHECKS = 1;
