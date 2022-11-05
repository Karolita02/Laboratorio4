package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public class Simpson3_8 extends Integracion_Numerica{

    public Simpson3_8(Funcion funcion) {
        super(funcion);
    }

    @Override
    public double calcular() {
        return 3*getH()/8 * (
            getFuncion().evaluar(getA()) + 
            2 * sumatoria(true) +
            3 * sumatoria(false) + 
            getFuncion().evaluar(getB())
        );
    }

    public double sumatoria(boolean deMultiplo3){
        double suma = 0;
        for (int x = deMultiplo3 ? 3 : 1; x < getN(); x += deMultiplo3 ? 3 : 1)
            suma += deMultiplo3 ? getFuncion().evaluar(x) : // si es multiplo de 3 procedemos normal
                x % 3 != 0 ? getFuncion().evaluar(x) : 0; // sino solo los q no son multiplos de 3
        return suma;
    }
}