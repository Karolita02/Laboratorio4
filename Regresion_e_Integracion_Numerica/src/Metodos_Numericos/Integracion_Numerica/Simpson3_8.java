package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public class Simpson3_8 extends Integracion_Numerica{

    public Simpson3_8(Funcion funcion, double a, double b) {
        super(funcion, a, b);
    }

    @Override
    public double calcular() {
        return 3*getH()/8 * (
            getEvaluaciones()[0] + 
            2 * sumatoria(true) +
            3 * sumatoria(false) + 
            getEvaluaciones()[getN()]
        );
    }

    public double sumatoria(boolean deMultiplo3){
        double suma = 0;
        for (int i = deMultiplo3 ? 3 : 1; i < getN() ; i += deMultiplo3 ? 3 : 1)
            suma += deMultiplo3 || i % 3 != 0 ? getEvaluaciones()[i] : 0; 
            // si es multiplo de 3  o no son multiplos de 3, procedemos normal  // sino 0 
        return suma;
    }
}