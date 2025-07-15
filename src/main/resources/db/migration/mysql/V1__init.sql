CREATE TABLE `catsgotogedog`.`user` (
    `user_id` BIGINT NOT NULL AUTO_INCREMENT,
    `display_name` VARCHAR(50) NOT NULL,
    `email` VARCHAR(255) NULL,
    `login_provider` VARCHAR(50) NULL,
    `image_filename` VARCHAR(255) NULL,
    `image_url` VARCHAR(255) NULL,
    `created_at` DATETIME NULL DEFAULT NOW(),
    `updated_at` DATETIME NULL DEFAULT NOW(),
    `is_active` TINYINT NULL DEFAULT 1,
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
COMMENT = '유저 정보 테이블';

CREATE TABLE `catsgotogedog`.`refresh_token` (
     `id` BIGINT NOT NULL AUTO_INCREMENT,
     `user_id` BIGINT NOT NULL,
     `refresh_token` VARCHAR(511) NOT NULL,
     `expires_at` DATETIME NOT NULL,
     `revoked` TINYINT NULL DEFAULT 0,
     PRIMARY KEY (`id`),
     UNIQUE INDEX `refresh_token_UNIQUE` (`refresh_token` ASC) VISIBLE,
     UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
     CONSTRAINT `refresh_token_user_user_id_fk`
         FOREIGN KEY (`user_id`)
             REFERENCES `catsgotogedog`.`user` (`user_id`)
             ON DELETE CASCADE
             ON UPDATE NO ACTION)
COMMENT = '리프레쉬 토큰 테이블';
