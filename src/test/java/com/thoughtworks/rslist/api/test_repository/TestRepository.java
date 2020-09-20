package com.thoughtworks.rslist.api.test_repository;

import com.thoughtworks.rslist.po.UserPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TestRepository extends CrudRepository<UserPO, Integer> {
    @Query(value = "select next_val from hibernate_sequence limit 1", nativeQuery = true)
    int getNextId();
}
