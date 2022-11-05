package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public class Simpson extends Integracion_Numerica{

    public Simpson(Funcion funcion) {
        super(funcion);
    }

    @Override
    public double calcular() {
        return (getB() - getA()) / 6.0 * (
            getFuncion().evaluar(getA()) + 
            4 * getFuncion().evaluar((getA() + getB()) / 2.0) + 
            getFuncion().evaluar(getB())
        );
    }    
}