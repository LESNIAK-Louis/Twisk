#ifndef DEF_H
#define DEF_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

// fonctions déclarées dans programmeC, à utiliser dans le code du client *******************************************************

void delai(int temps, int delta) ;

void entrer(int etape) ;
void transfert(int source, int destination) ;

int P(int semid, int numero) ;
int V(int semid, int numero) ;

int* start_simulation(int nbEtapes, int nbServices, int nbClients, int *tabJetonsServices) ;
int* ou_sont_les_clients(int nbEtapes, int nbClients) ;
void nettoyage() ;

// la fonction déclarée dans le code des clients ****************************************************************************

void simulation(int ids) ;

#endif
