package programa;

import interfaz.Ventana;
import metodos_numericos.integracion_numerica.Simpson;
import metodos_numericos.integracion_numerica.Simpson1_3;
import metodos_numericos.integracion_numerica.Simpson3_8;
import metodos_numericos.integracion_numerica.datos.Funcion;

public class Principales 
{
    public static void main(String[] args) 
    {
        // Funcion funcion = (x) -> Math.sqrt(5 + Math.pow(x,3));
        // String textoFuncion = "(5 + x^3)^(1/2)";
        // int a = 0, b = 1;

        new Ventana();

        // var s = new Simpson(funcion, a, b);
        // System.out.println(s.calcular()); // resp = 2.2901573

        // int n = 6;
        
        // var s1_3 = new Simpson1_3(funcion,a,b);
        // s1_3.setN(n) ;
        // System.out.println(s1_3.calcular()); // resp = 2.2904546

        // var s3_8 = new Simpson3_8(funcion,a,b);
        // s3_8.setN(n);
        // System.out.println(s3_8.calcular()); // resp = 2.2904658
    }
    
}
