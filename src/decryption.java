package leetcode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class decryption {

	public static void main(String[] args) throws Exception {

		byte[] masterpswd = "1234".getBytes();
		byte pad = (byte) '&';
		List<Byte> bufhold = new ArrayList<>();
		File vault = new File("vault");
		if (!vault.exists())
			return;
		FileInputStream re = new FileInputStream(new File("regigter.txt"));
		byte[] temp = new byte[1];
		ArrayList<String> pack = new ArrayList<>();
		String str = "";
		while (re.read(temp) != -1) {
			if ((char) temp[0] == ',') {
				pack.add(str);
				str = "";
			} else {
				str += "" + (char) temp[0];

			}
		}
		System.out.println(pack);
		for (String s : pack) {
			FileOutputStream fo = new FileOutputStream(new File(s));
			String[] list = vault.list();
			int key = 1, current = 0;

			for (int i = 0; i < list.length; i++) {
				if (list[i] == "")
					continue;
				byte[] listbyte = list[i].getBytes();
				String s2 = "", s3 = "";
				for (byte b : listbyte) {

					if ((char) b == '_') {
						s2 += (char) ((byte) Integer.parseInt(s3) ^ (byte) key);

						s3 = "";
					} else {
						s3 += (char) b;

					}
				}
				if (s2.equals(s)) {
					System.out.println("key val" + key + " s2" + s2);
					key++;

					FileInputStream fi = new FileInputStream(new File("vault", list[i]));
					while (fi.read(temp) != -1) {

						temp[0] = (byte) (temp[0] ^ masterpswd[current]);

						if (current == masterpswd.length - 1)
							current = 0;
						else
							current++;

						if (pad == temp[0]) {

							bufhold.add(temp[0]);
							if (bufhold.size() == 4)
								break;
						} else {
							if (bufhold.size() != 0) {
								bufhold.add(temp[0]);
								for (Byte b : bufhold) {
									fo.write(b.byteValue());
								}
								bufhold.clear();
							} else
								fo.write(temp[0]);

						}
					}
					System.out.println("\n" + new File(s).length());
					list[i] = "";
					i = -1;

				}

			}

		}
		System.out.println("decryption complete:");
	}

}
