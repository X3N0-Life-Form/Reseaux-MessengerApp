package client.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import view.ChatPanel;

import client.Client;

import common.CommonConstants;
import common.Message;
import common.MessageInfoStrings;
import common.handling.HandlingException;
import common.logging.EventType;

public class UDPHandlerClientListening extends UDPHandlerClient {
	
	public UDPHandlerClientListening(Client client) throws SocketException{
		super(client);
		client.setUDPMainListeningPort(socket.getLocalPort());
		log.log(EventType.INFO, "UDP port set to " + client.getUDPMainListeningPort());
	}
	
	@Override
	public void run(){
		while(client.isRunning()) {
			byte[] buf = new byte[CommonConstants.UDP_PACKET_SIZE];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(p);
				Message message = getMessage(p);
				switch (message.getType()) {
				case MSG_DISCUSS_CLIENT:
					if (client.getContactListController().getClw().getDiscMap().containsKey(message.getInfo("login client origin"))) {
						ChatPanel p1 = client.getContactListController().getClw().getDiscMap().get(message.getInfo("login client origin"));
						p1.addTexte(message.getInfo("msg"), Integer.parseInt(message.getInfo(MessageInfoStrings.MESSAGE_ID)), message.getInfo("login client origin"));
						p1.getFrame().toFront();
					} else {
						ChatPanel p2 = new ChatPanel(message.getInfo("login client origin"), client.getContactListController().getClw().getDiscMap(), client.getContactListController());
						client.getContactListController().getClw().getDiscMap().put(message.getInfo("login client origin"),p2);
						try {
							p2.lancerAffichage();
							p2.addTexte(message.getInfo("msg"), Integer.parseInt(message.getInfo(MessageInfoStrings.MESSAGE_ID)), message.getInfo("login client origin"));
							p2.getFrame().toFront();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					break;
					
				case MISSING_MSG:
					ChatPanel cp = client.getContactListController().getClw().getDiscMap().get(message.getInfo("login client origin"));
					int lostMsg = Integer.parseInt(message.getInfo(MessageInfoStrings.MESSAGE_ID));
					cp.displayMissingMessage(lostMsg);
					break;
				default:
					messageManager.handleMessage(message, socket);
					break;
			}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (HandlingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket) throws IOException, ClassNotFoundException {
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o = new ObjectOutputStream(b);
		    o.writeObject(message.getObjects());
			byte[] buf = b.toByteArray();
			InetAddress ad = InetAddress.getByName(client.getServerIp());
			//DatagramPacket p = new DatagramPacket(buf, buf.length, ad, client.getPort());
			DatagramPacket p = new DatagramPacket(buf, buf.length, ad, 8001);
			
			socket.send(p);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
