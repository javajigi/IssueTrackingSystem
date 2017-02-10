package infinitefire.project.domain;

public interface Ownerable {
    boolean isOwner(User loginUser);
}
