package ua.com.vitkovska.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.vitkovska.model.Team;

@Repository
public interface TeamDao extends JpaRepository<Team,Integer> {
    boolean existsByName(String name);
}
