package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CommunicatorService extends Remote {
    void registerUser(String var1, String var2) throws RemoteException;

    void addMessage(String var1, String var2, String var3, String var4) throws RemoteException;

    String getMessage(String var1, String var2) throws RemoteException;

    List<String> getUsers() throws RemoteException;
}
