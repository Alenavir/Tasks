insert into users (name, username, password)
    values ('Alen Sun', 'razinka296@gmail.com', '$2a$10$7QuFOsy9fWqwyNCE1lecC.r5Ey3.DBP4NBNNhjf4dpwwrJ.ZrabIW'),
           ('Vida Bys', 'wanderernavi@gmail.com', '$2a$10$rv5AfU4lPTyl/CRV6ocZMOV2POK48yXlwndrwy03LnuYPVsCPl/xC');

insert into tasks (title, description, status, expiration_data)
    values ('Do proga', null, 'TODO', '2024-07-22 19:26:00'),
           ('DO english', null, 'IN_PROGRESS', '2024-07-22 22:00:00'),
           ('Do dinner', null, 'DONE', null);

insert into users_tasks (task_id, user_id)
    values (1, 1),
           (2, 2),
           (3, 1);

insert into users_roles (user_id, role)
    values (1, 'ROLE_ADMIN'),
           (2, 'ROLE_USER');