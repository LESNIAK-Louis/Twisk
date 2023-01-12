#include "twisk_simulation_Simulation.h"
#include "def.h"

/* -------------------------------------------------------------------------------------------------------------------------------- */
// Export de la fonction start_simulation de C en java
JNIEXPORT jintArray JNICALL Java_twisk_simulation_Simulation_start_1simulation (JNIEnv *env, jobject obj, jint nbEtapes, jint nbGuichets, jint nbClients, jintArray tabJetonsGuichet)
{
  // récupération du tableau d'entiers passé en argument de la fonction
  jint *tab = (*env)->GetIntArrayElements(env, tabJetonsGuichet, 0);

  int* tabPid = start_simulation(nbEtapes, nbGuichets, nbClients, tab) ;

  // recopie du tableau temporaire dans la structure java qui va être retournée par cette fonction
  jintArray res = (*env)->NewIntArray(env, nbClients);
  (*env)->SetIntArrayRegion(env, res, 0, nbClients, tabPid);

  return res ;
}

/* -------------------------------------------------------------------------------------------------------------------------------- */
// Export de la fonction ou_sont_les_clients de C en java
JNIEXPORT jintArray JNICALL Java_twisk_simulation_Simulation_ou_1sont_1les_1clients (JNIEnv *env, jobject obj, jint nbEtapes, jint nbClients)
{
    int* tableau = ou_sont_les_clients(nbEtapes, nbClients) ;

    // recopie du tableau temporaire dans la structure java qui va être retournée par cette fonction
    jintArray res = (*env)->NewIntArray(env, nbEtapes * (nbClients + 1));
    (*env)->SetIntArrayRegion(env, res, 0, nbEtapes * (nbClients + 1), tableau);

    return res ;
}

/* -------------------------------------------------------------------------------------------------------------------------------- */
// Export de la fonction nettoyage de C en java
JNIEXPORT void JNICALL Java_twisk_simulation_Simulation_nettoyage (JNIEnv *env, jobject obj)
{
  nettoyage() ;
  return ;
}

