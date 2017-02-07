INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(1, 'First Milestone', 'First Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(2, 'Second Milestone', 'Second Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(3, 'Third Milestone', 'Third Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(4, 'Fourth Milestone', 'Fourth Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
INSERT INTO milestone (id, subject, description, start_date, end_date ) VALUES(5, 'Fifth Milestone', 'Fifth Milestone Description', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

insert into user (id, user_id, name, password, state, profile_url) values (1, 'test', 'jkh', '1234', 0, 'http://localhost:8080/file/new.png');
insert into user (id, user_id, name, password, state, profile_url) values (2, 'test2', 'jaewoong', '1234', 0, 'none.png');
insert into user (id, user_id, name, password, state, profile_url) values (3, 'test3', 'subin', '1234', 0, 'none.png');

INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id, milestone_id) VALUES (1, 'first issue contents', 0, 'CLOSE', 'first issue', CURRENT_TIMESTAMP(), 1, 2);
INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id, milestone_id) VALUES (2, 'second issue contents', 0, 'OPEN', 'second issue', CURRENT_TIMESTAMP(), 2, 2);
INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id, milestone_id) VALUES (3, 'third issue contents', 0, 'CLOSE', 'third issue', CURRENT_TIMESTAMP(), 1, 5);
INSERT INTO issue (id, contents, label, state, subject, write_date, writer_id, milestone_id) VALUES (4, 'fourth issue contents', 0, 'OPEN', 'fourth issue', CURRENT_TIMESTAMP(), 3, 3);

INSERT INTO comment (id, contents, write_date, issue_id, writer_id) VALUES (1, 'hi', CURRENT_TIMESTAMP(), 1, 2);

INSERT INTO label (id, name, color) VALUES (1, 'TASK', 'TASK');
INSERT INTO label (id, name, color) VALUES (2, 'ENHANCEMENT', 'ENHANCEMENT');
INSERT INTO label (id, name, color) VALUES (3, 'PROPOSAL', 'PROPOSAL');
INSERT INTO label (id, name, color) VALUES (4, 'BUG', 'BUG');

INSERT INTO label_issue (label_id, issue_id) VALUES (1, 1);
INSERT INTO label_issue (label_id, issue_id) VALUES (2, 1);
INSERT INTO label_issue (label_id, issue_id) VALUES (3, 1);
INSERT INTO label_issue (label_id, issue_id) VALUES (4, 2);
INSERT INTO label_issue (label_id, issue_id) VALUES (2, 3);  
 