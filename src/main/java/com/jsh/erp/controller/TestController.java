package com.jsh.erp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：stephen
 * @date ：Created in 2020/6/10 14:18
 * @description：TODO
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping("/post")
    public Object post(@RequestBody String body){
        logger.info(body);
        return "{status:ok}";
    }

    @GetMapping("/get")
    public Object get(String param){
        logger.info(param);
        return "{status:ok}";
    }

    @PutMapping("/put")
    public Object put(@RequestBody String body){
        logger.info(body);
        return "{status:ok}";
    }

    @DeleteMapping("/delete")
    public Object delete(String param){
        logger.info(param);
        return "{status:ok}";
    }
}
