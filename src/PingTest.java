import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingTest {
	String ip;
	

	public PingTest(String ip) {
		this.ip = ip;
	}
	
	public Object[] ResultPing(){
		Object[] results = new Object[3];
		String pingCmd = "ping -a " + ip;
		Runtime runtime = Runtime.getRuntime();
		
		try {
			Process process = runtime.exec(pingCmd);
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				Pattern pattern = Pattern.compile("(\\d+ms)\\s+TTL=(\\d+)");
				Matcher matcher = pattern.matcher(inputLine);
				Pattern pattern2 = Pattern.compile("Ping\\s+(.+)\\s+\\[");
				Matcher matcher2 = pattern2.matcher(inputLine);
				if(matcher2.find()) {
					results[2] = matcher2.group(1);
					System.out.println(matcher2.group(1));
				}
				if (matcher.find()) {
					results[0] = matcher.group(1);
					results[1] = matcher.group(2);
					System.out.println(matcher.group(1) + "," + matcher.group(2));
					break;
				}
			} 
		} catch (Exception e) {
		}		
		
		
		return results;
	}

	public static void main(String[] args) {
		PingTest pt = new PingTest("10.137.212.103");
		pt.ResultPing();
	}
}