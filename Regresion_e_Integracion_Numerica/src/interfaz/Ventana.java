package interfaz;

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

import metodos_numericos.integracion_numerica.Integracion_Numerica;
import metodos_numericos.integracion_numerica.Simpson;
import metodos_numericos.integracion_numerica.Simpson1_3;
import metodos_numericos.integracion_numerica.Simpson3_8;
import metodos_numericos.integracion_numerica.datos.Funcion;
import metodos_numericos.regresion.Polinomial_2do_Grado;
import metodos_numericos.regresion.Tabla;

public class Ventana extends JFrame
{
    private JPanel panelSuperior, panelIzquierdo, panelCentral;
    private JButton botonPrincipal, botonDatos, botonResultados;
    private JButton botonCerrar, botonMinimizar;
    private JPanel panelAccionesVentana;
    private Dimension tamanoVentana;
    private JPanel panelPrincipal, panelDatos, panelResultados;
    private JPanel panelBienvenida;
    private Boton botonCalcular, botonAceptar, botonLimpiar;
    private JPanel panelIntroducirDatos, panelCantidadFilas;
    private Campo campoPuntoA, campoPuntoB, campoN1_3, campoN3_8, campoCantidadFilas;
    private PanelScroll scrollXY;
    private ArrayList<JLabel> listaTextos = new ArrayList<>();
    private ArrayList<PanelScroll> listaScrolls = new ArrayList<>();
    private JLabel textoA0, textoA1, textoA2, textoR2, textoR, textoModelo;
    private JLabel textoA, textoB, textoN1_3, textoN3_8;
    private JLabel textoSimpson, textoSimpson1_3, textoSimpson3_8, textoFuncion;

    private int xMouse, yMouse;

    private DecimalFormat formato = new DecimalFormat("#.############");

    private static int rgb(String hex){
        return Integer.parseInt(hex,16);
    }

    public final static Color colorIzquierda = new Color(rgb("CC00CC")), 
    colorSuperior = new Color(rgb("FF00FF")), 
    colorBarraDeAcciones = new Color(rgb("FF4DFF")),
    colorCentral = Color.white,
    colorSeleccionarBotonChico = colorSuperior,
    colorBoton = new Color(rgb("800080")),
    colorSeleccionarBoton = new Color(rgb("FF00FF")),
    colorTitulo = Color.white,
    colorTextoConsejoCampos = new Color(rgb("e2e2e2")),
    colorTextoCampos = Color.white;
    

    private Font letraMenu = new Font("Forte", Font.PLAIN, 40),
    letraMenuChica = new Font("Forte", Font.PLAIN, 33),
    letraTitulo = new Font("Maiandra GD", Font.BOLD, 60), 
    letraSubTitulo = new Font("Maiandra GD", Font.BOLD, 40), 
    letraTexto = new Font("Maiandra GD", Font.PLAIN, 30), 
    letraBoton = new Font("Matura MT Script Capitals", Font.PLAIN, 40),
    letraBotonAccionVentana = new Font("Bauhaus 93", Font.PLAIN, 20);

    public Ventana(Funcion funcion, String textoFuncion){
        inicializarVentana();
        inicializarPanelIzquierdo();
        inicializarPanelSuperior();
        inicializarPanelCentral();
        panelCentral.add(panelPrincipal); // para que sea el primero en aparecer
        establecerFuncionBotonesMenu();
        establecerFuncionBotonCalcular(funcion, textoFuncion);
        establecerFuncionBotonAceptar();
        establecerFuncionBotonLimpiar();
        establecerFuncionesParaLimpiarTablas();
        panelAccionesVentana.addMouseMotionListener(new Movilidad());
        panelAccionesVentana.addMouseListener(new Movilidad());
        setVisible(true);
    }
    private void establecerFuncionBotonLimpiar() {
        botonLimpiar.addActionListener((e) -> {
            if(scrollXY.matriz.isEditing())
                scrollXY.matriz.getCellEditor().stopCellEditing();
            int cant = scrollXY.modeloTabla.getRowCount();
            scrollXY.modeloTabla.setRowCount(0);
            scrollXY.modeloTabla.setRowCount(cant);
        });
    }
    private void establecerFuncionBotonAceptar() {
        botonAceptar.addActionListener((e) -> {
            if(scrollXY.matriz.isEditing())
                scrollXY.matriz.getCellEditor().stopCellEditing();
            try {
                int cant = Integer.parseInt(campoCantidadFilas.getText());
                scrollXY.modeloTabla.setRowCount(cant);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error, Cantidad de Filas Invalida", "ERROR CANTIDAD INVALIDA", JOptionPane.ERROR_MESSAGE, null);
            }
        });
    }
    private void establecerFuncionesParaLimpiarTablas() {
        for (JLabel texto : listaTextos)
            texto.addMouseListener(new limpiarSeleccion());
        panelResultados.addMouseListener(new limpiarSeleccion());
        panelDatos.addMouseListener(new limpiarSeleccion());
        panelIntroducirDatos.addMouseListener(new limpiarSeleccion());
    }
    private void establecerFuncionBotonCalcular(Funcion funcion, String textoFuncion) {
        botonCalcular.addActionListener((e) -> {
            if(scrollXY.matriz.isEditing())
                scrollXY.matriz.getCellEditor().stopCellEditing();
            double[] x, y;
            double a,b;
            int n1_3,n3_8;
            try {
                x = extraerAMatriz(scrollXY, 0); 
                y = extraerAMatriz(scrollXY, 1); 
                
                a = Double.parseDouble(campoPuntoA.getText());
                b = Double.parseDouble(campoPuntoB.getText());
                n1_3 = Integer.parseInt(campoN1_3.getText());
                boolean seguir = true;
                if (n1_3 % 2 != 0)
                    seguir = opcionSiNo("Esta Seguro de querer proceder?\nAl no colocar un numero PAR no podremos asegurar la exactitud del calculo", "Precausion Numero Par Recomendado");
                if(!seguir) return;
                n3_8 = Integer.parseInt(campoN3_8.getText());
                if (n3_8 % 3 != 0)
                    seguir = opcionSiNo("Esta Seguro de querer proceder?\nAl no colocar un numero MULTIPLO DE 3 no podremos asegurar la exactitud del calculo", "Precausion Numero Multiplo de 3 Recomendado");
                if(!seguir) return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error, El Valor Ingresado en la Tabla es Invalido" + ex.getCause(), 
                    "ERROR VALOR INVALIDO", JOptionPane.ERROR_MESSAGE, null);
                return;
            }

            if(x != null && y != null){
                Tabla tabla = new Tabla();
                tabla.x = x;
                tabla.y = y;
    
                var regresion = new Polinomial_2do_Grado(tabla);
                regresion.calcularCoeficientes();
                
                JLabel[] textos1 = {
                    textoA0,
                    textoA1,
                    textoA2,
                    textoR2,
                    textoR
                };
    
                double[] valores1 = {
                    regresion.a[0],
                    regresion.a[1],
                    regresion.a[2],
                    regresion.getR2(),
                    regresion.getR()
                };
    
                for (int i = 0; i < textos1.length; i++) 
                    textos1[i].setText(textos1[i].getText().substring(0,textos1[i].getText().indexOf("=")+2) + formato.format(valores1[i]));
                    
                textoModelo.setText(textoModelo.getText().substring(0,textoModelo.getText().indexOf(":")+2) + regresion.getModelo());
            }

            Integracion_Numerica[] metodosSimpsons = {
                new Simpson(funcion, a, b),
                new Simpson1_3(funcion, a, b),
                new Simpson3_8(funcion, a, b)
            };

            metodosSimpsons[1].setN(n1_3);
            metodosSimpsons[2].setN(n3_8);

            double[] resulSimpsons = new double[3];

            for (int actual = 0; actual < metodosSimpsons.length; actual++) 
                resulSimpsons[actual] = metodosSimpsons[actual].calcular();
            
            JLabel[] textos2 = {
                textoA,
                textoB,
                textoN1_3,
                textoN3_8,
                textoSimpson,
                textoSimpson1_3,
                textoSimpson3_8
            };

            double[] valores2 = {
                a,
                b,
                n1_3,
                n3_8,
                resulSimpsons[0],
                resulSimpsons[1],
                resulSimpsons[2],
            };

            for (int i = 0; i < textos2.length; i++) 
                textos2[i].setText(textos2[i].getText().substring(0,textos2[i].getText().indexOf("=")+2) + formato.format(valores2[i]));
                
            this.textoFuncion.setText(this.textoFuncion.getText().substring(0,this.textoFuncion.getText().indexOf("=")+2) + textoFuncion);

            JOptionPane.showMessageDialog(this, "Los Calculos Han Sido Realizados Con Exito", 
            "Calculos Realizados Con Exito", JOptionPane.INFORMATION_MESSAGE, null);
        });
    }
    private double[] extraerAMatriz(PanelScroll scrollTabla, int columna) {
        if(scrollTabla.modeloTabla.getRowCount() == 0) return null;
        var matriz = new double[scrollTabla.modeloTabla.getRowCount()];
        for (int i = 0; i < matriz.length; i++) 
            matriz[i] = Double.parseDouble(scrollTabla.modeloTabla.getDataVector().get(i).get(columna).toString());
        return matriz;
    }

    private boolean opcionSiNo(String mensaje, String titulo){
        while (true) {
            int valor = JOptionPane.showOptionDialog(null, mensaje, titulo, 0, 1, null, "Si, No".split(", "), null);
            if(valor != -1)
                return valor == 0; // 0 = Si, 1 = No, -1 = Cerrar la ventana
            JOptionPane.showMessageDialog(this, "ERROR, DEBE ELEGIR UNA DE LAS OPCIONES", "ERROR OPCION INVALIDA", JOptionPane.ERROR_MESSAGE);
        }
    }
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

        inicializarPanelSimpsons(); 
        inicializarPanelRegresion();
    }
    private void inicializarPanelSimpsons() {
        JLabel resultadosSimpsons = new JLabel("Resultados de los Metodos de Simpson");
        resultadosSimpsons.setFont(letraTexto);
        resultadosSimpsons.setBackground(colorIzquierda);
        resultadosSimpsons.setOpaque(true);
        resultadosSimpsons.setForeground(colorTextoCampos);
        resultadosSimpsons.setHorizontalAlignment(JLabel.CENTER);
        resultadosSimpsons.setBounds(0,250,800,50);

        textoA = new JLabel("Desde A = ??");
        textoA.setFont(letraTexto);
        textoA.setHorizontalAlignment(JLabel.CENTER);

        textoB = new JLabel("Hasta B = ??");
        textoB.setFont(letraTexto);
        textoB.setHorizontalAlignment(JLabel.CENTER);

        textoN1_3 = new JLabel("N -> 1/3 = ??");
        textoN1_3.setFont(letraTexto);
        textoN1_3.setHorizontalAlignment(JLabel.CENTER);

        textoN3_8 = new JLabel("N -> 3/8 = ??");
        textoN3_8.setFont(letraTexto);
        textoN3_8.setHorizontalAlignment(JLabel.CENTER);

        textoSimpson = new JLabel("Simpson = ??");
        textoSimpson.setFont(letraTexto);
        textoSimpson.setHorizontalAlignment(JLabel.CENTER);

        textoSimpson1_3 = new JLabel("Simpson 1/3 = ??");
        textoSimpson1_3.setFont(letraTexto);
        textoSimpson1_3.setHorizontalAlignment(JLabel.CENTER);

        textoSimpson3_8 = new JLabel("Simpson 3/8 = ??");
        textoSimpson3_8.setFont(letraTexto);
        textoSimpson3_8.setHorizontalAlignment(JLabel.CENTER);

        textoFuncion = new JLabel("f(x) = ??");
        textoFuncion.setFont(letraTexto);
        textoFuncion.setHorizontalAlignment(JLabel.CENTER);

        JPanel panelSimpsons = new JPanel();
        panelSimpsons.setOpaque(false);
        panelSimpsons.setBounds(0,300,800,400);

        JPanel panelLimites = new JPanel();
        panelLimites.setOpaque(false);
        panelLimites.setLayout(new GridLayout(2,1));
        panelLimites.setPreferredSize(new Dimension(350,90));
        
        panelLimites.add(textoA);
        panelLimites.add(textoB);
        
        JPanel panelDivisiones = new JPanel();
        panelDivisiones.setOpaque(false);
        panelDivisiones.setLayout(new GridLayout(2,1));
        panelDivisiones.setPreferredSize(new Dimension(350,90));

        panelDivisiones.add(textoN1_3);
        panelDivisiones.add(textoN3_8);

        JPanel panelMetodosSimpsons = new JPanel();
        panelMetodosSimpsons.setOpaque(false);
        panelMetodosSimpsons.setLayout(new GridLayout(3,1));
        panelMetodosSimpsons.setPreferredSize(new Dimension(800,150));

        panelMetodosSimpsons.add(textoSimpson);
        panelMetodosSimpsons.add(textoSimpson1_3);
        panelMetodosSimpsons.add(textoSimpson3_8);

        panelSimpsons.add(panelLimites);
        panelSimpsons.add(panelDivisiones);
        panelSimpsons.add(panelMetodosSimpsons);
        panelSimpsons.add(textoFuncion);

        panelResultados.add(resultadosSimpsons);
        panelResultados.add(panelSimpsons);
    }
    private void inicializarPanelRegresion() {
        JLabel resultadosRegresion = new JLabel("Resultados de la Regresión Polinomial");
        resultadosRegresion.setFont(letraTexto);
        resultadosRegresion.setBackground(colorIzquierda);
        resultadosRegresion.setOpaque(true);
        resultadosRegresion.setForeground(colorTextoCampos);
        resultadosRegresion.setHorizontalAlignment(JLabel.CENTER);
        resultadosRegresion.setBounds(0,0,800,50);

        textoA0 = new JLabel("a0 = ??");
        textoA0.setFont(letraTexto);
        textoA0.setHorizontalAlignment(JLabel.CENTER);

        textoA1 = new JLabel("a1 = ??");
        textoA1.setFont(letraTexto);
        textoA1.setHorizontalAlignment(JLabel.CENTER);

        textoA2 = new JLabel("a2 = ??");
        textoA2.setFont(letraTexto);
        textoA2.setHorizontalAlignment(JLabel.CENTER);

        textoR2 = new JLabel("r^2 = ??");
        textoR2.setFont(letraTexto);
        textoR2.setHorizontalAlignment(JLabel.CENTER);

        textoR = new JLabel("r = ??");
        textoR.setFont(letraTexto);
        textoR.setHorizontalAlignment(JLabel.CENTER);

        textoModelo = new JLabel("Modelo: ??");
        textoModelo.setFont(letraTexto);
        textoModelo.setHorizontalAlignment(JLabel.CENTER);

        JPanel panelRegresion = new JPanel();
        panelRegresion.setOpaque(false);
        panelRegresion.setBounds(0,50,800,200);

        JPanel panelCoeficientes = new JPanel();
        panelCoeficientes.setOpaque(false);
        panelCoeficientes.setLayout(new GridLayout(3,1));
        panelCoeficientes.setPreferredSize(new Dimension(350,150));
        
        
        panelCoeficientes.add(textoA0);
        panelCoeficientes.add(textoA1);
        panelCoeficientes.add(textoA2);
        
        JPanel panelDetYCorr = new JPanel();
        panelDetYCorr.setOpaque(false);
        panelDetYCorr.setLayout(new GridLayout(2,1));
        panelDetYCorr.setPreferredSize(new Dimension(350,150));

        panelDetYCorr.add(textoR2);
        panelDetYCorr.add(textoR);

        panelRegresion.add(panelCoeficientes);
        panelRegresion.add(panelDetYCorr);
        panelRegresion.add(textoModelo);
        
        panelResultados.add(resultadosRegresion);
        panelResultados.add(panelRegresion);
    }
    private void inicializarPanelDatos() {
        panelDatos = new JPanel();
        panelDatos.setOpaque(false);
        panelDatos.setLayout(null);
        
        panelIntroducirDatos = new JPanel();
        panelIntroducirDatos.setOpaque(false);
        panelIntroducirDatos.setLayout(new GridLayout(4,2,50,20));
        panelIntroducirDatos.setBounds(480,170,300,300);

        JLabel textoPuntoA = new JLabel("Punto A");
        listaTextos.add(textoPuntoA);
        textoPuntoA.setFont(letraTexto);
        textoPuntoA.setHorizontalAlignment(JLabel.CENTER);

        campoPuntoA = new Campo(letraTexto);
        campoPuntoA.setText("1");

        JLabel textoPuntoB = new JLabel("Punto B");
        listaTextos.add(textoPuntoB);
        textoPuntoB.setFont(letraTexto);
        textoPuntoB.setHorizontalAlignment(JLabel.CENTER);

        campoPuntoB = new Campo(letraTexto);
        campoPuntoB.setText("6");

        JLabel textoN1_3 = new JLabel("N -> 1/3");
        listaTextos.add(textoN1_3);
        textoN1_3.setFont(letraTexto);
        textoN1_3.setHorizontalAlignment(JLabel.CENTER);

        campoN1_3 = new Campo(letraTexto);
        campoN1_3.setText("8");

        JLabel textoN3_8 = new JLabel("N -> 3/8");
        listaTextos.add(textoN3_8);
        textoN3_8.setFont(letraTexto);
        textoN3_8.setHorizontalAlignment(JLabel.CENTER);

        campoN3_8 = new Campo(letraTexto);
        campoN3_8.setText("9");

        panelIntroducirDatos.add(textoPuntoA);
        panelIntroducirDatos.add(campoPuntoA);
        panelIntroducirDatos.add(textoPuntoB);
        panelIntroducirDatos.add(campoPuntoB);
        panelIntroducirDatos.add(textoN1_3);
        panelIntroducirDatos.add(campoN1_3);
        panelIntroducirDatos.add(textoN3_8);
        panelIntroducirDatos.add(campoN3_8);

        // JLabel textoCoeficientes = new JLabel("            Coeficientes Variables               Independientes");
        // listaTextos.add(textoCoeficientes);
        // textoCoeficientes.setFont(letraTexto);
        // textoCoeficientes.setHorizontalAlignment(JLabel.CENTER);
        // textoCoeficientes.setBounds(30,100,800,50);

        panelCantidadFilas = new JPanel();
        panelCantidadFilas.setOpaque(false);
        panelCantidadFilas.setLayout(new GridLayout(4,2,50,20));
        panelCantidadFilas.setBounds(480,170,300,300);

        JTable tablaXY = new JTable(new DefaultTableModel());

        scrollXY = new PanelScroll(tablaXY);
        listaScrolls.add(scrollXY);
        scrollXY.setBounds(20, 170, 300, 300);
        scrollXY.set_dimensiones(0, 2);
        scrollXY.cambiar_identificadores("X,Y".split(","));

        botonCalcular = new Boton(letraBoton);
        botonCalcular.setText("Calcular");
        botonCalcular.setVerticalAlignment(JButton.CENTER);
        botonCalcular.setBounds(280,500,200,50);

        JLabel textoRegresion = new JLabel("Regresión Polinomial");
        listaTextos.add(textoRegresion);
        textoRegresion.setFont(letraTexto);
        textoRegresion.setHorizontalAlignment(JLabel.CENTER);
        textoRegresion.setBounds(20,0,300,50);

        JLabel textoSimpsons = new JLabel("Metodos Simpsons");
        listaTextos.add(textoSimpsons);
        textoSimpsons.setFont(letraTexto);
        textoSimpsons.setHorizontalAlignment(JLabel.CENTER);
        textoSimpsons.setBounds(480,50,300,50);

        panelCantidadFilas = new JPanel();
        panelCantidadFilas.setOpaque(false);
        // panelTamanoTabla.setLayout(new GridLayout(3,1,0,0));
        panelCantidadFilas.setBounds(0,50,400,200);

        JLabel textoCantidadFilas = new JLabel("Cantidad de Filas   ");
        listaTextos.add(textoCantidadFilas);
        textoCantidadFilas.setFont(letraTexto);
        textoCantidadFilas.setHorizontalAlignment(JLabel.CENTER);

        campoCantidadFilas = new Campo(letraTexto);
        campoCantidadFilas.setPreferredSize(new Dimension(100,50));
        campoCantidadFilas.setText("5");
        
        botonAceptar = new Boton(letraBoton);
        botonAceptar.setText("Aceptar");
        botonAceptar.setVerticalAlignment(JButton.CENTER);
        botonAceptar.setPreferredSize(new Dimension(200,50));

        
        panelCantidadFilas.add(textoCantidadFilas);
        panelCantidadFilas.add(campoCantidadFilas);
        panelCantidadFilas.add(botonAceptar);

        botonLimpiar = new Boton(letraBoton);
        botonLimpiar.setText("Limpiar");
        botonLimpiar.setVerticalAlignment(JButton.CENTER);
        botonLimpiar.setBounds(20,470,200,50);
        
        panelDatos.add(textoRegresion);
        panelDatos.add(textoSimpsons);
        panelDatos.add(panelCantidadFilas);
        panelDatos.add(scrollXY);
        panelDatos.add(botonLimpiar);
        panelDatos.add(panelIntroducirDatos);
        // panelDatos.add(textoCoeficientes);
        panelDatos.add(botonCalcular);
    }
    private void inicializarPanelPrincipal() {
        panelPrincipal = new JPanel();
        panelPrincipal.setOpaque(false);
        panelPrincipal.setLayout(null);

        panelBienvenida = new JPanel();
        panelBienvenida.setOpaque(false);
        panelBienvenida.setLayout(new GridLayout(10,1));
        panelBienvenida.setBounds(50, 200, 
        700, 500);

        JLabel textoMetodos = new JLabel("Usando los Metodos de Simpson");
        textoMetodos.setFont(letraSubTitulo);
        textoMetodos.setHorizontalAlignment(JLabel.CENTER);

        JLabel textoMetodos2 = new JLabel("y Regresión Polinomial");
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
        JLabel textoTitulo = new JLabel("Cálculo de Integrales y Regresión");
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