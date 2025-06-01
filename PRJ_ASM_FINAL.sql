CREATE DATABASE ASM_PRJ_FINAL;
GO
USE ASM_PRJ_FINAL;
GO

-- 1. Bảng Clubs (Câu lạc bộ)
CREATE TABLE Clubs (
    clubID INT PRIMARY KEY IDENTITY(1,1),
    clubName NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX) NULL,
    establishedDate DATE NULL,
    image NVARCHAR(255) NULL
);
GO

-- 2. Bảng Users (Người dùng)
CREATE TABLE Users (
    userID INT PRIMARY KEY IDENTITY(1,1),
    studentID VARCHAR(10) NOT NULL,
    fullName NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'User' CHECK (role IN ('Admin', 'User')),
    image NVARCHAR(255) NULL
);
GO

-- 3. Bảng Events (Sự kiện)
CREATE TABLE Events (
    eventID INT PRIMARY KEY IDENTITY(1,1),
    eventName NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX) NULL,
    eventDate DATETIME NOT NULL,
    location NVARCHAR(200) NOT NULL,
    clubID INT NULL,
    image NVARCHAR(255) NULL,
    FOREIGN KEY (clubID) REFERENCES Clubs(clubID)
);
GO

-- 4. Bảng EventParticipants (Tham gia sự kiện)
CREATE TABLE EventParticipants (
    eventParticipantID INT PRIMARY KEY IDENTITY(1,1),
    eventID INT NOT NULL,
    userID INT NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (Status IN ('Registered', 'Attended', 'Absent')),
    FOREIGN KEY (eventID) REFERENCES Events(eventID),
    FOREIGN KEY (userID) REFERENCES Users(userID)
);
GO

-- 5. Bảng Reports (Báo cáo)
CREATE TABLE Reports (
    reportID INT PRIMARY KEY IDENTITY(1,1),
    clubID INT NOT NULL,
    semester NVARCHAR(20) NOT NULL,
    memberChanges NVARCHAR(MAX) NULL,
    eventSummary NVARCHAR(MAX) NULL,
    participationStatus NVARCHAR(MAX) NULL,
    createdDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (clubID) REFERENCES Clubs(clubID)
);
GO

-- 6. Bảng EventFeedback (Phản hồi sự kiện)
CREATE TABLE EventFeedback (
    feedbackID INT PRIMARY KEY IDENTITY(1,1),
    eventID INT NOT NULL,
    userID INT NOT NULL,
    rating INT CHECK (Rating BETWEEN 1 AND 5),
    comments NVARCHAR(MAX) NULL,
    feedbackDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (eventID) REFERENCES Events(eventID),
    FOREIGN KEY (userID) REFERENCES Users(userID)
);
GO

-- 7. Bảng ClubAnnouncements (Thông báo của câu lạc bộ)
CREATE TABLE ClubAnnouncements (
    announcementID INT PRIMARY KEY IDENTITY(1,1),
    clubID INT NOT NULL,
    title NVARCHAR(255) NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    announcementDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (clubID) REFERENCES Clubs(clubID)
);
GO

-- 8. Bảng duyệt đơn xin tham gia câu lạc bộ
CREATE TABLE ClubJoinApplications (
    applicationID INT PRIMARY KEY IDENTITY(1,1),
    userID INT NOT NULL,
    clubID INT NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (Status IN ('Accept', 'Refuse', 'Waiting')),
    applicationDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (clubID) REFERENCES Clubs(clubID),
    FOREIGN KEY (userID) REFERENCES Users(userID)
);
GO

-- 9. Bảng History
CREATE TABLE History (
    historyID INT PRIMARY KEY IDENTITY(1,1),
    userID INT NULL,
    action NVARCHAR(MAX) NOT NULL,
    changeDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (userID) REFERENCES Users(userID)
);
GO

-- 10. Bảng Notifications
CREATE TABLE Notifications ( 
    notificationID INT PRIMARY KEY IDENTITY(1,1),
    userID INT NOT NULL,
    clubID INT NULL,
    content NVARCHAR(MAX) NOT NULL,
    notificationDate DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (userID) REFERENCES Users(userID),
    FOREIGN KEY (clubID) REFERENCES Clubs(clubID)
);
GO

-- 11. Bảng ReadNotifications
CREATE TABLE ReadNotifications (
    readNotificationID INT PRIMARY KEY IDENTITY(1,1),
    userID INT NOT NULL,
    notificationID INT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('Read')),
    FOREIGN KEY (userID) REFERENCES Users(userID),
    FOREIGN KEY (notificationID) REFERENCES Notifications(notificationID)
);
GO

-- 12. Bảng MemberClubs (Câu lạc bộ)
CREATE TABLE MemberClubs (
    userID INT,
    clubID INT,
    PRIMARY KEY (userID, clubID),
    roleClub VARCHAR(50) NOT NULL DEFAULT 'Member' CHECK (roleClub IN ('Chairman', 'ViceChairman', 'TeamLeader', 'Member')),
    FOREIGN KEY (clubID) REFERENCES Clubs(clubID),
    FOREIGN KEY (userID) REFERENCES Users(userID)
);
GO

-- Create table Messages (Tin nhắn kiểu email)
CREATE TABLE Messages (
    messageID INT PRIMARY KEY IDENTITY(1,1),
    senderID INT NOT NULL,
    receiverID INT NOT NULL,
    subject NVARCHAR(255) NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    sentDate DATETIME DEFAULT GETDATE(),
    status VARCHAR(20) NOT NULL CHECK (status IN ('Seen','Sent')),
    FOREIGN KEY (senderID) REFERENCES Users(userID),
    FOREIGN KEY (receiverID) REFERENCES Users(userID)
);
GO

-- Trigger trg_DeleteClub
CREATE TRIGGER trg_DeleteClub
ON Clubs
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE Events SET clubID = NULL WHERE clubID IN (SELECT clubID FROM deleted);
    DELETE FROM MemberClubs WHERE clubID IN (SELECT clubID FROM deleted);
    DELETE FROM Notifications WHERE clubID IN (SELECT clubID FROM deleted);
    DELETE FROM Reports WHERE clubID IN (SELECT clubID FROM deleted);
    DELETE FROM ClubAnnouncements WHERE clubID IN (SELECT clubID FROM deleted); 
    DELETE FROM ClubJoinApplications WHERE clubID IN (SELECT clubID FROM deleted);
    DELETE FROM Clubs WHERE clubID IN (SELECT clubID FROM deleted);
END;
GO

-- Trigger trg_DeleteUser
CREATE TRIGGER trg_DeleteUser
ON Users
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM EventFeedback WHERE userID IN (SELECT userID FROM deleted);
    DELETE FROM ClubJoinApplications WHERE userID IN (SELECT userID FROM deleted);
    DELETE FROM EventParticipants WHERE userID IN (SELECT userID FROM deleted);
    DELETE FROM ReadNotifications WHERE userID IN (SELECT userID FROM deleted);
    DELETE FROM Notifications WHERE userID IN (SELECT userID FROM deleted);
    DELETE FROM MemberClubs WHERE userID IN (SELECT userID FROM deleted);
    DELETE FROM Messages 
    WHERE senderID IN (SELECT userID FROM deleted) 
       OR receiverID IN (SELECT userID FROM deleted);
    UPDATE History SET userID = NULL WHERE userID IN (SELECT userID FROM deleted);
    DELETE FROM Users WHERE userID IN (SELECT userID FROM deleted);
END;
GO

-- Trigger trg_DeleteEvent
CREATE TRIGGER trg_DeleteEvent
ON Events
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM EventFeedback WHERE eventID IN (SELECT eventID FROM deleted);
    DELETE FROM EventParticipants WHERE eventID IN (SELECT eventID FROM deleted);
    DELETE FROM Events WHERE eventID IN (SELECT eventID FROM deleted);
END;
GO

-- Trigger trg_DeleteNotification
CREATE TRIGGER trg_DeleteNotification
ON Notifications
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;
    DELETE FROM ReadNotifications 
    WHERE notificationID IN (SELECT notificationID FROM deleted);
    DELETE FROM Notifications 
    WHERE notificationID IN (SELECT notificationID FROM deleted);
END;
GO

USE ASM_PRJ_FINAL;
GO

-- 1. Chèn dữ liệu vào bảng Clubs (5 câu lạc bộ)
INSERT INTO Clubs (clubName, description, establishedDate, image)
VALUES 
    (N'Câu lạc bộ Bóng đá', N'Câu lạc bộ dành cho những người yêu thích bóng đá', '2020-05-15', N'club_image_1.png'),
    (N'Câu lạc bộ Cờ vua', N'Nơi học và chơi cờ vua', '2019-09-10', N'club_image_2.png'),
    (N'Câu lạc bộ Âm nhạc', N'Dành cho những sinh viên đam mê âm nhạc', '2021-03-20', N'club_image_3.png'),
    (N'Câu lạc bộ Lập trình', N'Học lập trình và phát triển phần mềm', '2022-01-10', N'club_image_4.png'),
    (N'Câu lạc bộ Mỹ Thuật', N'Khám phá nghệ thuật trừu tượng', '2023-06-05', N'club_image_5.png');
GO

-- 2. Chèn dữ liệu vào bảng Users (3 admin + thành viên cho 5 câu lạc bộ)
INSERT INTO Users (studentID, fullName, email, password, role, image)
VALUES 
    -- 3 Admin
    ('HE170001', N'Đặng Thái Sơn', 'admin1@gmail.com', 'adminpass1', 'Admin', N'acc_1.png'),
    ('HA180002', N'Trần Ngọc Anh', 'admin2@gmail.com', 'adminpass2', 'Admin', N'acc_2.png'),
    ('HS190003', N'Lê Minh Châu', 'admin3@gmail.com', 'adminpass3', 'Admin', N'acc_3.png'),
    -- CLB Bóng đá (19 thành viên)
    ('HE170101', N'Phạm Đình Nguyên', 'chutichbd@gmail.com', 'pass123', 'User', N'acc_4.png'), -- Chủ tịch
    ('HA180102', N'Trần Bảo Ngọc', 'phobd1@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HS190103', N'Lê Anh Tuấn', 'phobd2@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HE170104', N'Phạm Hồng Phúc', 'tlbd1@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HA180105', N'Nguyễn Hải Đăng', 'tlbd2@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HS190106', N'Trần Minh Thư', 'tlbd3@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HE170107', N'Lê Quốc Bảo', 'tvbd1@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HA180108', N'Phạm Thanh Hà', 'tvbd2@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HS190109', N'Nguyễn Gia Huy', 'tvbd3@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HE170110', N'Trần Kim Anh', 'tvbd4@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HA180111', N'Lê Văn Phong', 'tvbd5@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HS190112', N'Phạm Ngọc Mai', 'tvbd6@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HE170113', N'Nguyễn Thái Bình', 'tvbd7@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HA180114', N'Trần Hồng Nhung', 'tvbd8@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HS190115', N'Lê Minh Đức', 'tvbd9@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HE170116', N'Phạm Bảo Linh', 'tvbd10@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HA180117', N'Nguyễn Quang Vinh', 'tvbd11@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HS190118', N'Trần Thảo Vy', 'tvbd12@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HE170119', N'Lê Hoàng Long', 'tvbd13@gmail.com', 'pass123', 'User', N'acc_1.png'),
    -- CLB Cờ vua (19 thành viên)
    ('HA180201', N'Hoàng Minh Tuấn', 'chutichcv@gmail.com', 'pass123', 'User', N'acc_2.png'), -- Chủ tịch
    ('HS190202', N'Trần Ngọc Lan', 'phocv1@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HE170203', N'Lê Anh Khoa', 'phocv2@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HA180204', N'Phạm Hồng Nhung', 'tlcv1@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HS190205', N'Nguyễn Gia Bảo', 'tlcv2@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HE170206', N'Trần Minh Anh', 'tlcv3@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HA180207', N'Lê Quốc Hưng', 'tvcv1@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HS190208', N'Phạm Thanh Tâm', 'tvcv2@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HE170209', N'Nguyễn Hải Nam', 'tvcv3@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HA180210', N'Trần Bảo Châu', 'tvcv4@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HS190211', N'Lê Minh Phúc', 'tvcv5@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HE170212', N'Phạm Ngọc Linh', 'tvcv6@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HA180213', N'Nguyễn Thái Dương', 'tvcv7@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HS190214', N'Trần Hồng Anh', 'tvcv8@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HE170215', N'Lê Quang Vinh', 'tvcv9@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HA180216', N'Phạm Thảo Nguyên', 'tvcv10@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HS190217', N'Nguyễn Minh Đức', 'tvcv11@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HE170218', N'Trần Bảo Ngọc', 'tvcv12@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HA180219', N'Lê Hoàng Anh', 'tvcv13@gmail.com', 'pass123', 'User', N'acc_6.png'),
    -- CLB Âm nhạc (19 thành viên)
    ('HS190301', N'Nguyễn Thanh Tùng', 'chutichan@gmail.com', 'pass123', 'User', N'acc_7.png'), -- Chủ tịch
    ('HE170302', N'Trần Ngọc Mai', 'phoan1@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HA180303', N'Lê Minh Hào', 'phoan2@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HS190304', N'Phạm Hồng Phượng', 'tlan1@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HE170305', N'Nguyễn Gia Hân', 'tlan2@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HA180306', N'Trần Minh Thảo', 'tlan3@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HS190307', N'Lê Quốc Phong', 'tvan1@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HE170308', N'Phạm Thanh Nhàn', 'tvan2@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HA180309', N'Nguyễn Hải Long', 'tvan3@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HS190310', N'Trần Bảo Vy', 'tvan4@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HE170311', N'Lê Minh Tuấn', 'tvan5@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HA180312', N'Phạm Ngọc Anh', 'tvan6@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HS190313', N'Nguyễn Thái Hưng', 'tvan7@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HE170314', N'Trần Hồng Phúc', 'tvan8@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HA180315', N'Lê Quang Nam', 'tvan9@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HS190316', N'Phạm Thảo Linh', 'tvan10@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HE170317', N'Nguyễn Minh Hoàng', 'tvan11@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HA180318', N'Trần Bảo Hân', 'tvan12@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HS190319', N'Lê Hoàng Minh', 'tvan13@gmail.com', 'pass123', 'User', N'acc_4.png'),
    -- CLB Lập trình (19 thành viên)
    ('HE170401', N'Nguyễn Quốc Bảo', 'chutichlt@gmail.com', 'pass123', 'User', N'acc_5.png'), -- Chủ tịch
    ('HA180402', N'Trần Ngọc Thảo', 'pholt1@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HS190403', N'Lê Minh Khang', 'pholt2@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HE170404', N'Phạm Hồng Anh', 'tllt1@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HA180405', N'Nguyễn Gia Phúc', 'tllt2@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HS190406', N'Trần Minh Ngọc', 'tllt3@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HE170407', N'Lê Quốc Hào', 'tvlt1@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HA180408', N'Phạm Thanh Mai', 'tvlt2@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HS190409', N'Nguyễn Hải Đăng', 'tvlt3@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HE170410', N'Trần Bảo Linh', 'tvlt4@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HA180411', N'Lê Minh Phong', 'tvlt5@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HS190412', N'Phạm Ngọc Hân', 'tvlt6@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HE170413', N'Nguyễn Thái Nam', 'tvlt7@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HA180414', N'Trần Hồng Vy', 'tvlt8@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HS190415', N'Lê Quang Đức', 'tvlt9@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HE170416', N'Phạm Thảo Anh', 'tvlt10@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HA180417', N'Nguyễn Minh Long', 'tvlt11@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HS190418', N'Trần Bảo Phúc', 'tvlt12@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HE170419', N'Lê Hoàng Nam', 'tvlt13@gmail.com', 'pass123', 'User', N'acc_2.png'),
    -- CLB Mỹ Thuật (19 thành viên)
    ('HA180501', N'Nguyễn Thanh Phong', 'chutichmt@gmail.com', 'pass123', 'User', N'acc_3.png'), -- Chủ tịch
    ('HS190502', N'Trần Ngọc Huyền', 'phomt1@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HE170503', N'Lê Minh Tuấn', 'phomt2@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HA180504', N'Phạm Hồng Linh', 'tlmt1@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HS190505', N'Nguyễn Gia Hùng', 'tlmt2@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HE170506', N'Trần Minh Thư', 'tlmt3@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HA180507', N'Lê Quốc Anh', 'tvmt1@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HS190508', N'Phạm Thanh Ngọc', 'tvmt2@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HE170509', N'Nguyễn Hải Bình', 'tvmt3@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HA180510', N'Trần Bảo Hân', 'tvmt4@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HS190511', N'Lê Minh Hoàng', 'tvmt5@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HE170512', N'Phạm Ngọc Mai', 'tvmt6@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HA180513', N'Nguyễn Thái Vinh', 'tvmt7@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HS190514', N'Trần Hồng Phượng', 'tvmt8@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HE170515', N'Lê Quang Hùng', 'tvmt9@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HA180516', N'Phạm Thảo Vy', 'tvmt10@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HS190517', N'Nguyễn Minh Phúc', 'tvmt11@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HE170518', N'Trần Bảo Ngọc', 'tvmt12@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HA180519', N'Lê Hoàng Long', 'tvmt13@gmail.com', 'pass123', 'User', N'acc_7.png');
GO

-- Thêm 10 người dùng mới chưa tham gia câu lạc bộ nào với ảnh từ acc_1.png đến acc_7.png
INSERT INTO Users (studentID, fullName, email, password, role, image)
VALUES 
    ('HE170601', N'Nguyễn Văn An', 'annguyen@gmail.com', 'pass123', 'User', N'acc_3.png'),
    ('HA180602', N'Trần Thị Bích', 'bichtran@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HS190603', N'Lê Hoàng Cường', 'cuongle@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HE170604', N'Phạm Minh Dũng', 'dungpham@gmail.com', 'pass123', 'User', N'acc_7.png'),
    ('HA180605', N'Nguyễn Thị Hoa', 'hoanguyen@gmail.com', 'pass123', 'User', N'acc_2.png'),
    ('HS190606', N'Trần Văn Khoa', 'khoatran@gmail.com', 'pass123', 'User', N'acc_5.png'),
    ('HE170607', N'Lê Thị Lan', 'lanle@gmail.com', 'pass123', 'User', N'acc_4.png'),
    ('HA180608', N'Phạm Quốc Nam', 'nampham@gmail.com', 'pass123', 'User', N'acc_1.png'),
    ('HS190609', N'Nguyễn Hồng Phúc', 'phucnguyen@gmail.com', 'pass123', 'User', N'acc_6.png'),
    ('HE170610', N'Trần Thị Thu', 'thutran@gmail.com', 'pass123', 'User', N'acc_3.png');
GO

-- 3. Chèn dữ liệu vào bảng MemberClubs (Phân vai trò: 1 Chairman, 2 ViceChairman, 3 TeamLeader, 13 Member)
INSERT INTO MemberClubs (userID, clubID, roleClub)
VALUES 
    -- CLB Bóng đá (ID = 1)
    (4, 1, 'Chairman'),      -- Chủ tịch
    (5, 1, 'ViceChairman'),  -- Phó chủ tịch 1
    (6, 1, 'ViceChairman'),  -- Phó chủ tịch 2
    (7, 1, 'TeamLeader'),    -- Team Leader 1
    (8, 1, 'TeamLeader'),    -- Team Leader 2
    (9, 1, 'TeamLeader'),    -- Team Leader 3
    (10, 1, 'Member'),       -- Thành viên 1
    (11, 1, 'Member'),       -- Thành viên 2
    (12, 1, 'Member'),       -- Thành viên 3
    (13, 1, 'Member'),       -- Thành viên 4
    (14, 1, 'Member'),       -- Thành viên 5
    (15, 1, 'Member'),       -- Thành viên 6
    (16, 1, 'Member'),       -- Thành viên 7
    (17, 1, 'Member'),       -- Thành viên 8
    (18, 1, 'Member'),       -- Thành viên 9
    (19, 1, 'Member'),       -- Thành viên 10
    (20, 1, 'Member'),       -- Thành viên 11
    (21, 1, 'Member'),       -- Thành viên 12
    (22, 1, 'Member'),       -- Thành viên 13
    -- CLB Cờ vua (ID = 2)
    (23, 2, 'Chairman'),
    (24, 2, 'ViceChairman'),
    (25, 2, 'ViceChairman'),
    (26, 2, 'TeamLeader'),
    (27, 2, 'TeamLeader'),
    (28, 2, 'TeamLeader'),
    (29, 2, 'Member'),
    (30, 2, 'Member'),
    (31, 2, 'Member'),
    (32, 2, 'Member'),
    (33, 2, 'Member'),
    (34, 2, 'Member'),
    (35, 2, 'Member'),
    (36, 2, 'Member'),
    (37, 2, 'Member'),
    (38, 2, 'Member'),
    (39, 2, 'Member'),
    (40, 2, 'Member'),
    (41, 2, 'Member'),
    -- CLB Âm nhạc (ID = 3)
    (42, 3, 'Chairman'),
    (43, 3, 'ViceChairman'),
    (44, 3, 'ViceChairman'),
    (45, 3, 'TeamLeader'),
    (46, 3, 'TeamLeader'),
    (47, 3, 'TeamLeader'),
    (48, 3, 'Member'),
    (49, 3, 'Member'),
    (50, 3, 'Member'),
    (51, 3, 'Member'),
    (52, 3, 'Member'),
    (53, 3, 'Member'),
    (54, 3, 'Member'),
    (55, 3, 'Member'),
    (56, 3, 'Member'),
    (57, 3, 'Member'),
    (58, 3, 'Member'),
    (59, 3, 'Member'),
    (60, 3, 'Member'),
    -- CLB Lập trình (ID = 4)
    (61, 4, 'Chairman'),
    (62, 4, 'ViceChairman'),
    (63, 4, 'ViceChairman'),
    (64, 4, 'TeamLeader'),
    (65, 4, 'TeamLeader'),
    (66, 4, 'TeamLeader'),
    (67, 4, 'Member'),
    (68, 4, 'Member'),
    (69, 4, 'Member'),
    (70, 4, 'Member'),
    (71, 4, 'Member'),
    (72, 4, 'Member'),
    (73, 4, 'Member'),
    (74, 4, 'Member'),
    (75, 4, 'Member'),
    (76, 4, 'Member'),
    (77, 4, 'Member'),
    (78, 4, 'Member'),
    (79, 4, 'Member'),
    -- CLB Mỹ Thuật (ID = 5)
    (80, 5, 'Chairman'),
    (81, 5, 'ViceChairman'),
    (82, 5, 'ViceChairman'),
    (83, 5, 'TeamLeader'),
    (84, 5, 'TeamLeader'),
    (85, 5, 'TeamLeader'),
    (86, 5, 'Member'),
    (87, 5, 'Member'),
    (88, 5, 'Member'),
    (89, 5, 'Member'),
    (90, 5, 'Member'),
    (91, 5, 'Member'),
    (92, 5, 'Member'),
    (93, 5, 'Member'),
    (94, 5, 'Member'),
    (95, 5, 'Member'),
    (96, 5, 'Member'),
    (97, 5, 'Member'),
    (98, 5, 'Member');
GO

INSERT INTO MemberClubs (userID, clubID, roleClub)
VALUES 
    (10, 2, 'Member'), (11, 3, 'Member'), (12, 4, 'Member'), (13, 5, 'Member'),
    (29, 1, 'Member'), (30, 3, 'Member'), (31, 4, 'Member'), (32, 5, 'Member'),
    (48, 1, 'Member'), (49, 2, 'Member'), (50, 4, 'Member'), (51, 5, 'Member'),
    (67, 1, 'Member'), (68, 2, 'Member'), (69, 3, 'Member'), (70, 5, 'Member'),
    (86, 1, 'Member'), (87, 2, 'Member'), (88, 3, 'Member'), (89, 4, 'Member');
GO

-- 4. Chèn dữ liệu vào bảng Events (Mỗi CLB có 1 sự kiện)
INSERT INTO Events (eventName, description, eventDate, location, clubID, image)
VALUES 
    (N'Giải đấu bóng đá sinh viên', N'Thi đấu bóng đá giữa các câu lạc bộ', '2025-03-23 14:00:00', N'Sân vận động trường', 1, N'event_image_1.png'),
    (N'Thi đấu cờ vua mở rộng', N'Giải cờ vua dành cho sinh viên', '2025-03-24 11:00:00', N'Phòng hội thảo A', 2, N'event_image_2.png'),
    (N'Hòa nhạc mùa xuân', N'Biểu diễn âm nhạc của CLB', '2025-04-10 19:00:00', N'Hội trường lớn', 3, N'event_image_3.png'),
    (N'Cuộc thi lập trình', N'Thi lập trình giải thuật', '2025-04-15 13:00:00', N'Phòng máy tính B', 4, N'event_image_4.png'),
    (N'Triển lãm nghệ thuật', N'Trưng bày tác phẩm của thành viên', '2025-04-20 10:00:00', N'Trung tâm triển lãm', 5, N'event_image_5.png');
GO

-- 5. Chèn dữ liệu vào bảng EventParticipants (Thành viên tham gia sự kiện)
INSERT INTO EventParticipants (eventID, userID, status)
VALUES 
    -- Sự kiện 1: Giải đấu bóng đá sinh viên (CLB Bóng đá - clubID = 1)
    (1, 4, 'Registered'),   -- Chủ tịch
    (1, 5, 'Attended'),     -- Phó chủ tịch 1
    (1, 6, 'Registered'),   -- Phó chủ tịch 2
    (1, 7, 'Attended'),     -- Team Leader 1
    (1, 8, 'Absent'),       -- Team Leader 2
    (1, 9, 'Registered'),   -- Team Leader 3
    (1, 10, 'Attended'),    -- Thành viên 1
    (1, 11, 'Registered'),  -- Thành viên 2
    (1, 12, 'Attended'),    -- Thành viên 3
    (1, 13, 'Absent'),      -- Thành viên 4
    -- Sự kiện 2: Thi đấu cờ vua mở rộng (CLB Cờ vua - clubID = 2)
    (2, 23, 'Registered'),  -- Chủ tịch
    (2, 24, 'Attended'),    -- Phó chủ tịch 1
    (2, 25, 'Registered'),  -- Phó chủ tịch 2
    (2, 26, 'Attended'),    -- Team Leader 1
    (2, 27, 'Absent'),      -- Team Leader 2
    (2, 28, 'Registered'),  -- Team Leader 3
    (2, 29, 'Attended'),    -- Thành viên 1
    (2, 30, 'Registered'),  -- Thành viên 2
    (2, 31, 'Attended'),    -- Thành viên 3
    (2, 32, 'Absent'),      -- Thành viên 4
    -- Sự kiện 3: Hòa nhạc mùa xuân (CLB Âm nhạc - clubID = 3)
    (3, 42, 'Registered'),  -- Chủ tịch
    (3, 43, 'Attended'),    -- Phó chủ tịch 1
    (3, 44, 'Registered'),  -- Phó chủ tịch 2
    (3, 45, 'Attended'),    -- Team Leader 1
    (3, 46, 'Absent'),      -- Team Leader 2
    (3, 47, 'Registered'),  -- Team Leader 3
    (3, 48, 'Attended'),    -- Thành viên 1
    (3, 49, 'Registered'),  -- Thành viên 2
    (3, 50, 'Attended'),    -- Thành viên 3
    (3, 51, 'Absent'),      -- Thành viên 4
    -- Sự kiện 4: Cuộc thi lập trình (CLB Lập trình - clubID = 4)
    (4, 61, 'Registered'),  -- Chủ tịch
    (4, 62, 'Attended'),    -- Phó chủ tịch 1
    (4, 63, 'Registered'),  -- Phó chủ tịch 2
    (4, 64, 'Attended'),    -- Team Leader 1
    (4, 65, 'Absent'),      -- Team Leader 2
    (4, 66, 'Registered'),  -- Team Leader 3
    (4, 67, 'Attended'),    -- Thành viên 1
    (4, 68, 'Registered'),  -- Thành viên 2
    (4, 69, 'Attended'),    -- Thành viên 3
    (4, 70, 'Absent'),      -- Thành viên 4
    -- Sự kiện 5: Triển lãm nghệ thuật (CLB Mỹ Thuật - clubID = 5)
    (5, 80, 'Registered'),  -- Chủ tịch
    (5, 81, 'Attended'),    -- Phó chủ tịch 1
    (5, 82, 'Registered'),  -- Phó chủ tịch 2
    (5, 83, 'Attended'),    -- Team Leader 1
    (5, 84, 'Absent'),      -- Team Leader 2
    (5, 85, 'Registered'),  -- Team Leader 3
    (5, 86, 'Attended'),    -- Thành viên 1
    (5, 87, 'Registered'),  -- Thành viên 2
    (5, 88, 'Attended'),    -- Thành viên 3
    (5, 89, 'Absent');      -- Thành viên 4
GO

-- 6. Chèn dữ liệu vào bảng Reports (Báo cáo cho mỗi CLB)
INSERT INTO Reports (clubID, semester, memberChanges, eventSummary, participationStatus)
VALUES 
    (1, N'HK1-2025', N'Thêm 2 thành viên mới', N'Giải đấu bóng đá thành công', N'80% tham gia'),
    (2, N'HK1-2025', N'Không thay đổi', N'Thi đấu cờ vua diễn ra tốt', N'90% tham gia'),
    (3, N'HK1-2025', N'Thêm 1 thành viên', N'Hòa nhạc thu hút nhiều khán giả', N'85% tham gia'),
    (4, N'HK1-2025', N'Không thay đổi', N'Cuộc thi lập trình thành công', N'75% tham gia'),
    (5, N'HK1-2025', N'Thêm 3 thành viên', N'Triển lãm nghệ thuật được đánh giá cao', N'95% tham gia');
GO

INSERT INTO Reports (clubID, semester, memberChanges, eventSummary, participationStatus, createdDate)
VALUES 
    -- Báo cáo cho CLB Bóng đá (clubID = 1)
    (1, N'HK1-2023', N'Thêm 3 thành viên mới', N'Giải đấu bóng đá nội bộ thành công', N'85% tham gia', '2023-03-15 10:00:00'),
    (1, N'HK2-2024', N'1 thành viên rời CLB', N'Tổ chức giao lưu bóng đá với trường khác', N'75% tham gia', '2024-08-20 14:30:00'),
    
    -- Báo cáo cho CLB Cờ vua (clubID = 2)
    (2, N'HK2-2023', N'Thêm 2 thành viên', N'Thi đấu cờ vua giao hữu thắng lợi', N'90% tham gia', '2023-09-10 09:15:00'),
    (2, N'HK1-2024', N'Không thay đổi', N'Giải cờ vua mở rộng đạt kết quả tốt', N'88% tham gia', '2024-02-25 11:00:00'),
    
    -- Báo cáo cho CLB Âm nhạc (clubID = 3)
    (3, N'HK1-2023', N'Thêm 1 thành viên', N'Hòa nhạc chào tân sinh viên ấn tượng', N'80% tham gia', '2023-04-05 15:00:00'),
    (3, N'HK2-2024', N'2 thành viên mới gia nhập', N'Biểu diễn âm nhạc ngoài trời thành công', N'92% tham gia', '2024-07-30 13:45:00'),
    
    -- Báo cáo cho CLB Lập trình (clubID = 4)
    (4, N'HK2-2023', N'Không thay đổi', N'Cuộc thi lập trình nội bộ diễn ra suôn sẻ', N'70% tham gia', '2023-08-15 10:30:00'),
    (4, N'HK1-2024', N'Thêm 4 thành viên', N'Hackathon lập trình thu hút nhiều đội tham gia', N'85% tham gia', '2024-03-10 09:00:00'),
    
    -- Báo cáo cho CLB Mỹ Thuật (clubID = 5)
    (5, N'HK1-2023', N'Thêm 2 thành viên', N'Triển lãm tranh mùa xuân được khen ngợi', N'90% tham gia', '2023-03-25 14:00:00'),
    (5, N'HK2-2024', N'1 thành viên rời CLB', N'Tổ chức workshop vẽ tranh ngoài trời', N'87% tham gia', '2024-09-05 11:30:00');
GO

-- 7. Chèn dữ liệu vào bảng EventFeedback (Thêm phản hồi từ nhiều thành viên)
INSERT INTO EventFeedback (eventID, userID, rating, comments)
VALUES 
    -- Sự kiện 1: Giải đấu bóng đá
    (1, 10, 4, N'Sự kiện rất vui, nhưng cần thêm nước uống'),
    (1, 11, 5, N'Tổ chức chuyên nghiệp, rất hài lòng'),
    (1, 12, 3, N'Sân hơi nhỏ, cần cải thiện'),
    -- Sự kiện 2: Thi đấu cờ vua
    (2, 29, 5, N'Giải đấu tổ chức chuyên nghiệp'),
    (2, 30, 4, N'Rất thú vị, nhưng thời gian hơi ngắn'),
    (2, 31, 5, N'Mọi thứ đều tuyệt vời'),
    -- Sự kiện 3: Hòa nhạc mùa xuân
    (3, 48, 3, N'Âm thanh cần cải thiện'),
    (3, 49, 4, N'Biểu diễn hay, không gian đẹp'),
    (3, 50, 5, N'Rất ấn tượng với các tiết mục'),
    -- Sự kiện 4: Cuộc thi lập trình
    (4, 67, 4, N'Đề thi thú vị, nhưng hơi khó'),
    (4, 68, 5, N'Tổ chức tốt, học được nhiều kinh nghiệm'),
    (4, 69, 3, N'Phòng máy hơi nóng, cần điều hòa tốt hơn'),
    -- Sự kiện 5: Triển lãm nghệ thuật
    (5, 86, 5, N'Triển lãm tuyệt vời!'),
    (5, 87, 4, N'Tác phẩm đẹp, nhưng ánh sáng cần tốt hơn'),
    (5, 88, 5, N'Rất sáng tạo và đáng xem');
GO

-- 8. Chèn dữ liệu vào bảng ClubAnnouncements (Thông báo từ CLB)
INSERT INTO ClubAnnouncements (clubID, title, content)
VALUES 
    (1, N'Tuyển thành viên mới', N'CLB Bóng đá tuyển thêm thành viên, đăng ký trước 30/04'),
    (2, N'Lịch thi đấu cờ vua', N'Thi đấu diễn ra vào 05/04, mọi người đến đúng giờ'),
    (3, N'Lịch tập luyện', N'Tập luyện cho hòa nhạc vào 07/04'),
    (4, N'Cuộc thi sắp tới', N'Chuẩn bị cho cuộc thi lập trình ngày 15/04'),
    (5, N'Triển lãm nghệ thuật', N'Mời mọi người tham gia triển lãm ngày 20/04');
GO

-- 9. Chèn dữ liệu vào bảng ClubJoinApplications (Đơn xin tham gia)
INSERT INTO ClubJoinApplications (userID, clubID, status)
VALUES 
    (14, 2, 'Waiting'), (15, 3, 'Accept'), (16, 4, 'Refuse'), (17, 5, 'Waiting'),
    (33, 1, 'Accept'), (34, 3, 'Refuse'), (35, 4, 'Waiting'), (36, 5, 'Accept'),
    (52, 1, 'Refuse'), (53, 2, 'Waiting'), (54, 4, 'Accept'), (55, 5, 'Refuse'),
    (71, 1, 'Waiting'), (72, 2, 'Accept'), (73, 3, 'Refuse'), (74, 5, 'Waiting'),
    (90, 1, 'Accept'), (91, 2, 'Refuse'), (92, 3, 'Waiting'), (93, 4, 'Accept');
GO

-- 10. Chèn dữ liệu vào bảng Notifications
INSERT INTO Notifications (userID, clubID, content, notificationDate)
VALUES 
    -- Thông báo cho CLB Bóng đá (clubID = 1)
    (5, 1, N'Nhắc nhở: Họp CLB Bóng đá vào thứ 5 lúc 18:00', '2025-03-05 09:00:00'),
    (4, 1, N'Giải đấu bóng đá: Đăng ký đội trước 28/03', '2025-03-06 15:30:00'),
    (10, 1, N'Bạn được mời tham gia sự kiện giao lưu bóng đá ngày 03/04', '2025-03-07 08:45:00'),
    (7, 1, N'Tập luyện cho giải đấu vào thứ 6 lúc 17:00', '2025-03-08 10:00:00'),
    (11, 1, N'Kiểm tra danh sách tham gia giải đấu trước 30/03', '2025-03-09 14:20:00'),
    (12, 1, N'CLB Bóng đá cần thêm tình nguyện viên cho sự kiện', '2025-03-10 11:15:00'),

    -- Thông báo cho CLB Cờ vua (clubID = 2)
    (23, 2, N'CLB Cờ vua cần chuẩn bị cho giải đấu ngày 05/04', '2025-03-11 10:30:00'),
    (24, 2, N'Buổi họp chiến lược thi đấu vào thứ 4 lúc 19:00', '2025-03-12 13:00:00'),
    (29, 2, N'CLB Cờ vua tổ chức buổi tập luyện vào Chủ nhật', '2025-03-13 13:20:00'),
    (26, 2, N'Nộp danh sách thành viên tham gia giải đấu trước 01/04', '2025-03-14 09:30:00'),
    (30, 2, N'Thử sức với bài cờ mới vào thứ 7', '2025-03-15 15:00:00'),
    (31, 2, N'Mời tham gia giao lưu cờ vua với CLB khác ngày 04/04', '2025-03-16 10:45:00'),

    -- Thông báo cho CLB Âm nhạc (clubID = 3)
    (42, 3, N'Hòa nhạc mùa xuân: Đăng ký tiết mục trước 05/04', '2025-03-17 14:15:00'),
    (43, 3, N'Tập luyện cho hòa nhạc vào thứ 6 lúc 18:30', '2025-03-18 16:00:00'),
    (48, 3, N'CLB Âm nhạc cần thêm nhạc cụ cho sự kiện', '2025-03-19 12:00:00'),
    (45, 3, N'Kiểm tra danh sách biểu diễn trước 03/04', '2025-03-20 09:15:00'),
    (49, 3, N'Mời tham gia buổi thử giọng ngày 02/04', '2025-03-21 11:30:00'),
    (50, 3, N'Họp chuẩn bị hậu cần cho hòa nhạc vào thứ 2', '2025-03-22 14:00:00'),

    -- Thông báo cho CLB Lập trình (clubID = 4)
    (61, 4, N'Cuộc thi lập trình: Gửi đề xuất ý tưởng trước 10/04', '2025-03-10 16:00:00'),
    (62, 4, N'Thảo luận đề thi vào thứ 5 lúc 17:00', '2025-03-11 13:45:00'),
    (67, 4, N'CLB Lập trình tổ chức workshop vào thứ 7', '2025-03-12 10:00:00'),
    (64, 4, N'Nộp bài dự thi trước 12/04', '2025-03-13 15:30:00'),
    (68, 4, N'Mời tham gia buổi chia sẻ kinh nghiệm lập trình ngày 05/04', '2025-03-14 09:00:00'),
    (69, 4, N'Kiểm tra phòng máy trước cuộc thi vào thứ 3', '2025-03-15 11:00:00'),

    -- Thông báo cho CLB Mỹ Thuật (clubID = 5)
    (80, 5, N'Triển lãm nghệ thuật: Nộp tác phẩm trước 15/04', '2025-03-16 11:00:00'),
    (81, 5, N'Họp chuẩn bị triển lãm vào thứ 6 lúc 16:00', '2025-03-17 14:30:00'),
    (86, 5, N'CLB Mỹ Thuật cần thêm tình nguyện viên trang trí', '2025-03-18 12:15:00');
GO

--11. Chèn dữ liệu vào bảng ReadNotifications (Thêm 18 trạng thái đã đọc)
INSERT INTO ReadNotifications (userID, notificationID, status)
VALUES 
    (5, 3, 'Read'),    -- Thông báo ID 3: userID 5 đã đọc
    (4, 4, 'Read'),    -- Thông báo ID 4: userID 4 đã đọc
    (10, 5, 'Read'),   -- Thông báo ID 5: userID 10 đã đọc
    (7, 6, 'Read'),    -- Thông báo ID 6: userID 7 đã đọc
    (11, 7, 'Read'),   -- Thông báo ID 7: userID 11 đã đọc
    (12, 8, 'Read'),   -- Thông báo ID 8: userID 12 đã đọc
    (23, 9, 'Read'),   -- Thông báo ID 9: userID 23 đã đọc
    (24, 10, 'Read'),  -- Thông báo ID 10: userID 24 đã đọc
    (29, 11, 'Read'),  -- Thông báo ID 11: userID 29 đã đọc
    (26, 12, 'Read'),  -- Thông báo ID 12: userID 26 đã đọc
    (30, 13, 'Read'),  -- Thông báo ID 13: userID 30 đã đọc
    (42, 15, 'Read'),  -- Thông báo ID 15: userID 42 đã đọc
    (43, 16, 'Read'),  -- Thông báo ID 16: userID 43 đã đọc
    (48, 17, 'Read'),  -- Thông báo ID 17: userID 48 đã đọc
    (61, 21, 'Read'),  -- Thông báo ID 21: userID 61 đã đọc
    (62, 22, 'Read'),  -- Thông báo ID 22: userID 62 đã đọc
    (67, 23, 'Read'),  -- Thông báo ID 23: userID 67 đã đọc
    (80, 27, 'Read');  -- Thông báo ID 27: userID 80 đã đọc
GO

-- 12. Chèn dữ liệu vào bảng Notifications
INSERT INTO Messages (senderID, receiverID, subject, content, sentDate, status)
VALUES 
    (4, 1, N'Kế hoạch giải đấu bóng đá', N'Chào Ngọc, chúng ta cần họp để chuẩn bị cho giải đấu ngày 01/04. Bạn rảnh lúc nào?', '2025-03-23 10:00:00', 'Seen'),
    (29, 1, N'Hỏi về lịch thi đấu', N'Chào anh Tuấn, em muốn hỏi lịch thi đấu cờ vua cụ thể ra sao để chuẩn bị?', '2025-03-24 14:30:00', 'Sent'),
    (42, 2, N'Đề xuất thêm kinh phí', N'Chào anh Sơn, CLB Âm nhạc cần thêm kinh phí cho hòa nhạc mùa xuân. Anh xem xét giúp nhé!', '2025-03-25 09:15:00', 'Sent'),
    (62, 3, N'Đề thi cuộc thi lập trình', N'Chào Bảo, mình đã soạn xong đề thi, gửi bạn xem qua nhé?', '2025-03-26 16:00:00', 'Seen'),
    (86, 3, N'Góp ý triển lãm nghệ thuật', N'Chào anh Phong, em thấy triển lãm nên thêm phần bình chọn tác phẩm đẹp, anh nghĩ sao?', '2025-03-27 11:45:00', 'Sent'),
    (1, 20, N'Xác nhận kinh phí giải đấu', N'Chào Nguyên, kinh phí giải đấu đã được duyệt, bạn kiểm tra nhé!', '2025-03-28 08:30:00', 'Seen'),
    (48, 3, N'Hỏi về lịch tập luyện', N'Chào anh Phượng, lịch tập luyện tuần này thế nào ạ?', '2025-03-29 13:00:00', 'Sent');
GO