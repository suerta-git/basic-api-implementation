package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserPO, Integer> {
    @Override
    List<UserPO> findAll();

    boolean existsByUserName(String userName);

    Optional<UserPO> findByUserName(String userName);

    @Query("select user.id from UserPO user where user.userName = :userName")
    Optional<Integer> findIdByUserName(String userName);
}
