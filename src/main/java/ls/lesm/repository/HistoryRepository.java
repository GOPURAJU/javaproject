package ls.lesm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.History;

public interface HistoryRepository extends JpaRepository<History, Integer> {

}

