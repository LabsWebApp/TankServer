package server;

import gameZone.landshaft;
import gameZone.logging;
import gameZone.tank;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    // Организовать поиск свободного порта
    public static int localport = 6666;
    public static landshaft land = new landshaft();
    public static logging log = new logging(land);
    public static Map<UUID, inRun> clients = new HashMap<>();

    public static void main(String[] args){
        try {
            System.out.println("Старт... (port: " + localport + ")");
            ServerSocket ss = new ServerSocket(localport);
            while (true) {
                //System.out.println("Старт... ");
                Socket client = ss.accept();
                System.out.println("Пришёл игрок... ");
                inRun _client = new inRun(client);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
