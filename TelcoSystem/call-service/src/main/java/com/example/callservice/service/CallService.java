package com.sparrow.callservice.service;

import com.sparrow.callservice.model.Call;
import com.sparrow.callservice.repository.CallRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CallService {

    private final CallRepository callRepository;

    public CallService(CallRepository callRepository) {
        this.callRepository = callRepository;
    }

    public Call initiateCall(String caller, String callee) {
        Call call = new Call();
        call.setCaller(caller);
        call.setCallee(callee);
        call.setStartTime(LocalDateTime.now());
        call.setStatus("ONGOING");
        return callRepository.save(call);
    }

    public List<Call> getAllCalls() {
        return callRepository.findAll();
    }

    public Call getCallById(Long id) {
        return callRepository.findById(id).orElseThrow(() -> new RuntimeException("Call not found"));
    }

    public Call endCall(Long id) {
        Call call = getCallById(id);
        call.setEndTime(LocalDateTime.now());
        call.setStatus("COMPLETED");
        return callRepository.save(call);
    }
}
