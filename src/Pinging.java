import java.awt.Image;		//jtable에 data 입력
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;

public class Pinging extends Thread {

	Object[] msg;
	String ip;
	ImageIcon originIcon3 = new ImageIcon(System.getProperty("user.dir") + "\\src\\question-icon.PNG");
	Image imaginImg3 = originIcon3.getImage();
	Image changeImg3 = imaginImg3.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
	ImageIcon questionIcon = new ImageIcon(changeImg3);	
	ImageIcon originIcon4 = new ImageIcon(System.getProperty("user.dir") + "\\src\\blue-icon.PNG");
	Image originImg4= originIcon4.getImage();
	Image changedImg4 = originImg4.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
	ImageIcon pingYIcon = new ImageIcon(changedImg4);
	private ImageIcon originIcon5 = new ImageIcon(System.getProperty("user.dir") + "\\src\\red-icon.PNG");
	private Image originImg5 = originIcon5.getImage();
	private Image changedImg5 = originImg5.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
	ImageIcon pingNIcon = new ImageIcon(changedImg5);
	
	public Pinging(String ip, Object[] msg) {
		this.ip = ip;
		this.msg = msg;	//stats i를 받아옴 , msg[1] == stats[i][1]
	}
	@Override
	public void run() {			//Thread method
		JLabel questionLabel = new JLabel(ip);	//Label에 icon을 넣고 그 Label을 사용
		JLabel pingYLabel = new JLabel(ip);
		JLabel pingNLabel = new JLabel(ip);
		questionLabel.setIcon(questionIcon);
		pingYLabel.setIcon(pingYIcon);
		pingNLabel.setIcon(pingNIcon);
		BufferedReader br = null;
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec("ping -a " + ip);
			msg[0] = questionLabel;
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String lineInput = null;
			
			
			while ((lineInput = br.readLine()) != null) {			//BufferedReader로 받아지면 lineInput에 저장
				if (lineInput.indexOf("[") >= 0) {
					msg[3] = lineInput.substring(5, lineInput.indexOf("[") - 1);
				}
				if (lineInput.indexOf("ms") >= 0) {
					msg[1] = lineInput.substring(0,lineInput.indexOf("ms") + 2);
					msg[1] = ((String)msg[1]).substring(((String)msg[1]).lastIndexOf("=")+1);
					if(msg[1].toString().length() > 6) {
						msg[1] = ((String)msg[1]).substring(((String)msg[1]).lastIndexOf("<")+1);
					}
					msg[2] = lineInput.substring(lineInput.indexOf("TTL=")+4);	//hostname까지 받아온 다음 label출력
					break;
				}
				
			}
			
			if(msg[1] == null && msg[2] == null && msg[3] == null) msg[0] = pingNLabel;//red-icon
			else												   msg[0] = pingYLabel;//blue-icon
			if (msg[1] == null) msg[1] = "[n/a]";	
			if (msg[2] == null) msg[2] = "[n/s]";
			if (msg[3] == null) msg[3] = "[n/s]";
		} catch (Exception e) {
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}		
	}

}
