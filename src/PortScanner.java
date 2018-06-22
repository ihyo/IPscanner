import java.net.*;
import java.util.*;
import java.util.concurrent.*;
public class PortScanner {

	public static void main(String[] args) throws InterruptedException, ExecutionException{
			final ExecutorService es = Executors.newFixedThreadPool(20);
			final String ip = "127.0.0.1";
			final int timeout = 200;
			final List<Future<ScanResult>> futures = new ArrayList<>();
			for(int port = 1; port <= 1024; port++) {
				futures.add(portIsOpen(es , ip , port , timeout));
			}
			es.awaitTermination(200L, TimeUnit.MICROSECONDS);
			int openPorts = 0;
			for(final Future<ScanResult> f : futures) {
				if(f.get().isOpen()) {
					openPorts++;
					System.out.println(f.get().getPort());
				}
			}
	}
	public static Future<ScanResult> portIsOpen(final ExecutorService es, final String ip, final int port, final int timeout) {
		return es.submit(new Callable<ScanResult>() {
			public ScanResult call(){
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port), timeout);
					socket.close();
					return new ScanResult(port, true);
				} catch (Exception e) {
					return new ScanResult(port,false);
				}
			}
		});
	}

}