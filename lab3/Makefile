CFLAGS = -fno-stack-protector -g3 -std=c11 -pedantic -Wall -Werror

all: genuine

genuine: bof.c
	$(CC) $(CFLAGS) $+ -o $@

run-full:
	./genuine full.ver

run-trial:
	./genuine trial.ver

clean:
	rm -rf genuine

