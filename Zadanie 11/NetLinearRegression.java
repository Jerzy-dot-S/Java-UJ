import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class NetLinearRegression implements NetConnection {
	private static class DataPair {
		double first, second;
		DataPair(double f, double s) {
			first = f;
			second = s;
		}
	}

	@Override
	public void connectExecuteClose(String host, int port) {
		double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
		int n = 0;
		try (
			Socket sock = new Socket(host, port);
			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true)
		) {
			String msg;
			while ((msg = reader.readLine()) != null) {
				msg = msg.strip();
				if (msg.isEmpty())
					continue;

				if (msg.contains("Program")) {
					writer.println("P");
					writer.flush();
					continue;
				}

				if (msg.toLowerCase().contains("współczynnik") && msg.toLowerCase().contains("a")) {
					double denominator = n * sumX2 - sumX * sumX;
					double a = denominator == 0 ? 0 : (n * sumXY - sumX * sumY) / denominator;
					writer.println(String.format(Locale.US, "%f", a));
					writer.flush();
					continue;
				}
				if (msg.toLowerCase().contains("wyraz wolny") && msg.toLowerCase().contains("b")) {
					double denominator = n * sumX2 - sumX * sumX;
					double a = denominator == 0 ? 0 : (n * sumXY - sumX * sumY) / denominator;
					double b = n == 0 ? 0 : (sumY - a * sumX) / n;
					writer.println(String.format(Locale.US, "%f", b));
					writer.flush();
					continue;
				}

				String[] split = msg.split("[ \t]+", 3);
				if (split.length < 2)
					continue;
				try {
					double v1 = parseSmart(split[0]);
					double v2 = parseSmart(split[1]);
					sumX += v1;
					sumY += v2;
					sumXY += v1 * v2;
					sumX2 += v1 * v1;
					n++;
				} catch (Exception ex) {
				}
			}
		} catch (IOException io) {
			System.err.println("Network error: " + io.getMessage());
		}
	}

	private double parseSmart(String s) throws ParseException {
		String normalized = s.replace(',', '.');
		try {
			return Double.parseDouble(normalized);
		} catch (NumberFormatException e) {
			NumberFormat[] nfs = {
				NumberFormat.getInstance(Locale.US),
				NumberFormat.getInstance(Locale.forLanguageTag("pl-PL")),
				NumberFormat.getInstance(Locale.GERMAN)
			};
			for (NumberFormat nf : nfs) {
				try {
					return nf.parse(s).doubleValue();
				} catch (ParseException ignored) {
				}
			}
			throw new ParseException("Cannot parse: " + s, 0);
		}
	}
}