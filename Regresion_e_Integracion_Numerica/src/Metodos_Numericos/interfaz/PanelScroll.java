package metodos_numericos.interfaz;


import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelScroll extends JScrollPane{
    public DefaultTableModel modeloTabla;
    public JTable matriz; 

    public PanelScroll(JTable arg0) {
        super(arg0);
        arg0.getTableHeader().setReorderingAllowed(false);
        matriz = arg0;
        modeloTabla = (DefaultTableModel)arg0.getModel();
    }

    public void set_dimensiones(int fila, int columna){
        String identificadores = "";
        for (int iteracion = 0; iteracion < columna; iteracion++) {
            identificadores += "X" + iteracion;
            if(iteracion != columna-1)
                identificadores += ",";
        }
        modeloTabla.setColumnIdentifiers(identificadores.split(","));
        modeloTabla.setRowCount(0);
        modeloTabla.setRowCount(fila);
        updateUI();
    }

    public void cambiar_identificadores(Object[] identificadores){
        modeloTabla.setColumnIdentifiers(identificadores);
    }

}
