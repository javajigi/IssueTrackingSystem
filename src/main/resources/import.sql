INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(1, 'First Milestone', 'First Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(2, 'Second Milestone', 'Second Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(3, 'Third Milestone', 'Third Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(4, 'Fourth Milestone', 'Fourth Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(5, 'Fifth Milestone', 'Fifth Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into user (id, user_id, name, password, state) values (1, 'test', 'jkh', '1234', 'y');
insert into user (id, user_id, name, password, state) values (2, 'test2', 'jaewoong', '1234', 'y');
insert into user (id, user_id, name, password, state) values (3, 'test3', 'subin', '1234', 'y');

INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id) VALUES (1, 'first issue contents', 0, 'close', 'first issue', CURRENT_TIMESTAMP(), 1);
INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id) VALUES (2, 'second issue contents', 0, 'close', 'second issue', CURRENT_TIMESTAMP(), 2);
INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id) VALUES (3, 'third issue contents', 0, 'close', 'third issue', CURRENT_TIMESTAMP(), 1);
INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id) VALUES (4, 'fourth issue contents', 0, 'close', 'fourth issue', CURRENT_TIMESTAMP(), 3);
