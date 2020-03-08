package main;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Election extends Remote {

	// Retorna um Set de nomes de candidatos:
	List<String> candidates() throws RemoteException;

	// Realiza a votação:
	void vote(String nomeCandidato, String hashVotante) throws RemoteException;

	// Fornece o numero de votos de um candidato:
	int result(String nomeCandidato) throws RemoteException;

}
