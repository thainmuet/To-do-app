DROP DATABASE IF EXISTS to_do;

CREATE DATABASE IF NOT EXISTS to_do;

USE to_do;

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

CREATE TABLE IF NOT EXISTS frequency (
    frequency_id TINYINT(2) AUTO_INCREMENT NOT NULL,
    frequency CHAR(20),
    PRIMARY KEY (frequency_id)
);

INSERT INTO frequency(frequency)
    VALUES
        ('Daily'),
        ('Weekly'),
        ('Monthly');


CREATE TABLE IF NOT EXISTS task (
    task_id INT AUTO_INCREMENT NOT NULL,
    username CHAR(16) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    added_date DATE,
    frequency_id TINYINT(2) DEFAULT NULL,
    tag VARCHAR(255),
    completed TINYINT(1) DEFAULT FALSE,
    flag CHAR(50) DEFAULT 'None',
    PRIMARY KEY (task_id, username),
    FOREIGN KEY (username)
        REFERENCES user (username),
    FOREIGN KEY (frequency_id)
        REFERENCES frequency (frequency_id),
    FOREIGN KEY (flag) 
        REFERENCES importance (flag)
);

CREATE TABLE IF NOT EXISTS project (
    project_id INT AUTO_INCREMENT NOT NULL,
    username CHAR(16) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    added_date DATE,
    tag VARCHAR(255),
    completed TINYINT(1) DEFAULT FALSE,
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


-- Create new user
INSERT INTO user (username, password)
    VALUES 
        ('root', 'toor'),
        ('admin', 'admin');


-- Add new task
INSERT INTO task (username, title)
    VALUES 
        ('root', 'Finish DB project'),
        ('root', 'Complete Cloud computing course'),
        ('admin', 'Complete discrete math'),
        ('admin', 'Complete 2 hackerRank problems');


-- Get user tasks
SELECT * FROM task WHERE username = 'root';

SELECT * FROM task WHERE username = 'admin';