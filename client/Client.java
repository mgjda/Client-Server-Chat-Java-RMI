package client;

import server.CommunicatorService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class Client {

    CommunicatorService service;

    private String userName;
    private String userPassword;

    public Client(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;

        String name = "komunikator";
        try {
            Registry registry = LocateRegistry.getRegistry(4444);
            service = (CommunicatorService) registry.lookup(name);
            System.out.println("Hello " + userName + " on chat. Type !h for help.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void start() {
        try {
            service.registerUser(userName, userPassword);
            Thread getMessageThread = new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        String message = service.getMessage(userName, userPassword);
                        if (!(message == null)) {
                            String[] messageArray = message.split(";");
                            String senderName = messageArray[0];
                            message = messageArray[1];
                            System.out.println(senderName + " says: " + message);
                            System.out.print(": ");
                        }
                    }
                } catch (RemoteException e) {
                    System.out.println("Server " +e.getMessage());
                }
            });
            getMessageThread.start();

            Thread sendMessageThread = new Thread(() -> {
                try {
                    Scanner scan = new Scanner(System.in);
                    while (!Thread.interrupted()) {
                        System.out.print(": ");
                        String message = scan.nextLine();
                        if (message.equals("!users") ) {
                            List<String> listOfUsers;
                            listOfUsers = service.getUsers();
                            System.out.println("SERVER: Online users:");
                            for (Object user:listOfUsers) {
                                System.out.println(user);
                            }
                        }
                        else if(message.equals("!h")){
                            System.out.println("SERVER: A message must follow the pattern: addresseeName;message" +
                                    "\nCommand List:" +
                                    "\n!users - shows online users");
                        }
                        else {
                            String[] messageArray = message.split(";");
                            if (messageArray.length == 2) {
                                String addresseeName = messageArray[0];
                                message = messageArray[1];
                                service.addMessage(userName, userPassword, addresseeName, message);
                            } else {
                                System.out.println("SERVER: The message must follow the pattern: addresseeName;message");
                            }
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
            sendMessageThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
