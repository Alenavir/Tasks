-- Создание схемы, если она не существует
CREATE SCHEMA IF NOT EXISTS task;

-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS task.users
(
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Создание таблицы задач
CREATE TABLE IF NOT EXISTS task.tasks
(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP
);

-- Создание таблицы связи пользователей и задач
CREATE TABLE IF NOT EXISTS task.users_tasks
(
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, task_id),
    CONSTRAINT fk_users_tasks_users FOREIGN KEY (user_id) REFERENCES task.users (id) ON DELETE CASCADE ON UPDATE NO ACTION,
    CONSTRAINT fk_users_tasks_tasks FOREIGN KEY (task_id) REFERENCES task.tasks (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

-- Создание таблицы ролей пользователей
CREATE TABLE IF NOT EXISTS task.users_roles
(
    user_id BIGINT NOT NULL,
    role VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, role),
    CONSTRAINT fk_users_roles_users FOREIGN KEY (user_id) REFERENCES task.users (id) ON DELETE CASCADE ON UPDATE NO ACTION
);
