package com.shayanamani;

import java.io.*;
import java.util.Random;

public class CBCEncrypt {
    public static void main(String[] args) throws IOException {

        int[] img = new int[2];
        boolean firstTime = true;  //to know when to apply IV or the previous encrypted block
        int[] cipher = new int[2];
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
                img[0] = dataIn.readInt();
                check = true;
                img[1] = dataIn.readInt();
                if (firstTime) {
                    //if true, the block is passed with IV to be encrypted by TEA algorithm
                    cipher = cbc.encrypt(img, IV);
                    firstTime = false;  //set firstTime to false sense IV is only encrypted in the first block
                } else
                    //pass the block with the previous encrypted block
                    cipher = cbc.encrypt(img, cipher);

                dataOut.writeInt(cipher[0]);
                dataOut.writeInt(cipher[1]);
                check = false;
            } catch (EOFException e) {
                //exception is thrown if the file ends and dataIn.readInt() is executed
                // then see if last block or half of last block is still not encrypted.
                System.out.println("Check:" + check);
                dataOut.writeInt(img[0]);
                if (!check) {
                    dataOut.writeInt(img[1]);
                }

            }

        }

        dataIn.close();
        dataOut.close();

        System.out.println("TEA Encryption in CBC mode done");
    }

}
