package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<VotePO, Integer> {
    @Override
    List<VotePO> findAll();
}
