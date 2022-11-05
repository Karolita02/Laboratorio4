package programa;

import metodos_numericos.integracion_numerica.Simpson;
import metodos_numericos.integracion_numerica.Simpson1_3;
import metodos_numericos.integracion_numerica.Simpson3_8;
import metodos_numericos.integracion_numerica.datos.Funcion;

public class Principal 
{
    public static void main(String[] args) 
    {
        Funcion funcion = (x) -> Math.sqrt(5 + Math.pow(x,3));
        var s = new Simpson(funcion);
        s.setA(0);
        s.setB(1);
        System.out.println(s.calcular()); // resp = 2.290157143

        var s1_3 = new Simpson1_3(funcion);
        s1_3.setA(0);
        s1_3.setB(1);
        s1_3.setN(4);
        System.out.println(s1_3.calcular()); // resp = 2.290454560

        var s3_8 = new Simpson3_8(funcion);
        s3_8.setA(0);
        s3_8.setB(1);
        s3_8.setN(4);
        System.out.println(s3_8.calcular()); // resp = 2.142446053
    }
    
}
