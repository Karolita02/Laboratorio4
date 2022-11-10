package programa;

import interfaz.Ventana;
// import metodos_numericos.integracion_numerica.Simpson;
// import metodos_numericos.integracion_numerica.Simpson1_3;
// import metodos_numericos.integracion_numerica.Simpson3_8;
// import metodos_numericos.integracion_numerica.datos.Funcion;
import metodos_numericos.regresion.Polinomial_2do_Grado;
import metodos_numericos.regresion.Tabla;

public class Principal 
{
    public static void main(String[] args) 
    {
        
        // Funcion funcion = (x) -> Math.sqrt(5 + Math.pow(x,3));
        // String textoFuncion = "(5 + x^3)^(1/2)";
        // int a = 0, b = 1;

        // new Ventana();

        Tabla tabla = new Tabla();
        
        tabla.x = new double[] {30,28,32,25,25,25,22,24,35,40};
        tabla.y = new double[] {25,30,27,40,42,40,50,45,30,25};


        var regresion = new Polinomial_2do_Grado(tabla);
        regresion.calcularCoeficientes();

        System.out.println(String.format("a0 = %.4f, a1 = %.4f, a2 = %.4f", regresion.a[0], regresion.a[1], regresion.a[2]));
        System.out.println("r2 = " + regresion.getR2() + ", r = " + regresion.getR());
        System.out.println("modelo = " + regresion.getModelo());

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
