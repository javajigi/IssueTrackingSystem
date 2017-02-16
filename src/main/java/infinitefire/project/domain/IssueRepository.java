package infinitefire.project.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long>{
	
	List<Issue> findByOrganizationAndState(Organization organization, IssueState state);
	
	List<Issue> findByState(IssueState state);
	//List<Issue> findByOrganizationIdAndState(Long groupId, IssueState state);
	
	//날짜순-최신
	List<Issue> findByOrganizationOrderByWriteDateAsc(Organization organization);
	//날짜순-오래된
	List<Issue> findAllByOrderByWriteDateDesc();
	//댓글순-많은
	List<Issue> findByIdOrderByCountCommentAsc(Long id);
	//댓글순-적은
	List<Issue> findByIdOrderByCountCommentDesc(Long id);
}
