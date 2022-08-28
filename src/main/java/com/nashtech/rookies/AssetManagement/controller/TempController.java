package com.nashtech.rookies.AssetManagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class TempController {
    @GetMapping("")
    public ResponseEntity<?> sayHi(){
        return ResponseEntity.ok("hello word!!");
    }
}
