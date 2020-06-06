package com.shayanamani;

import java.io.*;

public class ECBDecrypt {

    public static void main(String[] args) throws IOException {

        int[] img = new int[2];  //img Variable will contain the block to be encrypted
        int[] plain;
        boolean check = true;

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
        ECBMode ecb = new ECBMode(key);  //instantiating a TEA class

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
                plain = ecb.decrypt(img);
                dataOut1.writeInt(plain[0]);
                dataOut1.writeInt(plain[1]);
                check = false;
            } catch (EOFException e) {
                //exception is thrown if the file ends and dataIn.readInt() is executed
                // then see if last block or half of last block is still not encrypted
                System.out.println("Check:" + check);
                dataOut1.writeInt(img[0]);
                if (!check) {
                    dataOut1.writeInt(img[1]);
                }
            }
        }

        dataIn1.close();
        dataOut1.close();

        System.out.println("TEA Decryption in ECB mode done");
    }
}
