#include "def.h"

#define sas_entree 0
#define sas_sortie 1

void simulation(int ids){ 
    switch(CLIENT_NUMBER)
    {
        case 1:
        {
            int guichet = 2;
            int activite1 = 3;
            int activite2 = 4;
            int num_sem_guichet = 1;

            entrer(sas_entree);
            delai(6,3);
            transfert(sas_entree, guichet);
            P(ids, num_sem_guichet);
            transfert(guichet, activite1);
            delai(8,2);
            V(ids, num_sem_guichet);
            transfert(activite1, activite2);
            delai(8,2);
            transfert(activite2,sas_sortie);
        }
        break;
        case 2:
        {
        }
        break;
        default:
        break;
    }  
}