package ua.com.vitkovska.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vitkovska.model.Player;

import java.util.List;

@Repository
public interface PlayerDao extends JpaRepository<Player, Integer>{
    Page<Player> findAll(Specification<Player> specification, Pageable pageable);

    List<Player> findAll(Specification<Player> specification);
}

