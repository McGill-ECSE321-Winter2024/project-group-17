package ca.mcgill.ecse321.SportCenterManager.dto;

import java.util.List;


public class SessionListDto {

    private List<SessionResponseDto> sessions;

    private SessionListDto(){

    }
    public SessionListDto(List<SessionResponseDto> sessions){

        this.sessions = sessions;
    }

    public List<SessionResponseDto> getSessions(){
        return sessions;
    }

    public void setSessions(List<SessionResponseDto> sessions){
        this.sessions = sessions;
    }
}
