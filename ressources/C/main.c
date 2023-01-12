#include "def.h"
int* charger_client(int* NB_ETAPES, int* NB_GUICHETS, int* NB_CLIENTS);

int main(int argc, char** argv){

    int NB_CLIENTS = 0;
    int NB_GUICHETS = 0;
    int NB_ETAPES = 0;
    int* tabJetonsGuichet = charger_client(&NB_ETAPES, &NB_GUICHETS, &NB_CLIENTS);;
    
    int* tabStart = start_simulation(NB_ETAPES, NB_GUICHETS, NB_CLIENTS, tabJetonsGuichet);        
    printf("les clients: ");
    for(int i = 0; i < NB_CLIENTS; i++){
        printf("%d, ", tabStart[i]);
    }
    printf("\n");

    int nbClientsSortie = 0;
    while(nbClientsSortie < NB_CLIENTS){
        int* tabClients = ou_sont_les_clients(NB_ETAPES, NB_CLIENTS);
        nbClientsSortie = tabClients[NB_CLIENTS+1];
        for(int i = 0; i < NB_ETAPES; i++){
            int n = tabClients[i * (NB_CLIENTS+1)];
            printf("\netape %d - %d clients:  ", i, n);
            for(int j = 0; j < n; j++){
                printf("%d, ", tabClients[j+(i*(NB_CLIENTS+1))+1]);
            }
        }
        printf("\n");
        free(tabClients);
        sleep(1);
    }

    nettoyage();
    
    return 0;
}

int* charger_client(int* NB_ETAPES, int* NB_GUICHETS, int* NB_CLIENTS)
{
    switch(CLIENT_NUMBER)
    {
        case 1:
            *NB_CLIENTS = 1;
            *NB_GUICHETS = 1;
            *NB_ETAPES = 5;
        break;
        case 2:
        break;
        default:
        break;
    }
    int *tabJetonsGuichet = NULL;
    if(*NB_GUICHETS > 0)
    {  
        tabJetonsGuichet = (int*)malloc(*NB_GUICHETS * sizeof(int));
        for(int i = 0; i < *NB_GUICHETS; i++)
            tabJetonsGuichet[i] = 1;
    }
    return tabJetonsGuichet;
}