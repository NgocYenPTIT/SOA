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
}