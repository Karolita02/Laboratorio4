package interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Boton extends JButton{
    private Color colorEntered = Ventana.colorSeleccionarBoton, 
    colorExited = Ventana.colorBoton;
    public Boton(Font letra){
        setBackground(colorExited);
        setForeground(Color.white);
        setPreferredSize(new Dimension(200, 70));
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(letra);
        setVerticalAlignment(JButton.CENTER);
        addMouseListener(new AccionesMouse(this, colorEntered, 
        colorExited));
    }

    class AccionesMouse implements MouseListener{
    
        private Color colorEntered, colorExited;
        private JButton boton;
        
        public AccionesMouse(JButton boton, Color colorEntered, Color colorExited) {
            this.boton = boton;
            this.colorEntered = colorEntered;
            this.colorExited = colorExited;
        }
    
        @Override
        public void mouseClicked(MouseEvent arg0) {
            
        }
    
        @Override
        public void mouseEntered(MouseEvent arg0) {
            boton.setBackground(colorEntered);
            
        }
    
        @Override
        public void mouseExited(MouseEvent arg0) {
            boton.setBackground(colorExited);
            
        }
    
        @Override
        public void mousePressed(MouseEvent arg0) {
            
        }
    
        @Override
        public void mouseReleased(MouseEvent arg0) {
            
        }
    
    }
}

