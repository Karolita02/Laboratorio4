package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public class Simpson extends Integracion_Numerica{

    public Simpson(Funcion funcion, double a, double b) {
        super(funcion, a, b);
        setN(1);
    }

    @Override
    public double calcular() {
        return (getB() - getA()) / 6 * (
            getEvaluaciones()[0] + 
            4 * getFuncion().evaluar((getA() + getB()) / 2) + 
            getEvaluaciones()[getN()]
        );
    }    
}