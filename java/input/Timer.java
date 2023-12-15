package input;
import observer.*;

import java.util.ArrayList;

public class Timer implements Sujeto,Observador {
    private final ArrayList<Observador> observadores = new ArrayList<>();
    private float time;
    private final int minTime;

    /**
     * Para usar como un timer por polling
     * @param minTime - tiempo minimo a esperar
     */
    public Timer (int minTime){
        time = 0;
        this.minTime = minTime;
    }

    /**
     * Para usar como un timer que notifica cuando se cumple el tiempo
     * y se produce algun evento
     * @param minTime - tiempo minimo a esperar
     * @param event   - evento a esperar
     */
    public Timer (int minTime, Sujeto event){
        time = 0;
        this.minTime = minTime;
        event.addObservador(this);
    }

    public void update(float dt){
        time+=dt;
    }
    public void reset(){
        time=0;
    }
    public boolean isTime(){
        return time >= minTime;
    }
    @Override
    public void addObservador(Observador o) {
        observadores.add(o);
    }

    @Override
    public void removeObservador(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notifyObservadores () {
        for (int i = 0; i < observadores.size(); i++) {
            observadores.get(i).update();
        }
    }

    /**
     * Si ocurrio el evento que se estaba esperando
     * y se habia cumplido tambien el tiempo de espera,
     * se notifica a todos los observadores.
     */
    @Override
    public void update() {
        if (!isTime()) return;
        notifyObservadores();
        reset();
    }
}
