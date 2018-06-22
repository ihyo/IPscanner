import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;

public class PingOutline extends JFrame {

	private String[] titles;
	private Object[][] stats;
	private int IPStart;
	private int IPEnd;

	public PingOutline() {
		super("Network Scanner");

		// myIP and myHostname
		String myIP;
		String myHostname;

		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		myIP = ia.getHostAddress();
		myHostname = ia.getHostName();

		String fixedIP = myIP.substring(0, myIP.lastIndexOf(".") + 1);	
		
		// menu begin
		JMenuBar menuBar = new JMenuBar();

		setJMenuBar(menuBar);

		JMenu scanMenu = new JMenu("Scan");
		JMenu gotoMenu = new JMenu("Goto");
		JMenu cmdMenu = new JMenu("Command");
		JMenu favoriteMenu = new JMenu("Favorite");
		JMenu toolsMenu = new JMenu("Tools");
		JMenu helpMenu = new JMenu("Help");

		menuBar.add(scanMenu);
		menuBar.add(gotoMenu);
		menuBar.add(cmdMenu);
		menuBar.add(favoriteMenu);
		menuBar.add(toolsMenu);
		menuBar.add(helpMenu);

		JMenuItem loadFromFileAction = new JMenuItem("Load From File");
		JMenuItem exportAllAction = new JMenuItem("Export All");
		JMenuItem exportSelectioAction = new JMenuItem("Export Selection");
		JMenuItem quitAction = new JMenuItem("Quit");

		scanMenu.add(loadFromFileAction);
		scanMenu.add(exportAllAction);
		scanMenu.add(exportSelectioAction);
		scanMenu.addSeparator();
		scanMenu.add(quitAction);

		JMenuItem nextAliveHostAction = new JMenuItem("next alive host");
		JMenuItem nextOpenPortAction = new JMenuItem("next open port");
		JMenuItem nextDeadHostAction = new JMenuItem("next dead host");
		JMenuItem previousAliveHostAction = new JMenuItem("previous alive host");
		JMenuItem previousOpenPortAction = new JMenuItem("previous open port");
		JMenuItem previousDeadHostAction = new JMenuItem("previous dead host");
		JMenuItem findAction = new JMenuItem("Find");

		gotoMenu.add(nextAliveHostAction);
		gotoMenu.add(nextOpenPortAction);
		gotoMenu.add(nextDeadHostAction);
		gotoMenu.addSeparator();
		gotoMenu.add(previousAliveHostAction);
		gotoMenu.add(previousOpenPortAction);
		gotoMenu.add(previousDeadHostAction);
		gotoMenu.addSeparator();
		gotoMenu.add(findAction);

		JMenuItem showDetailsAction = new JMenuItem("Show details");
		JMenuItem rescanIpAction = new JMenuItem("Rescan IP");
		JMenuItem deleteIpAction = new JMenuItem("Delete IP");
		JMenuItem copyIpAction = new JMenuItem("Copy IP");
		JMenuItem copyDetailsAction = new JMenuItem("Copy details");
		JMenuItem openAction = new JMenuItem("Open");

		cmdMenu.add(showDetailsAction);
		cmdMenu.addSeparator();
		cmdMenu.add(rescanIpAction);
		cmdMenu.add(deleteIpAction);
		cmdMenu.addSeparator();
		cmdMenu.add(copyIpAction);
		cmdMenu.add(copyDetailsAction);
		cmdMenu.addSeparator();
		cmdMenu.add(openAction);

		JMenuItem addCurrentAction = new JMenuItem("Add current");
		JMenuItem manageFavoriteAction = new JMenuItem("Manage Favorite");

		favoriteMenu.add(addCurrentAction);
		favoriteMenu.add(manageFavoriteAction);

		JMenuItem preferenceAction = new JMenuItem("Preference");
		JMenuItem fetchersAction = new JMenuItem("Fetchers");
		JMenuItem selectionAction = new JMenuItem("Selection");
		JMenuItem scanStaticsAction = new JMenuItem("Scan statics");

		toolsMenu.add(preferenceAction);
		toolsMenu.add(fetchersAction);
		toolsMenu.addSeparator();
		toolsMenu.add(selectionAction);
		toolsMenu.add(scanStaticsAction);

		JMenuItem gettingStartedAction = new JMenuItem("Getting started");
		JMenuItem officialWebsiteAction = new JMenuItem("Official Website");
		JMenuItem faqAction = new JMenuItem("FAQ");
		JMenuItem reportAnIssueAction = new JMenuItem("Report an issue");
		JMenuItem pluginsAction = new JMenuItem("plug-ins");
		JMenuItem commandLineUsageAction = new JMenuItem("command-line usage");
		JMenuItem checkfornewversionAction = new JMenuItem("Check for new version");
		JMenuItem aboutAction = new JMenuItem("About");

		helpMenu.add(gettingStartedAction);
		helpMenu.addSeparator();
		helpMenu.add(officialWebsiteAction);
		helpMenu.add(faqAction);
		helpMenu.add(reportAnIssueAction);
		helpMenu.add(pluginsAction);
		helpMenu.addSeparator();
		helpMenu.add(commandLineUsageAction);
		helpMenu.add(checkfornewversionAction);
		helpMenu.addSeparator();
		helpMenu.add(aboutAction);
		// menu end

		// Toolbar begin
		Font myFont = new Font("Serif", Font.BOLD, 16);
		JToolBar toolbar1 = new JToolBar();
		toolbar1.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 2));
		JToolBar toolbar2 = new JToolBar();
		toolbar2.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 2));

		JLabel ipRangeLabel = new JLabel("IP Range:");
		JTextField ipStartTextField = new JTextField(10);
		JLabel toLabel = new JLabel("to");
		JTextField ipEndTextField = new JTextField(10);
		JComboBox sourceSelectionComboBox = new JComboBox();
		sourceSelectionComboBox.addItem("IP Range");
		sourceSelectionComboBox.addItem("Random");
		sourceSelectionComboBox.addItem("File");
		ImageIcon originIcon0 = new ImageIcon(System.getProperty("user.dir") + "\\src\\setting-icon.PNG");
		Image originImg0 = originIcon0.getImage();
		Image changeImg0 = originImg0.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
		ImageIcon settingIcon = new ImageIcon(changeImg0);
		JButton settingButton = new JButton(settingIcon);
		ipRangeLabel.setFont(myFont);
		toLabel.setFont(myFont);
		ipRangeLabel.setPreferredSize(new Dimension(75, 30));
		toLabel.setPreferredSize(new Dimension(13, 30));
		sourceSelectionComboBox.setPreferredSize(new Dimension(80, 22));

		toolbar1.add(ipRangeLabel);
		toolbar1.add(ipStartTextField);
		toolbar1.add(toLabel);
		toolbar1.add(ipEndTextField);
		toolbar1.add(sourceSelectionComboBox);
		toolbar1.add(settingButton);

		JLabel hostNameLabel = new JLabel("Hostname:");
		JTextField hostNameTextField = new JTextField(myHostname, 10);
		JButton ipButton = new JButton("IP");
		JComboBox ipSelectionComboBox = new JComboBox();
		ipSelectionComboBox.addItem("Netmask");
		ipSelectionComboBox.addItem("/24");
		ipSelectionComboBox.addItem("/16");
		
		ImageIcon originIcon = new ImageIcon(System.getProperty("user.dir") + "\\src\\start-icon.PNG");//System.getProperty("user.dir")는 java project 위치 반환
		Image originImg = originIcon.getImage();	//imageicon을 image로 변환
		Image changeImg = originImg.getScaledInstance(12, 12, Image.SCALE_SMOOTH);	//smooth하게 12,12크기로 넣기
		ImageIcon startIcon = new ImageIcon(changeImg);
		JButton startButton = new JButton(startIcon);
		startButton.setText(" START");
		ImageIcon originIcon2 = new ImageIcon(System.getProperty("user.dir") + "\\src\\stop-icon.PNG");
		Image originImg2 = originIcon2.getImage();
		Image changeImg2 = originImg2.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
		ImageIcon stopIcon = new ImageIcon(changeImg2);
		JButton stopButton = new JButton(stopIcon);
		stopButton.setText(" STOP");
		ImageIcon originIcon6 = new ImageIcon(System.getProperty("user.dir") + "\\src\\Fetchers-icon.PNG");
		Image originImg6 = originIcon6.getImage();
		Image changeImg6 = originImg6.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
		ImageIcon fetchersIcon = new ImageIcon(changeImg6);
		JButton fetchersButton = new JButton(fetchersIcon);
		
		hostNameLabel.setFont(myFont);
		hostNameLabel.setPreferredSize(new Dimension(75, 30));
		ipButton.setPreferredSize(new Dimension(40, 22));
		ipSelectionComboBox.setPreferredSize(new Dimension(86, 22));
		startButton.setPreferredSize(new Dimension(82, 22));
		stopButton.setPreferredSize(new Dimension(82, 22));
		toolbar2.add(hostNameLabel);
		toolbar2.add(hostNameTextField);
		toolbar2.add(ipButton);
		toolbar2.add(ipSelectionComboBox);
		toolbar2.add(startButton);
		toolbar2.add(fetchersButton);

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(toolbar1, BorderLayout.NORTH);
		pane.add(toolbar2, BorderLayout.SOUTH);

		getContentPane().add(pane, BorderLayout.NORTH);
		// Toolbar end

		// Table begin
		titles = new String[] { "IP", "Ping", "TTL", "Hostname", "Ports" };
		stats = initializeTable();

		JTable jTable = new JTable(stats, titles);

		JScrollPane jScrollPane = new JScrollPane(jTable);
		add(jScrollPane, BorderLayout.CENTER);
		// Table end

		// status bar begin
		JPanel statusPanel = new JPanel();
		JPanel statusPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));//-
		statusPanel1.setPreferredSize(new Dimension(160, 20));					//panel 3개에 나눠서 저장
		JPanel statusPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));//
		statusPanel2.setPreferredSize(new Dimension(60, 20));					// 
		JPanel statusPanel3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));// 
		statusPanel3.setPreferredSize(new Dimension(60, 20));					//-
		getContentPane().add(statusPanel, BorderLayout.SOUTH);
		JLabel readyStatusLabel = new JLabel("Ready");
		JLabel displayStatusLabel = new JLabel("Display: All");
		JLabel threadsStatusLabel = new JLabel("Threads:0");
		statusPanel1.add(readyStatusLabel);
		statusPanel2.add(displayStatusLabel);
		statusPanel3.add(threadsStatusLabel);
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));	//box layout 왼쪽부터 정렬
		statusPanel.add(statusPanel1);
		statusPanel.add(statusPanel2);
		statusPanel.add(statusPanel3);
		
		JProgressBar progressBar = new JProgressBar();			//progress bar 생성
		progressBar.setPreferredSize(new Dimension(150, 20));	//
		statusPanel.add(progressBar);							//
		progressBar.setIndeterminate(false);					//
		// status bar end
		
		quitAction.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IPStart = Integer.parseInt(ipStartTextField.getText().substring(ipStartTextField.getText().lastIndexOf(".") + 1));//Integer.parseInt는 String을 int로 ,.0받음
				IPEnd = Integer.parseInt(ipEndTextField.getText().substring(ipEndTextField.getText().lastIndexOf(".") + 1));//.255받음
				progressBar.setIndeterminate(true);//progressBar 움직임
				toolbar2.remove(startButton);	//▶ start
				toolbar2.add(stopButton);		//■ stop
				jTable.repaint();
				readyStatusLabel.setText("Started");
				statusPanel.repaint();
				
				//jTable에 icon 넣기
				jTable.getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {	//첫행 전체 불러옴

					@Override
					public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column) {
						if (value instanceof JLabel) return (JLabel) value;		//icon이 없으면 X, 첫행을 Object값으로 변경
						return null;
					}
					
				});
				//진오의 도움을 받음(String data만 입력 가능한 jTable을 첫 행만 Object형으로 받을 수 있게 해줌)
				
				//ping, TTL, Hostname Thread start
				new Thread(() -> {									//람다식 Thread
						
						Pinging[] pinging = new Pinging[IPEnd];		//Pinging class 받아옴
						for (int i = IPStart; i < IPEnd; i++) {
							Object[] msg = stats[i];	//1행 다 받음
							try {
								Thread.sleep(10);		//과부화 방지
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							pinging[i] = new Pinging(fixedIP +(i), msg);	//1행씩 Thread 생성
							pinging[i].start();								//pinging start
							jTable.repaint();
						}
					//Thread개수 표시
						while (Thread.activeCount() > 3) {	//Thread 개수 3개 : 기본 실행 Thread 2개에 람다식 Thread1개
							try {
								Thread.sleep(200);
								jTable.repaint();
								threadsStatusLabel.setText("Threads: " + (Thread.activeCount()-3));
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					//
						
					//Ports Thread start
					new Thread(() -> {		//Thread가 종료되게 만듬, Thread개수 표시 X
						for (int i = IPStart; i < IPEnd; i++) {
							if (stats[i][1] != "[n/a]" && stats[i][2] != "[n/s]" && stats[i][3] != "[n/s]") {
								PortScanner ps = new PortScanner();
								final ExecutorService es = Executors.newFixedThreadPool(100);	//Thread 100 사용
								final int timeout = 200;
								final List<Future<ScanResult>> futures = new ArrayList<>();
								
								for (int port = 1; port <= 1024; port++) {
									futures.add(ps.portIsOpen(es, fixedIP + i, port, timeout));
								}
								try {
									es.awaitTermination(200L, TimeUnit.MICROSECONDS);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							
								int PortsCount = 0;
								for (final Future<ScanResult> f : futures) {
									try {
										if (f.get().isOpen()) {
											PortsCount++;
											if(stats[i][4] ==null)	stats[i][4] = f.get().getPort();
											else					stats[i][4] = (stats[i][4].toString() + "," +f.get().getPort());
											jTable.repaint();
										}
									} catch (Exception e1) {
										e1.printStackTrace();
									}
								}
							} else {
								stats[i][4] = "[n/s]";
								jTable.repaint();
							}
							if(stats[i][4] == null)	stats[i][4] = "[n/s]";	//Port가 없는 경우 [n/s]표시
						}
						jTable.repaint();
						progressBar.setIndeterminate(false);
						toolbar2.remove(stopButton);
						toolbar2.add(startButton);
						readyStatusLabel.setText("Ready");
						jTable.repaint();
					}).start();	//Thread 시작
					//Ports Thread end
				
				}).start();
				//ping, TTL, Hostname Thread end	
			}
		});
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == stopButton) {
					progressBar.setIndeterminate(false);
					toolbar2.remove(stopButton);
					toolbar2.add(startButton);
					readyStatusLabel.setText("Ready");
					jTable.repaint();
				}
			}
		});	//Stop 버튼 누르면 정지
		
		ipStartTextField.setText(fixedIP + 0);
		ipEndTextField.setText(fixedIP + 255);
		setSize(700, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private Object[][] initializeTable() {
		Object[][] results = new Object[255][titles.length];
		return results;
	}
	public static void main(String[] args) {
		PingOutline po = new PingOutline();
	}

}