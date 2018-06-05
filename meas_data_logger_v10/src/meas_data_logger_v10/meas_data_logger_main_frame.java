package meas_data_logger_v10;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import meas_data_logger_v10.kommunikacio;
import meas_data_logger_v10.MultipleChartFactory;

import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.*;

public class meas_data_logger_main_frame extends JFrame {

	private JPanel contentPane;
	public static JPanel panelCards = new JPanel();
	public static JPanel panelOnline = new JPanel();
	public static JPanel panelLoad = new JPanel();
	public static JPanel panelSettings = new JPanel();
	public static JPanel panelMeasSettings = new JPanel();
	
	static MultipleChartFactory mcf = new MultipleChartFactory("measured_values", "meas_point");
	//private AccessDatabase myDatabaseObject = new AccessDatabase("C:/Dropbox/AgeRange.mdb","tblEmployees");
	//private AccessDatabase myDatabaseObject = new AccessDatabase("C:/Dropbox/measData.mdb","measVoltageDataTbl");
	
	//private AccessDatabase myDatabaseObject = new AccessDatabase("C:/Dropbox/measData.mdb","measDataSheetTbl");
	  public static AccessDatabase myDatabaseObject = new AccessDatabase("C:/Dropbox/meas_data_logger_2/measData.mdb","measVoltageDataTbl");
	//private AccessDatabase myCurrentDataObject = new AccessDatabase("C:/Dropbox/measData.mdb","measCurrentDataTbl");
	
	private final JLabel lblMrsProfil = new JLabel("M\u00E9r\u00E9s profil");
	private final JLabel lblKrtyaBelltsok = new JLabel("K\u00E1rtya be\u00E1ll\u00EDt\u00E1sok");
	private final JLabel lblMentettMrsAdatok = new JLabel("Mentett m\u00E9r\u00E9s adatok kezel\u00E9se");
	private final JButton btnList = new JButton("List");
	private final JButton btnAdd = new JButton("Add");
    
	public String[] generatedFieldNames;
	private final JCheckBox chckbxOnlineAdatokRajzolsa = new JCheckBox("Online adatok rajzol\u00E1sa");
	private final JLabel lblMeasdatalogger = new JLabel("meas_data_logger_2");
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					meas_data_logger_main_frame frame = new meas_data_logger_main_frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public meas_data_logger_main_frame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 887, 619);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel panelButtons = new JPanel();
		contentPane.add(panelButtons);
		
		
		JButton btnOnline = new JButton("Online g\u00F6rbe");
		JButton btnLoad = new JButton("Mentett adatok");
		JButton btnSettings = new JButton("Be\u00E1ll\u00EDt\u00E1sok");
		JButton btnMeasSettings = new JButton("Mérés profil");
		
		panelButtons.add(btnOnline);
		panelButtons.add(btnLoad);
		panelButtons.add(btnSettings);
		panelButtons.add(btnMeasSettings);

		
		contentPane.add(chckbxOnlineAdatokRajzolsa);
		
		contentPane.add(lblMeasdatalogger);
		
		contentPane.add(panelCards);
		panelCards.setLayout(new CardLayout(0, 0));
		panelCards.add(panelOnline, "idOnline");
		panelCards.add(panelLoad, "idLoad");
		GridBagLayout gbl_panelLoad = new GridBagLayout();
		gbl_panelLoad.columnWidths = new int[]{218, 49, 370, 0};
		gbl_panelLoad.rowHeights = new int[]{23, 0, 0, 0, 0};
		gbl_panelLoad.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelLoad.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelLoad.setLayout(gbl_panelLoad);
		btnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				kommunikacio.drawOnlineGraph = false;
				int[] myLoadedGraphArray = new int[255];
				myDatabaseObject.requestRowByIdx("measVoltageDataTbl",3);
				int myIdx = 0;
				for (myIdx=0;myIdx<254;myIdx++) {
					 System.out.println(myLoadedGraphArray[myIdx]+"; ");
					meas_data_logger_main_frame.draw_new_feszultesg(myIdx,myLoadedGraphArray[myIdx]);
				}

				
			}
		});
		lblMentettMrsAdatok.setFont(new Font("Txt_IV50", Font.PLAIN, 18));
		
		GridBagConstraints gbc_lblMentettMrsAdatok = new GridBagConstraints();
		gbc_lblMentettMrsAdatok.insets = new Insets(0, 0, 5, 0);
		gbc_lblMentettMrsAdatok.anchor = GridBagConstraints.WEST;
		gbc_lblMentettMrsAdatok.gridx = 2;
		gbc_lblMentettMrsAdatok.gridy = 0;
		panelLoad.add(lblMentettMrsAdatok, gbc_lblMentettMrsAdatok);
		
		GridBagConstraints gbc_btnList = new GridBagConstraints();
		gbc_btnList.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnList.insets = new Insets(0, 0, 5, 5);
		gbc_btnList.gridx = 1;
		gbc_btnList.gridy = 2;
		panelLoad.add(btnList, gbc_btnList);
		
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 2;
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//myDatabaseObject.runStatement("INSERT INTO tblEmployees (strLastName,strFirstName,strTitle)  VALUES  ('Nemet', 'Krisztian','engineer')"); 
				//String testFileds[] = {"strLastName","strFirstName","strTitle"};
				//String testValues[] = {"Nemet","Krisztian","engineer"};
				//myDatabaseObject.insertRow(testFileds,testValues);
				//myDatabaseObject.insertRow(generatedFieldNames,generatedTestValues());
				/*
				String testFileds[] = {"measID","VoltageGainIdx","CurrentGainIdx"};
				String testValues[] = {"999","2","1"};
				myDatabaseObject.insertRow(testFileds,testValues);
				*/
				//String testFileds[] = {"measId","a1","a2"};
				//String testValues[] = {"999","501","502"};
				//myDatabaseObject.insertRow(generateDataFieldNames(),generatedTestValues());
				//myDatabaseObject.runStatement("INSERT INTO test (Id,a1,a2) VALUES (2,333,444)");
				
				
			}
		});
		panelLoad.add(btnAdd, gbc_btnAdd);
		panelCards.add(panelSettings, "idSettings");
		lblKrtyaBelltsok.setFont(new Font("Txt_IV50", Font.PLAIN, 18));
		
		panelSettings.add(lblKrtyaBelltsok);
		panelCards.add(panelMeasSettings, "idMeasSettings");
		lblMrsProfil.setFont(new Font("Txt_IV50", Font.PLAIN, 18));
		
		panelMeasSettings.add(lblMrsProfil);
		
		panelOnline.add(mcf.getChart());
        mcf.createAdditionalDataset();
        mcf.createAdditionalDataset();
        mcf.createAdditionalDataset();
		
        kommunikacio.kommunikacio_init();
        generatedFieldNames= generateDataFieldNames();
        
		btnOnline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelCards.removeAll();
				panelCards.add(panelOnline);
				panelCards.repaint();
				panelCards.revalidate();
			}
		});
		
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelCards.removeAll();
				panelCards.add(panelLoad);
				panelCards.repaint();
				panelCards.revalidate();
			}
		});
		
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelCards.removeAll();
				panelCards.add(panelSettings);
				panelCards.repaint();
				panelCards.revalidate();
			}
		});

		btnMeasSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelCards.removeAll();
				panelCards.add(panelMeasSettings);
				panelCards.repaint();
				panelCards.revalidate();
			}
		});	
		
		event e = new event();
		chckbxOnlineAdatokRajzolsa.addItemListener(e);
		

		
		/*if this.
				kommunikacio.drawOnlineGraph=true;*/
		
	}
	
	public class event implements ItemListener{
		public void itemStateChanged(ItemEvent e) {
			if (chckbxOnlineAdatokRajzolsa.isSelected()) {
				kommunikacio.drawOnlineGraph=true;
			}
			else {
				kommunikacio.drawOnlineGraph=false;
			}
		}
	}
	
	public static void draw_new_feszultesg(int index,int number){
		mcf.getXYSeries(0).getSeries(0).add(index,  number);
		panelOnline.repaint();
	}
	
	public static void draw_new_aram(int index,int number){
		//mcf.getXYSeries(1).getSeries(0).add(index,  number);
		//panelOnline.repaint();
	}
	
	public static void draw_new_imp(int index,int number){
		//mcf.getXYSeries(2).getSeries(0).add(index,  number);
		//panelOnline.repaint();
	}
	
	

	public static String[] generateDataFieldNames() {
		String[] myFunctionArray = new String[255];
		myFunctionArray[0]="measId";
		for (int i=1;i<255;i++) {
			myFunctionArray[i] = "a"+String.valueOf(i);
		}
		
		return myFunctionArray;
	}
	

	public static String[] generatedTestValues () {
		String[] myFunctionArray = new String[255];
		myFunctionArray[0]="999";
		for (int i=1;i<255;i++) {
			myFunctionArray[i] = String.valueOf(1000+i);
		}
		
		return myFunctionArray;
	}
	
	
} //class
