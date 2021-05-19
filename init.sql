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
    frequency_id INT AUTO_INCREMENT NOT NULL,
    frequency CHAR(20) NOT NULL,
    PRIMARY KEY (frequency_id)
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
    flag CHAR(50) DEFAULT 'None',
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
    frequency_id INT,
    tag VARCHAR(255),
    completed TINYINT(1) DEFAULT FALSE,
    flag CHAR(50) DEFAULT 'None',
    PRIMARY KEY (task_id, username),
    FOREIGN KEY (project_id)
        REFERENCES project (project_id),
    FOREIGN KEY (username)
        REFERENCES user (username),
    FOREIGN KEY (frequency_id)
        REFERENCES frequency (frequency_id),
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

INSERT INTO user(username, password)
    VALUES
        ('root', "root"),
        ('admin', "admin");

INSERT INTO project(title, username)
    VALUES
        ("DB project", "root"),
        ("AI project", "root");

INSERT INTO task(title, username, flag, completed, project_id, tag)
    VALUES 
        ("DB assignment", "root", "None", TRUE, 1, "assignment"),
        ("Lab course", "root", "Normal", TRUE, 1, ""),
        ("SE course", "root", "Importance", TRUE, 2, ""),
        ("SE assignment", "root", "Importance", TRUE, 2, "assignment"),
        ("AI practice", "root", "No worry", FALSE, 2, "");


-- Get all tasks from user "root"
SELECT * FROM task WHERE username="root";


-- Get all tasks having "assignment" as tag
SELECT * FROM task WHERE tag="assignment";


-- Get tasks from project which has ID 1 of user "root"
SELECT * FROM task WHERE username="root" and project_id=1;


-- Get tasks having "assignment" substring in title 
SELECT * FROM task WHERE title LIKE "%assignment%";


-- Get total grade from completed taks
SELECT SUM(grade) as Total_grade FROM importance 
    WHERE flag IN (SELECT flag FROM task WHERE completed=TRUE);