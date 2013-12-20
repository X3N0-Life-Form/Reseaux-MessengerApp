package client.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

import view.ChatPanel;
import client.Client;
import common.Message;
import common.MessageInfoStrings;
import common.MessageType;
import common.handling.HandlingException;

/**
 * UDPHandler capable of both receiving and sending messages to other clients.
 * @author etudiant
 *
 */
public class UDPHandlerClientDiscuss extends UDPHandlerClient {
	
	/**
	 * Constructs a UDPHandlerClientDiscuss for the specified {@link Client}.
	 * @param client - Client
	 * @throws SocketException
	 */
	public UDPHandlerClientDiscuss(Client client) throws SocketException {
		super(client);
	}

	/**
	 * Sends Message to multiple clients.
	 * @param text
	 * @param listLoginOtherClient
	 * @param msgCount
	 * @return false if the message could not be send to everyone.
	 */
	public boolean run(String text, Vector<String> listLoginOtherClient, ChatPanel panel, int msgCount) {
		boolean status = true;
		if(client.isRunning()){
			System.out.println("la liste des autres client a qui envoyé le msg: "+listLoginOtherClient);
			for(String loginOtherClient : listLoginOtherClient) {
				if (client.getClientIps().get(loginOtherClient) != null) {
					try{		
						System.out.println("j'envois au login : "+loginOtherClient);
						Message msgClient = new Message(MessageType.MSG_DISCUSS_CLIENT_SEVERAL);
						msgClient.addInfo("msg", text);
						msgClient.addInfo("login client origin", client.getLogin());
						msgClient.addObject("list login other client", listLoginOtherClient);
						msgClient.addInfo("ip other client", client.getClientIps().get(loginOtherClient).getHostAddress());
						msgClient.addInfo("port other client", client.getClientPorts().get(loginOtherClient) + "");
						msgClient.addInfo(MessageInfoStrings.MESSAGE_ID, msgCount + "");
						getMessageManager().handleMessage(msgClient, socket);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (HandlingException e) {
						e.printStackTrace();
					}
					// status stays true
				} else {
					panel.displayDisconnectedMessage(loginOtherClient);
					status = false;
				}
			}
			return status;
		}
		return false; // client wasn't running
	}
	
	/**
	 * Sends a message to a single client.
	 * @param text
	 * @param loginOtherClient
	 * @param msgCount
	 * @return false if the message could not be send.
	 */
	public boolean run(String text, String loginOtherClient, ChatPanel panel, int msgCount) {
		if(client.isRunning()) {
			if (client.getClientIps().get(loginOtherClient) != null) {
				try{
					Message msgClient = new Message(MessageType.MSG_DISCUSS_CLIENT);
					msgClient.addInfo("msg", text);
					msgClient.addInfo("ip other client", client.getClientIps().get(loginOtherClient).getHostAddress());
					msgClient.addInfo("port other client", client.getClientPorts().get(loginOtherClient) + "");
					msgClient.addInfo("login client origin", client.getLogin());
					msgClient.addInfo(MessageInfoStrings.MESSAGE_ID, msgCount + "");
					getMessageManager().handleMessage(msgClient, socket);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (HandlingException e) {
					e.printStackTrace();
				}
				return true;
			} else {
				panel.displayDisconnectedMessage(loginOtherClient);
				return false;
			}
		}
		return false; // client wasn't running
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

	public void run(MessageType missingMsg, int lostMsg, String otherLogin) {
		if(client.isRunning()) {
			 try{
				Message msgMissing = new Message(missingMsg);
				msgMissing.addInfo(MessageInfoStrings.MESSAGE_ID, lostMsg + "");
				
				msgMissing.addInfo("ip other client", client.getClientIps().get(otherLogin).getHostAddress());
				msgMissing.addInfo("port other client", client.getClientPorts().get(otherLogin) + "");
				msgMissing.addInfo("login client origin", client.getLogin());
				
				getMessageManager().handleMessage(msgMissing, socket);
			 } catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (HandlingException e) {
					e.printStackTrace();
				}
			}
	}

}
