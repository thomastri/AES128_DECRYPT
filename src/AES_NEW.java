import java.security.InvalidKeyException;
import java.util.*;
import java.io.*;

// Brandon Senter & Thomas Le
// CS 457: Information Security
// Xunhua Wang; 09.27.2015

public class AES_NEW {

	public static String correctKey = "";

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();

//		Scanner in = new Scanner(System.in);

		String fileName = "Le_Senter.txt";
		File out = new File(fileName);

		if (out.exists() == false) {
			out.createNewFile();
		}

//		System.out.println("Where to start?");
//		int start = in.nextInt();
//
//		System.out.println("Where to end?");
//		int end = in.nextInt();
//
//		in.close();
//
//		System.out.println("Starting AES_Decrpytion.java");

		decrypt(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		PrintWriter writer = new PrintWriter(fileName, "UTF-8");
		writer.println("Correct Key: " + correctKey);
		writer.println("Total runtime (ms): " + totalTime);
		writer.close();

//		System.out.println("Done.");
	}

	public static void decrypt(int start, int end) throws InvalidKeyException {
		byte[] potentialKey = new byte[16];
		potentialKey[15] = 0x03;
		for (int i = 5; i <= 14; i++)
			potentialKey[i] = 0x00;

		for (int i = start; i <= end; i++) {
			potentialKey[0] = (byte) i;
			for (int j = 0; j <= 255; j++) {
				potentialKey[1] = (byte) j;
				for (int k = 0; k <= 255; k++) {
					potentialKey[2] = (byte) k;
					for (int l = 0; l <= 255; l++) {
						potentialKey[3] = (byte) l;
						for (int m = 6; m <= 254; m += 8) {
							potentialKey[4] = (byte) m;
							
//							for (byte ki : potentialKey) System.out.print (String.format("%02X", ki & 0xff));
//							System.out.println ("");
							
							boolean isCorrect = testKey(potentialKey);
							if (isCorrect) {
								correctKey = potentialKey.toString();
								return;
							}
						} // m-loop
					} // l-loop
				} // k-loop
			} // j-loop
		} // i-loop
	} // decrypt()

	public static boolean testKey(byte[] inKey) throws InvalidKeyException {

		String IV_string = "5BF16CF65F7D2E547AAF6522342C30D8";
		String C1_string = "E84811608B52001CDD9FC48334433B6F";
		String C2_string = "C87C0B94F6A7747383881E18A6518DDF";
		String C3_string = "3E64B928FA5EFC67275D77D18F505335";
		String C4_string = "202F8CD7D8984917681CB1686D3239FB";

		byte[] iv = hexStringToByteArray(IV_string);
		byte[] c1 = hexStringToByteArray(C1_string);
		byte[] c2 = hexStringToByteArray(C2_string);
		byte[] c3 = hexStringToByteArray(C3_string);
		byte[] c4 = hexStringToByteArray(C4_string);

		Object roundKeys = null;

		roundKeys = Rijndael_Algorithm.makeKey(Rijndael_Algorithm.DECRYPT_MODE, inKey);

		byte[] returnArray = Rijndael_Algorithm.blockDecrypt2(c1, 0, roundKeys);

		if (keyCheck(iv, returnArray)) {
			byte[] returnArray2 = Rijndael_Algorithm.blockDecrypt2(c2, 0, roundKeys);

			if (keyCheck(returnArray, returnArray2)) {
				byte[] returnArray3 = Rijndael_Algorithm.blockDecrypt2(c3, 0, roundKeys);

				if (keyCheck(returnArray2, returnArray3)) {
					byte[] returnArray4 = Rijndael_Algorithm.blockDecrypt2(c4, 0, roundKeys);

					if (keyCheck(returnArray3, returnArray4))
						return true;
				}
			}
		}
		return false;
	}

	public static boolean keyCheck(byte[] previous, byte[] current) {
		if (checkASCII(previous, xor(previous, current)))
			return true;
		return false;
	}

	public static byte[] xor(byte[] previous, byte[] current) {

		for (int x = 0; x < current.length; x++) {
			current[x] = (byte) (current[x] ^ previous[x]);
		}
		return current;
	}

	public static boolean checkASCII(byte[] previous, byte[] current) {

		for (int y = 0; y < current.length; y++) {
			if ((current[y] < 32) || (current[y] >= 127)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}

		return data;
	}

}