package com.example.callservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Call {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caller;

    private String callee;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status; // e.g., "ONGOING", "COMPLETED"

    // Getters and Setters
}
