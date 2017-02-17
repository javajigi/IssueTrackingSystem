package infinitefire.project.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MilestoneRepository extends CrudRepository<Milestone, Long> {
	List<Milestone> findByOrganizationOrderByStartDateDesc(Organization organization);
}
