package server;

//import rmi.komunikatorSimple.CommunicatorService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    public static void main(String[] args) {
        try {
            String name = "komunikator";
            CommunicatorService server = new CommunicatorServiceImpl();
            CommunicatorService stub = (CommunicatorService) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(4444);
            //Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Server runs on port:4444");
        } catch(Exception e) {
            System.err.println("Connection error:");
            e.printStackTrace();
        }
    }
}
