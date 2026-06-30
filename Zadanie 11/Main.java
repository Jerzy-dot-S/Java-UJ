public class Main {
    public static void main(String[] args) {
        NetConnection client = new NetLinearRegression();
        client.connectExecuteClose("172.30.24.12", 9090);
    }
}
