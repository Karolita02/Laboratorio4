package metodos_numericos.integracion_numerica;

import metodos_numericos.integracion_numerica.datos.Funcion;

public class Simpson1_3 extends Integracion_Numerica{

    

    public Simpson1_3(Funcion funcion) {
        super(funcion);
    }

    @Override
    public double calcular() {
        return getH()/3 * (
            getFuncion().evaluar(getA()) + 
            2 * sumatoria(true) + 
            4 * sumatoria(false) +
            getFuncion().evaluar(getB())
        );
        
    }

    public double sumatoria(boolean dePares){
        double suma = 0;
        for (double x = dePares ? 2*getH() : getA() + getH(); x < getB(); x += 2*getH())
            suma += getFuncion().evaluar(x);
        return suma;
    }
}
