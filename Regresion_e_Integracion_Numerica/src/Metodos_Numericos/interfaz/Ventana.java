package metodos_numericos.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.table.DefaultTableModel;

import metodos.Gauss_Jordan;
import metodos.Matricial_Inversa;
import metodos.Resolucion_de_Sistemas_de_Ecuaciones;

public class Ventana extends JFrame
{
    private JPanel panelSuperior, panelIzquierdo, panelCentral;
    private JButton botonPrincipal, botonDatos, botonResultados;
    private JButton botonCerrar, botonMinimizar;
    private JPanel panelAccionesVentana;
    private Dimension tamanoVentana;
    private JPanel panelPrincipal, panelDatos, panelResultados;
    private JPanel panelBienvenida;
    private JButton botonCalcular;
    private PanelScroll scrollGaussJordan, scrollJordanIndep, scrollMatrizInversa, scrollInversaIndep;
    private JPanel panelIncognitas;
    private Boton botonConfirmar;
    private Campo campoIncognitas;
    private PanelScroll scrollVariables, scrollIndependientes;
    private PanelScroll scrollResultadosJordan, scrollResultadosInversa;
    private ArrayList<JLabel> listaTextos = new ArrayList<>();
    private ArrayList<PanelScroll> listaScrolls = new ArrayList<>();

    private double[][] coeficientesVariables, coeficientesIndependientes;

    private int xMouse, yMouse;

    private DecimalFormat formato = new DecimalFormat("#.############");

    public final static Color colorIzquierda = new Color(104,249,205), 
    colorSuperior = new Color(29,248,181), 
    colorBarraDeAcciones = new Color(24,196,145),
    colorCentral = Color.white,
    colorSeleccionarBotonChico = new Color(104,249,205),
    colorBoton = new Color(14,120,88),
    colorSeleccionarBoton = new Color(87,108,102),
    colorTitulo = colorBoton;
    

    private Font letraMenu = new Font("Forte", Font.PLAIN, 40),
    letraMenuChica = new Font("Forte", Font.PLAIN, 33),
    letraTitulo = new Font("Maiandra GD", Font.BOLD, 60), 
    letraSubTitulo = new Font("Maiandra GD", Font.BOLD, 40), 
    letraTexto = new Font("Maiandra GD", Font.PLAIN, 30), 
    letraBoton = new Font("Matura MT Script Capitals", Font.PLAIN, 40),
    letraBotonAccionVentana = new Font("Bauhaus 93", Font.PLAIN, 20);

    public Ventana(){
        inicializarVentana();
        inicializarPanelIzquierdo();
        inicializarPanelSuperior();
        inicializarPanelCentral();
        panelCentral.add(panelPrincipal); // para que sea el primero en aparecer
        establecerFuncionBotonesMenu();
        establacerFuncionBotonConfirmar();
        establecerFuncionBotonCalcular();
        establecerFuncionesParaLimpiarTablas();
        panelAccionesVentana.addMouseMotionListener(new Movilidad());
        panelAccionesVentana.addMouseListener(new Movilidad());
        setVisible(true);
    }
    private void establecerFuncionesParaLimpiarTablas() {
        for (JLabel texto : listaTextos)
            texto.addMouseListener(new limpiarSeleccion());
        panelResultados.addMouseListener(new limpiarSeleccion());
        panelDatos.addMouseListener(new limpiarSeleccion());
        panelIncognitas.addMouseListener(new limpiarSeleccion());
    }
    private void establecerFuncionBotonCalcular() {
        botonCalcular.addActionListener((e) -> {
            if(scrollVariables.matriz.isEditing())
                scrollVariables.matriz.getCellEditor().stopCellEditing();
            if(scrollIndependientes.matriz.isEditing())
                scrollIndependientes.matriz.getCellEditor().stopCellEditing();
            try {
                extraerAMatriz(scrollVariables, coeficientesVariables); 
                extraerAMatriz(scrollIndependientes, coeficientesIndependientes); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error, El Valor Ingresado en la Tabla es Invalido", 
                    "ERROR VALOR INVALIDO", JOptionPane.ERROR_MESSAGE, null);
                return;
            }
    
            int numeroIncognitas = coeficientesVariables.length;
    
            Resolucion_de_Sistemas_de_Ecuaciones[] metodos = {
                new Gauss_Jordan(coeficientesVariables, coeficientesIndependientes, numeroIncognitas),
                new Matricial_Inversa(coeficientesVariables, coeficientesIndependientes, numeroIncognitas)
            };
    
            PanelScroll[] panelesScroll = {
                scrollGaussJordan,
                scrollJordanIndep,
                scrollResultadosJordan,
                scrollMatrizInversa,
                scrollInversaIndep,
                scrollResultadosInversa
            };
    
            var actual = 0;
            for (var metodo : metodos){
                metodo.calcular();
                extraerATabla(metodo, panelesScroll[actual],false, coeficientesIndependientes);
                extraerATabla(metodo, panelesScroll[actual+1],true, coeficientesIndependientes);
                ponerResultadosEnTabla(metodo, panelesScroll[actual+2]);
                actual += 3;
            }
            
            JOptionPane.showMessageDialog(this, "Los Calculos Han Sido Realizados Con Exito", 
            "Calculos Realizados Con Exito", JOptionPane.INFORMATION_MESSAGE, null);
        });
    }
    private void ponerResultadosEnTabla(Resolucion_de_Sistemas_de_Ecuaciones metodo, PanelScroll tabla){
        var valores = metodo.get_sistema_de_ecuaciones().get_coeficientes_independientes();
        for (int pos = 0; pos < metodo.get_numero_incognitas(); pos++)
            tabla.modeloTabla.setValueAt(formato.format(valores[pos][0]), 0, pos);
    }
    private void extraerATabla(Resolucion_de_Sistemas_de_Ecuaciones metodo, PanelScroll tabla, boolean esIndependiente, double[][] matrizB){
        var matrizResultado = esIndependiente ? 
            metodo.get_sistema_de_ecuaciones().get_coeficientes_independientes() :
            metodo.get_sistema_de_ecuaciones().get_coeficientes_variables();
        var objetoResultado = esIndependiente && metodo instanceof Matricial_Inversa ? 
            convertirMatrizAObjeto(matrizB) :
            convertirMatrizAObjeto(matrizResultado);
        tabla.modeloTabla.setRowCount(0);
        for (Object[] fila : objetoResultado) 
            tabla.modeloTabla.addRow(fila);
    }
    private Object[][] convertirMatrizAObjeto(double[][] matriz){
        var resultado = new Object[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) 
            for (int j = 0; j < matriz[0].length; j++) 
                resultado[i][j] = formato.format(matriz[i][j]);
        return resultado;
    }
    private void extraerAMatriz(PanelScroll scrollTabla, double[][] matriz) {
        var tablaVariables = scrollTabla.modeloTabla.getDataVector();
        for (int i = 0; i < matriz.length; i++) 
            for (int j = 0; j < matriz[0].length; j++) 
                matriz[i][j] = Double.parseDouble(tablaVariables.get(i).get(j).toString());
    }
    private void establacerFuncionBotonConfirmar() {
        botonConfirmar.addActionListener((e) -> {
            try {
                int tamano = Integer.parseInt(campoIncognitas.getText());

                scrollVariables.set_dimensiones(tamano, tamano);
                scrollIndependientes.set_dimensiones(tamano, 1);
                scrollIndependientes.cambiar_identificadores(new String[]{" "});
                
                scrollGaussJordan.set_dimensiones(tamano, tamano);
                scrollJordanIndep.set_dimensiones(tamano, 1);
                scrollJordanIndep.cambiar_identificadores(new String[]{" "});
                
                scrollMatrizInversa.set_dimensiones(tamano, tamano);
                scrollInversaIndep.set_dimensiones(tamano, 1);
                scrollInversaIndep.cambiar_identificadores(new String[]{"Matriz B"});
                
                scrollResultadosJordan.set_dimensiones(1, tamano);
                scrollResultadosInversa.set_dimensiones(1, tamano);


                coeficientesVariables = new double[tamano][tamano];
                coeficientesIndependientes = new double[tamano][1];
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error, Numero Ingresado Invalido", 
                "ERROR NUMERO INVALIDO", JOptionPane.ERROR_MESSAGE, null);
            }
        });
    }
    // private boolean opcionSiNo(String mensaje, String titulo){
    //     while (true) {
    //         int valor = JOptionPane.showOptionDialog(null, mensaje, titulo, 0, 1, null, "Si, No".split(", "), null);
    //         if(valor != -1)
    //             return valor == 0; // 0 = Si, 1 = No, -1 = Cerrar la ventana
    //         JOptionPane.showMessageDialog(this, "ERROR, DEBE ELEGIR UNA DE LAS OPCIONES", "ERROR OPCION INVALIDA", JOptionPane.ERROR_MESSAGE);
    //     }
    // }
    private void establecerFuncionBotonesMenu() {
        botonPrincipal.addActionListener((e) -> {
            panelCentral.removeAll();
            panelCentral.add(panelPrincipal);
            panelCentral.updateUI();
        });

        botonDatos.addActionListener((e) -> {
            panelCentral.removeAll();
            panelCentral.add(panelDatos);
            panelCentral.updateUI();
        });

        botonResultados.addActionListener((e) -> {
            panelCentral.removeAll();
            panelCentral.add(panelResultados);
            panelCentral.updateUI();
        });
    }
    private void inicializarPanelCentral() {
        panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new GridLayout(1,1));
        add(panelCentral, BorderLayout.CENTER);
        inicializarPanelPrincipal();//! Principal
        inicializarPanelDatos();//! Datos
        inicializarPanelResultados();//! Resultados

    }
    private void inicializarPanelResultados() {
        panelResultados = new JPanel();
        panelResultados.setOpaque(false);
        panelResultados.setLayout(null);

        JLabel textoGaussJordan = new JLabel("Gauss-Jordan");
        listaTextos.add(textoGaussJordan);
        textoGaussJordan.setFont(letraTexto);
        textoGaussJordan.setHorizontalAlignment(JLabel.CENTER);
        textoGaussJordan.setBounds(220, 5, 300, 50);

        int posVertical = 60;
        int posHorizontal = 20; 
        int alto = 150;
        int ancho = 600;
        JTable tablaGaussJordan = new JTable(new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        });
        
        scrollGaussJordan = new PanelScroll(tablaGaussJordan);
        listaScrolls.add(scrollGaussJordan);
        scrollGaussJordan.setBounds(posHorizontal, posVertical, ancho, alto);
        
        posVertical += alto*2;

        JLabel textoMatrizInversa = new JLabel("Matriz Inversa * Matriz B");
        listaTextos.add(textoMatrizInversa);
        textoMatrizInversa.setFont(letraTexto);
        textoMatrizInversa.setHorizontalAlignment(JLabel.CENTER);
        textoMatrizInversa.setBounds(150, posVertical - 60, 450, 50);

        JTable tablaMatrizInversa = new JTable(new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        });

        scrollMatrizInversa = new PanelScroll(tablaMatrizInversa);
        listaScrolls.add(scrollMatrizInversa);
        scrollMatrizInversa.setBounds(posHorizontal, posVertical, ancho, alto);

        posHorizontal += ancho + 40;
        ancho = 100;

        JTable tablaInversaIndep = new JTable(new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        });
        
        scrollInversaIndep = new PanelScroll(tablaInversaIndep);
        listaScrolls.add(scrollInversaIndep);
        scrollInversaIndep.setBounds(posHorizontal,posVertical,ancho,alto);
        
        posVertical -= alto*2;

        JTable tablaJordanIndep = new JTable(new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        });
        
        scrollJordanIndep = new PanelScroll(tablaJordanIndep);
        listaScrolls.add(scrollJordanIndep);
        scrollJordanIndep.setBounds(posHorizontal,posVertical,ancho,alto);

        posVertical += alto + 40;
        ancho = 700 + 50;
        posHorizontal = 20;
        alto = 39;

        JTable tablaResultados = new JTable(new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        });

        scrollResultadosJordan = new PanelScroll(tablaResultados);
        listaScrolls.add(scrollResultadosJordan);
        scrollResultadosJordan.setBounds(posHorizontal, posVertical, ancho, alto);

        JLabel resultadosObtenidos1 = new JLabel("Resultados Obtenidos");
        listaTextos.add(resultadosObtenidos1);
        resultadosObtenidos1.setFont(letraTexto);
        resultadosObtenidos1.setHorizontalAlignment(JLabel.CENTER);
        resultadosObtenidos1.setBounds(posHorizontal, posVertical-40, ancho, alto);

        posVertical += 300;

        tablaResultados = new JTable(new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        });

        scrollResultadosInversa = new PanelScroll(tablaResultados);
        listaScrolls.add(scrollResultadosInversa);
        scrollResultadosInversa.setBounds(posHorizontal, posVertical, ancho, alto);

        JLabel resultadosObtenidos2 = new JLabel("Resultados Obtenidos");
        listaTextos.add(resultadosObtenidos2);
        resultadosObtenidos2.setFont(letraTexto);
        resultadosObtenidos2.setHorizontalAlignment(JLabel.CENTER);
        resultadosObtenidos2.setBounds(posHorizontal, posVertical-40, ancho, alto);
        
        panelResultados.add(textoGaussJordan);
        panelResultados.add(scrollGaussJordan);
        panelResultados.add(scrollJordanIndep);
        panelResultados.add(scrollResultadosJordan);
        panelResultados.add(resultadosObtenidos1);
        
        panelResultados.add(textoMatrizInversa);
        panelResultados.add(scrollMatrizInversa);
        panelResultados.add(scrollInversaIndep);
        panelResultados.add(scrollResultadosInversa);
        panelResultados.add(resultadosObtenidos2);
    }
    private void inicializarPanelDatos() {
        panelDatos = new JPanel();
        panelDatos.setOpaque(false);
        panelDatos.setLayout(null);
        
        panelIncognitas = new JPanel();
        panelIncognitas.setOpaque(false);
        panelIncognitas.setLayout(new GridLayout(1,3,50,0));
        panelIncognitas.setBounds(10,30,750,50);

        JLabel textoIncognitas = new JLabel("Incognitas");
        listaTextos.add(textoIncognitas);
        textoIncognitas.setFont(letraTexto);
        textoIncognitas.setHorizontalAlignment(JLabel.CENTER);

        campoIncognitas = new Campo(letraTexto);
        campoIncognitas.setText("4");

        botonConfirmar = new Boton(letraBoton);
        botonConfirmar.setText("Confirmar");
        botonConfirmar.setVerticalAlignment(JButton.CENTER);

        panelIncognitas.add(textoIncognitas);
        panelIncognitas.add(campoIncognitas);
        panelIncognitas.add(botonConfirmar);

        JLabel textoCoeficientes = new JLabel("            Coeficientes Variables               Independientes");
        listaTextos.add(textoCoeficientes);
        textoCoeficientes.setFont(letraTexto);
        textoCoeficientes.setHorizontalAlignment(JLabel.CENTER);
        textoCoeficientes.setBounds(30,100,800,50);

        JTable tablaVariables = new JTable(new DefaultTableModel());

        scrollVariables = new PanelScroll(tablaVariables);
        listaScrolls.add(scrollVariables);
        scrollVariables.setBounds(20, 170, 600, 300);

        JTable tablaIndependientes = new JTable(new DefaultTableModel());

        scrollIndependientes = new PanelScroll(tablaIndependientes);
        listaScrolls.add(scrollIndependientes);
        scrollIndependientes.setBounds(600+20+20, 170, 130, 300);

        botonCalcular = new Boton(letraBoton);
        botonCalcular.setText("Calcular");
        botonCalcular.setVerticalAlignment(JButton.CENTER);
        botonCalcular.setBounds(280,500,200,50);

        panelDatos.add(scrollVariables);
        panelDatos.add(scrollIndependientes);
        panelDatos.add(panelIncognitas);
        panelDatos.add(textoCoeficientes);
        panelDatos.add(botonCalcular);
    }
    private void inicializarPanelPrincipal() {
        panelPrincipal = new JPanel();
        panelPrincipal.setOpaque(false);
        panelPrincipal.setLayout(null);

        panelBienvenida = new JPanel();
        panelBienvenida.setOpaque(false);
        panelBienvenida.setLayout(new GridLayout(10,1));
        panelBienvenida.setBounds(50, 150, 
        700, 500);

        JLabel textoMetodos = new JLabel("Usando los Metodos de Simpson");
        textoMetodos.setFont(letraSubTitulo);
        textoMetodos.setHorizontalAlignment(JLabel.CENTER);

        JLabel textoMetodos2 = new JLabel("y Regresion Polinomial");
        textoMetodos2.setFont(letraSubTitulo);
        textoMetodos2.setHorizontalAlignment(JLabel.CENTER);

        JLabel textoCreadoPor = new JLabel("Creado por:");
        textoCreadoPor.setFont(letraTexto);
        textoCreadoPor.setHorizontalAlignment(JLabel.CENTER);

        JLabel textoIntegrantes = new JLabel("Ricardo Sanjur");
        textoIntegrantes.setFont(letraTexto);
        textoIntegrantes.setHorizontalAlignment(JLabel.CENTER);

        JLabel textoIntegrantes2 = new JLabel("Thaís Samudio");
        textoIntegrantes2.setFont(letraTexto);
        textoIntegrantes2.setHorizontalAlignment(JLabel.CENTER);

        
        panelBienvenida.add(textoMetodos);
        panelBienvenida.add(textoMetodos2);
        panelBienvenida.add(new JLabel());
        panelBienvenida.add(textoCreadoPor);
        panelBienvenida.add(textoIntegrantes);
        panelBienvenida.add(textoIntegrantes2);

        JLabel logoUTP = new JLabel();
        logoUTP.setBounds(5,5,180,180);
        ponerIcono(logoUTP, "src/interfaz/imagenes/LogoUTP.jpg"); //? funciona con Eclipse
        // ponerIcono(logoUTP, "Sistemas_de_Ecuaciones/src/interfaz/imagenes/LogoUTP.jpg"); //! funciona con VSCode

        JLabel logoSistemas = new JLabel();
        logoSistemas.setBounds(618-5,5,180,180);
        ponerIcono(logoSistemas, "src/interfaz/imagenes/LogoSistemas.png"); //? funciona con Eclipse
        // ponerIcono(logoSistemas, "Sistemas_de_Ecuaciones/src/interfaz/imagenes/LogoSistemas.png"); //! funciona con VSCode
        
        panelPrincipal.add(logoUTP);
        panelPrincipal.add(logoSistemas);
        panelPrincipal.add(panelBienvenida);

        
    }
    private void ponerIcono(JLabel etiqueta, String ruta) {
        ImageIcon imagen = new ImageIcon(ruta);
        Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(etiqueta.getWidth(), etiqueta.getHeight(), Image.SCALE_SMOOTH));
        etiqueta.setIcon(icono);
    }
    private void inicializarPanelSuperior() {
        JLabel textoTitulo = new JLabel("Cálculo de Integrales y Regresion");
        textoTitulo.setFont(letraTitulo);
        textoTitulo.setHorizontalAlignment(JLabel.CENTER);
        textoTitulo.setForeground(colorTitulo);

        panelAccionesVentana = new JPanel();
        panelAccionesVentana.setBackground(colorBarraDeAcciones);
        panelAccionesVentana.setLayout(new FlowLayout(FlowLayout.RIGHT,0,0));
        panelAccionesVentana.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        botonCerrar = new BotonChico(letraBotonAccionVentana);
        botonCerrar.setText("X");
        botonCerrar.addActionListener((e) -> dispose());


        botonMinimizar = new BotonChico(letraBotonAccionVentana);
        botonMinimizar.setText("—");
        botonMinimizar.addActionListener((e) -> setState(ICONIFIED));

        panelAccionesVentana.add(botonCerrar);
        panelAccionesVentana.add(botonMinimizar);

        panelSuperior.add(textoTitulo, BorderLayout.CENTER);
        panelSuperior.add(panelAccionesVentana, BorderLayout.NORTH);

    }
    private void inicializarPanelIzquierdo() {
        botonPrincipal = new Boton(letraMenu);
        botonPrincipal.setText("Principal");

        botonDatos = new Boton(letraMenu);
        botonDatos.setText("Datos");

        botonResultados = new Boton(letraMenuChica);
        botonResultados.setText("Resultados");

        panelIzquierdo.add(new JLabel());
        panelIzquierdo.add(new JLabel());
        panelIzquierdo.add(botonPrincipal);
        panelIzquierdo.add(botonDatos);
        panelIzquierdo.add(botonResultados);
        
    }
    private void inicializarVentana() {
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        tamanoVentana = new Dimension(1000,700);
        setSize(tamanoVentana);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        getContentPane().setBackground(colorCentral);

        panelSuperior = new JPanel();
        panelSuperior.setBackground(colorSuperior);
        panelSuperior.setPreferredSize(new Dimension(getWidth(), 100));
        panelSuperior.setLayout(new BorderLayout());
        panelSuperior.setMinimumSize(panelSuperior.getSize());
        add(panelSuperior, BorderLayout.NORTH);

        panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(colorIzquierda);
        panelIzquierdo.setPreferredSize(new Dimension(200, getHeight()));
        panelIzquierdo.setLayout(new GridLayout(7,1,0,20));
        panelIzquierdo.setMinimumSize(panelIzquierdo.getSize());
        add(panelIzquierdo, BorderLayout.WEST);
    }

    class limpiarSeleccion implements MouseInputListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            for (PanelScroll tabla : listaScrolls) {
                tabla.matriz.clearSelection();
                if(tabla.matriz.isEditing())
                    tabla.matriz.getCellEditor().stopCellEditing();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
        }
    }

    class Movilidad implements MouseInputListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getXOnScreen();
            int y = e.getYOnScreen();
            setLocation(x - xMouse, y - yMouse);        
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            
        }

    }
}