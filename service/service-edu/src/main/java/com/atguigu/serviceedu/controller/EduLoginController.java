package com.atguigu.serviceedu.controller;

import com.atguigu.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/serviceedu/user")
@CrossOrigin
public class EduLoginController {
    @PostMapping("login")
    public R login() {
        return R.ok().data("token", "admin");
    }

    //    {"roles":["admin"],"name":"admin","avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"}}
    @GetMapping("info")
    public R info() {
        return R.ok()
                .data("roles", "[admin]")
                .data("name", "admin")
                .data("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    @PostMapping("logout")
    public R logout() {
        return R.ok().data("token", "admin");
    }
}
