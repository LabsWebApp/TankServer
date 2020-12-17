package proxy;

import java.util.UUID;

public class move extends base {
    public transient UUID id;
    transient short x = -1, y = -1;
    public int X() {
        return x;
    }
    public int Y() {
        return y;
    }
}
