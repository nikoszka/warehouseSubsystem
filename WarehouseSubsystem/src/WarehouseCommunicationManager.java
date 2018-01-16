import java.awt.SecondaryLoop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WarehouseCommunicationManager {

	// entry method of the Application

	private DatabaseController controller;

	// adress of the server
	private final String serverAddress = "localhost";
	private final int port = 9093;

	// Objects for communicating with the server
	private Socket s;
	private BufferedReader input;
	private PrintWriter output;

	public WarehouseCommunicationManager() {
		// TODO Auto-generated constructor stub

		System.out.println("initializing communication connection");
		try {
			s = new Socket(serverAddress, port);
			input = new BufferedReader(new InputStreamReader(s.getInputStream()));
			output = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("connection established");
		System.out.println("starting to listen to messages");
		startListening();
	}

	// listens to incoming messages and delegates them to the recieveMessage method
	private void startListening() {
		new Thread(){
            @Override
            public void run() {
            	while (true) {
        			String message = null;
        			try {
        				message = input.readLine();
        			} catch (IOException e) {
        				e.printStackTrace();
        				System.exit(0);
        			}

        			recieveMessage(message);
        		}            	
            }
		}.start();
	}

	private void recieveMessage(String message) {
		// decides what to do with the incoming message
		System.out.println("message recieved: " + message);
		String[] msgSplit = message.split(";");
		if (msgSplit.length < 2) {
			System.out.println("invalid message: " + message);
			return;
		}
		String sender = msgSplit[0];
		String command = msgSplit[1];
		String params = "";
		if (msgSplit.length > 2)
			params = message.substring(sender.length() + command.length() + 2);

		switch (command) {
		case "tookMaterial": {
			String[] split = params.split(";");
			split[0] = split[0];
			String itemName = split[0];
			String amount = split[1];
			controller.updateSupplies(Integer.valueOf(amount), itemName);
			break;
		}
		}
	}

	public void arrangeNeeded() {
		sendMessage("Warehouse;NeedArrange");
	}

	public void orderMaterial(String itemName, int amount) {
		sendMessage("Warehouse;OrderMaterial" + itemName + amount);
	}

	// general method for sending a message to the server
	private void sendMessage(String str) {
		output.println(str);
		System.out.println("message sent; " + str);
	}

}
