package server;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CommunicatorServiceImpl implements CommunicatorService {
    private Map<String, String> usersCollection = new HashMap<String, String>();
    private Map<String, BlockingQueue<String>> messagesCollection = new HashMap();

    CommunicatorServiceImpl() {
        this.usersCollection = Collections.synchronizedMap(usersCollection);
        this.messagesCollection = Collections.synchronizedMap(messagesCollection);
    }

    protected String hashPassword(String password){
        return Integer.toString(password.hashCode());
    }

    @Override
    public void registerUser(String s, String s1) throws RemoteException {
        this.usersCollection.computeIfAbsent(s, (k) -> {
            this.messagesCollection.put(s, new LinkedBlockingQueue());
            return this.hashPassword(s1);
        });
    }

    @Override
    public void addMessage(String s, String s1, String s2, String s3) throws RemoteException {
        if (usersCollection.containsKey(s2)) {
            if (usersCollection.get(s).equals(hashPassword(s1))) {
                String newMessage = s + ";" + s3;
                messagesCollection.computeIfAbsent(s2, k -> new LinkedBlockingQueue<>()).add(newMessage);
            }
        }else{
            throw new RemoteException("Wrong username!");
        }
    }

    @Override
    public String getMessage(String s, String s1) throws RemoteException {
        try {
            if (usersCollection.containsKey(s)) {
                if (usersCollection.get(s).equals(hashPassword(s1))) {
                    BlockingQueue<String> q = (BlockingQueue) messagesCollection.get(s);
                    if (q != null) {
                        return (String) q.poll(2L, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (InterruptedException e) {

        }
        return null;
    }

    @Override
    public List<String> getUsers() throws RemoteException {
        return new ArrayList<>(this.usersCollection.keySet());
    }
}
