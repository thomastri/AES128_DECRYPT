
/**
 * @author Xunhua Wang (wangxx@jmu.edu). All rights reserved.
 * @date 09/25/2014
 */

public class AES_Structure {
	
	public void crack (int start, int end) {

		byte[] myguessedkey = new byte[16];
		for (int i = 0; i < 16; i ++) myguessedkey[i] = (byte) 0x00; // This may be unnecessary but to make sure ...
		myguessedkey[15] = (byte) 0x03;

		for (int i = start; i <= end; i++) {
			for (int j = 0; j <= 255; j++) {
				for (int k = 0; k <= 255; k++) {
					for (int x = 0; x <= 255; x++) {
						for (int y = 6; y <= 255; y+=8) { // Why does y start from 6 and increment by 8? Think!
							myguessedkey[0] = (byte) i;
	 						myguessedkey[1] = (byte) j;
	 						myguessedkey[2] = (byte) k; 	
							myguessedkey[3] = (byte) x;
	 						myguessedkey[4] = (byte) y;

							//
							// In my test run, print out myguessedkey to make sure that it is correct
							// This line of code prints out and slows things down	
							//
							//	TODO: the following two lines should be commented out when you start running your code
							//
							for (byte ki : myguessedkey) System.out.print (String.format("%02X", ki & 0xff));
							System.out.println ("");

							//
							// Next, we will test whether this guessed key is good or not
							// TODO: YOU need to fill in the details here
							//
							boolean isCorrectKey = testOneKey (myguessedkey);

							if (isCorrectKey) return;
						}
					}
				}
			}
		}
	}

	//
	// Method UNFINISHED
	//
	public boolean testOneKey (byte[] inKey) {
		//
		// Is this key correct? How to find out?
		//

		//
		// Step 0. Prepare the IV and ciphertext blocks
		//
		String ivStr = "FEE6CB5BE4BBFC3DC623B8FE9F0006B2";
		String c1Str = "C2B8B6A64DB0E2101B147381442C271C";
		String c2Str = "33845FD95F0C589680CB822C6A0CB950";
		String c3Str = "F4190D6C5D15FC968756DF5E229F32FA";
		String c4Str = "C8406CF6E3C812EED825CB15970AFBB7";

		byte[] iv = hexStringToByteArray (ivStr);
		byte[] c1 = hexStringToByteArray (c1Str);
		byte[] c2 = hexStringToByteArray (c2Str);
		byte[] c3 = hexStringToByteArray (c3Str);
		byte[] c4 = hexStringToByteArray (c4Str);

		//
		// Step 1. Prepare for the AES round keys
		//
		Object aesRoundKeys = null;

		try {
			aesRoundKeys = Rijndael_Algorithm.makeKey(Rijndael_Algorithm.DECRYPT_MODE, inKey);
		} catch (Exception ex) {
			ex.printStackTrace ();

			return false;
		}

		//
		// Step 2. Now decrypt ciphertext block 1 _and_ XOR with IV; UNFINISHED
		//	
		byte[] returnArray = Rijndael_Algorithm.blockDecrypt2(c1, 0, aesRoundKeys);
		// TODO: XOR with IV and test whether the cleartext block is good or not. If not, return false
		//	Otherwise, continue
		//

		//
		// Step 3. Now decrypt ciphertext block 2 _and_ XOR with c1; UNFINISHED
		//	
		byte[] returnArray2 = Rijndael_Algorithm.blockDecrypt2(c2, 0, aesRoundKeys);
		// TODO: XOR with c1 and test whether the cleartext block is good or not. If not, return false
		//	Otherwise, continue
		//

		//
		// Step 4. Now decrypt ciphertext block 3 _and_ XOR with c2; UNFINISHED
		//	
		byte[] returnArray3 = Rijndael_Algorithm.blockDecrypt2(c3, 0, aesRoundKeys);
		// TODO: XOR with c2 and test whether the cleartext block is good or not. If not, return false
		//	Otherwise, continue
		//

		//
		// Step 5. Now decrypt ciphertext block 4 _and_ XOR with c3; UNFINISHED
		//	
		byte[] returnArray4 = Rijndael_Algorithm.blockDecrypt2(c4, 0, aesRoundKeys);
		// TODO: XOR with c3 and test whether the cleartext block is good or not. If not, return false
		//	Otherwise, found the key; print the key and all four plaintext blocks to a file
		//
		
		//
		// The following line should be removed after you complete the code
		//
		return false;
		
	}

	// 
	// The following method is copied from Medovar and Sharp. Thank them for the code when you get a chance
	// 
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}

		return data;
	}

	public static void main (String args[]) {
		if (args.length < 2) {
			System.out.println ("Use java AESChallengeCrackerWithFiveLoops start end");
			return;
		}

		try {
			int start = Integer.parseInt (args[0]);
			int end = Integer.parseInt (args[1]);

			AES_Structure acc = new AES_Structure ();

			acc.crack (start, end);
		} catch (Exception ex) {
			ex.printStackTrace ();
		}
	}
}