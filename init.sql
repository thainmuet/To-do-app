DROP DATABASE IF EXISTS to_do_app;

CREATE DATABASE IF NOT EXISTS to_do_app;

USE to_do_app;

CREATE TABLE IF NOT EXISTS user (
    username CHAR(16) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS importance (
    flag CHAR(50),
    grade TINYINT(2),
    PRIMARY KEY (flag)
);

INSERT INTO importance(flag, grade)
    VALUES
        ('None', 1 ),
        ('No worry', 2),
        ('Normal', 3),
        ('Importance', 4),
        ('Very importance', 5);        


CREATE TABLE IF NOT EXISTS task (
    task_id INT AUTO_INCREMENT NOT NULL,
    username CHAR NOT NULL,
    title VARCHAR(255) NOT NULL,
    decription TEXT,
    due_date DATE,
    added_date DATE,
    iteration BOOLEAN DEFAULT FALSE,
    tag VARCHAR(255),
    completed TINYINT(1) DEFAULT FALSE,
    flag CHAR(50),
    PRIMARY KEY (task_id, username),
    FOREIGN KEY (username)
        REFERENCES user (username),
    FOREIGN KEY (flag) 
        REFERENCES importance (flag)
);

CREATE TABLE IF NOT EXISTS project (
    project_id INT AUTO_INCREMENT NOT NULL,
    username CHAR NOT NULL,
    title VARCHAR(255) NOT NULL,
    decription TEXT,
    due_date DATE,
    added_date DATE,
    tag VARCHAR(255),
    completed TINYINT(1),
    flag CHAR(50),
    PRIMARY KEY (project_id, username),
    FOREIGN KEY (username)
        REFERENCES user (username),
    FOREIGN KEY (flag) 
        REFERENCES importance (flag)
);

-- Get the total task grade of an user
-- SELECT SUM(grade) FROM (
--     SELECT grade FROM importance
--         WHERE flag IN (SELECT flag FROM task
--             WHERE completed = TRUE)
-- );

INSERT INTO user (username, password)
    VALUES ('root', 'toor'); 