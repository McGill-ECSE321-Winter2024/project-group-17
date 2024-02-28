package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;
import ca.mcgill.ecse321.SportCenterManager.model.Session;

public class SessionListDto {

    private List<Session> sessions;
    
    public SessionListDto(List<Session> sessions){
        this.sessions = sessions;
    }

    public List<Session> getSessions(){
        return sessions;
    }

    public void setSessions(List<Session> sessions){
        this.sessions = sessions;
    }
}
