package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserPO, Integer> {
    @Override
    List<UserPO> findAll();

    boolean existsByUserName(String userName);

    UserPO findByUserName(String userName);

    @Query(value = "select id from user where user_name = ?1", nativeQuery = true)
    int findIdByUserName(String userName);
}
