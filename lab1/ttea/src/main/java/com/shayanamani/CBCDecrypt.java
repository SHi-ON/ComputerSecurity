package com.shayanamani;

import java.io.*;
import java.util.Random;

public class CBCDecrypt {
    public static void main(String[] args) throws IOException {

        int[] img = new int[2];
        int[] copyCipher = new int[2];
        boolean firstTime = true;  //to know when to apply IV or the previous encrypted block
        int[] plain = new int[2];
        boolean check = true;  //to catch where the reading from the file is stopped

        Random rand = new Random();

        String raw_key = args[0];

        if (raw_key.length() != 8) {
            System.out.println("Use a key of length 8");
            System.exit(0);
        }

        int k0 = raw_key.charAt(0) << 16 | raw_key.charAt(1);
        int k1 = raw_key.charAt(2) << 16 | raw_key.charAt(3);
        int k2 = raw_key.charAt(4) << 16 | raw_key.charAt(5);
        int k3 = raw_key.charAt(6) << 16 | raw_key.charAt(7);

        // putting the key parts together in an array
        int[] key = {k0, k1, k2, k3};
        CBCMode cbc = new CBCMode(key); //instantiating a TEA class in CBC mode

        int IV[] = {rand.nextInt(), rand.nextInt()};  //generating a random IV

        DataInputStream dataIn1 = new DataInputStream(new FileInputStream(args[1]));
        DataOutputStream dataOut1 = new DataOutputStream(new FileOutputStream(args[2]));

        /* Skipping the first 10 blocks
         * each block is 64 bit. Thus, ReadInt() is applied twice
         * because ReadInt() return 32 bits
         */
        for (int i = 0; i < 10; i++) {
            if (dataIn1.available() > 0) {
                img[0] = dataIn1.readInt();
                img[1] = dataIn1.readInt();
                dataOut1.writeInt(img[0]);
                dataOut1.writeInt(img[1]);
            }
        }

        while (dataIn1.available() > 0) {
            try {
                img[0] = dataIn1.readInt();
                check = true;
                img[1] = dataIn1.readInt();

                if (firstTime) {
                    //if true, the first block is passed with IV to be decrypted
                    plain = cbc.decrypt(img, IV);
                    firstTime = false;  //set first time to false
                } else
                    //if false, the block is passed with the previously encrypted block
                    plain = cbc.decrypt(img, copyCipher);

                dataOut1.writeInt(plain[0]);
                dataOut1.writeInt(plain[1]);

                copyCipher[0] = img[0];  //Save the previously encrypted block in copyCipher to use it
                copyCipher[1] = img[1];

                check = false;
            } catch (EOFException e) {
                //exception is thrown if the file ends and dataIn.readInt() is executed
                // then see if last block or half of last block is still not encrypted.
                System.out.println("Check:" + check);
                dataOut1.writeInt(img[0]);
                if (!check) {
                    dataOut1.writeInt(img[1]);
                }
                ;
            }

        }

        dataIn1.close();
        dataOut1.close();

        System.out.println("TEA Decryption in CBC mode done");
    }

}
