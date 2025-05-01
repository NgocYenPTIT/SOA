package com.example.clientKTPM;

import com.example.clientKTPM.model.*;
import com.example.clientKTPM.util.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;

@Controller
public class ClientController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ServiceAPI serviceAPI;

    @Value("${app.global.url.user-service}")
    private String userServiceUrl;

    @Value("${app.global.url.auth-service}")
    private String authServiceUrl;

    @Value("${app.global.url.tournament-service}")
    private String urlTournamentService;

    // Hiển thị trang đăng nhập
    @GetMapping("/")
    public String showLoginPage() {
        // Kiểm tra nếu đã đăng nhập thì chuyển đến trang hello
        if (session.getAttribute("user") != null) {
            return "redirect:/home";
        }
        return "login.html";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        // Kiểm tra đăng nhập đơn giản (username: admin, password: password)
        try {
            Token token = this.serviceAPI.call(authServiceUrl + "login", HttpMethod.POST, new Account(username, password), Token.class, "");

            session.setAttribute("token", token.getToken());
            session.setAttribute("user", token.getUser());

            // Chuyển đến trang home
            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "login";
        }
    }

    // Hiển thị trang hello
    @GetMapping("/home")
    public String showHomePage(Model model) {
        // Kiểm tra người dùng đã đăng nhập chưa
        User user = (User) session.getAttribute("user");

        if (user != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("username", user.getUsername());
            return "home";
        } else {
            // Chưa đăng nhập, quay lại trang login.html
            return "redirect:/";
        }
    }

    // Xử lý đăng xuất
    @PostMapping("/logout")
    public String logout() {
        // Xóa session
        session.invalidate();

        // Quay lại trang đăng nhập
        return "redirect:/";
    }

    // Hiển thị trang giải đấu
    @GetMapping("/tournaments")
    public String showTournamentPage(Model model) {
        // Kiểm tra nếu đã đăng nhập thì chuyển đến trang hello
        if (session.getAttribute("user") != null) {
//            System.out.println(session.getAttribute("user"));
            TournamentPageableDto flag_all = this.serviceAPI.call(
                    this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=all",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );

            TournamentPageableDto flag_create_and_join = this.serviceAPI.call(
                    this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=create-and-join",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );

            TournamentPageableDto flag_only_create = this.serviceAPI.call(
                    this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=only-create",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );

            TournamentPageableDto flag_only_join = this.serviceAPI.call(
                    this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=only-join",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );
            model.addAttribute("flag_all", flag_all.getTotalItems());
            model.addAttribute("flag_create_and_join", flag_create_and_join.getTotalItems());
            model.addAttribute("flag_only_create", flag_only_create.getTotalItems());
            model.addAttribute("flag_only_join", flag_only_join.getTotalItems());
            return "tournament-management";
        }
        return "redirect:/";
    }

    @GetMapping("/tournament/create")
    public String showCreateTournamentForm(Model model) {
        // Kiểm tra nếu đã đăng nhập thì chuyển đến trang hello
        if (session.getAttribute("user") != null) {
            System.out.println(session.getAttribute("user"));
            model.addAttribute("urlTournamentService", this.urlTournamentService);
            return "create-tournament";
        }
        return "redirect:/";
    }

    // Phương thức này đã được sửa để trả về ResponseEntity thay vì String
    @PostMapping("/tournament/create")
    @ResponseBody
    public ResponseEntity<?> createTournamentForm(@RequestBody TournamentDto tournamentData) {
        // Kiểm tra nếu đã đăng nhập
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Người dùng chưa đăng nhập");
        }

        try {
            // Lấy token từ session
            String token = (String) session.getAttribute("token");
            System.out.println(tournamentData);
            // Gọi API tạo giải đấu
            TournamentDto response = this.serviceAPI.call(
                    urlTournamentService + "tournament",
                    HttpMethod.POST,
                    tournamentData,
                    TournamentDto.class,
                    token
            );

            System.out.println("Tournament created successfully");
            System.out.println(response);

            // Trả về thành công với đối tượng tournament mới
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Nếu có lỗi, trả về lỗi dưới dạng JSON với HTTP status 400 Bad Request
            System.out.println("Error creating tournament: " + e.getMessage());

            String errorMessage = e.getMessage();

            try {
                // Pattern để tìm các đối tượng JSON trong chuỗi lỗi
                Pattern pattern = Pattern.compile("\\[\\{.*?\\}\\]");
                Matcher matcher = pattern.matcher(errorMessage);

                if (matcher.find()) {
                    // Trích xuất phần JSON từ thông báo lỗi
                    String jsonError = matcher.group(0);
                    System.out.println("Extracted error JSON: " + jsonError);

                    // Trả về JSON lỗi với Content-Type là application/json
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(jsonError);
                }
            } catch (Exception ex) {
                System.out.println("Error processing exception message: " + ex.getMessage());
            }

            // Nếu không tìm thấy JSON trong thông báo lỗi, trả về định dạng JSON thủ công
            String formattedError = "[{\"field\":\"general\",\"message\":\""
                    + errorMessage.replace("\"", "'")
                    + "\",\"errorCode\":\"T901\"}]";

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(formattedError);
        }
    }

    @GetMapping("/tournaments/all")
    public String showAllTournaments(
            @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        // Kiểm tra người dùng đã đăng nhập chưa
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("username", user.getUsername());
            TournamentPageableDto tournamentAllPagination = this.serviceAPI.call(
                    // this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=all",
                    this.urlTournamentService + "tournament?" + "name=" + name + "&currentPage=" + currentPage + "&pageSize=" + pageSize + "&flag=all",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );
            model.addAttribute("tournament_all_pagination", tournamentAllPagination);
            System.out.println(model.getAttribute("tournament_all_pagination"));
            return "all-tournament";
        } else {
            // Chưa đăng nhập, quay lại trang login.html
            return "redirect:/";
        }
    }

    @GetMapping("/tournaments/create-and-join")
    public String showCreateAndJoinTournaments(
            @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        // Kiểm tra người dùng đã đăng nhập chưa
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("username", user.getUsername());
            TournamentPageableDto tournamentAllPagination = this.serviceAPI.call(
                    // this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=all",
                    this.urlTournamentService + "tournament?" + "name=" + name + "&currentPage=" + currentPage + "&pageSize=" + pageSize + "&flag=create-and-join",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );
            model.addAttribute("tournament_create_and_join_pagination", tournamentAllPagination);
            System.out.println(model.getAttribute("tournament_create_and_join_pagination"));
            return "create-and-join";
        } else {
            // Chưa đăng nhập, quay lại trang login.html
            return "redirect:/";
        }
    }

    @GetMapping("/tournaments/only-create")
    public String showOnlyCreateTournaments(
            @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        // Kiểm tra người dùng đã đăng nhập chưa
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("username", user.getUsername());
            TournamentPageableDto tournamentAllPagination = this.serviceAPI.call(
                    // this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=all",
                    this.urlTournamentService + "tournament?" + "name=" + name + "&currentPage=" + currentPage + "&pageSize=" + pageSize + "&flag=only-create",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );
            model.addAttribute("tournament_only_create_pagination", tournamentAllPagination);
            System.out.println(model.getAttribute("tournament_only_create_pagination"));
            return "only-create";
        } else {
            // Chưa đăng nhập, quay lại trang login.html
            return "redirect:/";
        }
    }

    @GetMapping("/tournaments/only-join")
    public String showOnlyJoinTournaments(
            @RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "name", defaultValue = "") String name,
            Model model
    ) {
        // Kiểm tra người dùng đã đăng nhập chưa
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("username", user.getUsername());
            TournamentPageableDto tournamentAllPagination = this.serviceAPI.call(
                    // this.urlTournamentService + "tournament?name=&currentPage=1&pageSize=10&flag=all",
                    this.urlTournamentService + "tournament?" + "name=" + name + "&currentPage=" + currentPage + "&pageSize=" + pageSize + "&flag=only-join",
                    HttpMethod.GET,
                    null,
                    TournamentPageableDto.class,
                    (String) session.getAttribute("token")
            );
            model.addAttribute("tournament_only_join_pagination", tournamentAllPagination);
            System.out.println(model.getAttribute("tournament_only_join_pagination"));
            return "only-join";
        } else {
            // Chưa đăng nhập, quay lại trang login.html
            return "redirect:/";
        }
    }

    @GetMapping("/tournament/{id}/details")
    public String showDetailTournament(
            @PathVariable(name = "id") int id,
            Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("username", user.getUsername());
            TournamentResponse tournament = this.serviceAPI.call(
                    this.urlTournamentService + "tournament/" + id,
                    HttpMethod.GET,
                    null,
                    TournamentResponse.class,
                    (String) session.getAttribute("token")
            );
            model.addAttribute("tournament", tournament);
            model.addAttribute("role", "host");
            model.addAttribute("isMember", false);
            model.addAttribute("isKickOff", true);
            model.addAttribute("showButtonUpdate", true);
            model.addAttribute("showButtonDelete", true);
            //TODO ẩn các button khi status = Kết Thúc


//            System.out.println(model.getAttribute("tournament"));
            System.out.println(tournament.getOrganizingMethod());
            return "detail-tournament";
        } else {
            // Chưa đăng nhập, quay lại trang login.html
            return "redirect:/";
        }
    }

    @GetMapping("/tournament/{id}/update")
    public String showUpdateTournament(
            @PathVariable(name = "id") int id,
            Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("username", user.getUsername());
            TournamentResponse tournament = this.serviceAPI.call(
                    this.urlTournamentService + "tournament/" + id,
                    HttpMethod.GET,
                    null,
                    TournamentResponse.class,
                    (String) session.getAttribute("token")
            );
            model.addAttribute("tournament", tournament);
//            model.addAttribute("role", "host");
//            model.addAttribute("isMember", false);
//            model.addAttribute("isKickOff", true);
//            model.addAttribute("showButtonUpdate", true);
//            model.addAttribute("showButtonDelete", true);



//            System.out.println(model.getAttribute("tournament"));
            System.out.println(tournament.getOrganizingMethod());
            return "update-tournament";
        } else {
            // Chưa đăng nhập, quay lại trang login.html
            return "redirect:/";
        }
    }

    @GetMapping("/rank")
    public String rank(
                        Model model) {
      return  "rank" ; 
    }
}