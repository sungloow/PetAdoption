package com.sunhongbing.petadoption.forestage.service;

import com.sunhongbing.petadoption.forestage.entity.ApplyRecord;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(isolation = Isolation.DEFAULT)
public interface AdoptionService {

    //apply
    int apply(int userId, int petId);

    //cancel
    int cancel(int userId, int petId);

    //accept
    int accept(int userId, int petId);

    //reject
    int reject(int userId, int petId);

    //get apply list
    List<ApplyRecord> getApplyListByStatus(int status, String order, String sort);

}
