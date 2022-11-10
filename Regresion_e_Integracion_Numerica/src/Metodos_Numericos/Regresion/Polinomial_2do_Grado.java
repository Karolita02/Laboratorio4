package metodos_numericos.regresion;

import metodos.Gauss_Jordan;

public class Polinomial_2do_Grado  extends Regresion 
{
    TablaPolinomial_2do_Grado tabla;

    public Polinomial_2do_Grado(Tabla tabla) {
        this.tabla = new TablaPolinomial_2do_Grado(tabla);
        a = new double[tabla.x.length];
    }
   
    @Override
    public void calcularCoeficientes() {
        var gauss = new Gauss_Jordan(tabla.getEcuaciones(), 3);
        gauss.calcular();
       
        for (int i = 0; i < 3; i++)
            a[i] = gauss.get_sistema_de_ecuaciones().get_coeficientes_independientes()[i][0];
        
        calcularSr();
        calcularCoeficienteDeterminacion(tabla.sumatoria(tabla.y_yMedia2),tabla.sumatoria(tabla.sr));
        calcularCoeficienteCorrelacion();
        
    }

    @Override
    public void calcularSr() {
        modelo = (x) -> a[0] + a[1]*x + a[2]*Math.pow(x,2);
        for (int i = 0; i < tabla.x.length; i++) {
            tabla.sr[i] = Math.pow(tabla.y[i] - modelo.evaluar(tabla.x[i]),2);
        }
    }

	@Override
	public String getModelo() {
		return String.format("y = %.4f + %.4f * X + %.4f * X^2", a[0], a[1], a[2]);
	}

    
}     