package interfaz;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

public class Campo extends JTextField{

    public Campo(Font letra) {
        setFont(letra);
        setBackground(Ventana.colorBarraDeAcciones);
        setForeground(Ventana.colorTextoConsejoCampos);
        addMouseListener(new QuitarSugerencia(this));
        setHorizontalAlignment(JTextField.CENTER);
        setBorder(null);
    }

    class QuitarSugerencia implements MouseListener{
        private JTextField campo;

        QuitarSugerencia(JTextField campo){
            this.campo = campo;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            campo.setText("");
            campo.setForeground(Ventana.colorTextoCampos);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

    }
    
}
