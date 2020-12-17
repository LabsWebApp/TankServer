package gameZone;

import proxy.move;
import proxy.proxyTank;

import java.awt.*;
import java.util.*;
import java.util.List;

public class landshaft {
    public final int w = 800, h = 600;
    final Rectangle field = new Rectangle(w, h);

    Map<UUID, tank> tanks;

    public boolean hasTank(UUID id){ return tanks.containsKey(id);}

    public landshaft(){
        tanks = new HashMap<>();
    }

    public tank newTank(UUID id){
        if(tanks.containsKey(id))
            return tanks.get(id);
        tank res = new tank();
        tanks.put(id, res);
        return res;
    }

    public List<proxyTank> tanks(UUID id){
        List<proxyTank> res = new ArrayList<>();
        tanks.forEach((k, v) ->{
            if(k != id) res.add(new proxyTank(k, v,false));
        });
        return res;
    }

    public boolean move(move m, UUID id) {
        int x = m.X(), y = m.Y();
        if (x < 0 && y < 0)
            return false;
        Rectangle proxy = new Rectangle(tanks.get(id).getSizePlace());
        if (x < 0) x = proxy.x;
        if (y < 0) y = proxy.y;
        proxy.setLocation(x, y);
        if (!field.contains(proxy)) return false;

        for (Map.Entry<UUID, tank> entry : tanks.entrySet()) {
            if (entry.getKey() != id && entry.getValue().getSizePlace().intersects(proxy)) {
                return false;
            }
        }
        tanks.get(id).move(x, y);
        return true;
    }
   // private void sendMove(UUID tank) {
   // }


    @Override
    public String toString() {
        String res = "Поле " + w + " на " + h + "\n\n";
        if(tanks.size()==0)
            return res + "в ожидании игроков...";
        res = "В игре танков: " + tanks.size() + "\n";
        for (Map.Entry<UUID, tank> entry : tanks.entrySet()) {
            entry.getValue().toString(entry.getKey());
        }
        return res;
    }

    public String toString(UUID id){
        if(tanks.containsKey(id))
            return tanks.get(id).toString();
        return "У игрока /" + id + "\" нет танка";
    }
}
