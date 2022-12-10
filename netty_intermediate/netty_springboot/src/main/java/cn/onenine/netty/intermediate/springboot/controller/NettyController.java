package cn.onenine.netty.intermediate.springboot.controller;

import cn.onenine.netty.intermediate.springboot.server.NettyServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author li.hongjian
 * @email lhj502819@163.com
 * @since 2022/12/10 12:19
 */
@RestController
@RequestMapping(value = "/netttyServer", method = RequestMethod.GET)
public class NettyController {

    @Resource
    private NettyServer nettyServer;

    @RequestMapping("/localAddress")
    public String localAddress(){
        return "nettyServer localAddress " + nettyServer.getChannel().localAddress();
    }

    @RequestMapping("/isOpen")
    public String isOpen(){
        return "nettyServer isOpen " + nettyServer.getChannel().isOpen();
    }

    @RequestMapping("/destroy")
    public String destroy(){
        nettyServer.destroy();
        return "success";
    }


}
