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
import java.util.List;

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

    @Value("${app.global.register-read-model-service-url}")
    private String urlRegisterReadModelService;

    @Value("${app.global.register-subject-service-url}")
    private String registerSubjectServiceUrl;

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

            //TODO: Validate tổng số tín chỉ của các môn nguyện vọng của sinh viên, nếu không đủ, redirect tới trang not-enough-credit.html
            
            // Chuyển đến trang register
            return  "redirect:/register";
        } catch (Exception e) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "login";
        }
    }

    @PostMapping("/regist")
    public String registLogic(@RequestParam List<Long> courseIds,
                        Model model) {
        // Kiểm tra đăng nhập đơn giản (username: admin, password: password)
        try {
            System.out.println(courseIds);
            System.out.println("herre");
             try {
                 this.serviceAPI.call(
                         this.registerSubjectServiceUrl + "/register",
                         HttpMethod.POST,
                         bodyPost.builder()
                                 .courseIds(courseIds)
                                 .build(),
                         RegisterSubjectView.class,
                         (String) session.getAttribute("token")
                 );
             }
             catch (Exception e) {
                 System.out.println(e.getMessage());
             }
            // Chuyển đến trang register
            return  "waiting";
        } catch (Exception e) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "login";
        }
    }

    // Hiển thị trang đăng ký
    @GetMapping("/register")
    public String showTournamentPage(Model model) {
        // Kiểm tra nếu đã đăng nhập thì chuyển đến trang hello
        if (session.getAttribute("user") != null) {
            System.out.println(session.getAttribute("user"));

            try{
                ValidateFirstForm validateFirstForm = this.serviceAPI.call(
                        this.registerSubjectServiceUrl + "/register",
                        HttpMethod.GET,
                        null,
                        ValidateFirstForm.class,
                        (String) session.getAttribute("token")
                );
                if (!validateFirstForm.getSuccess().equals("SUCCESS")) {
                    return "not-enough-credit";
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }


            RegisterSubjectView view = this.serviceAPI.call(
                    this.urlRegisterReadModelService + "/register-subject-read-model",
                    HttpMethod.GET,
                    null,
                    RegisterSubjectView.class,
                    (String) session.getAttribute("token")
            );
            System.out.println("xinchao");
            System.out.println(view);
            model.addAttribute("view", view);
            return "register" ;
        }
        return "redirect:/";
    }

    // Xử lý đăng xuất
    @PostMapping("/logout")
    public String logout() {
        // Xóa session
        session.invalidate();

        // Quay lại trang đăng nhập
        return "redirect:/";
    }
}