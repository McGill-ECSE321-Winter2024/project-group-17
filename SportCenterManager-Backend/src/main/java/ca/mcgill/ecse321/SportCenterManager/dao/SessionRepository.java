package ca.mcgill.ecse321.SportCenterManager.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

public interface SessionRepository extends CrudRepository<Session, Integer> {
  public Session findSessionById(int id);
}
