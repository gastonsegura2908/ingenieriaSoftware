package observer;

public interface Sujeto {
    public void addObservador(Observador o);
    public void removeObservador(Observador o);
    public void notifyObservadores();
}
