package client.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.Vector;

import view.ChatPanel;

import client.Client;

import common.CommonConstants;
import common.Message;
import common.MessageInfoStrings;
import common.handling.HandlingException;
import common.logging.EventType;

/**
 * UDPHandler dedicated to listening for incoming UDP packets.
 * Technically capable of sending messages back, this is discouraged, favor the use
 * of a dedicated sender class instead.
 * @author etudiant
 *
 */
public class UDPHandlerClientListening extends UDPHandlerClient {
	
	/**
	 * Constructs a UDPHandlerClientListening object for the specified {@link Client}.
	 * @param client
	 * @throws SocketException
	 */
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
				case MSG_DISCUSS_CLIENT_SEVERAL:
					System.out.println("J'ai reçus un message");
					@SuppressWarnings("unchecked")
					Vector<String> listLoginOtherClient = new Vector<String>((Vector<String>) message.getObject("list login other client"));
					System.out.println("dont la liste des logins est la suivante: "+listLoginOtherClient);
					listLoginOtherClient.remove(client.getLogin());
					listLoginOtherClient.add(message.getInfo("login client origin"));
					Collections.sort(listLoginOtherClient);
					if(client.getContactListController().getClw().getMapListChat().containsKey(listLoginOtherClient)) {
						ChatPanel p1 = client.getContactListController().getClw().getMapListChat().get(listLoginOtherClient);
						p1.addText(message.getInfo("msg"), Integer.parseInt(message.getInfo(MessageInfoStrings.MESSAGE_ID)), message.getInfo("login client origin"));
						p1.getFrame().toFront();
					}else {
						ChatPanel p2 = new ChatPanel(listLoginOtherClient, client.getContactListController().getClw().getMapListChat(), client.getContactListController());
						client.getContactListController().getClw().getMapListChat().put(listLoginOtherClient, p2);
						try {
							p2.lancerAffichage();
							p2.addText(message.getInfo("msg"), Integer.parseInt(message.getInfo(MessageInfoStrings.MESSAGE_ID)), message.getInfo("login client origin"));
							p2.getFrame().toFront();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					break;
				case MSG_DISCUSS_CLIENT:
					if (client.getContactListController().getClw().getDiscMap().containsKey(message.getInfo("login client origin"))) {
						ChatPanel p1 = client.getContactListController().getClw().getDiscMap().get(message.getInfo("login client origin"));
						p1.addText(message.getInfo("msg"), Integer.parseInt(message.getInfo(MessageInfoStrings.MESSAGE_ID)), message.getInfo("login client origin"));
						p1.getFrame().toFront();
					} else {
						ChatPanel p2 = new ChatPanel(message.getInfo("login client origin"), client.getContactListController().getClw().getDiscMap(), client.getContactListController());
						client.getContactListController().getClw().getDiscMap().put(message.getInfo("login client origin"),p2);
						try {
							p2.lancerAffichage();
							p2.addText(message.getInfo("msg"), Integer.parseInt(message.getInfo(MessageInfoStrings.MESSAGE_ID)), message.getInfo("login client origin"));
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
			DatagramPacket p = new DatagramPacket(buf, buf.length, ad, client.getServerPort());
			
			socket.send(p);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
