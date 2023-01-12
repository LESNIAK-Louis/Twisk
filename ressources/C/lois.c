
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <time.h>
#include <unistd.h>
#include <fcntl.h>

#define RAND ((double) rand())/((double) RAND_MAX)

void delaiUniforme(int temps, int delta) {
    int bi, bs ;
    int n, nbSec ;
    bi = temps - delta ;
    if (bi < 0) bi = 0 ;
    bs = temps + delta ;
    n = bs - bi ;
    nbSec = (rand()/ (float)RAND_MAX) * n ;
    nbSec += bi ;

    usleep(nbSec * 1000000);
}

void delaiGauss(double moyenne, double ecartype){
    double u1 = RAND;
    double x;
    if(u1 != 0.)
        x = sqrt(-2 * log(u1)) * cos(2 * 3.14 * RAND) * ecartype + moyenne;
    else
        x = 0.;

    if(x < 0){
    	x = moyenne;
    }
    printf("%lf\n",x);
    //usleep(x * 1000000);
}

void delaiExponentiel(double lambda){
    double x;
    double random = RAND;
    if(random != 0.)
        x = -(((double)(log(random)))/lambda);
    else
        x = 0.;

    printf("%lf\n",x);
    //usleep(x * 1000000);
}

void delaiLoi(int loi, int temps, int ecart_temps){

	switch(loi){
		case 1:
			delaiGauss(temps, ecart_temps);
		case 2:
			delaiExponentiel(1.0 / temps);
		default:
			delaiUniforme(temps, ecart_temps);
	}
}

int main(){
    srand(time(NULL));

    int fichier = open("fich.dot", O_WRONLY|O_CREAT);

    dup2(fichier, STDOUT_FILENO);

    for(int i = 0; i < 1000; i++){
        delaiGauss(4.0, 2.0);
        //delaiExponentiel(0.25);
        //delaiLoi(1, 4, 2);
        //printf("Le client %d est entre\n", i);
    }

    close(fichier);

    return 0;
}
