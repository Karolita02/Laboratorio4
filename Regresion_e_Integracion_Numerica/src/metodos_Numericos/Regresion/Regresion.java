package metodos_numericos.regresion;

import metodos_numericos.integracion_numerica.datos.Funcion;

public abstract class Regresion
{ 
    private double r, r2;
	Funcion modelo;
    public double[] a;

    public abstract void calcularCoeficientes();
    
    public double calcularCoeficienteDeterminacion(double suma_y_yMedia, double suma_sr){
        r2 = (suma_y_yMedia - suma_sr) / suma_y_yMedia;
        return r2;
    }
    
    public double calcularCoeficienteCorrelacion(){
        r = Math.sqrt(r2);
        return r;
    }

    public abstract void calcularSr();

    public abstract String getModelo();

    public double getR() {
		return r;
	}

	public double getR2() {
		return r2;
	}
    
} 





