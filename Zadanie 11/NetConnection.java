/**
 * OgĂłlny interfejs pozwalajÄcy na obsĹugÄ poĹÄczeĹ sieciowych.
 */
public interface NetConnection {
	/**
	 * Program otwiera poĹÄczenie do serwera dostÄpnego protokoĹem TCP/IP pod adresem
	 * host i numerem portu TCP port, wykonuje swoje zadanie i zamyka poĹÄczenie.
	 * 
	 * @param host adres IP lub nazwa komputera
	 * @param port numer portu, na ktĂłrym serwer oczekuje na poĹÄczenie
	 */
	public void connectExecuteClose(String host, int port);
}