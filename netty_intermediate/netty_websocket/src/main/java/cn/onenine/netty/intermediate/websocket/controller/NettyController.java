package cn.onenine.netty.intermediate.websocket.controller;

import cn.onenine.netty.intermediate.websocket.server.NettyServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
public class NettyController {

    @Resource
    private NettyServer nettyServer;

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("name", "xiaohao");
        return "index";
    }

}