package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public class Simpson1_3 extends Integracion_Numerica{

    

    public Simpson1_3(Funcion funcion, double a, double b) {
        super(funcion, a, b);
    }

    @Override
    public double calcular() {
        return getH()/3 * (
            getEvaluaciones()[0] + 
            2 * sumatoria(true) + 
            4 * sumatoria(false) +
            getEvaluaciones()[getN()]
        );
        
    }

    public double sumatoria(boolean dePares){
        double suma = 0;
        for (int i = dePares ? 2 : 1; i < getN(); i += 2)
            suma += getEvaluaciones()[i];
        return suma;
    }
}
