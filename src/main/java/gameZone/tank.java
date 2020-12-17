package gameZone;

import java.awt.*;
import java.util.UUID;

public class tank {
    public final Rectangle stnd = new Rectangle(-8, -4, 8,4 );

    private Rectangle sizePlace = new Rectangle(stnd);

    private byte health = 100;
    public byte getHealth(){
        return health;
    }
    //private byte power = 1;
    private byte ammo = 100;
    public byte getAmmo(){
        return ammo;
    }

    public Rectangle getSizePlace() {
        return new Rectangle(sizePlace.width, sizePlace.width, sizePlace.x, sizePlace.y);
    }

    public String toString(UUID id) {
        String res = "Танк\n";
        String pos = sizePlace.x< 0 ? "стартовая" :
                "Х = " + sizePlace.x  + ", Y = " + sizePlace.y;
        if (id!=null)
            res = "\t" + res + " игрок id=\"" + id + ";\"\n";
        return res + "\tпозиция: " + pos +
                "; размер " + sizePlace.width + " на " + sizePlace.height +
                "\n\tздоровье " + health + "%; " + "; боеприпасы:" + ammo;
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public void move(int x, int y){
        sizePlace.setLocation(x, y);
    }
}
