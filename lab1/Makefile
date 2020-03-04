all: bmpinfo run_bmp

bmpinfo: src/bmpinfo.c src/bmp.c src/bmp.h
	gcc src/bmpinfo.c src/bmp.c -o bmpinfo

run_bmp: bmpinfo
	./bmpinfo res/alice.bmp

run_crypto: src/tea_crypto.c
	gcc src/tea_crypto.c -o teacrypto


clean:
	pwd
	rm -rf bmpinfo
