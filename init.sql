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

CREATE TABLE IF NOT EXISTS frequency (
    frequency CHAR(20) NOT NULL,
    PRIMARY KEY (frequency)
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


CREATE TABLE IF NOT EXISTS task (
    task_id INT AUTO_INCREMENT NOT NULL,
    project_id INT,
    username CHAR(16) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    added_date DATE,
    frequency CHAR(20) DEFAULT 'None',
    tag VARCHAR(255),
    completed TINYINT(1) DEFAULT FALSE,
    flag CHAR(50) DEFAULT 'None',
    PRIMARY KEY (task_id, username),
    FOREIGN KEY (project_id)
        REFERENCES project (project_id),
    FOREIGN KEY (username)
        REFERENCES user (username),
    FOREIGN KEY (frequency)
        REFERENCES frequency (frequency),
    FOREIGN KEY (flag) 
        REFERENCES importance (flag)
);


INSERT INTO frequency(frequency)
    VALUES
        ('None'),
        ('Daily'),
        ('Weekly'),
        ('Monthly');

INSERT INTO importance(flag, grade)
    VALUES
        ('None', 1 ),
        ('No worry', 2),
        ('Normal', 3),
        ('Importance', 4),
        ('Very importance', 5);


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