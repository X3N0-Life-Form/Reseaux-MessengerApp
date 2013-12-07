package client.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import client.Client;

import common.Message;
import common.MessageType;
import common.handling.HandlingException;

public class UDPHandlerClientDiscuss extends UDPHandlerClient {
	
	public UDPHandlerClientDiscuss(Client client) throws SocketException {
		super(client);
	}

	@Override
	public void run(){
		int count=0;
		while(client.isRunning()) {
			try{
				String msg = client.getLogin() + " discuss with : " + client.getLoginotherclient() + " msg: " + count;
				Message msgClient = new Message(MessageType.MSG_DISCUSS_CLIENT);
				msgClient.addInfo("msg", msg);
				msgClient.addInfo("ip other client", client.getIpotherclient().getHostAddress());
				msgClient.addInfo("port other client", client.getPortotherclient() + "");
				messageManager.handleMessage(msgClient, socket);
				count++;
				Thread.sleep(2000);//TODO: mettre ça dans une constante
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (HandlingException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket) throws IOException, ClassNotFoundException {
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o = new ObjectOutputStream(b);
		    o.writeObject(message);
			byte[] buf = b.toByteArray();
			InetAddress ipotherclient = InetAddress.getByName(message.getInfo("ip other client"));
			int portotherclient = Integer.valueOf(message.getInfo("port other client"));
			DatagramPacket p = new DatagramPacket(buf, buf.length, ipotherclient, portotherclient);
			socket.send(p);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
