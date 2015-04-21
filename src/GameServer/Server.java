package GameServer;
/**Group Names: Tyler Glass, Michael House, Holly Ruyle, Phu Hoang.    
 * Project Part: Echo Server. 
 * Program Title: Tic-tac-toe Game. 
 * Course: CSCE 320 - Software Engineering.
 * Date: February 23, 2015.
 * Language and Compiler: Java written in eclipse and Netbeans.
 * Sources: CSCE 320 references - Trivial Java Example.
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable{

	// Variables declaration.
	private int port;
	private Thread worker;
	private ServerSocket ss;
	private ServerGUI view;
	ArrayList<Connection> list = new ArrayList<Connection>();
	private Authentication model;
	int ptpPort;
	// End of variables declaration.

	/**
	 * Default Constructor - Create the new server at assigned port.
	 * @throws IOException.
	 */
	public Server(int port) throws IOException{
		this.port = port;
		ss = new ServerSocket(port);
	} //end Server.

	/**
	 * Sends message to all Connection objects.
	 * @param msg - Message from the user.
	 * @throws IOException.
	 */
	public void broadcast(String msg) throws IOException{
		for(Connection c: list)			
			c.sendServerMsg(msg);

	} // end broadcast.

	/**
	 * Accepts connections and creates Connection object.
	 */
	@Override
	public void run() {
		view.serverTA.append("Server waiting for Clients on port " + port + ".\n");
		while(true){			
			try{
				Socket sockClient = ss.accept();
				view.serverTA.append("Client at " + sockClient.getInetAddress().getHostAddress() + " on port " + sockClient.getPort() + "\n");
				view.connectTF.setText(1+list.size()+"");
				Connection connect = new Connection(sockClient, this); //Creates a new Thread
				connect.setModel(model);
				connect.setIP(sockClient.getInetAddress().toString());
				ptpPort = connect.getPort() + 5; // 5 is to move away from taken port
				list.add(connect);
				connect.start();	
			} catch (IOException e) {
				e.printStackTrace();			
			}
		}
	} // end run.

	/**
	 * Remove the connection from the list if disconnected.
	 * @param connect 
	 */
	public void remove(Connection connect){
		//Remove all disconnected clients
		view.serverTA.append("Client at " + connect.getSocket().getInetAddress().getHostAddress() + " on port " + connect.getSocket().getPort() + " disconnected\n");
		list.remove(connect);				
		view.connectTF.setText(list.size()+"");
	}// end remove.

	/**
	 * Set the GUI display.
	 * @param view
	 */
	public void setView(ServerGUI view){
		this.view = view;
	} //end setView.

	/**
	 * Calls run() in the new Thread.
	 */
	public void listen(){
		worker = new Thread(this);
		worker.start(); 
	} // end listen.

	/**
	 * Set the model into the Connection.
	 * @param model Authentication.
	 */
	public void setModel(Authentication model){
		this.model = model;
	}// end setModel.

	/**
	 * Checks the exist connection - online user inside the ArrayList of Connection.
	 * Return IP address of the online user who connected to the server.
	 * @param user - userName.
	 * @return returns String - IP of the userName.
	 */
	public String getOnlineUserIp(String user){ 
		//return model.onlineUser.get(user);
		String ip = "";
		for(Connection c: list)
			if(c.getUserName().equals(user)){
				ip = c.getIP();
				break;
			}
		return ip;
	}// end getOnlineUserIp.

	/**
	 * Return the list of connected online user.
	 * @return returns String - list of online user. 
	 */
	public String getOnlineUserList(){
		String userList = "List_";
		for(Connection c: list)
			userList += c.getUserName() + "_";
		return userList;
	}// end getOnlineUserList.

	/*
	 * Comment: this methos seems doing nothing
	 * please check to modify or remove
	 * 
	 * 
	public void setUserIP(){


	}
	*/


	/**
	 * Checks the user if online and connected to the server.
	 * Go through the ArrayList of Connection and check the user name.
	 * @param userName - player user name.
	 * @return returns true if  the user is online, otherwise returns false.
	 */
	boolean checkOnlineUser(String userName) {
		//  HashMap<String, String> online = getOnlineUser();
		for(Connection c: list)
			if(c.getUserName().equals(userName))
				//return online.containsKey(SendTo);
				return true; //for now ,
		return false;
	} // end checkOnlineUser.


}//end class Server
