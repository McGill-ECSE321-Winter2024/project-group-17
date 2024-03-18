import org.hibernate.annotations.TimeZoneStorage;

import ca.mcgill.ecse321.SportCenterManager.dao.ScheduleRepository;
import ca.mcgill.ecse321.SportCenterManager.dto.ScheduleRequestDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ScheduleIntegrationTests {
    @Autowired 
    private ScheduleRepository scheduleRepo;
    private final Time VALID_START_TIME = Time(1);
    private final Time VALID_END_TIME = Time(3600000);
    private final Time INVALID_START_TIME = null;
    private final Time INVALID_END_TIME = null;
    @Test 
    public void validScheduleUpdate(){
        //Set up
        ScheduleRequestDto request = new Schedule(VALID_START_TIME, VALID_END_TIME);
        
        //Act
        ResponseEntity<Schedule> response = client.putForEntity("/schedule/1",request,Schedule.class);
        
        //Assert

    }

    @Test 
    public void invalidScheduleUpdateStartEmpty(){

    }
    @Test 
    public void invalidScheduleUpdateEndEmpty(){

    }
    @Test 
    public void invalidScheduleUpdateBothEmpty(){

    }

    @Test
    public void invalidScheduleUpdateCombo(){

    }
    
}
