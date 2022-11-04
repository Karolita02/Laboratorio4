package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public abstract class Integracion_Numerica {

    private Funcion funcion;
    private double ultimo, anterior;

    public Integracion_Numerica(Funcion funcion) {
        this.funcion = funcion;
        ultimo = funcion.evaluar(0);
    }

    public double errorRelativo(){
        return (ultimo-anterior)/ultimo*100;
    }

    public abstract void calcular();

    public Funcion getFuncion() {
        return funcion;
    }

    public double getUltimo() {
        return ultimo;
    }

    public void setUltimo(double ultimo) {
        this.ultimo = ultimo;
    }

    public double getAnterior() {
        return anterior;
    }

    public void setAnterior(double anterior) {
        this.anterior = anterior;
    }
}