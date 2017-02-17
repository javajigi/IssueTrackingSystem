package infinitefire.project.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{
	List<Organization> findByOrganizationMakerId(Long id);
	
//	List<Organization> findByState(OrganizationState state);
//	List<Organization> findByUserIn(List<User> memberList);
}