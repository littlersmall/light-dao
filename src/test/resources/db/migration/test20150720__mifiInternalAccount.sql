CREATE TABLE `idempotent` (
  `instruction_id` BIGINT  NOT NULL
  COMMENT '流水号',
  `service_name` TINYINT NOT NULL
  COMMENT '模块名',
  `account_kind`   TINYINT NOT NULL
  COMMENT '1实体 2结算 3实体离线 4结算离线',
  `create_time`    BIGINT  NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`instruction_id`, `service_name`, `account_kind`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `customer_info` (
  `account_id`           BIGINT  NOT NULL
  COMMENT '客户账务ID',
  `mifi_id`        BIGINT NOT NULL
  COMMENT 'mifiId',
  `product_id`    BIGINT NOT NULL
  COMMENT '产品id',
  `organization_id` BIGINT NOT NULL
  COMMENT '机构id',
  `kind`                 TINYINT NOT NULL
  COMMENT '账户类别',
  `balance`              BIGINT  NOT NULL
  COMMENT '账户余额',
  `usable_balance`       BIGINT  NOT NULL
  COMMENT '可用余额',
  `frozen_balance`       BIGINT  NOT NULL
  COMMENT '冻结金额',
  `principal`            BIGINT  NOT NULL
  COMMENT '本金',
  `interest`             BIGINT  NOT NULL
  COMMENT '利息',
  `accumulated_interest` BIGINT  NOT NULL
  COMMENT '累积利息和',
  `interest_start`       BIGINT  NOT NULL
  COMMENT '起息日',
  `property`             TINYINT NOT NULL
  COMMENT '账户性质(1资产 2负债)',
  `account_time`         BIGINT  NOT NULL
  COMMENT '会计时间',
  `create_time`          BIGINT  NOT NULL
  COMMENT '创建时间',
  `status`               TINYINT NOT NULL
  COMMENT '账户状态(1正常 2暂禁)',
  PRIMARY KEY (`account_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `customer_statistics` (
  `account_id`  BIGINT NOT NULL
  COMMENT '账户ID',
  `date`        BIGINT NOT NULL
  COMMENT '日期',
  `balance`     BIGINT NOT NULL
  COMMENT '本日余额',
  `create_time` BIGINT NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`account_id`, `date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `customer_instruction` (
  `id`                    BIGINT  NOT NULL AUTO_INCREMENT
  COMMENT '自增id',
  `instruction_id`        BIGINT  NOT NULL
  COMMENT '流水号',
  `account_id`            BIGINT  NOT NULL
  COMMENT '账户id(主体)',
  `b_account_id`          BIGINT  NOT NULL
  COMMENT '对手方accountId',
  `account_time`          BIGINT  NOT NULL
  COMMENT '交易时间',
  `trade_code`            BIGINT NOT NULL
  COMMENT '交易代码',
  `type`       INT NOT NULL
  COMMENT '业务类型',
  `direction`             TINYINT NOT NULL
  COMMENT '交易方向(1资金流出 借方 2资金流入 贷方)',
  `amount`                BIGINT  NOT NULL
  COMMENT '交易金额',
  `property`              TINYINT NOT NULL
  COMMENT '账户性质',
  `notice_summary`        VARCHAR(8192)
  COMMENT '通知摘要',
  `b_service_time`        BIGINT  NOT NULL
  COMMENT '对手方交易时间',
  `b_instruction_id`      BIGINT  NOT NULL
  COMMENT '对手方流水号',
  `trade_type`            TINYINT NOT NULL
  COMMENT '交易类型(1无 2冲账 3补账)',
  `strike_flag`           TINYINT NOT NULL
  COMMENT '本次交易是否被冲账(1N 2R[被冲] 3Y[冲])',
  `strike_account_time`   BIGINT  NOT NULL
  COMMENT '冲(被冲)账会计时间',
  `strike_instruction_id` BIGINT  NOT NULL
  COMMENT '冲(被冲)账务流水号',
  `status`                TINYINT NOT NULL
  COMMENT '1正常 2作废(当日冲账，状态为作废)',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `organ_info` (
  `organization_id`         BIGINT  NOT NULL
  COMMENT '商户ID',
  `category_id`             BIGINT  NOT NULL
  COMMENT '产品ID',
  `balance`                BIGINT  NOT NULL
  COMMENT '账户余额',
  `usable_balance`         BIGINT  NOT NULL
  COMMENT '可用余额',
  `frozen_balance`          BIGINT  NOT NULL
  COMMENT '冻结余额',
  `accumulated_in_amount`  BIGINT  NOT NULL
  COMMENT '累积入账金额',
  `accumulated_in_times`   BIGINT  NOT NULL
  COMMENT '累积入账次数',
  `accumulated_out_amount` BIGINT  NOT NULL
  COMMENT '累积出账金额',
  `accumulated_out_times`  BIGINT  NOT NULL
  COMMENT '累积出账次数',
  `account_time`           BIGINT  NOT NULL
  COMMENT '最后交易时间',
  `create_time`            BIGINT  NOT NULL
  COMMENT '创建时间',
  `status`                 TINYINT NOT NULL
  COMMENT '账户状态(1正常 2暂禁)',
  PRIMARY KEY (`organization_id`, `category_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `organ_instruction` (
  `instruction_id`        BIGINT  NOT NULL
  COMMENT '流水号',
  `organization_id`         BIGINT  NOT NULL
  COMMENT '商户ID',
  `product_id`             BIGINT  NOT NULL
  COMMENT '产品ID',
  `amount`                BIGINT  NOT NULL
  COMMENT '交易金额',
  `direction`                TINYINT  NOT NULL
  COMMENT '1 out 2 in',
  `account_time`           BIGINT  NOT NULL
  COMMENT '最后交易时间',
  `status`                 TINYINT NOT NULL
  COMMENT '账户状态(1正常 2暂禁)',
  PRIMARY KEY (`instruction_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `organ_statistics` (
  `organization_id` BIGINT NOT NULL
  COMMENT '商户ID',
  `product_id`     BIGINT NOT NULL
  COMMENT '产品ID',
  `date`           BIGINT NOT NULL
  COMMENT '日期',
  `balance`        BIGINT NOT NULL
  COMMENT '本日余额',
  `in_amount`      BIGINT NOT NULL
  COMMENT '本日入账金额',
  `in_times`       BIGINT NOT NULL
  COMMENT '本日入账次数',
  `out_amount`     BIGINT NOT NULL
  COMMENT '本日出账金额',
  `out_times`      BIGINT NOT NULL
  COMMENT '本日出账次数',
  `create_time`    BIGINT NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`organization_id`, `product_id`, `date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internal_info` (
  `account_id`        BIGINT  NOT NULL
  COMMENT '账务ID',
  `subject`           BIGINT  NOT NULL
  COMMENT '科目',
  `sub_title`         BIGINT  NOT NULL
  COMMENT '子目',
  `specific_item`     BIGINT  NOT NULL
  COMMENT '细目',
  `serial_number`     BIGINT  NOT NULL
  COMMENT '顺序号',
  `balance`           BIGINT  DEFAULT(0)
  COMMENT '账户金额',
  `usable_balance`    BIGINT DEFAULT(0)
  COMMENT '可用余额',
  `account_time`      BIGINT DEFAULT(0)
  COMMENT '最后交易时间',
  `create_time`       BIGINT  NOT NULL
  COMMENT '创建时间',
  `property`          TINYINT NOT NULL
  COMMENT '账户性质(1资产 2负债)',
  `direction`         TINYINT NOT NULL
  COMMENT '发生方向(1借方 2贷方 3借贷双方)',
  `balance_direction` TINYINT NOT NULL
  COMMENT '发生方向(1借方 2贷方 3借贷双方)',
  `status`            TINYINT DEFAULT(0)
  COMMENT '账户状态(1正常 2暂禁)',
  PRIMARY KEY (`account_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internal_instruction` (
  `id`                    BIGINT  NOT NULL AUTO_INCREMENT
  COMMENT '自增id',
  `instruction_id`        BIGINT  NOT NULL
  COMMENT '流水号',
  `account_id`            BIGINT  NOT NULL
  COMMENT '账户id',
  `b_account_id`          BIGINT  NOT NULL
  COMMENT '对手方accountId',
  `account_time`          BIGINT  NOT NULL
  COMMENT '交易时间',
  `trade_code`            BIGINT NOT NULL
  COMMENT '交易代码',
  `type`       INT NOT NULL
  COMMENT '业务类型',
  `direction`             TINYINT NOT NULL
  COMMENT '交易方向(1资金流出 借方 2资金流入 贷方)',
  `amount`                BIGINT  NOT NULL
  COMMENT '交易金额',
  `property`              TINYINT NOT NULL
  COMMENT '账户性质',
  `notice_summary`        VARCHAR(8192)
  COMMENT '通知摘要',
  `b_service_time`        BIGINT  NOT NULL
  COMMENT '对手方交易时间',
  `b_instruction_id`      BIGINT  NOT NULL
  COMMENT '对手方流水号',
  `trade_type`            TINYINT NOT NULL
  COMMENT '交易类型(1无 2冲账 3补账)',
  `strike_flag`           TINYINT NOT NULL
  COMMENT '本次交易是否被冲账(1N 2R[被冲] 3Y[冲])',
  `strike_account_time`   BIGINT  NOT NULL
  COMMENT '冲(被冲)账会计时间',
  `strike_instruction_id` BIGINT  NOT NULL
  COMMENT '冲(被冲)账务流水号',
  `status`                TINYINT NOT NULL
  COMMENT '1正常 2作废(当日冲账，状态为作废)',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internal_statistics` (
  `account_id`    BIGINT NOT NULL
  COMMENT '账务ID',
  `date`          BIGINT NOT NULL
  COMMENT '顺序号',
  `balance`       BIGINT NOT NULL
  COMMENT '本日金额',
  `create_time`   BIGINT NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`account_id`, `date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internal_rule` (
  `product_id`    BIGINT NOT NULL
  COMMENT '产品Id',
  `trade_code`          BIGINT NOT NULL
  COMMENT '交易码',
  `type`       INT NOT NULL
  COMMENT '业务类型',
  `serial_number` TINYINT NOT NULL
  COMMENT '序号',
  `create_time`     BIGINT NOT NULL
  COMMENT '创建时间',
  `account_id_d` BIGINT NOT NULL
  COMMENT '借方账户',
  `account_id_c` BIGINT NOT NULL
  COMMENT '贷方账户',
  PRIMARY KEY (`product_id`, `trade_code`, `type`, `serial_number`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internal_rule_v2` (
  `category_id`    BIGINT NOT NULL
  COMMENT '产品类别Id',
  `trade_code`          BIGINT NOT NULL
  COMMENT '交易码',
  `type`       INT NOT NULL
  COMMENT '业务类型',
  `serial_number` TINYINT NOT NULL
  COMMENT '序号',
  `create_time`     BIGINT NOT NULL
  COMMENT '创建时间',
  `account_id_d` BIGINT NOT NULL
  COMMENT '借方账户',
  `account_id_c` BIGINT NOT NULL
  COMMENT '贷方账户',
  PRIMARY KEY (`category_id`, `trade_code`, `type`, `serial_number`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `internal_rule_v3` (
  `category_id`    BIGINT NOT NULL
  COMMENT '产品类别Id',
  `trade_code`          BIGINT NOT NULL
  COMMENT '交易码',
  `type`       INT NOT NULL
  COMMENT '业务类型',
  `serial_number` TINYINT NOT NULL
  COMMENT '序号',
  `create_time`     BIGINT NOT NULL
  COMMENT '创建时间',
  `account_id_d` BIGINT NOT NULL
  COMMENT '借方账户',
  `account_id_c` BIGINT NOT NULL
  COMMENT '贷方账户',
  `amount_sign` TINYINT NOT NULL
  COMMENT '金额符号(1正 2负)',

  PRIMARY KEY (`category_id`, `trade_code`, `type`, `serial_number`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE INDEX `account_id_index` ON customer_instruction (`account_id`);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11111, -1, -1, -1, -1, 20150827, 1, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11112, -1, -1, -1, -1, 20150827, 1, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11113, -1, -1, -1, -1, 20150827, 1, 3, 3);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (22221, -1, -1, -1, -1, 20150827, 4, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (22222, -1, -1, -1, -1, 20150827, 4, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (22223, -1, -1, -1, -1, 20150827, 4, 3, 3);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (33331, -1, -1, -1, -1, 20150827, 4, 2, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (33332, -1, -1, -1, -1, 20150827, 4, 2, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (33333, -1, -1, -1, -1, 20150827, 4, 2, 3);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (4444, -1, -1, -1, -1, 20150827, 3, 3, 3);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (3, 6, 1, 1, 11111, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 3, 1, -1, 11112, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 3, 1, -1, 11112, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 3, 1, -1, 11112, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 7, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 7, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 7, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 7, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 8, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 8, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 8, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 8, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 9, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 9, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 9, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 9, 1, 11113, -1, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 13, 1, 11113, 4444, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 13, 1, 11113, 4444, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 13, 1, 11113, 4444, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 13, 1, 11113, 4444, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 4, 1, -1, 22221, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 5, 1, -1, 22222, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 6, 1, -1, 22223, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 4, 1, -1, 22221, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 5, 1, -1, 22222, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 6, 1, -1, 22223, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 4, 1, -1, 22221, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 5, 1, -1, 22222, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 6, 1, -1, 22223, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 10, 1, 22221, 33331, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 11, 1, 22222, 33332, 1, 20150827);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 12, 1, 22223, 33333, 1, 20150827);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11121, -1, -1, -1, -1, 20151123, 1, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11122, -1, -1, -1, -1, 20151123, 1, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11123, -1, -1, -1, -1, 20151123, 1, 3, 3);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (22231, -1, -1, -1, -1, 20151123, 4, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (22232, -1, -1, -1, -1, 20151123, 4, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (22233, -1, -1, -1, -1, 20151123, 4, 3, 3);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (33341, -1, -1, -1, -1, 20151123, 4, 2, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (33342, -1, -1, -1, -1, 20151123, 4, 2, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (33343, -1, -1, -1, -1, 20151123, 4, 2, 3);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (4445, -1, -1, -1, -1, 20151123, 3, 3, 3);

CREATE TABLE `cut_day_idempotent` (
  `account_id` BIGINT  NOT NULL
  COMMENT '账户id',
  `cut_day_date` BIGINT NOT NULL
  COMMENT '日切日期',
  `copy_type`   TINYINT NOT NULL
  COMMENT '1 old to new 2 new to old',
  `create_time`    BIGINT  NOT NULL
  COMMENT '创建时间',
  PRIMARY KEY (`account_id`, `cut_day_date`, `copy_type`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `cut_day_customer_info` (
  `account_id`           BIGINT  NOT NULL
  COMMENT '客户账务ID',
  `cut_day_date`         BIGINT NOT NULL
  COMMENT '日切日期',
  `mifi_id`        BIGINT NOT NULL
  COMMENT 'mifiId',
  `product_id`    BIGINT NOT NULL
  COMMENT '产品id',
  `organization_id` BIGINT NOT NULL
  COMMENT '机构id',
  `kind`                 TINYINT NOT NULL
  COMMENT '账户类别',
  `balance`              BIGINT  NOT NULL
  COMMENT '账户余额',
  `usable_balance`       BIGINT  NOT NULL
  COMMENT '可用余额',
  `frozen_balance`       BIGINT  NOT NULL
  COMMENT '冻结金额',
  `principal`            BIGINT  NOT NULL
  COMMENT '本金',
  `interest`             BIGINT  NOT NULL
  COMMENT '利息',
  `accumulated_interest` BIGINT  NOT NULL
  COMMENT '累积利息和',
  `interest_start`       BIGINT  NOT NULL
  COMMENT '起息日',
  `property`             TINYINT NOT NULL
  COMMENT '账户性质(1资产 2负债)',
  `account_time`         BIGINT  NOT NULL
  COMMENT '会计时间',
  `create_time`          BIGINT  NOT NULL
  COMMENT '创建时间',
  `status`               TINYINT NOT NULL
  COMMENT '账户状态(1正常 2暂禁)',
  PRIMARY KEY (`account_id`, `cut_day_date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `cut_day_internal_info` (
  `account_id`        BIGINT  NOT NULL
  COMMENT '账务ID',
  `cut_day_date`      BIGINT NOT NULL
  COMMENT '日切日期',
  `subject`           BIGINT  NOT NULL
  COMMENT '科目',
  `sub_title`         BIGINT  NOT NULL
  COMMENT '子目',
  `specific_item`     BIGINT  NOT NULL
  COMMENT '细目',
  `serial_number`     BIGINT  NOT NULL
  COMMENT '顺序号',
  `balance`           BIGINT  NOT NULL
  COMMENT '账户金额',
  `usable_balance`    BIGINT  NOT NULL
  COMMENT '可用余额',
  `account_time`      BIGINT  NOT NULL
  COMMENT '最后交易时间',
  `create_time`       BIGINT  NOT NULL
  COMMENT '创建时间',
  `property`          TINYINT NOT NULL
  COMMENT '账户性质(1资产 2负债)',
  `direction`         TINYINT NOT NULL
  COMMENT '发生方向(1借方 2贷方 3借贷双方)',
  `balance_direction` TINYINT NOT NULL
  COMMENT '发生方向(1借方 2贷方 3借贷双方)',
  `status`            TINYINT NOT NULL
  COMMENT '账户状态(1正常 2暂禁)',
  PRIMARY KEY (`account_id`, `cut_day_date`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `refund` (
  `id` BIGINT  NOT NULL  AUTO_INCREMENT
  COMMENT '自增id',
  `instruction_id` BIGINT NOT NULL
  COMMENT '流水号',
  `b_instruction_id` BIGINT NOT NULL
  COMMENT '关联流水号',
  `amount`   BIGINT NOT NULL
  COMMENT '金额',
  `reason`   VARCHAR(8192)
  COMMENT '退款原因',
  `operator` VARCHAR(8192)
  COMMENT '操作人员',
  `operate_time` BIGINT NOT NULL
  COMMENT '操作时间',
  `date` BIGINT NOT NULL
  COMMENT '日期',
  `status` TINYINT NOT NULL
  COMMENT '状态(1正常 2暂禁)',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `unbalance_instruction` (
  `id` BIGINT  NOT NULL  AUTO_INCREMENT
  COMMENT '自增id',
  `instruction_id` BIGINT NOT NULL
  COMMENT '流水号',
  `service_name` TINYINT NOT NULL
  COMMENT '模块名',
  `amount`   BIGINT NOT NULL
  COMMENT '金额',
  `unbalance_type`   TINYINT NOT NULL
  COMMENT '失败原因',
  `account_time` BIGINT NOT NULL
  COMMENT '会计时间',
  `create_time` BIGINT NOT NULL
  COMMENT '创建时间',
  `date` BIGINT NOT NULL
  COMMENT '日期',
  `status` TINYINT NOT NULL
  COMMENT '状态(1正常 2已处理)',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `strike_account` (
  `id` BIGINT  NOT NULL  AUTO_INCREMENT
  COMMENT '自增id',
  `instruction_id` BIGINT NOT NULL
  COMMENT '流水号',
  `b_instruction_id` BIGINT NOT NULL
  COMMENT '关联流水号',
  `reason`   VARCHAR(8192)
  COMMENT '冲账原因',
  `date` BIGINT NOT NULL
  COMMENT '日期',
  `create_time` BIGINT NOT NULL
  COMMENT '创建时间',
  `status` TINYINT NOT NULL
  COMMENT '状态(1正常 2暂禁)',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `special_account` (
  `id` BIGINT  NOT NULL  AUTO_INCREMENT
  COMMENT '自增id',
  `instruction_id` BIGINT NOT NULL
  COMMENT '流水号',
  `type` TINYINT NOT NULL
  COMMENT '特殊账务类型',
  `type_describe`   VARCHAR(8192)
  COMMENT '类型描述',
  `operator`   VARCHAR(8192)
  COMMENT '操作人',
  `date` BIGINT NOT NULL
  COMMENT '日期',
  `create_time` BIGINT NOT NULL
  COMMENT '创建时间',
  `status` TINYINT NOT NULL
  COMMENT '状态(1正常 2暂禁)',
  `amount` BIGINT NOT NULL
  COMMENT '金额',
  `mifi_id` BIGINT NOT NULL
  COMMENT 'mifiId',
  `mi_id` BIGINT NOT NULL
  COMMENT 'miId',
  `b_instruction_id` BIGINT
  COMMENT '关联流水号',
  `reason`   VARCHAR(8192)
  COMMENT '原因',
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

ALTER TABLE `customer_instruction` add `mifi_id` BIGINT DEFAULT 0;
ALTER TABLE `customer_instruction` add `product_id` BIGINT DEFAULT 0;
ALTER TABLE `customer_instruction` add `organization_id` BIGINT DEFAULT 0;
ALTER TABLE `customer_instruction` add `create_time` BIGINT DEFAULT 0;

ALTER TABLE `customer_statistics` add `mifi_id` BIGINT DEFAULT 0;
ALTER TABLE `customer_statistics` add `product_id` BIGINT DEFAULT 0;
ALTER TABLE `customer_statistics` add `organization_id` BIGINT DEFAULT 0;
ALTER TABLE `customer_statistics` add `principal` BIGINT DEFAULT 0;
ALTER TABLE `customer_statistics` add `interest` BIGINT DEFAULT 0;

ALTER TABLE `internal_instruction` add `mifi_id` BIGINT DEFAULT 0;
ALTER TABLE `internal_instruction` add `product_id` BIGINT DEFAULT 0;
ALTER TABLE `internal_instruction` add `organization_id` BIGINT DEFAULT 0;
ALTER TABLE `internal_instruction` add `create_time` BIGINT DEFAULT 0;

ALTER TABLE `customer_statistics` add `expense` BIGINT DEFAULT 0;
ALTER TABLE `customer_info` add `expense` BIGINT DEFAULT 0;
ALTER TABLE `cut_day_customer_info` add `expense` BIGINT DEFAULT 0;

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11114, -1, -1, -1, -1, 20160121, 1, 3, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (11115, -1, -1, -1, -1, 20160121, 1, 3, 3);

insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (55551, -1, -1, -1, -1, 20160121, 4, 2, 3);
insert into internal_info(account_id, subject, sub_title, specific_item, serial_number, create_time, property, direction, balance_direction) VALUES (55552, -1, -1, -1, -1, 20160121, 4, 2, 3);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 15, 1, 11114, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 15, 1, 11114, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 15, 1, 11114, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 15, 1, 11114, -1, 1, 20160121);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 16, 1, 11115, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 16, 1, 11115, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 6, 16, 1, 11115, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 16, 1, 11115, -1, 1, 20160121);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 8, 15, 1, 11114, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 8, 15, 1, 11114, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 8, 15, 1, 11114, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 8, 15, 1, 11114, -1, 1, 20160121);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 8, 16, 1, 11115, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 8, 16, 1, 11115, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (4, 8, 16, 1, 11115, -1, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 8, 16, 1, 11115, -1, 1, 20160121);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 17, 1, -1, 55551, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 17, 1, -1, 55551, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 17, 1, -1, 55551, 1, 20160121);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 6, 18, 1, -1, 55552, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 6, 18, 1, -1, 55552, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 6, 18, 1, -1, 55552, 1, 20160121);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 8, 17, 1, -1, 55551, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 8, 17, 1, -1, 55551, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 8, 17, 1, -1, 55551, 1, 20160121);

insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (1, 8, 18, 1, -1, 55552, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (2, 8, 18, 1, -1, 55552, 1, 20160121);
insert into internal_rule_v3(trade_code, category_id, type, serial_number, account_id_d, account_id_c, amount_sign, create_time) VALUES (5, 8, 18, 1, -1, 55552, 1, 20160121);
