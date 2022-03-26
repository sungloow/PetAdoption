package com.sunhongbing.petadoption.forestage.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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

}
