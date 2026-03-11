package com.heritage.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class P8SchemaInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public P8SchemaInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        createSensitiveWordTable();
        createOrderTables();
        createAccessLogTable();
    }

    private void createSensitiveWordTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS sensitive_word (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "word VARCHAR(100) NOT NULL," +
                "status TINYINT NOT NULL DEFAULT 1," +
                "remark VARCHAR(255) NULL," +
                "create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "deleted TINYINT NOT NULL DEFAULT 0," +
                "UNIQUE KEY uk_sensitive_word_word (word)," +
                "KEY idx_sensitive_word_status (status)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
    }

    private void createOrderTables() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `order` (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "user_id BIGINT NOT NULL," +
                "status TINYINT NOT NULL DEFAULT 0," +
                "pay_amount DECIMAL(10, 2) NOT NULL DEFAULT 0.00," +
                "create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "pay_time DATETIME NULL," +
                "deleted TINYINT NOT NULL DEFAULT 0," +
                "KEY idx_order_user_id (user_id)," +
                "KEY idx_order_status (status)," +
                "KEY idx_order_create_time (create_time)," +
                "KEY idx_order_pay_time (pay_time)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS order_item (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "order_id BIGINT NOT NULL," +
                "product_id BIGINT NOT NULL," +
                "product_name VARCHAR(255) NOT NULL," +
                "quantity INT NOT NULL DEFAULT 1," +
                "price DECIMAL(10, 2) NOT NULL DEFAULT 0.00," +
                "KEY idx_order_item_order_id (order_id)," +
                "KEY idx_order_item_product_id (product_id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
    }

    private void createAccessLogTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS access_log (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "user_id BIGINT NULL," +
                "target_type VARCHAR(50) NOT NULL," +
                "target_id BIGINT NULL," +
                "path VARCHAR(255) NOT NULL," +
                "ip VARCHAR(64) NULL," +
                "user_agent VARCHAR(500) NULL," +
                "create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "KEY idx_access_log_target (target_type, target_id)," +
                "KEY idx_access_log_create_time (create_time)," +
                "KEY idx_access_log_user_id (user_id)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4");
    }
}
