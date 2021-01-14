import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import model.BeepModel;
import model.BeepSample;

public class BeepUtil {

	public static void generateWavFromModel(BeepModel model) throws IOException {
		byte[] result = new byte[0];

		for (BeepSample s : model.getBeepList()) {
			byte[] singleResult = generateFromBeepSample(s, model.getSampleRate());
			int endOf = result.length;
			byte[] big = new byte[endOf + singleResult.length];
			System.arraycopy(result, 0, big, 0, endOf);
			System.arraycopy(singleResult, 0, big, endOf, singleResult.length);
			result = big;
		}

		PCMToWAV(result, 1, model.getSampleRate(), 16);
	}

	private static byte[] generateFromBeepSample(BeepSample l, int sampleRate) {
		int numSamples = (l.getDuration() * sampleRate) / 1000;
		double[] samples = new double[numSamples];
		byte[] buffer = new byte[2 * numSamples];
		for (int i = 0; i < numSamples; ++i) {
			if (l.getFrequency() == 0) {
				samples[i] = 0;
			} else
				samples[i] = Math.sin((2 * Math.PI * i) / (sampleRate / (double) l.getFrequency())); // Sine wave
		}
		int idx = 0;
		for (final double dVal : samples) {
			// scale to maximum amplitude
			final short val = (short) (dVal * 32767);
			// in 16 bit wav PCM, first byte is the low order byte
			buffer[idx++] = (byte) (val & 0x00ff);
			buffer[idx++] = (byte) ((val & 0xff00) >>> 8);
		}
		return buffer;
	}

	/**
	 * @param input
	 *            raw PCM data limit of file size for wave file: < 2^(2*4) - 36
	 *            bytes (~4GB)
	 * @param channelCount
	 *            number of channels: 1 for mono, 2 for stereo, etc.
	 * @param sampleRate
	 *            sample rate of PCM audio
	 * @param bitsPerSample
	 *            bits per sample, i.e. 16 for PCM16
	 * @throws IOException
	 *             in event of an error between input/output files
	 * @see <a href=
	 *      "http://soundfile.sapp.org/doc/WaveFormat/">soundfile.sapp.org/doc/WaveFormat</a>
	 */
	private static void PCMToWAV(byte[] input, int channelCount, int sampleRate, int bitsPerSample) throws IOException {
		int myDataSize = (int) input.length;
		int myChunk2Size = myDataSize * channelCount * bitsPerSample / 8;
		int myChunkSize = 36 + myChunk2Size;
		OutputStream os;
		File dir = new File("beeps");
		dir.mkdirs();
		File newfile = new File(dir, "beep_" + sampleRate + ".wav");
		os = new FileOutputStream(newfile);

		try (OutputStream encoded = os) {
			// WAVE RIFF header
			writeToOutput(encoded, "RIFF"); // chunk id
			writeToOutput(encoded, myChunkSize); // chunk size
			writeToOutput(encoded, "WAVE"); // format

			// SUB CHUNK 1 (FORMAT)
			writeToOutput(encoded, "fmt "); // subchunk 1 id
			writeToOutput(encoded, 16); // subchunk 1 size
			writeToOutput(encoded, (short) 1); // audio format (1 = PCM)
			writeToOutput(encoded, (short) channelCount); // number of channelCount
			writeToOutput(encoded, sampleRate); // sample rate
			writeToOutput(encoded, sampleRate * channelCount * bitsPerSample / 8); // byte rate
			writeToOutput(encoded, (short) (channelCount * bitsPerSample / 8)); // block align
			writeToOutput(encoded, (short) bitsPerSample); // bits per sample

			// SUB CHUNK 2 (AUDIO DATA)
			writeToOutput(encoded, "data"); // subchunk 2 id
			writeToOutput(encoded, myDataSize); // subchunk 2 size
			encoded.write(input);
			encoded.flush();
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Size of buffer used for transfer, by default
	 */
	private static final int TRANSFER_BUFFER_SIZE = 10 * 1024;

	/**
	 * Writes string in big endian form to an output stream
	 *
	 * @param output
	 *            stream
	 * @param data
	 *            string
	 * @throws IOException
	 */
	private static void writeToOutput(OutputStream output, String data) throws IOException {
		for (int i = 0; i < data.length(); i++)
			output.write(data.charAt(i));
	}

	private static void writeToOutput(OutputStream output, int data) throws IOException {
		output.write(data);
		output.write(data >> 8);
		output.write(data >> 16);
		output.write(data >> 24);
	}

	private static void writeToOutput(OutputStream output, short data) throws IOException {
		output.write(data);
		output.write(data >> 8);
	}

	public static long copy(InputStream source, OutputStream output) throws IOException {
		return copy(source, output, TRANSFER_BUFFER_SIZE);
	}

	private static long copy(InputStream source, OutputStream output, int bufferSize) throws IOException {
		long read = 0L;
		byte[] buffer = new byte[bufferSize];
		for (int n; (n = source.read(buffer)) != -1; read += n) {
			output.write(buffer, 0, n);
		}
		return read;
	}

}
