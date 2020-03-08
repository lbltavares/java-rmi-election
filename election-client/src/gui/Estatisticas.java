package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import main.Client;

// Dialogo de estatísticas de candidatos:
public class Estatisticas extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private Map<String, Integer> candidatos;
	private int totalVotos = 0;

	public Estatisticas(Frame base) throws RemoteException {
		super(base, "Estatísticas");
		setModal(true);
		candidatos = new HashMap<>();
		List<String> candList = Client.election.candidates();
		for (String c : candList) {
			int numVotos = Client.election.result(c);
			candidatos.put(c, numVotos);
		}
		setMinimumSize(new Dimension(400, 210));
		setPreferredSize(new Dimension(500, 250));
		setMaximumSize(new Dimension(600, 300));
		initComponents();
		pack();
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		JPanel root = new JPanel(new BorderLayout());
		root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		Border b1 = BorderFactory.createTitledBorder("Estatísticas:");
		Border b2 = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		panel.setBorder(BorderFactory.createCompoundBorder(b1, b2));
		root.add(panel, BorderLayout.CENTER);
		setContentPane(root);

		initTable(panel);

		panel.add(new JLabel("Total de Votos: " + totalVotos), BorderLayout.SOUTH);

	}

	// Inicia a tabela:
	private void initTable(JPanel panel) {
		String[] colunas = { "Candidato", "Votos", "(%)" };
		List<String[]> dataList = new ArrayList<>();
		candidatos.values().forEach(v -> totalVotos += v);
		for (Map.Entry<String, Integer> entry : candidatos.entrySet()) {
			int numVotos = entry.getValue();
			float perc = totalVotos > 0 ? (numVotos * 1.0f) / totalVotos : 0;
			String nome = entry.getKey();
			String[] row = { nome, numVotos + "", (perc * 100.0) + "" };
			dataList.add(row);
		}
		Object[][] dados = new Object[dataList.size()][];
		dados = dataList.toArray(dados);

		JTable table = new JTable(dados, colunas);
		table.setDefaultEditor(Object.class, null);

		Dimension d = table.getPreferredSize();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(d.width, table.getRowHeight() * (dataList.size() + 2) + 1));
		panel.add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

}
