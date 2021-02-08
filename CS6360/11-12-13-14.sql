-- 11. Client
CREATE TABLE client
(
    customerid NUMERIC(6,0) PRIMARY KEY
);

-- 12. Organization
CREATE TABLE organization
(
    customerid NUMERIC(6,0) PRIMARY KEY
);

ALTER TABLE organization add CONSTRAINT fk_organization
	FOREIGN KEY (customerid)	REFERENCES client (customerid);

-- 13. Individual
CREATE TABLE individual
(
    customerid NUMERIC(6,0) PRIMARY KEY,
    birthdate DATE NULL,
    individual_name VARCHAR2(20) NULL,
    individual_sex VARCHAR2(20) NULL
);

ALTER TABLE organization add CONSTRAINT fk_individual
	FOREIGN KEY (customerid)	REFERENCES client (customerid);

-- 14. Membersip
CREATE TABLE membership
(
    membership_number NUMERIC(11,0) PRIMARY KEY
);

-- 15. Room
CREATE TABLE room
(
    room_number NUMERIC(4,0) PRIMARY KEY,
    bed_type VARCHAR2(20) NULL,
    room_type VARCHAR2(20) NULL,
    per_night_price INTEGER NULL
);

-- 16. Clear
CREATE TABLE clear
(
    employeeid NUMERIC(9,0) NOT NULL,
    room_number NUMERIC(4,0) NOT NULL,
    cdate DATE NULL,
    ctime TIME NULL,
    PRIMARY KEY (employeeid, room_number)
);

ALTER TABLE clear add CONSTRAINT fk_clear
	FOREIGN KEY (employeeid)	REFERENCES employee (employeeid),
    FOREIGN KEY (room_number)   REFERENCES room (room_number);

-- 17. check_out_bill
CREATE TABLE check_out_bill
(
    room_number NUMERIC(6,0) NOT NULL,
    customerid NUMERIC(6,0) NOT NULL,
    check_out_bill_amount INTEGER NOT NULL,
    check_out_bill_issed_date DATE NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    PRIMARY KEY (room_number, customerid)
);

-- 18. has
CREATE TABLE has
(
    customerid NUMERIC(6,0) NOT NULL,
    membership_number NUMERIC(11,0) NOT NULL,
    PRIMARY KEY (customerid, membership_number)
);

ALTER TABLE has add CONSTRAINT fk_has
    FOREIGN KEY (customerid)    REFERENCES client (customerid),
    FOREIGN KEY (membership_number) REFERENCES membership (membership_number);

-- 22. direct_bill_account
CREATE TABLE direct_bill_account
(
    customerid NUMERIC(6,0) NOT NULL,
    direct_bill_account VARCHAR2(20) NOT NULL,
    PRIMARY KEY (customerid, direct_bill_account)
);

ALTER TABLE direct_bill_account add CONSTRAINT fk_direct_bill_account
    FOREIGN KEY (customerid)    REFERENCES client (customerid);

-- 23. phone
CREATE TABLE phone
(
    customerid NUMERIC(6,0) PRIMARY KEY,
    phone NUMERIC(10,0) NULL
)

ALTER TABLE phone add CONSTRAINT fk_phone
    FOREIGN KEY (customerid)    REFERENCES client (customerid);

-- 24. bill_event_payment
CREATE TABLE bill_event_payment
(
    customerid NUMERIC(6,0) NOT NULL,
    room_number NUMERIC(4,0) NOT NULL,
    cop_time TIME NOT NULL,
    cop_date DATE NOT NULL,
    cop_amount_of_payment NUMERIC(8,2),
    PRIMARY KEY (customerid, room_number)
);

ALTER TABLE bill_event_payment add CONSTRAINT fk_bill_event_payment
    FOREIGN KEY (customerid)    REFERENCES client (customerid),
    FOREIGN KEY (room_number)   REFERENCES room (room_number);
