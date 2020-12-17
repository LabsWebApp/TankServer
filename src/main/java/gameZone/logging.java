package gameZone;

import java.util.Date;
import java.util.UUID;

public class logging {
    public enum info{
        INFO, PLAYER_IN, MOVE, SHUT, SHUT_IN
    }

    private landshaft land;
    public logging(landshaft land){this.land = land;}

    public void log(info inf, UUID id){
        String report = "[" + new Date() +"]\n" + "Событие: " + inf + "\n";

        if(id==null || inf == info.INFO)
            System.out.println(report + land.toString());
        else
            System.out.println(report + land.toString(id));
    }
    public void log(info inf){
        log(inf, null);
    }
}
