package infinitefire.project.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long>{
	
	List<Issue> findByState(IssueState state);
	
}
