package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.Border;

import main.Client;
import main.Votante;

public class Janela extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// Tenta utilizar o Look-And-Feel Nimbus:
	private static void initLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (info.getClassName().equalsIgnoreCase("nimbus")) {
					UIManager.setLookAndFeel(info.getClassName());
					return;
				}
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private Votante votante;
	private List<String> candidatos;
	private JComboBox<String> candidatosCB;
	private JButton voteButton;

	public Janela(Votante votante, List<String> candidatos) {
		super("Ultimate Voting System");
		this.votante = votante;
		this.candidatos = candidatos;

		String[] candidatosArr = new String[this.candidatos.size()];
		candidatosArr = this.candidatos.toArray(candidatosArr);
		candidatosCB = new JComboBox<>(candidatosArr);

		initLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		initComponents();
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		JPanel panel = new JPanel();
		BoxLayout bl = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(bl);
		panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
		setContentPane(panel);

		Border borda = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Dimension gap = new Dimension(0, 20);
		panel.add(initHashPanel(borda));
		panel.add(Box.createRigidArea(gap));
		panel.add(initVotePanel(borda));
		panel.add(Box.createRigidArea(gap));
		panel.add(initControlPanel(borda));

	}

	// Painel do Hash do Usuário:
	private JPanel initHashPanel(Border borda) {
		JPanel hashPanel = new JPanel(new BorderLayout());
		JLabel hashLbl = new JLabel(votante.getId());
		hashLbl.setEnabled(false);
		Border titleBorder = BorderFactory.createTitledBorder("ID do Votante:");
		hashPanel.setBorder(BorderFactory.createCompoundBorder(titleBorder, borda));
		hashPanel.add(hashLbl, BorderLayout.CENTER);
		return hashPanel;
	}

	// Painel com dropdown de candidatos:
	private JPanel initVotePanel(Border borda) {
		JPanel votePanel = new JPanel(new BorderLayout(10, 0));
		Border titleBorder = BorderFactory.createTitledBorder("Escolha um Candidato:");
		votePanel.setBorder(BorderFactory.createCompoundBorder(titleBorder, borda));

		voteButton = new JButton("Votar");
		voteButton.setActionCommand("votar");
		voteButton.addActionListener(this);

		votePanel.add(candidatosCB, BorderLayout.CENTER);
		votePanel.add(voteButton, BorderLayout.EAST);

		return votePanel;
	}

	// Painel com botões "Votar" e "Estatísticas"
	private JPanel initControlPanel(Border borda) {
		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.setBorder(borda);

		JButton statsBtn = new JButton("Estatísticas");
		statsBtn.setActionCommand("estatisticas");
		statsBtn.addActionListener(this);

		controlPanel.add(statsBtn, BorderLayout.EAST);
		return controlPanel;
	}

	// Ação ao clicar em "Votar"
	private void votar() {
		String candidato = (String) candidatosCB.getSelectedItem();
		int opt = JOptionPane.showConfirmDialog(this, "Confirma a votação em " + candidato + "?", "Confirmar Votação",
				JOptionPane.YES_NO_OPTION);
		if (opt == JOptionPane.YES_OPTION) {
			int numTentativas = 3;
			for (int i = 1; i <= numTentativas; i++) {
				try {
					Client.election.vote(candidato, this.votante.getId());
					JOptionPane.showMessageDialog(this, "Seu voto foi computado para o(a) candidato(a) " + candidato);
					voteButton.setEnabled(false);
					candidatosCB.setEnabled(false);
					return;
				} catch (RemoteException e) {
					String msg = e.getLocalizedMessage();
					System.out.println(msg);
					if (e.getLocalizedMessage().startsWith("Connection refused")) {
						if (i == numTentativas) {
							StringBuilder sb = new StringBuilder();
							sb.append("<html>");
							sb.append("Foram realizadas " + numTentativas + " tentativas de conexão sem sucesso.");
							sb.append("<br>");
							sb.append("Tente novamente mais tarde");
							JOptionPane.showMessageDialog(this, sb.toString());
							return;
						}
					} else if (e.getLocalizedMessage().contains("Esta pessoa já votou")) {
						JOptionPane.showMessageDialog(this, "Esta pessoa já votou!");
						return;
					}
				}
			}
		}
	}

	// Ação ao clicar em "Estatísticas"
	private void estatisticas() {
		try {
			Estatisticas stats = new Estatisticas(this);
			stats.setVisible(true);
		} catch (RemoteException e) {
			JOptionPane.showMessageDialog(this, "Houve um erro ao abrir as estatísticas. Tente novamente mais tarde.");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("votar")) {
			votar();
		} else if (e.getActionCommand().equalsIgnoreCase("estatisticas")) {
			estatisticas();
		}
	}

}
