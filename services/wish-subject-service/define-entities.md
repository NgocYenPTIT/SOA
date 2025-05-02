# Schema Database Hệ thống Đăng ký Môn học

## 1. User (Người dùng)
- **id**: Long (PK)
- **username**: String (NOT NULL, UNIQUE)
- **password**: String (NOT NULL)
- **fullName**: String (NOT NULL)
- **email**: String (NOT NULL, UNIQUE)
- **phone**: String
- **role**: Enum (STUDENT, INSTRUCTOR, ADMIN)
- **studentId**: String (UNIQUE) - Mã sinh viên
- **majorId**: Long (FK) NOT NULL

## 2. Subject (Môn học)
- **id**: Long (PK)
- **subjectCode**: String (NOT NULL, UNIQUE) - Mã môn học
- **subjectName**: String (NOT NULL) - Tên môn học
- **credit**: Integer (NOT NULL) - Số tín chỉ
- **description**: Text - Mô tả môn học
- **prerequisiteSubjects**: String - Môn học tiên quyết

## 3. Schedule (Lịch học)
- **id**: Long (PK)
- **courseId**: Long (FK) - Tham chiếu đến Course
- **roomId**: Long (FK) - Tham chiếu đến Room
- **teacherId**: Long(FK) -Tham chiếu tới User
- **startTime**: Date
- **endTime**: Date
- **type**: String - Loại lịch (LÝ THUYẾT, THỰC HÀNH)

## 4. Course (Lớp học phần)
- **id**: Long (PK)
- **code**: String (NOT NULL, UNIQUE)
- **group**: String (NOT NULL) - Nhóm lớp
- **practiseGroup**: String (NOT NULL) - Tổ thực hành
- **subjectId**: Long (FK) - Tham chiếu đến Subject
- **semesterId**: Long (FK) - Tham chiếu đến Semester
- **instructorId**: Long (FK) - Tham chiếu đến User (giảng viên)
- **maxStudents**: Integer (NOT NULL) - Sĩ số tối đa
- **remainSlot**: Integer (NOT NULL) - Còn lại
- **currentStudents**: Integer (NOT NULL, DEFAULT 0) - Số sinh viên đã đăng ký
- **isActive**: Boolean (NOT NULL, DEFAULT true) - Trạng thái hoạt động

## 5. Room (Phòng học)
- **id**: Long (PK)
- **roomCode**: String (NOT NULL, UNIQUE) - Mã phòng

## 6. Enrollment (Đăng ký môn học)
- **id**: Long (PK)
- **studentId**: Long (FK) - Tham chiếu đến User (sinh viên)
- **courseId**: Long (FK) - Tham chiếu đến Course
- **enrollmentDate**: DateTime (NOT NULL) - Ngày đăng ký
- **status**: Enum (PENDING, APPROVED, REJECTED) - Trạng thái đăng ký

## 7. Major (Ngành)
- **id**: Long (PK)
- **name**: Long (FK) - Tham chiếu đến User (sinh viên)

## 8. Semester (Học kỳ)
- **id**: Long (PK)
- **semesterCode**: String (NOT NULL, UNIQUE) - Mã học kỳ
- **semesterName**: String (NOT NULL) - Tên học kỳ
- **majorId**: Long (PK) - Ngành
- **academicYear**: String (NOT NULL) - Năm học
- **startDate**: Date (NOT NULL) - Ngày bắt đầu
- **endDate**: Date (NOT NULL) - Ngày kết thúc
- **registrationStartDate**: Date - Thời gian bắt đầu đăng ký môn học
- **registrationEndDate**: Date - Thời gian kết thúc đăng ký môn học
- **status**: Enum (UPCOMING, ONGOING, COMPLETED) - Trạng thái học kỳ

## 9. CreditRule (Quy định tín chỉ)
- **id**: Long (PK)
- **minCredits**: Integer (NOT NULL) - Số tín chỉ tối thiểu
- **maxCredits**: Integer (NOT NULL) - Số tín chỉ tối đa
- **semesterId**: Long (FK) - Tham chiếu đến Semester
