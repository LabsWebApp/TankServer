package server;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

import gameZone.logging;
import gameZone.tank;
import proxy.base;
import proxy.clientIn;
import proxy.move;
import proxy.proxyTank;

public class inRun extends Thread{

    Socket client;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private UUID id;

    public inRun(Socket client) throws IOException {
        this.client = client;
        out = new ObjectOutputStream(client.getOutputStream());
        System.out.println("Игрок пытается отправить 0");
        in = new ObjectInputStream(client.getInputStream());
        System.out.println("Игрок пытается отправить 1");
        start();
    }

    @Override
    public void run() {
        try {
            clientIn cin  = (clientIn)in.readObject();

            id = cin.getId();
            if (Server.clients.containsKey(id)){
                Server.clients.remove(id);
                Server.clients.get(id).down();
            }
            else {
                boolean newTank = !Server.land.hasTank(id);
                proxyTank pt = new proxyTank(id, Server.land.newTank(id), true);
                send(pt);

                if (newTank) {
                    pt = new proxyTank(pt);
                    for (Map.Entry<UUID, inRun> entry : Server.clients.entrySet()) {
                        if (entry.getKey() == id) continue;
                        entry.getValue().send(pt);
                    }
                }
                for (proxyTank t : Server.land.tanks(id)) {
                    send(t);
                }
            }

            Server.clients.put(id, this);
            Server.log.log(logging.info.PLAYER_IN, id);
            try {
                while (true) {
                        try {
                            Object obj = in.readObject();
                            base msg = (base) obj;

                            switch (msg.type){
                                case base.MOVE:
                                    move t = (move)obj;
                                    t.id = id;
                                    synchronized (Server.land){
                                        if(Server.land.move(t, id)){
                                           // System.out.println(t.X() + " OK  ");
                                            Server.clients.forEach((k, v) -> v.send(t));
                                        }
                                    }
                                    Server.log.log(logging.info.MOVE, id);
                                    break;
                                default:
                                    throw new ClassNotFoundException("Класс для посыла не найден");
                            }
//обработка сообщений
                        }
                        catch (ClassNotFoundException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                    }
                }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        catch (NullPointerException ignored) {}
        catch (IOException | ClassNotFoundException e) {
            this.down();
        }
    }

    public void down() {
        try {
            if(!client.isClosed()) {
                client.close();
                in.close();
                out.close();
                this.interrupt();
            }
        } catch (IOException ignored) {}
        finally {
            if(id != null && Server.clients.containsKey(id)) Server.clients.remove(id);
        }
    }

    public void send (base obj) {
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException ignored) {Server.clients.remove(this); }
    }
}
