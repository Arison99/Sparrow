package com.example.callservice.controller;

import com.example.callservice.model.Call;
import com.example.callservice.service.CallService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calls")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping("/initiate")
    public Call initiateCall(@RequestParam String caller, @RequestParam String callee) {
        return callService.initiateCall(caller, callee);
    }

    @GetMapping
    public List<Call> getAllCalls() {
        return callService.getAllCalls();
    }

    @GetMapping("/{id}")
    public Call getCallById(@PathVariable Long id) {
        return callService.getCallById(id);
    }

    @PostMapping("/{id}/end")
    public Call endCall(@PathVariable Long id) {
        return callService.endCall(id);
    }
}
