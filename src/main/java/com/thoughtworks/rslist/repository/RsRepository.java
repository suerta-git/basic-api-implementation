package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.RsEventPO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RsRepository extends CrudRepository<RsEventPO, Integer> {
    @Override
    List<RsEventPO> findAll();
}
