import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.IntStream;

public class encrypt {

	public static void main(String[] args) throws Exception {
		// declaration
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String filepath = null;
		byte[] masterpswd = "1234".getBytes();
		File file;
		byte[] buf = new byte[10000];
		int i = 0, j = 0;
		List<String> list;
		String filename;
		boolean padexceed = false;

		System.out.println("Enter filepath : ");
		filepath = br.readLine();
		filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
		file = new File(filepath);
		long eld = file.length() / 10000;
		System.out.println("File size : " + file.length() +"Kb");
		if (file.length() % 10000 != 0) {
			eld++;
			if (10000 - (file.length() % 10000) < 5)
				eld++;
		}
		System.out.println("File will be broken into " + eld+" files");
		list = namefinder((int) eld, filename);
		byte[] tempbuf = new byte[1];
		if (!new File("vault").exists())
			new File("vault").mkdir();
		FileInputStream fs = new FileInputStream(file);
		int c = 0;
		while (fs.read(tempbuf) != -1) {

			buf[i] = (byte) (tempbuf[0] ^ masterpswd[c]);

			if (c == masterpswd.length - 1)
				c = 0;
			else
				c++;

			if (i == 9999) {

				FileOutputStream fo = new FileOutputStream(new File("vault", list.get(j)));
				fo.write(buf);
				fo.close();
				j++;
				i = 0;

			} else
				i++;
		}
		fs.close();

		byte pad = (byte) '&';

		if (i != 0 && padexceed == false) {
			int n = i + 4;
			FileOutputStream fo = new FileOutputStream(new File("vault", list.get(j)));

			while (i < n) {

				buf[i] = (byte) (pad ^ masterpswd[c]);
				if (c == masterpswd.length - 1)
					c = 0;
				else
					c++;

				i++;
			}

			if (j == 0) {
				int x = i;
				int z = 0;

				while (x <= 9999) {

					if (z == i)
						z = 0;
					else
						z++;
					x++;

				}
				fo.write(buf);

			} else
				fo.write(buf);

		} else if (i != 0 && padexceed == true) {
			System.out.println("exception");
			int padnum = 1;
			while (padnum <= 4) {
				buf[i] = (byte) (pad ^ masterpswd[c]);

				if (i == 9999) {
					FileOutputStream fo = new FileOutputStream(new File("vault", list.get(j)));
					fo.write(buf);
					fo.close();
					i = 0;
					j++;
				} else
					i++;
				padnum++;

				if (padnum == 5) {
					FileOutputStream fo = new FileOutputStream(new File("vault", list.get(j)));
					fo.write(buf);
					fo.close();
				}

			}

		}

		System.out.println("Encryption is completed, Encrypted file/s will be in vault directory.");

	}

	public static ArrayList<String> namefinder(int x, String filename) throws Exception {
		ArrayList<String> names = new ArrayList<String>();
		byte[] buf = filename.getBytes();
		FileOutputStream fo = new FileOutputStream(new File("regigter.txt"));
		fo.write(buf);
		fo.write((byte) ',');
		fo.close();
		IntStream is = IntStream.rangeClosed(1, x);
		int[] arr = is.toArray();
		int i = 0;
		while (i < x) {
			String s = "";
			for (byte b : buf) {
				s +=(b ^ (byte) arr[i]) + "_";
			}
			names.add(s + ".txt");
			i++;
		}
		return names;
	}

}
