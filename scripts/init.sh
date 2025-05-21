# #!/bin/bash

# # Đường dẫn tới thư mục services
BASE_DIR=$(pwd)/services

# # Khởi động từng service trên terminal riêng biệt với title

# # UI
# gnome-terminal --title="UI" -- bash -c "cd $BASE_DIR/ui && ./gradlew bootRun; exec bash" 

# # Auth Service
gnome-terminal --title="Auth Service" -- bash -c "cd $BASE_DIR/auth-service && ./gradlew bootRun; exec bash" 

# # Course Service
gnome-terminal --title="Course Service" -- bash -c "cd $BASE_DIR/course-service && ./gradlew bootRun; exec bash"

# # Course Background Service
gnome-terminal --title="Course Background Service" -- bash -c "cd $BASE_DIR/course-background-service && ./gradlew bootRun; exec bash"

# # Credit Rule Service
gnome-terminal --title="Credit Rule Service" -- bash -c "cd $BASE_DIR/credit-rule-service && ./gradlew bootRun; exec bash"

# # Enrollment Service
gnome-terminal --title="Enrollment Service" -- bash -c "cd $BASE_DIR/enrollment-service && ./gradlew bootRun; exec bash"

# Enrollment Background Service
gnome-terminal --title="Enrollment Background Service" -- bash -c "cd $BASE_DIR/enrollment-background-service && ./gradlew bootRun; exec bash"


# # Register Read Model Service
gnome-terminal --title="Register Read Model Service" -- bash -c "cd $BASE_DIR/register-read-model-service && ./gradlew bootRun; exec bash"

# Register Subject Service
gnome-terminal --title="Register Subject Service" -- bash -c "cd $BASE_DIR/register-subject-service && ./gradlew bootRun; exec bash"


# Register Subject Background Service
gnome-terminal --title="Register Subject Background Service" -- bash -c "cd $BASE_DIR/register-subject-background-service && ./gradlew bootRun; exec bash"

# # Room Service
gnome-terminal --title="Room Service" -- bash -c "cd $BASE_DIR/room-service && ./gradlew bootRun; exec bash"

# # Schedule Service
gnome-terminal --title="Schedule Service" -- bash -c "cd $BASE_DIR/schedule-service && ./gradlew bootRun; exec bash"

# # Semester Service
gnome-terminal --title="Semester Service" -- bash -c "cd $BASE_DIR/semester-service && ./gradlew bootRun; exec bash"

# # Subject Service
gnome-terminal --title="Subject Service" -- bash -c "cd $BASE_DIR/subject-service && ./gradlew bootRun; exec bash"

# # User Service
gnome-terminal --title="User Service" -- bash -c "cd $BASE_DIR/user-service && ./gradlew bootRun; exec bash"

# # Wish Subject Service
gnome-terminal --title="Wish Subject Service" -- bash -c "cd $BASE_DIR/wish-subject-service && ./gradlew bootRun; exec bash"

# echo "Tất cả các service đã được khởi động trong các terminal riêng biệt!"