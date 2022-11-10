package metodos_numericos.regresion;

public class Tabla
{
    public double[] x, y;
    double yMedia;

    public double sumatoria(double[] columna)
    {
        double suma = 0;
        for (double elemento : columna) 
            suma+=elemento;
        return suma;
    }

    public double calcular_y_media(){ 
        yMedia = sumatoria(y)/y.length;
        return yMedia;
    }
}
