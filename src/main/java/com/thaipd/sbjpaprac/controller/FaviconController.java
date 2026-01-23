package com.thaipd.sbjpaprac.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FaviconController {

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        log.trace("Favicon requested, returning no content");
        // Return 200/204 to suppress browser favicon requests in REST APIs
    }
}
