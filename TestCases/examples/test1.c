#include<stdio.h>
#include<string.h>
#include<stdlib.h>

int testInt(int a);

int main(int argc, char** argv)
{
    
    int a = atoi(argv[1]);
    testInt(a);
    return 1;
}



int testInt(int a)
{
    int b = 0;
    if(a == 1) {
        b = 1;
    }
    return b;
    
}


