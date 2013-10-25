package serveur.handling;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import serveur.Serveur;

import commun.Message;

public interface Handler {

	public void sendMessage(Message message, Socket socket) throws IOException, HandlingException;
	public void sendMessage(Message message, DatagramSocket socket) throws HandlingException;
	public Serveur getServeur();

}
