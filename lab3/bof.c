#include <stdio.h>
#include <string.h>

int chkserial(char *s)
{
    char buffer[16];
    strcpy(buffer, s);

    return strcmp(buffer, "cs727827");
}

void fullversion()
{
    printf("Thanks for purchasing! Enjoy the full-version software!\n");
}

void trialversion()
{
    printf("This is a trial version. Please purchase the full version to enable all features!\n");
}

int main(int argc, char **argv)
{
    char buf[512];
    FILE *f = fopen(argv[1], "r");

    fread(buf, 512, 1, f);

    printf("buf: %s\n", buf);

    fullversion();

//    if (chkserial(buf) == 0)
//        fullversion();
//    else
//        trialversion();

    return 0;
}
