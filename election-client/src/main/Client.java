package main;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import gui.Janela;

public class Client {

	// Interface remota:
	public static Election election;

	public static void main(String[] args) {
		try {

			// Obtem o endereço do servidor:
			String end = JOptionPane.showInputDialog("Insira o Endereço do Servidor", "rmi://localhost/Election");

			// Inicia a conexão:
			System.out.println("Iniciando Client...");
			election = (Election) Naming.lookup(end);
			System.out.println("Cliente iniciado.");

			List<String> candidatos = election.candidates();

			// Inicia a Interface Gráfica:
			SwingUtilities.invokeLater(() -> {

				Votante votante = new Votante(UUID.randomUUID().toString());
				Janela janela = new Janela(votante, candidatos);
				janela.setVisible(true);

			});

		} catch (MalformedURLException ex1) {
			JOptionPane.showMessageDialog(null, "URL Inválida! O Cliente será finalizado.");

		} catch (RemoteException | NotBoundException ex2) {
			System.err.println(ex2.getMessage());
			String msg = "<html>Houve um erro durante a conexão.<br>Consulte o log no Console para mais informações.</html>";
			JOptionPane.showMessageDialog(null, msg);
		}
	}

}
