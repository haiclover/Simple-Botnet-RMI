/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import SServer_package.SServer;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rmi.IBotnet;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 *
 * @author haiclover
 */
public class RMIClient {
    public static void main(String[] args){
        try {
            IBotnet botnet = new BotnetImpl();
            LocateRegistry.createRegistry(1234);
            Naming.bind("rmi://127.0.0.1:1234/BotnetRMI", botnet);
            System.out.println(">>>>>INFO: RMI Server started!!!!!!!!");
            SServer sserver = new SServer(2345);
            sserver.start();
            System.out.println(">>>>>INFO: Socket Server started!!!!!!!!");
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AlreadyBoundException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
