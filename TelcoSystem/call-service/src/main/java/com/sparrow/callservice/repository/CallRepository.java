package com.sparrow.callservice.repository;

import com.sparrow.callservice.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call, Long> {
}
