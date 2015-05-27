package com.querybyexample.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListDataListener;

import net.miginfocom.swing.MigLayout;

import com.querybyexample.functionality.DatabasesAvailable;
import com.querybyexample.functionality.QueryByExampleAPI;
import com.querybyexample.functionality.QueryData;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;

public class WIndowMain {

	private JPanel panel;
	private JLabel conStatus;
	private JTabbedPane tabbedPane;
	private JComboBox<Object> conDB;
	private JFrame frame;
	private JTextField username;
	private JPasswordField password;
	private JTextField server;
	private JTextField port;
	private JLabel lblOracleSid;
	private JTextField oracleSID;

	private JPanel panel_1;
	private JLabel label;
	private JTextField queryTextField;
	private JComboBox<String> schemas;
	private JList<String> tableList;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JScrollPane scrollPane;
	private JList<?> columnLIst;
	private JButton btnQuery;
	private JTable table;
	private DefaultTableModel tablemodel;
	QueryByExampleAPI api = null;
	boolean conFieldsCheck = false;
	private DefaultListModel<String> modelList;
	private boolean collsSelected = false;
	private Object[] ColsSelected;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WIndowMain window = new WIndowMain();
					window.api = new QueryByExampleAPI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WIndowMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 523);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
		tabbedPane.addTab("Connection", null, panel, null);
		panel.setLayout(new MigLayout("", "[][][277.00,grow]",
				"[][][][][][][][][]"));

		conStatus = new JLabel("");
		conStatus.setLabelFor(panel);
		conStatus.setToolTipText("Status Label !");
		panel.add(conStatus, "cell 2 0");

		JLabel lblSelectDatabase = new JLabel("Select Database: ");
		panel.add(lblSelectDatabase, "cell 1 1,alignx trailing");

		conDB = new JComboBox<Object>();
		conDB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				conDBItemStateChnaged(e);
			}

		});
		conDB.setToolTipText("Database to be used !");
		conDB.setModel(new DefaultComboBoxModel<Object>(new String[] {
				"--Please Select Database--", "MYSQL", "ORACLE" }));
		panel.add(conDB, "cell 2 1,growx");

		JLabel lblUserName = new JLabel("User Name: ");
		panel.add(lblUserName, "cell 1 2,alignx trailing");

		username = new JTextField();
		username.setToolTipText("Insert User Name !");
		panel.add(username, "cell 2 2,growx");
		username.setColumns(10);

		JLabel lblPassword = new JLabel("Password: ");
		panel.add(lblPassword, "cell 1 3,alignx trailing");

		password = new JPasswordField();
		password.setToolTipText("Please Insert Password !");
		password.setEchoChar('*');
		panel.add(password, "cell 2 3,growx");

		JLabel lblServerAddress = new JLabel("Server Address: ");
		panel.add(lblServerAddress, "cell 1 4,alignx trailing");

		server = new JTextField();
		server.setToolTipText("Please Insert Server Address !");
		panel.add(server, "cell 2 4,growx");
		server.setColumns(10);

		JLabel lblPort = new JLabel("Port: ");
		panel.add(lblPort, "cell 1 5,alignx trailing");

		port = new JTextField();
		port.setToolTipText("Please Insert Port Number !");
		panel.add(port, "cell 2 5,growx");
		port.setColumns(10);

		lblOracleSid = new JLabel("Oracle SID: ");
		lblOracleSid.setVisible(false);
		panel.add(lblOracleSid, "cell 1 6,alignx trailing");

		oracleSID = new JTextField();
		oracleSID.setToolTipText("Please Insert SID !");
		oracleSID.setVisible(false);
		panel.add(oracleSID, "cell 2 6,growx");
		oracleSID.setColumns(10);

		JButton btnConnect = new JButton("Connect !");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectionButtonActionPerformed(e);
			}
		});
		btnConnect.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(btnConnect, "cell 2 7,alignx center");

		panel_1 = new JPanel();
		// make tab 2 hidden
		//tabbedPane.addTab("Query by Example", null, panel_1, null);

		panel_1.setLayout(new MigLayout(
				"",
				"[][163.00,grow][30.00][][21.00][-21.00][-26.00][-33.00][-29.00][-40.00][350.00,grow][][][][][][][][103.00,grow][][]",
				"[][][grow]"));

		schemas = new JComboBox();
		schemas.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				schemasItemStateChanged(e);
			}
		});
		schemas.setToolTipText("Tablespaces");
		panel_1.add(schemas, "cell 1 0,growx");

		label = new JLabel(".");
		label.setFont(new Font("Dialog", Font.BOLD, 50));
		label.setToolTipText("Connection  Status !");
		panel_1.add(label, "cell 20 0");

		JLabel lblQueryByExample = new JLabel("Query By Example");
		lblQueryByExample.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblQueryByExample, "cell 10 0,alignx center,aligny center");

		JLabel lblTables = new JLabel("Tables");
		panel_1.add(lblTables, "cell 1 1");

		queryTextField = new JTextField();
		queryTextField.setToolTipText("Query");
		queryTextField.setEditable(false);
		panel_1.add(queryTextField, "flowx,cell 10 1,alignx center");
		queryTextField.setColumns(10);

		JLabel lblColumns = new JLabel("Columns");
		panel_1.add(lblColumns, "cell 18 1");

		scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "cell 1 2,grow");

		tableList = new JList();
		tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				tableListValueChanged(e);
			}
		});
		scrollPane.setViewportView(tableList);

		scrollPane_2 = new JScrollPane();
		panel_1.add(scrollPane_2, "cell 4 2 9 1,grow");

		table = new JTable();
		scrollPane_2.setViewportView(table);

		scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, "cell 15 2 6 1,grow");

		columnLIst = new JList();

		columnLIst.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				columnValueChanged(e);
			}
		});
		columnLIst.setSelectionModel(new DefaultListSelectionModel() {
			@Override
			public void setSelectionInterval(int index0, int index1) {
				if (super.isSelectedIndex(index0)) {
					super.removeSelectionInterval(index0, index1);
				} else {
					super.addSelectionInterval(index0, index1);
				}
			}

			@Override
			public void addSelectionInterval(int index0, int index1) {
				if (index0 == index1) {
					if (isSelectedIndex(index0)) {
						removeSelectionInterval(index0, index0);
						return;
					}
					super.addSelectionInterval(index0, index1);
				}
			}

		});
		scrollPane_1.setViewportView(columnLIst);

		btnQuery = new JButton("Query..");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QueryActionPerformed(e);
			}
		});
		btnQuery.setHorizontalAlignment(SwingConstants.RIGHT);
		btnQuery.setEnabled(false);
		panel_1.add(btnQuery, "cell 10 1,alignx right");

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnAbout = new JMenu("Help");
		menuBar.add(mnAbout);

		JMenuItem mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Component dialog = null;
				JOptionPane.showMessageDialog(dialog,
						"Query By Example \n \u00a9 spectral369 \n"
								+ "Version 1.0");
			}
		});
		mnAbout.add(mntmAbout);

	}

	protected void connectionButtonActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean checkInfo = false;
		conStatus.setForeground(Color.red);
		if (conDB.getSelectedIndex() == 0)
			conStatus.setText("Please Select a Database");

		if (checkInfo == false) {
			if (conDB.getSelectedIndex() == 0)
				conStatus.setText("Please Select a Database");
			else if (username.getText().isEmpty())
				conStatus.setText("User not set !");
			else if (password.getPassword().length < 1)
				conStatus.setText("Password not set");
			else if (server.getText().isEmpty())
				conStatus.setText("Server address  not set");
			else if (port.getText().isEmpty())
				conStatus.setText("Port not set");
			else if (!port.getText().isEmpty()) {
				try {
					Integer.parseInt(port.getText());
					if (conDB.getSelectedItem().equals("ORACLE")) {

						if (oracleSID.getText().isEmpty())
							conStatus.setText("Oracle SID not set");
					} else {
						conStatus.setForeground(Color.black);
						conStatus.setText("Waiting for connection....");
						// something to add here

						checkInfo = true;

					}
				} catch (Exception ex) {
					conStatus.setText("Port must be an integer number !");
				}

			}
		}
		// next
		if (checkInfo) {
			boolean connectionValid = false;
			if (conDB.getSelectedItem().equals("MYSQL")) {
				api.setMYSQLServerInformation(username.getText(),
						String.copyValueOf(password.getPassword()),
						server.getText(), Integer.parseInt(port.getText()));
				api.beginSession();
				try {
					connectionValid = api.getConnection().isValid(2000);
					conStatus.setText("Connection Success!");

				} catch (Exception exception) {
					conStatus.setForeground(Color.red);
					checkInfo = false;
					conStatus.setText("Connection Failed");
				}
				if (connectionValid) {
					conStatus.setForeground(Color.green);
					conStatus.setText("Connection Success");
					// make tab 2 visible
					 tabbedPane.addTab("Query by Example", null, panel_1,null);
					tabbedPane.remove(panel);
					label.setForeground(Color.GREEN);
					try {
						String[] schem = api.showAvailableSchemas();

						for (String s : schem)
							schemas.addItem(s);
						tablemodel = new DefaultTableModel();
						tabbedPane.setSelectedIndex(0);
					} catch (Exception ex) {
						ex.getMessage();
						Component frame = null;
						JOptionPane.showConfirmDialog(frame,
								"NO databases available \n for current user");
					}

				} else {
					conStatus.setForeground(Color.red);
					conStatus.setText("Connection Failed");
				}
			}
		}

	}

	protected void QueryActionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		tablemodel.fireTableDataChanged();
		Object[] selected;
		String[] sel = null;
		if (columnLIst.isSelectionEmpty())
			collsSelected = false;
		else
			collsSelected = true;
		if (collsSelected == true) {
			selected = columnLIst.getSelectedValuesList().toArray();
			sel = new String[columnLIst.getSelectedValuesList().size()];
			for (int i = 0; i < columnLIst.getSelectedValuesList().size(); i++) {
				sel[i] = selected[i].toString();
			}
		} else if (collsSelected == false
				&& columnLIst.getModel().getSize() > 1) {
			sel = new String[columnLIst.getModel().getSize()];
			for (int i = 0; i < columnLIst.getModel().getSize(); i++) {
				sel[i] = columnLIst.getModel().getElementAt(i).toString();
			}
		} else if (collsSelected == false
				&& columnLIst.getModel().getSize() < 1) {
			queryTextField.setText("table contains no columns");
			queryTextField.setEditable(false);
			btnQuery.setEnabled(false);
		}
		
		QueryData qd = new QueryData();
		qd = api.QBE(schemas.getSelectedItem().toString(), tableList
				.getSelectedValue().toString(), queryTextField.getText(), sel);

		tablemodel.setDataVector(qd.getData(), qd.getQBECols());
		table.setModel(tablemodel);
		tablemodel.fireTableDataChanged();
		table.revalidate();

	}

	protected void columnValueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (columnLIst.isSelectionEmpty()) {
			if (columnLIst.getSelectedValuesList().isEmpty()) {
				collsSelected = false;
			} else {
				collsSelected = true;
				if (columnLIst.getSelectedValuesList().size() > 0) {
					ColsSelected = new String[columnLIst
							.getSelectedValuesList().size()];
					ColsSelected = columnLIst.getSelectedValuesList().toArray();

				}

			}

		}
		queryTextField.setEditable(true);
		btnQuery.setEnabled(true);

	}

	protected void tableListValueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (tableList.getSelectedValue() != null) {
			String t = tableList.getSelectedValue().toString();
			ArrayList<Object> arr = api.getColumnsFromTable(t);
			ListModel model = new DefaultListModel<String>();
			for (Object s : arr)
				((DefaultListModel<String>) model).addElement(s.toString());
			columnLIst.setModel(model);
			columnLIst.setSelectedIndex(0);

		}

	}

	protected void schemasItemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

		try {

			api.changeDatabase(e.getItem().toString());
			modelList = new DefaultListModel<String>();
			modelList.removeAllElements();
			for (String s : api.getStringArraytables()) {
				modelList.addElement(s.toString());

			}
			tableList.setModel(modelList);
			try {
				tableList.setSelectedIndex(0);
			} catch (Exception ex) {
			}
			tableList.revalidate();
			tableList.updateUI();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	protected void conDBItemStateChnaged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getItem().equals("ORACLE")) {
			lblOracleSid.setVisible(true);
			oracleSID.setVisible(true);
			api.setDatabase(DatabasesAvailable.ORACLE);
		}
		if (e.getItem().equals("MYSQL")) {
			lblOracleSid.setVisible(false);
			oracleSID.setVisible(false);
			api.setDatabase(DatabasesAvailable.MYSQL);
		}

	}
}
