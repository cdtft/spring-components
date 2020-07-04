package com.github.cdtft.microservices.serviceprovider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wangcheng
 * @date : 2020年07月04日 13:44
 */
@RestController
public class EchoController {

    @GetMapping("/echo/{string}")
    public String echo(@PathVariable("string") String string) {
        return "Hello Nacos Discovery " + string;
    }

}
