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
			for(String loginOtherClient : listLoginOtherClient) {
				if (client.getClientIps().get(loginOtherClient) != null) {
					try{		
						Message msgClient = new Message(MessageType.MSG_DISCUSS_CLIENT_SEVERAL);
						msgClient.addInfo(MessageInfoStrings.MSG, text);
						msgClient.addInfo(MessageInfoStrings.LOGIN_CLIENT_ORIGIN, client.getLogin());
						msgClient.addObject(MessageInfoStrings.LIST_LOGIN_OTHER_CLIENT, listLoginOtherClient);
						msgClient.addInfo(MessageInfoStrings.IP_OTHER_CLIENT, client.getClientIps().get(loginOtherClient).getHostAddress());
						msgClient.addInfo(MessageInfoStrings.PORT_OTHER_CLIENT, client.getClientPorts().get(loginOtherClient) + "");
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
					msgClient.addInfo(MessageInfoStrings.MSG, text);
					msgClient.addInfo(MessageInfoStrings.IP_OTHER_CLIENT, client.getClientIps().get(loginOtherClient).getHostAddress());
					msgClient.addInfo(MessageInfoStrings.PORT_OTHER_CLIENT, client.getClientPorts().get(loginOtherClient) + "");
					msgClient.addInfo(MessageInfoStrings.LOGIN_CLIENT_ORIGIN, client.getLogin());
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
			InetAddress ipotherclient = InetAddress.getByName(message.getInfo(MessageInfoStrings.IP_OTHER_CLIENT));
			int portotherclient = Integer.valueOf(message.getInfo(MessageInfoStrings.PORT_OTHER_CLIENT));
			DatagramPacket p = new DatagramPacket(buf, buf.length, ipotherclient, portotherclient);
			socket.send(p);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends a Message signaling that a message was missed.
	 * @param missingMsg
	 * @param lostMsg
	 * @param otherLogin
	 */
	public void run(MessageType missingMsg, int lostMsg, String otherLogin) {
		if(client.isRunning()) {
			 try{
				Message msgMissing = new Message(missingMsg);
				msgMissing.addInfo(MessageInfoStrings.MESSAGE_ID, lostMsg + "");
				
				msgMissing.addInfo(MessageInfoStrings.IP_OTHER_CLIENT, client.getClientIps().get(otherLogin).getHostAddress());
				msgMissing.addInfo(MessageInfoStrings.PORT_OTHER_CLIENT, client.getClientPorts().get(otherLogin) + "");
				msgMissing.addInfo(MessageInfoStrings.LOGIN_CLIENT_ORIGIN, client.getLogin());
				
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
