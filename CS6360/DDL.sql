create table employee
(
	employeeid numeric(9,0) primary key,
	age number(3) not null,
	salary_rate numeric(9,2),
	name varchar2(20) not null,
	state varchar2(2) not null,
	city varchar2(20) not null,
	street_name varchar2(20) not null,
	street_number varchar2(20) not null,
	zipcode varchar2(10) not null,
	event_staff_id numeric(9,0) null
)

alter table employee add constraint fk_employee
	foreign key (event_staff_id)	references employee (employeeid);

create table dinning
(
	shift varchar2(10) null,
	type varchar2(20) null,
	employeeid numeric(9,0) null
)

alter table dinning add constraint fk_dinning
	foreign key (employeeid)	references employee (employeeid);

create table concierge
(
	concierge_experience_year number(3) null,
	employeeid numeric(9,0) null
)

alter table concierge add constraint fk_concierge
	foreign key (employeeid)	references employee (employeeid);

create table housekeeping
(
	housekeeping_experience_year number(3) null,
	employeeid numeric(9,0) null
)

alter table housekeeping add constraint fk_housekeeping
	foreign key (employeeid)	references employee (employeeid);

create table receptionist
(
	language varchar2(10) null,
	employeeid numeric(9,0) null
)

alter table housekeeping add constraint fk_housekeeping
	foreign key (employeeid)	references employee (employeeid);