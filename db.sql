CREATE TABLE `video_tencent_data` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `vid` varchar(11) CHARACTER SET utf8mb4 NOT NULL COMMENT '视频标识',
  `href` varchar(100) NOT NULL COMMENT '地址栏',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `episode` tinyint(5) NOT NULL COMMENT '剧集',
  `gmt_create` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

