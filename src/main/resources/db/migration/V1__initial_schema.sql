create table IF NOT EXISTS Department
(
    id serial primary key,
    dep_code varchar(20),
    dep_job varchar(100),
    description varchar(255),
    unique(dep_code, dep_job)
);

insert into Department (dep_code, dep_job, description) values
('10', 'analyst', 'test comment'),
('1', 'SEO', 'test comment'),
('23', 'CEO', 'test'),
('14', 'ARCHITECT', 'test comment'),
('31', 'GRAPHIC', 'comment'),
('12', 'technologist', 'test comment');

commit;