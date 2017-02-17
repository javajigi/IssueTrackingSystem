package infinitefire.project.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long>{
	
	List<Issue> findByOrganizationAndState(Organization organization, IssueState state);
	
	List<Issue> findByState(IssueState state);
	//List<Issue> findByOrganizationIdAndState(Long groupId, IssueState state);
	
	//날짜순-오래된
	List<Issue> findByOrganizationAndStateOrderByWriteDateAsc(Organization organization, IssueState state);
	//날짜순-최신
	List<Issue> findByOrganizationAndStateOrderByWriteDateDesc(Organization organization, IssueState state);
	//댓글순-많은
	List<Issue> findByOrganizationAndStateOrderByCountCommentAsc(Organization organization, IssueState state);
	//댓글순-적은
	List<Issue> findByOrganizationAndStateOrderByCountCommentDesc(Organization organization, IssueState state);
}
