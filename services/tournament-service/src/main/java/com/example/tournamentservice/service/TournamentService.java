package com.example.tournamentservice.service;

import com.example.tournamentservice.DTOs.*;
import com.example.tournamentservice.constants.CreateTournamentErrorCodes;
import com.example.tournamentservice.constants.TournamentStatus;
import com.example.tournamentservice.model.BoardType;
import com.example.tournamentservice.model.OrganizingMethod;
import com.example.tournamentservice.model.Tournament;
import com.example.tournamentservice.repository.BoardTypeRepository;
import com.example.tournamentservice.repository.OrganizingMethodRepository;
import com.example.tournamentservice.repository.TournamentRepository;
import com.example.tournamentservice.util.ServiceAPI;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final BoardTypeRepository boardTypeRepository;
    private final OrganizingMethodRepository organizingMethodRepository;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final ServiceAPI serviceAPI;

    @Value("${app.global.url.user-service}")
    private String urlUserService;

    @Value("${app.global.url.tournament-round-service}")
    private String urlTournamentRoundService;

    @Value("${app.global.url.tournament-player-service}")
    private String urlTournamentPlayerService;

    private final Pattern datePattern = Pattern.compile(
            "^([0-9]{1,2})/([0-9]{1,2})/([0-9]{4})\\s([0-9]{1,2}):([0-9]{1,2})$");

    @Autowired
    public TournamentService(TournamentRepository tournamentRepository,
                             BoardTypeRepository boardTypeRepository,
                             OrganizingMethodRepository organizingMethodRepository,
                             ServiceAPI serviceAPI) {
        this.tournamentRepository = tournamentRepository;
        this.boardTypeRepository = boardTypeRepository;
        this.organizingMethodRepository = organizingMethodRepository;
        this.serviceAPI = serviceAPI;
    }

    @Transactional
    public ResponseEntity<?> create(TournamentDto createTournamentDto, HttpServletRequest request) {
        List<ErrorResponseDto> errors = new ArrayList<>();

        // Kiểm tra tên giải đấu có trùng trong DB không
        if (tournamentRepository.existsByName(createTournamentDto.getName())) {
            errors.add(new ErrorResponseDto(
                    "name",
                    "Tên giải đấu đã tồn tại",
                    CreateTournamentErrorCodes.NAME_ALREADY_EXISTS  // T001: Tên giải đấu đã tồn tại
            ));
        }

        // Kiểm tra boardTypeId có tồn tại không
        Long boardTypeId = createTournamentDto.getBoardTypeId();
        boolean boardTypeExists = false;

        if (boardTypeId == null) {
            errors.add(new ErrorResponseDto(
                    "boardTypeId",
                    "BoardTypeId không được để trống",
                    CreateTournamentErrorCodes.BOARD_TYPE_NOT_FOUND  // T501: BoardType không tồn tại
            ));
        } else {
            Optional<BoardType> boardType = boardTypeRepository.findById(boardTypeId);
            if (boardType.isPresent()) {
                // Kiểm tra xem BoardType có active không
                if (boardType.get().getIsActive() != null && !boardType.get().getIsActive()) {
                    errors.add(new ErrorResponseDto(
                            "boardTypeId",
                            "BoardType không hoạt động",
                            CreateTournamentErrorCodes.BOARD_TYPE_INACTIVE  // Thêm mã lỗi nếu cần
                    ));
                } else {
                    boardTypeExists = true;
                }
            } else {
                errors.add(new ErrorResponseDto(
                        "boardTypeId",
                        "BoardType không tồn tại",
                        CreateTournamentErrorCodes.BOARD_TYPE_NOT_FOUND  // T501: BoardType không tồn tại
                ));
            }
        }

        // Kiểm tra organizingMethodId có tồn tại không
        Long organizingMethodId = createTournamentDto.getOrganizingMethodId();
        boolean organizingMethodExists = false;

        if (organizingMethodId == null) {
            errors.add(new ErrorResponseDto(
                    "organizingMethodId",
                    "OrganizingMethodId không được để trống",
                    CreateTournamentErrorCodes.ORGANIZING_METHOD_NOT_FOUND  // T502: OrganizingMethod không tồn tại
            ));
        } else {
            Optional<OrganizingMethod> organizingMethod = organizingMethodRepository.findById(organizingMethodId);
            if (organizingMethod.isPresent()) {
                organizingMethodExists = true;

                // Kiểm tra số người tham gia tối đa phù hợp với phương thức tổ chức
                Integer maxPlayer = createTournamentDto.getMaxPlayer();
                if (maxPlayer != null && maxPlayer > 0) {
                    int methodSize = organizingMethod.get().getSize();
                    // Thêm logic kiểm tra nếu cần, ví dụ:
                    // Nếu size=7 vòng đấu nhưng số người chơi quá đông
                    if (methodSize == 7 && maxPlayer > 128) {
                        errors.add(new ErrorResponseDto(
                                "maxPlayer",
                                "Số lượng người chơi quá lớn cho 7 vòng đấu, tối đa 128 người",
                                CreateTournamentErrorCodes.MAX_PLAYER_TOO_LARGE  // Thêm mã lỗi nếu cần
                        ));
                    }
                }
            } else {
                errors.add(new ErrorResponseDto(
                        "organizingMethodId",
                        "OrganizingMethod không tồn tại",
                        CreateTournamentErrorCodes.ORGANIZING_METHOD_NOT_FOUND  // T502: OrganizingMethod không tồn tại
                ));
            }
        }

        // Kiểm tra maxPlayer phải > 0 và là số chẵn
        Integer maxPlayer = createTournamentDto.getMaxPlayer();
        if (maxPlayer == null || maxPlayer <= 0) {
            errors.add(new ErrorResponseDto(
                    "maxPlayer",
                    "Số lượng người chơi phải lớn hơn 0",
                    CreateTournamentErrorCodes.MAX_PLAYER_LESS_THAN_ONE  // T401: Số người chơi phải lớn hơn 0
            ));
        } else if (maxPlayer % 2 != 0) {
            errors.add(new ErrorResponseDto(
                    "maxPlayer",
                    "Số lượng người chơi phải là số chẵn",
                    CreateTournamentErrorCodes.MAX_PLAYER_NOT_EVEN  // T402: Số người chơi phải là số chẵn
            ));
        }

        // Validate và parse startDate
        String startDateStr = createTournamentDto.getStartDate();
        String endDateStr = createTournamentDto.getEndDate();

        Date startDate = null;
        Date endDate = null;

        // Kiểm tra định dạng startDate
        if (startDateStr == null || startDateStr.trim().isEmpty()) {
            errors.add(new ErrorResponseDto(
                    "startDate",
                    "Ngày bắt đầu không được để trống",
                    CreateTournamentErrorCodes.START_DATE_EMPTY  // T101: Ngày bắt đầu không được để trống
            ));
        } else if (!datePattern.matcher(startDateStr).matches()) {
            errors.add(new ErrorResponseDto(
                    "startDate",
                    "Ngày bắt đầu phải có định dạng dd/MM/yyyy HH:mm",
                    CreateTournamentErrorCodes.START_DATE_INVALID_FORMAT  // T102: Ngày bắt đầu không đúng định dạng
            ));
        } else {
            try {
                // Chuẩn hóa định dạng ngày (đảm bảo đủ số 0 ở đầu)
                startDate = dateFormatter.parse(startDateStr);
                // Format lại để đảm bảo định dạng chuẩn dd/MM/yyyy HH:mm
                startDateStr = dateFormatter.format(startDate);
            } catch (ParseException e) {
                errors.add(new ErrorResponseDto(
                        "startDate",
                        "Ngày bắt đầu không hợp lệ",
                        CreateTournamentErrorCodes.START_DATE_PARSE_ERROR  // T103: Lỗi khi parse ngày bắt đầu
                ));
            }
        }

        // Kiểm tra định dạng endDate
        if (endDateStr == null || endDateStr.trim().isEmpty()) {
            errors.add(new ErrorResponseDto(
                    "endDate",
                    "Ngày kết thúc không được để trống",
                    CreateTournamentErrorCodes.END_DATE_EMPTY  // T201: Ngày kết thúc không được để trống
            ));
        } else if (!datePattern.matcher(endDateStr).matches()) {
            errors.add(new ErrorResponseDto(
                    "endDate",
                    "Ngày kết thúc phải có định dạng dd/MM/yyyy HH:mm",
                    CreateTournamentErrorCodes.END_DATE_INVALID_FORMAT  // T202: Ngày kết thúc không đúng định dạng
            ));
        } else {
            try {
                // Chuẩn hóa định dạng ngày
                endDate = dateFormatter.parse(endDateStr);
                // Format lại để đảm bảo định dạng chuẩn dd/MM/yyyy HH:mm
                endDateStr = dateFormatter.format(endDate);
            } catch (ParseException e) {
                errors.add(new ErrorResponseDto(
                        "endDate",
                        "Ngày kết thúc không hợp lệ",
                        CreateTournamentErrorCodes.END_DATE_PARSE_ERROR  // T203: Lỗi khi parse ngày kết thúc
                ));
            }
        }

        // Kiểm tra logic nếu cả hai ngày đều hợp lệ
        if (startDate != null && endDate != null) {
            // Kiểm tra startDate phải trước endDate
            if (!startDate.before(endDate)) {
                errors.add(new ErrorResponseDto(
                        "dateRange",
                        "Ngày bắt đầu phải trước ngày kết thúc",
                        CreateTournamentErrorCodes.START_DATE_AFTER_END_DATE  // T301: Ngày bắt đầu sau ngày kết thúc
                ));
            }

            // Kiểm tra ngày hiện tại phải trước startDate
            if (!new Date().before(startDate)) {
                errors.add(new ErrorResponseDto(
                        "startDate",
                        "Ngày bắt đầu phải sau ngày hiện tại",
                        CreateTournamentErrorCodes.START_DATE_IN_PAST  // T104: Ngày bắt đầu nằm trong quá khứ
                ));
            }

            // Kiểm tra khoảng thời gian giải đấu có hợp lý không
            long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

            if (boardTypeExists && organizingMethodExists) {
                // Ví dụ: Giải đấu 7 vòng cần ít nhất 2 ngày
                Optional<OrganizingMethod> method = organizingMethodRepository.findById(organizingMethodId);
                if (method.isPresent() && method.get().getSize() > 7 && diffInDays < 2) {
                    errors.add(new ErrorResponseDto(
                            "dateRange",
                            "Giải đấu với " + method.get().getSize() + " vòng cần ít nhất 2 ngày",
                            CreateTournamentErrorCodes.TOURNAMENT_DURATION_TOO_SHORT  // Thêm mã lỗi nếu cần
                    ));
                }
            }
        }

        // Trả về lỗi nếu có
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        // Nếu không có lỗi, tiếp tục xử lý và lưu vào DB
        Tournament tournament = new Tournament();
        tournament.setName(createTournamentDto.getName());
        tournament.setDescription(createTournamentDto.getDescription());
        tournament.setOrganizerId((Long) request.getAttribute("id"));
        tournament.setBoardTypeId(createTournamentDto.getBoardTypeId());
        tournament.setOrganizingMethodId(createTournamentDto.getOrganizingMethodId());
        tournament.setMaxPlayer(createTournamentDto.getMaxPlayer());
        tournament.setStartDate(startDateStr);  // Đã được chuẩn hóa
        tournament.setEndDate(endDateStr);      // Đã được chuẩn hóa
        tournament.setFreeToJoin(createTournamentDto.isFreeToJoin());      // Đã được chuẩn hóa
        tournament.setAttend((createTournamentDto.isAttend()));
        tournament.setStatus(TournamentStatus.NOT_STARTED);  // Trạng thái mặc định là chưa bắt đầu

        Tournament savedTournament = tournamentRepository.save(tournament);


        for (int i = 0; i < createTournamentDto.getRounds().size(); i++) {
            TournamentRoundDto round = createTournamentDto.getRounds().get(i);
            round.setTournamentId(savedTournament.getId());
            this.serviceAPI.call(urlTournamentRoundService + "tournament-round", HttpMethod.POST, round, TournamentRoundDto.class, (String) request.getAttribute("token"));
        }

        return ResponseEntity.ok(savedTournament);
    }

    public ResponseEntity<?> getDetail(Long id, HttpServletRequest request) {
        Tournament tournament = this.tournamentRepository.findById(id).get();
        Map<String, Object> map = new HashMap<>();

        List<TournamentRoundDto> round = this.serviceAPI.callForList(this.urlTournamentRoundService + "tournament/" + id, HttpMethod.GET, null, TournamentRoundDto.class, (String) request.getAttribute("token"));

        UserDto organizer = this.serviceAPI.call(
                this.urlUserService + "user/" + tournament.getOrganizerId(),
                HttpMethod.GET,
                null,
                UserDto.class,
                (String) request.getAttribute("token")
        );

        map.put("id", tournament.getId());
        map.put("name", tournament.getName());
        map.put("freeToJoin", tournament.isFreeToJoin());
        map.put("description", tournament.getDescription());
        map.put("organizer", organizer.getUsername());
        map.put("boardType", tournament.getBoardType().getName());
        map.put("organizingMethod", tournament.getOrganizingMethod().getName());
        map.put("maxPlayer", tournament.getMaxPlayer());
        map.put("startDate", tournament.getStartDate());
        map.put("endDate", tournament.getEndDate());
        map.put("status", tournament.getStatus());
        map.put("rounds", round);

        // TODO call Player List;


        return ResponseEntity.ok(map);
    }

    public ResponseEntity<?> getList(String name, String flag, Long currentPage, Long pageSize, HttpServletRequest request) {
        // Lấy playerId từ request
        Long playerId = (Long) request.getAttribute("id");
        if (playerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player ID is required");
        }

        // Kiểm tra tham số phân trang
        if (currentPage < 1 || pageSize < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid page or page size");
        }

        // Thiết lập phân trang và sắp xếp theo name
        Pageable pageable = PageRequest.of(
                currentPage.intValue() - 1, // currentPage bắt đầu từ 1, PageRequest dùng index từ 0
                pageSize.intValue(),
                Sort.by("name").ascending() // Sắp xếp theo name
        );

        Page<Tournament> tournamentPage;
        boolean hasName = name != null && !name.trim().isEmpty();

        // Chọn truy vấn dựa trên flag
        switch (flag) {
            case "all":
                if (hasName) {
                    tournamentPage = tournamentRepository.findByNameContainingIgnoreCaseAndDeletedAtIsNull(name, pageable);
                } else {
                    tournamentPage = tournamentRepository.findByDeletedAtIsNull(pageable);
                }
                break;

            case "create-and-join":
                if (hasName) {
                    tournamentPage = tournamentRepository.findByOrganizerIdAndAttendTrueAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                            playerId, name, pageable);
                } else {
                    tournamentPage = tournamentRepository.findByOrganizerIdAndAttendTrueAndDeletedAtIsNull(playerId, pageable);
                }
                break;

            case "only-create":
                if (hasName) {
                    tournamentPage = tournamentRepository.findByOrganizerIdAndAttendFalseAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                            playerId, name, pageable);
                } else {
                    tournamentPage = tournamentRepository.findByOrganizerIdAndAttendFalseAndDeletedAtIsNull(playerId, pageable);
                }
                break;

            case "only-join":
                if (hasName) {
                    tournamentPage = tournamentRepository.findByOrganizerIdNotAndNameContainingIgnoreCaseAndDeletedAtIsNull(
                            playerId, name, pageable);
                } else {
                    tournamentPage = tournamentRepository.findByOrganizerIdNotAndDeletedAtIsNull(playerId, pageable);
                }
                break;

            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid flag value");
        }

        List<Map<String, Object>> result = new ArrayList<>();

        // Xử lý từng tournament trong trang
        for (Tournament tournament : tournamentPage.getContent()) {
            Map<String, Object> tournamentInfo = new HashMap<>();
            tournamentInfo.put("id", tournament.getId());
            tournamentInfo.put("name", tournament.getName());
            tournamentInfo.put("status", tournament.getStatus());
            tournamentInfo.put("startDate", tournament.getStartDate());
            tournamentInfo.put("endDate", tournament.getEndDate());

            // Get organizer info
            UserDto organizer = this.serviceAPI.call(
                    this.urlUserService + "user/" + tournament.getOrganizerId(),
                    HttpMethod.GET,
                    null,
                    UserDto.class,
                    (String) request.getAttribute("token")
            );

            // Get participant count
            TournamentPlayerPageableDto participants = this.serviceAPI.call(
                    this.urlTournamentPlayerService + "tournament/" + tournament.getId() + "?username=&currentPage=1&pageSize=10",
                    HttpMethod.GET,
                    null,
                    TournamentPlayerPageableDto.class,
                    (String) request.getAttribute("token")
            );

            // Add organizer and participant count to tournament info
            tournamentInfo.put("organizer", organizer != null ? organizer.getUsername() : "Unknown");
            tournamentInfo.put("participantNum", participants != null ? participants.getTotalItems() + "/" + tournament.getMaxPlayer() : "0/" + tournament.getMaxPlayer());

            // Add this combined info to result list
            result.add(tournamentInfo);
        }

        // Tạo response với thông tin phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("tournaments", result);
        response.put("currentPage", tournamentPage.getNumber() + 1);
        response.put("totalPages", tournamentPage.getTotalPages());
        response.put("totalItems", tournamentPage.getTotalElements());
        response.put("pageSize", pageSize);

        return ResponseEntity.ok(response);
    }
}