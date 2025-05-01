package com.example.tournamentRoundService.constants;

/**
 * Constants for error codes used when creating a tournament
 */
public class CreateTournamentErrorCodes {
    // Lỗi liên quan đến tên giải đấu
    public static final String NAME_ALREADY_EXISTS = "T001"; // Tên giải đấu đã tồn tại
    public static final String NAME_TOO_SHORT = "T002"; // Tên giải đấu quá ngắn
    public static final String NAME_TOO_LONG = "T003"; // Tên giải đấu quá dài
    public static final String NAME_INVALID_CHARACTERS = "T004"; // Tên giải đấu chứa ký tự không hợp lệ

    // Lỗi liên quan đến ngày bắt đầu
    public static final String START_DATE_EMPTY = "T101"; // Ngày bắt đầu không được để trống
    public static final String START_DATE_INVALID_FORMAT = "T102"; // Ngày bắt đầu không đúng định dạng
    public static final String START_DATE_PARSE_ERROR = "T103"; // Lỗi khi parse ngày bắt đầu
    public static final String START_DATE_IN_PAST = "T104"; // Ngày bắt đầu nằm trong quá khứ
    public static final String START_DATE_TOO_FAR_IN_FUTURE = "T105"; // Ngày bắt đầu quá xa trong tương lai

    // Lỗi liên quan đến ngày kết thúc
    public static final String END_DATE_EMPTY = "T201"; // Ngày kết thúc không được để trống
    public static final String END_DATE_INVALID_FORMAT = "T202"; // Ngày kết thúc không đúng định dạng
    public static final String END_DATE_PARSE_ERROR = "T203"; // Lỗi khi parse ngày kết thúc
    public static final String END_DATE_TOO_FAR_IN_FUTURE = "T204"; // Ngày kết thúc quá xa trong tương lai

    // Lỗi liên quan đến thời gian
    public static final String START_DATE_AFTER_END_DATE = "T301"; // Ngày bắt đầu sau ngày kết thúc
    public static final String TOURNAMENT_DURATION_TOO_SHORT = "T302"; // Thời gian giải đấu quá ngắn
    public static final String TOURNAMENT_DURATION_TOO_LONG = "T303"; // Thời gian giải đấu quá dài

    // Lỗi liên quan đến số lượng người chơi
    public static final String MAX_PLAYER_LESS_THAN_ONE = "T401"; // Số người chơi phải lớn hơn 0
    public static final String MAX_PLAYER_NOT_EVEN = "T402"; // Số người chơi phải là số chẵn
    public static final String MAX_PLAYER_TOO_LARGE = "T403"; // Số người chơi quá lớn cho phương pháp tổ chức
    public static final String MAX_PLAYER_TOO_SMALL = "T404"; // Số người chơi quá ít cho phương pháp tổ chức
    public static final String THREE_PLAYER_BOARD_REQUIRES_MULTIPLE_OF_THREE = "T405"; // Bàn cờ 3 người chơi cần số người chơi chia hết cho 3

    // Lỗi liên quan đến khóa ngoại
    public static final String BOARD_TYPE_NOT_FOUND = "T501"; // BoardType không tồn tại
    public static final String ORGANIZING_METHOD_NOT_FOUND = "T502"; // OrganizingMethod không tồn tại
    public static final String BOARD_TYPE_INACTIVE = "T503"; // BoardType không hoạt động
    public static final String INVALID_ORGANIZER_ID = "T504"; // Người tổ chức không hợp lệ

    // Lỗi liên quan đến mô tả
    public static final String DESCRIPTION_TOO_LONG = "T601"; // Mô tả quá dài
    public static final String DESCRIPTION_CONTAINS_INVALID_CONTENT = "T602"; // Mô tả chứa nội dung không hợp lệ

    // Lỗi hệ thống
    public static final String SYSTEM_ERROR = "T901"; // Lỗi hệ thống
    public static final String DATABASE_ERROR = "T902"; // Lỗi cơ sở dữ liệu

    // Ngăn việc khởi tạo class này
    private CreateTournamentErrorCodes() {
    }
}