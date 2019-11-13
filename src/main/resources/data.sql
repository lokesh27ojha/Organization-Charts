insert into designation(DESIGNATION_ID, NAME, LEVEL_ID)
        values (1, 'Director', 1),
       (2, 'Manager', 2),
       (3, 'Lead', 3),
       (4, 'Developer', 4),
       (5, 'DevOps', 4),
       (6, 'QA', 4),
       (7, 'Intern', 5);

insert into employee(ID, NAME, JOB_TITLE, MANAGER_ID)
        values (1, 'Thor', 'Director', -1),
		(2,'IronMan','Manager',1),
		(3,'Hulk','Lead',1),
		(4,'CaptainAmerica','Manager',1),
		(5,'WarMachine','QA',2),
		(6,'Vison','DevOps',2),
		(7,'Falcon','Developer',4),
		(8,'AntMan','Lead',4),
		(9,'SpiderMan','Intern',2),
		(10,'BlackWidow','Developer',3);
