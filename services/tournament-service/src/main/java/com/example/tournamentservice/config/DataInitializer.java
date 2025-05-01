package com.example.tournamentservice.config;

import com.example.tournamentservice.constants.TournamentStatus;
import com.example.tournamentservice.model.BoardType;
import com.example.tournamentservice.model.OrganizingMethod;
import com.example.tournamentservice.model.Tournament;
import com.example.tournamentservice.repository.BoardTypeRepository;
import com.example.tournamentservice.repository.OrganizingMethodRepository;
import com.example.tournamentservice.repository.TournamentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initBoardTypeData(BoardTypeRepository boardTypeRepository) {
        return args -> {
            // Kiểm tra xem đã có dữ liệu chưa
            if (boardTypeRepository.count() == 0) {
                // Tạo BoardType mặc định
                BoardType standardBoard = new BoardType();
                standardBoard.setId(1L);
                standardBoard.setName("Bàn cờ 8x8");
                standardBoard.setSize("8x8");
                standardBoard.setDescription("Bàn cờ vua chuẩn kích thước 8x8");
                standardBoard.setIsActive(true);



                List<BoardType> boardTypes = Arrays.asList(standardBoard);
                boardTypeRepository.saveAll(boardTypes);

                System.out.println("Đã khởi tạo dữ liệu BoardType");
            }
        };
    }

    @Bean
    @Transactional
    public CommandLineRunner initOrganizingMethodData(OrganizingMethodRepository organizingMethodRepository) {
        return args -> {
            // Kiểm tra xem đã có dữ liệu chưa
            if (organizingMethodRepository.count() == 0) {
                // Tạo các phương pháp tổ chức giải đấu
                OrganizingMethod method1 = new OrganizingMethod();
                method1.setId(1L);
                method1.setName("7 vòng đấu");
                method1.setSize(7);
                method1.setDescription("Giải đấu được tổ chức với 7 vòng đấu");

                OrganizingMethod method2 = new OrganizingMethod();
                method2.setId(2L);
                method2.setName("9 vòng đấu");
                method2.setSize(9);
                method2.setDescription("Giải đấu được tổ chức với 9 vòng đấu");

                OrganizingMethod method3 = new OrganizingMethod();
                method3.setId(3L);
                method3.setName("11 vòng đấu");
                method3.setSize(11);
                method3.setDescription("Giải đấu được tổ chức với 11 vòng đấu");



                // Sử dụng Arrays.asList thay vì List.of cho Java 8
                List<OrganizingMethod> methods = Arrays.asList(method1, method2, method3);
                organizingMethodRepository.saveAll(methods);

                System.out.println("Đã khởi tạo dữ liệu OrganizingMethod");
            }
        };
    }

    @Bean
    @Transactional
    public CommandLineRunner initTournamentData(
            TournamentRepository tournamentRepository,
            BoardTypeRepository boardTypeRepository,
            OrganizingMethodRepository organizingMethodRepository) {
        return args -> {
            // Chỉ tạo dữ liệu mẫu nếu chưa có
            if (tournamentRepository.count() == 0) {
                // Lấy dữ liệu từ các repository
                List<BoardType> boardTypes = boardTypeRepository.findAll();
                List<OrganizingMethod> organizingMethods = organizingMethodRepository.findAll();

                if (!boardTypes.isEmpty() && !organizingMethods.isEmpty()) {
                    // Lấy các loại bàn cờ
                    BoardType standardBoard = boardTypeRepository.findById(1L).orElse(null);
                    BoardType largeBoard = boardTypeRepository.findById(2L).orElse(null);
                    BoardType threePlayersBoard = boardTypeRepository.findById(3L).orElse(null);

                    // Lấy các phương thức tổ chức
                    OrganizingMethod sevenRounds = organizingMethodRepository.findById(1L).orElse(null);
                    OrganizingMethod nineRounds = organizingMethodRepository.findById(2L).orElse(null);
                    OrganizingMethod eliminationMethod = organizingMethodRepository.findById(4L).orElse(null);

                    if (standardBoard != null && sevenRounds != null) {
                        // Tournament 1 - Giải đấu cờ vua quốc tế
                        Tournament internationalTournament = new Tournament();
                        internationalTournament.setName("International Chess Championship 2025");
                        internationalTournament.setDescription("Giải đấu cờ vua quốc tế với sự tham gia của các kỳ thủ hàng đầu");
                        internationalTournament.setOrganizerId(1L);  // Admin là người tổ chức
                        internationalTournament.setBoardTypeId(standardBoard.getId());
                        internationalTournament.setOrganizingMethodId(sevenRounds.getId());
                        internationalTournament.setMaxPlayer(64);
                        internationalTournament.setStatus(TournamentStatus.NOT_STARTED);
                        internationalTournament.setStartDate("15/05/2025 09:00");
                        internationalTournament.setEndDate("20/05/2025 18:00");

                        tournamentRepository.save(internationalTournament);
                    }

                    if (standardBoard != null && nineRounds != null) {
                        // Tournament 2 - Giải đấu cờ vua nhanh
                        Tournament rapidTournament = new Tournament();
                        rapidTournament.setName("Rapid Chess Challenge");
                        rapidTournament.setDescription("Giải đấu cờ vua nhanh với thời gian suy nghĩ giới hạn 15 phút mỗi người");
                        rapidTournament.setOrganizerId(2L);
                        rapidTournament.setBoardTypeId(standardBoard.getId());
                        rapidTournament.setOrganizingMethodId(nineRounds.getId());
                        rapidTournament.setMaxPlayer(16);
                        rapidTournament.setStatus(TournamentStatus.NOT_STARTED);
                        rapidTournament.setStartDate("01/06/2025 10:00");
                        rapidTournament.setEndDate("02/06/2025 17:00");

                        tournamentRepository.save(rapidTournament);
                    }

                    if (largeBoard != null && eliminationMethod != null) {
                        // Tournament 3 - Giải đấu cờ vua loại trực tiếp
                        Tournament eliminationTournament = new Tournament();
                        eliminationTournament.setName("Knockout Chess Tournament");
                        eliminationTournament.setDescription("Giải đấu cờ vua loại trực tiếp, thua một trận bị loại");
                        eliminationTournament.setOrganizerId(3L);
                        eliminationTournament.setBoardTypeId(largeBoard.getId());
                        eliminationTournament.setOrganizingMethodId(eliminationMethod.getId());
                        eliminationTournament.setMaxPlayer(32);
                        eliminationTournament.setStatus(TournamentStatus.NOT_STARTED);
                        eliminationTournament.setStartDate("15/07/2025 09:00");
                        eliminationTournament.setEndDate("16/07/2025 18:00");

                        tournamentRepository.save(eliminationTournament);
                    }

                    if (threePlayersBoard != null && sevenRounds != null) {
                        // Tournament 4 - Giải đấu cờ vua 3 người chơi
                        Tournament threePlayersTournament = new Tournament();
                        threePlayersTournament.setName("Three-Player Chess Tournament");
                        threePlayersTournament.setDescription("Giải đấu cờ vua đặc biệt với 3 người chơi trên một bàn cờ");
                        threePlayersTournament.setOrganizerId(1L);
                        threePlayersTournament.setBoardTypeId(threePlayersBoard.getId());
                        threePlayersTournament.setOrganizingMethodId(sevenRounds.getId());
                        threePlayersTournament.setMaxPlayer(24); // Phải chia 3
                        threePlayersTournament.setStatus(TournamentStatus.NOT_STARTED);
                        threePlayersTournament.setStartDate("10/08/2025 09:00");
                        threePlayersTournament.setEndDate("12/08/2025 18:00");

                        tournamentRepository.save(threePlayersTournament);
                    }

                    System.out.println("Đã khởi tạo dữ liệu Tournament");
                }
            }
        };
    }
}