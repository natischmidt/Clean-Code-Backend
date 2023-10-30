package com.example.cleancode.payment.klarna;

import com.example.cleancode.job.Job;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KlarnaService {
    @Autowired
    private EntityManager entityManager;

    public void saveKlarnaJobMapping(Job job, String klaranaOrderId){
        MapklarnWithJob mapping = new MapklarnWithJob(job, klaranaOrderId);
        entityManager.persist(mapping);
    }
}
