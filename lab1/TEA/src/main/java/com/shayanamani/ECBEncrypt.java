package com.shayanamani;

import java.io.*;

public class ECBEncrypt {

    public static void main(String[] args) throws IOException {

        int[] img = new int[2];  //img Variable will contain the block to be encrypted
        int[] cipher;
        boolean check = true;  //Check variable used to know where did the file end

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

        DataInputStream dataIn = new DataInputStream(new FileInputStream(args[1]));
        DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(args[2]));


        /* Skipping the first 10 blocks
         * each block is 64 bit. Thus, ReadInt() is applied twice
         * because ReadInt() return 32 bits
         */
        for (int i = 0; i < 10; i++) {
            if (dataIn.available() > 0) {
                img[0] = dataIn.readInt();
                img[1] = dataIn.readInt();
                dataOut.writeInt(img[0]);
                dataOut.writeInt(img[1]);
            }
        }

        while (dataIn.available() > 0) {
            try {
                img[0] = dataIn.readInt();        //left sub block
                check = true;
                img[1] = dataIn.readInt();        //right sub block
                cipher = ecb.encrypt(img);        //Passing the block to TEA algorithm to encrypt it
                dataOut.writeInt(cipher[0]);    //writing back the encrypted block
                dataOut.writeInt(cipher[1]);
                check = false;
            } catch (EOFException e) {
                //exception is thrown if the file ends and dataIn.readInt() is executed
                // then see if last block or half of last block is still not encrypted
                System.out.println("Check:" + check);
                dataOut.writeInt(img[0]);
                if (!check) {
                    dataOut.writeInt(img[1]);
                }
            }
        }

        dataIn.close();
        dataOut.close();

        System.out.println("TEA Encryption in ECB mode done");
    }
}