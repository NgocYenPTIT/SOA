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
- **dayOfWeek**: String : Ví dụ Thứ 2, Thứ 3,... ,Chủ nhật.
- **startHour**: Time (NOT NULL) - Thời điểm bắt đầu: Ví dụ : 15:00
- **endHour**: Time (NOT NULL) - Thời điểm kết thúc : Ví dụ : 16:00
- **startDay**: Time (NOT NULL) - Ngày bắt đầu : 15/01/2023
- **endDay**: Time (NOT NULL) - Ngày kết thúc : 15/01/2023
- **type**: String - Loại lịch (LÝ THUYẾT, THỰC HÀNH)

## 4. Course (Lớp học phần)
- **id**: Long (PK)
- **group**: String (NOT NULL, UNIQUE) - Nhóm lớp
- **practiseGroup**: String (NOT NULL, UNIQUE) - Tổ thực hành
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
