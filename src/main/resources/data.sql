INSERT INTO users(username,password,full_name,role,email,phone_number,is_enabled)
VALUES
    ('admin','admin123','System Admin','ROLE_ADMIN','admin@gmail.com','0900000001',true),

    ('Thuyn','123456','Nguyễn Văn Thủy','ROLE_MANAGER','thuyn@gmail.com','0900000002',true),

    ('Binhnt','123456','Trần Văn Bình','ROLE_MANAGER','binhnt@gmail.com','0900000003',true),

    ('Mylt','123456','Lệ Thị My','ROLE_CUSTOMER','mylt05@gmail.com','0900000004',true),

    ('Dong','123456','Phạm Văn Đồng','ROLE_CUSTOMER','dongp@gmail.com','0900000005',true);


INSERT INTO courts(court_name,type,image_url,is_available,cluster_id)
VALUES
    ('Court A','VIP','abc.jpg',true,1),
    ('Court B','NORMAL','abc.jpg',true,1);


INSERT INTO timeslots(start_time,end_time)
VALUES
    ('08:00:00','10:00:00'),
    ('10:00:00','12:00:00'),
    ('18:00:00','20:00:00');