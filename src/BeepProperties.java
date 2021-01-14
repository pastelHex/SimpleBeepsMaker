import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import model.BeepModel;
import model.BeepSample;

public class BeepProperties {
	private final String durationSuffix = "_duration";
	private final String frequencySuffix = "_freq";

	public BeepModel loadPropertiesToModel() throws IOException {
		List<BeepSample> l = new ArrayList<>();
		InputStream inputStream = null;
		int sampleRate = 0;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			int intervals = Integer.parseInt(prop.getProperty("no_of_intervals"));
			sampleRate = Integer.parseInt(prop.getProperty("sample_rate"));
			l = loadBeepsFromConfig(intervals, prop);

		} catch (Exception e) {
			throw new IOException("Can't read properties file");
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new BeepModel(sampleRate, l);
	}

	private List<BeepSample> loadBeepsFromConfig(int noOfBeeps, Properties prop) {
		ArrayList<BeepSample> beeps = new ArrayList<>();
		for (int i = 0; i < noOfBeeps; i++) {
			int duration = Integer.parseInt(prop.getProperty("n" + i + durationSuffix));
			int freq = Integer.parseInt(prop.getProperty("n" + i + frequencySuffix));
			beeps.add(new BeepSample(duration, freq));
		}
		return beeps;
	}
}
