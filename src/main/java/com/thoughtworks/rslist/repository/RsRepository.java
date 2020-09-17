package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsRepository extends CrudRepository<RsEventPO, Integer> {
    @Override
    List<RsEventPO> findAll();

    @Query(value = "select id from rs_event where event_name = ?1 and key_word = ?2 and user_id = ?3", nativeQuery = true)
    int findIdByRsEventFields(String eventName, String keyWord, int userId);
}
