package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public abstract class Integracion_Numerica {

    private Funcion funcion;
    private double a, b, h;
    private int n;

    public Integracion_Numerica(Funcion funcion) {
        this.funcion = funcion;
    }

    public abstract double calcular();

    public Funcion getFuncion() {
        return funcion;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        setH((getB()-getA())/n);
    }
    
}