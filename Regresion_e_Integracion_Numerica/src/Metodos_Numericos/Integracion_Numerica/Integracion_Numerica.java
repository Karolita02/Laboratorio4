package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public abstract class Integracion_Numerica {

    private Funcion funcion;
    private double[] evaluaciones;
    private double a, b, h;
    private int n;

    public Integracion_Numerica(Funcion funcion, double a, double b) {
        this.funcion = funcion;
        this.a = a;
        this.b = b;
    }

    public abstract double calcular();

    public Funcion getFuncion() {
        return funcion;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getH() {
        return h;
    }

    private void setH(double h) {
        this.h = h;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
        setH((getB()-getA())/n);
        setEvaluaciones(new double[n+1]);
    }

    public double[] getEvaluaciones() {
        return evaluaciones;
    }

    private void setEvaluaciones(double[] evaluaciones) {
        this.evaluaciones = evaluaciones;
        for (double i = 0, x = getA(); i < evaluaciones.length; i++, x += getH()) 
            this.evaluaciones[(int)i] = getFuncion().evaluar(x);
    } 
}