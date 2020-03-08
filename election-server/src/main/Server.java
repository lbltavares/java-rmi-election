package main;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Server {

	public static void main(String[] args) {
		try {

			System.out.println("Iniciando Servidor...");

			ElectionImpl e = new ElectionImpl();

			Naming.rebind("rmi://localhost/Election", e);

			System.out.println("Servidor Iniciado.");

		} catch (RemoteException | MalformedURLException e1) {
			System.err.println("Ocorreu um erro: " + e1.getMessage());
		}

	}

}
