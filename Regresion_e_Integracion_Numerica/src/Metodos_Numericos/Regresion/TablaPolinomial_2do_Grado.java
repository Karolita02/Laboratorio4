package metodos_numericos.regresion;

import metodos_numericos.integracion_numerica.datos.Funcion;

public class TablaPolinomial_2do_Grado extends Tabla {

    double[] x2, x3, x4, xy, x2y, y_yMedia2, sr;

    public TablaPolinomial_2do_Grado(Tabla tabla) {
        x = tabla.x;
        y = tabla.y;    
        inicializarTodasLasColumnas();
        calcular_y_media();
        calcular((x) -> Math.pow(x, 2), x2, x);
        calcular((x) -> Math.pow(x, 3), x3, x);
        calcular((x) -> Math.pow(x, 4), x4, x);
        calcular(xy, x, y);
        calcular(x2y, x2, y);
        calcular(y_yMedia2, y, yMedia);
    }
    
    //                   funcion = x^2    columnaObjetivo = x2       columnaRequerida = x
    public void calcular(Funcion funcion, double[] columnaObjetivo, double[] columnaRequerida){
        for (int fila = 0; fila < columnaObjetivo.length; fila++)
            columnaObjetivo[fila] = funcion.evaluar(columnaRequerida[fila]); 
    }

    public void calcular(double[] columnaObjetivo, double[] columnaRequerida, double[] columnaMultiplicada){
        for (int fila = 0; fila < columnaObjetivo.length; fila++)
            columnaObjetivo[fila] = columnaRequerida[fila] * columnaMultiplicada[fila];
    }

    public void calcular(double[] columnaObjetivo, double[] columnaRequerida, double valorRestado){
        for (int fila = 0; fila < columnaObjetivo.length; fila++)
            columnaObjetivo[fila] = Math.pow(columnaRequerida[fila] - valorRestado, 2);
    }

    public double[][] getEcuaciones(){
        double sumaX = sumatoria(x);
        double sumaX2 = sumatoria(x2);
        double sumaX3 = sumatoria(x3);
    
        return new double[][] {
            {x.length, sumaX , sumaX2       , sumatoria(y)  },
            {sumaX   , sumaX2, sumaX3       , sumatoria(xy) },
            {sumaX2  , sumaX3, sumatoria(x4), sumatoria(x2y)}
          };
    }

    private void inicializarTodasLasColumnas(){
        x2 = new double[x.length];
        x3 = new double[x.length];
        x4 = new double[x.length];
        xy = new double[x.length];
        x2y = new double[x.length];
        y_yMedia2 = new double[x.length];
        sr = new double[x.length];
    }
} 