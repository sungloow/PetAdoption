package com.sunhongbing.petadoption.backstage.service;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import org.springframework.lang.Nullable;

import java.sql.Blob;
import java.text.ParseException;
import java.util.List;

/**
 * @className: PetManage
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-17 15:59
 */
public interface PetManageService {
    List<Animal> findAll(int status, String order, String sort) throws ParseException;

    List<Animal> findPetByStatusAndType(String status, String type);

    Animal findPetById(int id);

    int updatePet(Animal animal);

    int insertPet(Animal animal);

    int insertPetImg(int id, String pics);

    String findPetPicById(int id);

    int deletePet(int id);

    int deletePets(int[] ids);

}
