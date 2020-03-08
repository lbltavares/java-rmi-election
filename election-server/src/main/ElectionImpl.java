package main;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElectionImpl extends UnicastRemoteObject implements Election {

	private static final long serialVersionUID = 1L;

	// Hash map de candidados/numero de votos:
	private Map<String, Integer> candidatos;

	// HashSet para controlar os votantes:
	private Set<String> votantes;

	public ElectionImpl() throws RemoteException {

		candidatos = new HashMap<>();
		votantes = new HashSet<>();
 
		// Adiciona alguns candidatos:
		candidatos.put("Mônica", 0);
		candidatos.put("Cebolinha", 0);
		candidatos.put("Magali", 0);
		candidatos.put("Cascão", 0);
		candidatos.put("Chico Bento", 0);
	}

	@Override
	public List<String> candidates() throws RemoteException {
		return new ArrayList<String>(candidatos.keySet());
	}

	@Override
	public void vote(String nomeCandidato, String hashVotante) throws RemoteException {
		if (votantes.contains(hashVotante))
			throw new RemoteException("Esta pessoa já votou!");
		votantes.add(hashVotante);
		candidatos.put(nomeCandidato, result(nomeCandidato) + 1);
	}

	@Override
	public int result(String nomeCandidato) throws RemoteException {
		return candidatos.get(nomeCandidato);
	}

}
