package com.example.cleancode.payment.klarna;

import com.example.cleancode.job.Job;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class MapklarnWithJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public String klarnaOrderId;


    public MapklarnWithJob(Job job, String klaranaOrderId) {
        this.job = job;
        this.klarnaOrderId = klaranaOrderId;
    }
}
