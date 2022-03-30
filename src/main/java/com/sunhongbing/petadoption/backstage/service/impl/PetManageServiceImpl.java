package com.sunhongbing.petadoption.backstage.service.impl;

import com.sunhongbing.petadoption.backstage.entity.Animal;
import com.sunhongbing.petadoption.backstage.service.PetManageService;
import com.sunhongbing.petadoption.forestage.dao.AdoptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Blob;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @className: PetManageImpl
 * @Description: TODO
 * @author: Hunter Sun
 * @date: 2022-03-17 16:01
 */
@Service
public class PetManageServiceImpl implements PetManageService {

    @Autowired
    private AdoptionMapper adoptionMapper;

    //计算年龄
    private int calculatingAge(Date birth) {
        // 当前时间
        Date dateNow = new Date();
        // 年龄
        return (int) ((dateNow.getTime() - birth.getTime()) / 1000 / 60 / 60 / 24 / 365);
    }

    @Override
    public List<Animal> findAll(int status, String order, String sort) throws ParseException {
        List<Animal> animalList = adoptionMapper.findAll(status, order, sort);
        for (Animal animal : animalList) {
            // animal.getBirth() 转 Date
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(animal.getBirth());

            animal.setAge(calculatingAge(date));
        }

        return animalList;
    }

    @Override
    public List<Animal> findPetByStatusAndType(String type, int status, String order, String sort) throws ParseException {
        List<Animal> animalList = adoptionMapper.findPetByStatusAndType(type, status, order, sort);
        for (Animal animal : animalList) {
            // animal.getBirth() 转 Date
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(animal.getBirth());

            animal.setAge(calculatingAge(date));
        }

        return animalList;
    }

    @Override
    public Animal findPetById(int id) {
        return adoptionMapper.findPetById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updatePet(Animal animal) {
        try{
            //如果已领养状态,修改为未领养需要删除领养记录
            if (animal.getStatus() == 0) {
                Animal pet = adoptionMapper.findPetById(animal.getId());
                if (pet.getStatus() == 1) {
                    adoptionMapper.delete_adoption_ref(animal.getId());
                }
            }
            return adoptionMapper.updatePet(animal);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
    }

    @Override
    public int insertPet(Animal animal) {
        //校验
        if (animal.getName() == null || animal.getName().equals("")) {
            return -1;
        }
        if (animal.getType() == null || animal.getType().equals("")) {
            return -1;
        }
        if (animal.getSex() == null || animal.getSex().equals("")) {
            return -1;
        }
        if (animal.getBirth() == null) {
            return -1;
        }
        return adoptionMapper.insertPet(animal);
    }

    @Override
    public int insertPetImg(int id, String pics) {
        return adoptionMapper.insertPetPic(id, pics);
    }

    @Override
    public String findPetPicById(int id) {
        return adoptionMapper.findPetPicById(id);
    }

    @Override
    public int deletePet(int id) {
        return adoptionMapper.deletePet(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePets(int[] ids) {
        try {
            adoptionMapper.delete_adoption_refs(ids);
            return adoptionMapper.deletePets(ids);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
    }

    @Override
    public List<Animal> getRandomPets() {
        return adoptionMapper.getRandomPets();
    }
}
