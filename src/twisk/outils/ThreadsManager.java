package twisk.outils;

import javafx.concurrent.Task;

import java.util.ArrayList;

public class ThreadsManager {

    private static ThreadsManager instance = new ThreadsManager();
    private ArrayList<Thread> threads;

    /**
     * Constructeur privé
     */
    private ThreadsManager(){ threads = new ArrayList<Thread>(); }

    /**
     * Getter de l'instance de la classe
     * @return instance
     */
    public static ThreadsManager getInstance(){ return instance;}

    /**
     * Retourne le nombre de threads gérés par le manager
     * @return nombre de threads
     */
    public int getNbThreads(){
        return threads.size();
    }

    /**
     * Permet de créer un thread et de le lancer
     * @param task
     */
    public void lancer(Task task)
    {
        Thread thread = new Thread(task);
        this.threads.add(thread);
        thread.start();

    }

    /**
     * Permet de tuer tous les threads
     */
    public void detruireTout()
    {
        for(Thread thread : threads)
            thread.interrupt();
        threads.clear();
    }
}
