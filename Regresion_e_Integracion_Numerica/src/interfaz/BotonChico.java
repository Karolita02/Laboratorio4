package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class BotonChico extends Boton{

    public BotonChico(Font letra) {
        super(letra);
        setBackground(Ventana.colorBarraDeAcciones);
        setForeground(Color.white);
        setPreferredSize(new Dimension(50,30));
        addMouseListener(new AccionesMouse(this, Ventana.colorSeleccionarBotonChico, 
        Ventana.colorBarraDeAcciones));
    }
    
}
