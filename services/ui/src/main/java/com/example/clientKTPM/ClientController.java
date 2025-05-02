package com.example.clientKTPM;

import com.example.clientKTPM.model.*;
import com.example.clientKTPM.util.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @Value("${app.global.url.register-subject-read-model-service}")
    private String urlRegisterReadModelService;

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

            // Chuyển đến trang register
            return  "redirect:/register";
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
    @GetMapping("/register")
    public String showTournamentPage(Model model) {
        // Kiểm tra nếu đã đăng nhập thì chuyển đến trang hello
        if (session.getAttribute("user") != null) {
            System.out.println(session.getAttribute("user"));
            RegisterSubjectView view = this.serviceAPI.call(
                    this.urlRegisterReadModelService + "register-subject-read-model",
                    HttpMethod.GET,
                    null,
                    RegisterSubjectView.class,
                    (String) session.getAttribute("token")
            );
            System.out.println(view);
            model.addAttribute("view", view);
            return "register" ;
        }
        return "redirect:/";
    }
}