#include<stdio.h>
#include<string.h>
#include<stdlib.h>

int testCmp(char *g, int h);

int main(int argc, char** argv)
{
    
    int h = atoi(argv[2]);
    char* g = argv[1];
    int m = testCmp(g, h);
    printf("return:%d\n", m);
    return 1;
}




int testCmp(char* g, int h){
    char* h1 = "abc";
    char* h2 = "def";
    int d = 0;
    if(strcmp(g, h1) == 0) {
        d = 1;
    }else if (strcmp(g, h2) == 0){
        d = 2;
    }else {
        if(h > 1) {
            int tmp = h % 2;
            			if(tmp <= 0) {
                            d = tmp;
            			}else {
                            d = tmp - 1;
            			}
            
        }
    }
    return d;
}