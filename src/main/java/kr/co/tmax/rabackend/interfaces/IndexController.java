package com.tmax.security1.controller;

import com.tmax.security1.config.auth.PrincipalDetails;
import com.tmax.security1.model.User;
import com.tmax.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view 를 리턴하겠다
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,  // DI(의존성 주입)
                                          @AuthenticationPrincipal PrincipalDetails userDetails) { // @AuthenticationPrincipal을 통해서 세션 정보 접근
        System.out.println("/test/login ===============");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); // down casting

        System.out.println("authentication: " + principalDetails.getUser()); // 아래와 같음
       System.out.println("userDetails.getUser() = " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication,  // DI(의존성 주입)
                                          @AuthenticationPrincipal OAuth2User oauth) { // @AuthenticationPrincipal을 통해서 세션 정보 접근
        System.out.println("/test/oauth/login ===============");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes()); // 아래와 동일
        System.out.println("oauth.getAttributes() = " + oauth.getAttributes());

        return "OAuth 세션 정보 확인하기";
    }

    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) yml에서 생략 가능!
        return "index"; // src/main/resources/templates/index.mustache를 찾음, index.html만 있는데 어떻게 찾느냐? config에서 설정하면된다.
    }

    // OAuth 로그인을 해도 PrincipalDetails
    // 일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails.getUser() = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // 스프링 시큐리티가 해당주소를 낚아채버림 - SecurityConfig 파일을 생성 하니 낚아채지않음
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public @ResponseBody String join(User user) {
        System.out.println("user = " + user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);

        userRepository.save(user); // 회원가입 잘됨. 비밀번호: 1234 => 시큐리티로 로그인을 할 수 없음. 이유는 패스워드가 암호화가 안되었기 때문!!
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정메서드에 간단히 보안 적용
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 데이터라는 메서드가 실행되기 직전에 실행됨, 여러개 걸 수 있음. 하나만 걸때는 @Secured 사용
    //@PostAuthorize() // 데이터라는 메서드가 끝나고 난뒤에 실행됨
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
