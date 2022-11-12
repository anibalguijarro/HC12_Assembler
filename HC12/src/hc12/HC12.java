/*
 * "Proyecto simulador - Ensamblador de procesador HC12"
 * Universidad de Guadalajara
 * Centro Universitario de Los Altos
 * Seminario de Solución de Problemas de Traductores de Lenguaje I
 */
package hc12;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Martin Raygoza De León
 */
public class HC12 {
    
    /**
     * Método para realizar lectura y comprobación total del documento de texto
     * @param archivo Documento texto a leer
     * @throws FileNotFoundException 
     */
    public static EstructuraArray lectura(String archivo) throws FileNotFoundException{
        Scanner in = null; //Instanciar Scanner (Filtro)
        String linea; //Variable que guarda la linea del archivo que está leyendo (en curso)
        String [] palabras; //Guarda las palabras que existen de cada línea (Etiqueta, Codop, Operando)
        boolean [] existente = new boolean[2]; //Detecta si existe ETIQUETA [0] y CODOP [1]. (OPERADOR no necesario al momento)
        Detector detector = new Detector(); //Objeto
        EstructuraArray codigoinicio = null; //Objeto que contendrá la primera linea (lista)
        EstructuraArray codigo = new EstructuraArray(); //Objeto
        boolean inicio = false;
        
        try{
            
            in = new Scanner(new FileReader(archivo)); //Abrir el fichero de texto con FileReader (Iniciador)
            
            while(in.hasNextLine()){ //Bucle mientras haya datos que leer dentro del archivo
                linea = in.nextLine(); //Obtener la fila siguiente
                palabras = linea.split("\\s+"); //Guardar palabras que tiene la linea (Ignorando espacios en blanco)
                
                if(detector.comentario(linea)){
                    codigo.list.add(linea);
                }
                else if(palabras.length==1 && palabras[0].equals("")){
                    codigo.list.add("LINEA EN BLANCO");
                }
                else if(palabras[1].equalsIgnoreCase("FCC")){
                    palabras[1] = "FCC"; //Ponerlo en mayusculas (Para realizar 'split')
                    
                    String [] prueba = new String [3];
                    prueba[0] = palabras[0];
                    prueba[1] = palabras[1];
                    prueba[2] = linea.split("FCC")[1].trim();
                    
                    palabras = prueba;
                    
                    codigo.list.add(palabras[0]);
                    codigo.list.add(palabras[1]);
                    codigo.list.add(palabras[2]);
                    
                }
                else if(palabras.length<2 || palabras.length>3){
                    //codigo.list.add("ERROR - CANTIDAD DE INSTRUCCIONES NO VALIDO");
                    throw new IllegalArgumentException("ERROR - Cantidad de instrucciones no valido");
                }
                else{
                    
                    boolean error = false; //Variable para detectar errores en la linea
                    
                    //Comprobación de etiqueta
                    if(palabras[0].equals("")){
                        existente[0] = false;
                    }
                    else{
                        if(detector.etiqueta(palabras[0])){
                            existente[0] = true;
                        }
                        else{
                            codigo.list.add("ERROR - ESTRUCTURA DE ETIQUETA INCORRECTA");
                            error = true;
                            throw new IllegalArgumentException("ERROR - Estructura de etiqueta incorrecta");
                        }
                    }
                    //Comprobación de código de operación
                    if(detector.codop(palabras[1])){
                        if(palabras[1].compareToIgnoreCase("EQU")==0){
                            if(existente[0]==false){
                                throw new IllegalArgumentException("EQU tiene que contener etiqueta");
                            }
                        }else if(!inicio){
                            if(palabras[1].compareToIgnoreCase("ORG")==0 && !existente[0]){
                                inicio = true;
                            }else if(palabras[1].compareToIgnoreCase("ORG")!=0){
                                throw new IllegalArgumentException("ORG tiene que ser el iniciador de programa");
                            }else{
                                throw new IllegalArgumentException("ORG no puede contener etiqueta");
                            }
                        }else{
                            if(palabras[1].compareToIgnoreCase("ORG")==0){
                                throw new IllegalArgumentException("ORG solo puede estar una vez");
                            }
                        }
                            
                        existente[1] = true;
                    }
                    else{
                        codigo.list.add("ERROR - ESTRUCTURA DE CODOP INCORRECTA");
                        error = true;
                        throw new IllegalArgumentException("ERROR - Estructura de CODOP incorrecta");
                    }
                    
                    boolean end = false; //Variable para detectar si existe el codigo de operacion 'end' (Para finalizar lectura de archivo)
                    
                    //Validacion para detectar 'END' en el Codigo de operación
                    if(!palabras[0].equals("") || palabras.length>2){
                        if(palabras[1].compareToIgnoreCase("End")==0){
                            codigo.list.add("ERROR - 'END' DEBE IR OBLIGATORIAMENTE SIN ETIQUETA Y SIN OPERANDO");
                            error = true; //Palabra 'End' no está sola en la linea - Error
                            throw new IllegalArgumentException("ERROR - END debe ir obligatoriamente sin etiqueta y sin operando");
                        }
                    }
                    
                    if(error==false){
                        //Guardar etiqueta en lista
                        if(existente[0]){
                            codigo.list.add(palabras[0]);
                        }else{
                            codigo.list.add("");
                        }
                        
                        //Guardar código de operación en lista
                        if(existente[1]){
                            codigo.list.add(palabras[1]);
                            
                            if(palabras[1].compareToIgnoreCase("End")==0){
                                end = true;
                            }
                        }
                        
                        //Guardar operando en lista
                        if(palabras.length==3){
                            codigo.list.add(palabras[2]);
                        }
                        else{
                            codigo.list.add("");
                        }
                    }
                    
                    if(end){
                        break;
                    }
                    
                }
                
                if(codigoinicio == null){ //En caso de ser la primera linea (lista)
                    codigo.setNext(new EstructuraArray(codigo.getSize()+1,null,codigo,codigo,codigo.getActualLine()+1)); //Crear nueva y agregarla como siguiente
                    codigo.getNext().setBefore(codigo); //Organizar anterior de lista
                    codigoinicio = codigo; //Especificar inicio
                    codigo = codigo.getNext(); //Tomar la siguiente linea (lista)
                }
                else{ //En caso de no ser la primera linea (lista)
                    codigo.setNext(new EstructuraArray(codigo.getSize()+1,null,codigo,codigo.getFirst(),codigo.getActualLine()+1)); //Crear nueva y agregarla como siguiente
                    codigo.getNext().setBefore(codigo); //Organizar anterior de lista
                    codigo = codigo.getNext();//Tomar la siguiente linea (lista)
                }
                
            }
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
        }

        return codigoinicio;
        
    } //Fin de método 'lectura'
    
    /**
     * Método para leer el TABOP y buscar los CODOPs en él
     * @param doc Lineas (estructura de lista) que contiene el archivo leido al principio
     * @throws FileNotFoundException 
     */
    public static void tabop(EstructuraArray doc) throws FileNotFoundException, IOException{
        Scanner in = null; //Instanciar Scanner (Filtro)
        String linea; //Variable que guarda la linea del archivo que está leyendo (en curso)
        Conversor conversor = new Conversor();
        PrintWriter out = null;
        PrintWriter tabsim = null;
        
        int contloc = 0;
        doc.list.add("N");
        doc.list.add("");
        
        try{
            
            ArrayList etiquetas = new ArrayList();
            in = new Scanner(new FileReader("TABOP.csv")); //Abrir el fichero de texto con FileReader (Iniciador)
            in.nextLine(); //Toma la primera fila del TABOP (Encabezados, no importantes)
            tabsim = new PrintWriter(new FileWriter("TABSIM.txt"));
            out = new PrintWriter(new FileWriter("P10TMP.txt"));
            out.println("\t\tVALOR\tETIQUETA\tCODOP\tOPERANDO\tMOD.DIR\tBYTES.CALCULAR\tCOP");
            tabsim.println("\t\t\t\t\tETIQUETA\tVALOR");
            
            while(doc.getNext()!= null){ //Bucle mientras haya datos que leer dentro de la lista
                
                System.out.println(">> Linea " + doc.getActualLine() + ": ");
                
                if(doc.list.get(0).toString().length()>8){
                    if(doc.list.get(0).toString().contains(";")){
                        System.out.println("COMENTARIO\n");
                        doc = doc.getNext();
                    }
                    else{
                        System.out.println(doc.list.get(0) + "\n" );
                        doc = doc.getNext();
                    }
                }
                else{
                    
                    boolean encontrado = false; //Variable booleano que representará si ya se encontró el CODOP en el TABOP o no
                    
                    while(in.hasNextLine()){ //Bucle mientras haya datos que leer en TABOP
                        linea = in.nextLine(); //Linea toma valor de la siguiente fila del TABOP (Fila inicial de CODOP)
                        String [] infoCODOP = linea.split(","); //Guardar la linea del TABOP en curso en un arreglo
                        
                        Detector detector = new Detector();
                        
                        
                        if(doc.list.get(1).toString().compareToIgnoreCase("ORG")==0){
                            if(detector.Directo(doc) || detector.ExtendidoNoEtiqueta(doc)){
                                String comp[];
                                
                                if(doc.list.get(2).toString().startsWith("$")){
                                    comp = doc.list.get(2).toString().split("\\$");
                                    contloc = Integer.parseInt(conversor.hexDec(comp[1]));
                                }else if(doc.list.get(2).toString().startsWith("@")){
                                    comp = doc.list.get(2).toString().split("@");
                                    contloc = Integer.parseInt(conversor.octDec(comp[1]));
                                }else if(doc.list.get(2).toString().startsWith("%")){
                                    comp = doc.list.get(2).toString().split("%");
                                    contloc = Integer.parseInt(conversor.binDec(comp[1]));
                                }else{
                                    contloc = Integer.parseInt(doc.list.get(2).toString());
                                }
                                
                                if(contloc>65535){
                                    throw new IllegalArgumentException("ERROR - Memoria desbordada");
                                }
                                
                                String valor = conversor.decHex(contloc);
                                valor = relleno(valor);
                                encontrado = true;
                                out.println("DIR_INIC\t" + valor + "\tNULL" + "\t\tORG\t" + doc.list.get(2).toString());
                                System.out.println("INICIO DE PROGRAMA EN $" + valor);
                                System.out.println("");
                                break;
                            }else{
                                throw new IllegalArgumentException("Operando de ORG tiene que ser un valor de 0 a 65535 en decimal, hexadecimal, octal o binario");
                            }
                        }else if(doc.list.get(1).toString().compareToIgnoreCase("EQU")==0){
                            if(detector.Directo(doc) || detector.ExtendidoNoEtiqueta(doc)){// doc.list.get(2).toString().equals("")==false && 
                                System.out.println("DIRECTIVA " + doc.list.get(1).toString());
                                String comp[];
                                String valor;
                                if(doc.list.get(2).toString().startsWith("$")){
                                    comp = doc.list.get(2).toString().split("\\$");
                                    valor = comp[1];
                                }else if(doc.list.get(2).toString().startsWith("@")){
                                    comp = doc.list.get(2).toString().split("@");
                                    valor = conversor.octHex(comp[1]);
                                }else if(doc.list.get(2).toString().startsWith("%")){
                                    comp = doc.list.get(2).toString().split("%");
                                    valor = conversor.binHex(comp[1]);
                                }else{
                                    valor = conversor.decHex(Integer.parseInt(doc.list.get(2).toString()));
                                }
                                
                                valor = relleno(valor);
                                encontrado = true;
                                
                                if(!etiquetas.contains(doc.list.get(0).toString())){
                                    etiquetas.add(doc.list.get(0).toString());
                                    tabsim.println("EQU (ETIQUETA ABSOLUTA) \t"+doc.list.get(0).toString()+"\t\t"+valor);
                                }else{
                                    //NO GUARDAR ETIQUETA EN TABSIM
                                    throw new IllegalArgumentException("No se pueden repetir etiquetas");
                                }
                                out.println("VALOR_EQU\t" + valor + "\t" + doc.list.get(0).toString() + "\t\tEQU\t" + doc.list.get(2).toString() + "\tNULL");
                                System.out.println("");
                                break;
                            }else{
                                throw new IllegalArgumentException("Operando de EQU tiene que ser un valor de 0 a 65535 en decimal, hexadecimal, octal o binario");
                            }
                            
                        }
                        else if(!detector.Directivas(doc).equals("NO DIRECTIVA")){
                            encontrado = true;
//                            if(doc.list.get(0).toString().equals("")){
                                System.out.println("DIRECTIVA " + doc.list.get(1).toString());
                                String valor = conversor.decHex(contloc);
                                String dir_etiqueta = "NULL";
                                valor = relleno(valor);
                                
                                if(!doc.list.get(0).toString().equals("")){
                                    if(!etiquetas.contains(doc.list.get(0).toString())){
                                        dir_etiqueta = doc.list.get(0).toString();
                                        etiquetas.add(doc.list.get(0).toString());
                                        tabsim.println("CONTLOC (ETIQUETA RELATIVA) \t" + doc.list.get(0).toString() + "\t\t" + valor);
                                    }else{
                                        //NO GUARDAR ETIQUETA EN TABSIM
                                        throw new IllegalArgumentException("No se pueden repetir etiquetas");
                                    }
                                }
                                
                                if(detector.Directivas(doc).contains("VALOR-")){ //En caso de ser Directiva de reserva de memoria (no generan codigo maquina)
                                    String value = detector.Directivas(doc).split("-")[1];
                                    contloc = contloc + Integer.parseInt(value); //Aumentar contloc
                                    
                                    if(contloc>65535){
                                        throw new IllegalArgumentException("ERROR - Memoria desbordada");
                                    }
                                    
                                    out.println("CONTLOC\t" + valor + "\t" + dir_etiqueta + "\t\t" + doc.list.get(1).toString() + "\t" + doc.list.get(2).toString() + "\t\tDIRECTIVA\tNULL\tNULL");
                                    
                                }else{ //En caso de ser Directiva que si genera codigo maquina
                                    
                                    contloc = contloc + detector.Directivas(doc).length()/2;
                                    
                                    if(contloc>65535){
                                        throw new IllegalArgumentException("ERROR - Memoria desbordada");
                                    }
                                    
                                    out.println("CONTLOC\t" + valor + "\t" + dir_etiqueta + "\t\t" + doc.list.get(1).toString() + "\t" + doc.list.get(2).toString() + "\t\tDIRECTIVA\tNULL\t" + detector.Directivas(doc));
                                    
                                }
                                
                                System.out.println("");
                                break;
                            
                        }else if(infoCODOP[0].compareToIgnoreCase(doc.list.get(1).toString())==0){
                            
                            encontrado = true;
                            
                            boolean error = false;
                            while(infoCODOP[0].compareToIgnoreCase(doc.list.get(1).toString())==0){
                                
                                if(comprobacionCODOP(doc, infoCODOP)==false){
                                    throw new IllegalArgumentException("ERROR - Estructura de CODOP y operando erronea");
                                }
                                if(CODOP_equivalente(doc,infoCODOP)){
                                    boolean eti = false;
                                    error = false;
                                    System.out.println(doc.list.get(4).toString() + ", " + infoCODOP[5] + " bytes");
                                    String etiqueta;
                                    String operando;
                                    
                                    
                                    if(doc.list.get(0).equals("")){
                                        etiqueta = "NULL";
                                    }else{
                                        etiqueta = doc.list.get(0).toString();
                                        if(!etiquetas.contains(doc.list.get(0).toString())){
                                            tabsim.print("CONTLOC (ETIQUETA RELATIVA) \t"+ doc.list.get(0).toString()+"\t\t");
                                            etiquetas.add(doc.list.get(0).toString());
                                            eti = true;
                                        }else{
                                            //NO GUARDAR ETIQUETA EN TABSIM
                                            throw new IllegalArgumentException("ERROR - No se pueden repetir etiquetas");
                                        }
                                        
                                    }
                                    
                                    if(doc.list.get(2).equals("")){
                                        operando = "NULL";
                                    }else{
                                        operando = doc.list.get(2).toString();
                                    }
                                    
                                    String valor = conversor.decHex(contloc);
                                    valor = relleno(valor);
                                    contloc = contloc + Integer.parseInt(infoCODOP[5]);
                                    
                                    if(contloc>65535){
                                        throw new IllegalArgumentException("ERROR - Memoria desbordada");
                                    }
                                    
                                    if(eti){
                                        tabsim.println(valor);
                                    }
                                    //En caso de tener Códigos máquina de menor longitud (validación de bytes completos)
                                    if(infoCODOP[3].length()>2){
                                        error = false;
                                        
                                        if(doc.list.get(4).toString().contains("IDX")){
                                            out.println("CONTLOC\t" + valor + "\t" + etiqueta + "\t\t" + doc.list.get(1).toString() + "\t" + operando + "\t\t" + infoCODOP[2] + "\t\t" + infoCODOP[4] + "\t\t" + infoCODOP[3]+"\t"+doc.list.get(4).toString());
                                            
                                        }else{
                                            out.println("CONTLOC\t" + valor + "\t" + etiqueta + "\t\t" + doc.list.get(1).toString() + "\t" + operando + "\t\t" + infoCODOP[2] + "\t\t" + infoCODOP[4] + "\t\t" + infoCODOP[3]);
                                        }
                                    }else{
                                        error = false;
                                        
                                        if(doc.list.get(4).toString().contains("IDX")){
                                            out.println("CONTLOC\t" + valor + "\t" + etiqueta + "\t\t" + doc.list.get(1).toString() + "\t" + operando + "\t\t" + infoCODOP[2] + "\t\t" + infoCODOP[4] + "\t\t" + relleno(infoCODOP[3],2)+"\t"+doc.list.get(4).toString());
                                        }else{
                                            out.println("CONTLOC\t" + valor + "\t" + etiqueta + "\t\t" + doc.list.get(1).toString() + "\t" + operando + "\t\t" + infoCODOP[2] + "\t\t" + infoCODOP[4] + "\t\t" + relleno(infoCODOP[3],2));
                                        }
                                    }
                                    
                                    break;
                                    
                                }else{
                                    error = true;
                                }
                                linea = in.nextLine();
                                infoCODOP = linea.split(",");
                                
                            }
                            if(error){
                                System.out.println(doc.list.get(3).toString());
                                throw new IllegalArgumentException("ERROR - " + doc.list.get(3).toString());
                            }
                            System.out.println("");
                            break;
                        }
                        
                    }
                    if(encontrado==false){
                        throw new IllegalArgumentException("ERROR - CODOP no existente");
                        
                    }
                    
                    in = new Scanner(new FileReader("TABOP.csv")); //Volver al inicio del TABOP para siguiente busqueda
                    doc = doc.getNext(); //Tomar siguiente fila (lista)
                    
                }
                doc.list.add("N");
                doc.list.add("");
            }
            
            String valor = conversor.decHex(contloc);
            valor = relleno(valor);
            out.println("CONTLOC\t" + valor + "\tNULL" + "\t\tEND\t" + "NULL");
            System.out.println("FIN DEL PROGRAMA EN $" + valor);
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            }
            if(tabsim != null){
                tabsim.close();
            }
            if(out != null){
                out.close();
            }
        }
    } //Fin de método 'tabop'
    
    /**
     * Método para rellenar numero hexadecimal a 4 dígitos con 0s a la izquierda
     * @param valor Numero hexadecimal sin rellenar
     * @return Numero hexadecimal rellenado
     */
    private static String relleno(String valor){
        for(int i = valor.length(); i<4;i++){
            valor = "0" + valor;
        }
        return valor;
    } //Fin de método 'relleno'
    
    /**
     * Método para rellenar numero hexadecimal a 2 dígitos con 0s a la izquierda
     * @param valor Numero hexadecimal sin rellenar
     * @param lenght Tamaño del relleno
     * @return Numero hexadecimal rellenado
     */
    private static String relleno(String valor, int lenght){
        if(valor.length()>2){
            valor = valor.substring(valor.length()-2);
        }else{
            for(int i = valor.length(); i<lenght; i++){
                valor = "0" + valor;
            }
        }
        return valor;
    } //Fin de método 'relleno'
    
    /**
     * Método para rellenar numero hexadecimal a cantidad de dígitos requeridos con 0s a la izquierda
     * @param valor Numero hexadecimal sin rellenar
     * @param lenght Tamaño del relleno
     * @return Numero hexadecimal rellenado
     */
    private static String rellenoComplemento(String valor, int lenght){
        for(int i = valor.length(); i<lenght; i++){
            valor = "0" + valor;
        }
        return valor;
    } //Fin de método 'relleno'
    
    
    /**
     * Método para detectar los distintos modos de direcciónamiento respecto a lo que se muestra en el CODOP
     * @param doc Estructura en donde se encuentra el operando
     * @param infoCODOP Información del CODOP con la que se quiere comprobar
     * @return retorna un valor true si el operando corresponde a lo que indica el CODOP y modifica la EstructuraArray en la posición(3) con el posible error
     */
    public static boolean CODOP_equivalente(EstructuraArray doc, String[] infoCODOP){
        Detector d = new Detector();
        switch(infoCODOP[2]){
            case "Inmediato":
                if(d.Inmediato8bit(doc) || d.Inmediato16bit(doc)){
                    doc.list.set(4, "Inmediato");
                    return true;
                }else{
                    return false;
                }
            case "REL":
                if(d.Relativo(doc)){
                    if(infoCODOP[4].equals("1")){
                        doc.list.set(4, "Relativo 8 bits");
                        return true;
                    }else if(infoCODOP[4].equals("2")){
                        doc.list.set(4, "Relativo 16 bits");
                        return true;
                    }
                }else{
                    return false;
                }
            case "Directo":
                if(d.Directo(doc)){
                    doc.list.set(4, "Directo");
                    return true;
                }else{
                    return false;
                }
            case "Extendido":
                if(d.Extendido(doc)){
                    doc.list.set(4, "Extendido");
                    return true;
                }else{
                    return false;
                }
            case "IDX": case "IDX ":
                if(d.Indizado5bit(doc)){
                    doc.list.set(4, "Indizado 5 bits, (IDX)");
                    return true;
                }else if(d.IndizadoPrePost(doc)){
                    //Nombre del modo de direccionamiento se agrega directamente en método IndizadoPrePost (En clase Detector)
                    return true;
                }else if(d.IndizadoAcumulador(doc)){
                    doc.list.set(4,"Indizado Acumulador, (IDX)");
                    //doc.list.add("Acumulador");
                    return true;
                }
                else{
                    return false;
                }
            case "IDX1":
                if( d.Indizado9bit(doc)){
                    doc.list.set(4, "Indizado 9 bit, (IDX1)");
                    return true;
                }else{
                    return false;
                }
            case "IDX2":
                if( d.Indizado16bit(doc)){
                    doc.list.set(4, "Indizado 16 bit, (IDX2)");
                    return true;
                }else{
                    return false;
                }
            case "[D;IDX]":
                if(d.IndizadoAcumuladorIndirecto(doc)){
                    doc.list.set(4, "Indizado indirecto de acumulador 'D', ([D,IDX])");
                    return true;
                }else{
                    return false;
                }
            case"[IDX2]":
                if(d.IndizadoIndirecto16bit(doc)){
                    doc.list.set(4, "Indizado indirecto 16 bit, ([IDX2])");
                    return true;
                }else{
                    return false;
                }
            case "Inherente " :case "Inherente":
                if(doc.list.get(2).equals("")){
                    doc.list.set(4, "Inherente");
                    return true;
                }
                else{
                    return false;
                }
            default:
                System.out.println(infoCODOP[2]);
                System.out.println(infoCODOP[0]);
                doc.list.set(3, "Error");
                return false;
                    
        }
    } //Fin de método 'CODOP_equivalente'
    
    /**
     * Método que comprueba la estructura del CODOP con respecto a información del TABOP (Si codop lleva operando o no)
     * @param doc Linea de codigo que se esta leyendo
     * @param infoCODOP Información del CODOP con la que se quiere comprobar
     * @return True si la estructura CODOP-OPERANDO es correcta, False de lo contrario
     */
    public static boolean comprobacionCODOP(EstructuraArray doc, String [] infoCODOP){
   
        if(infoCODOP[1].equals("Operando") && doc.list.get(2).equals("")){
            System.out.println("ERROR - OPERANDO FALTANTE");
            return false;
        }
        else if(infoCODOP[1].equals("No Operando") && !doc.list.get(2).equals("")){
            System.out.println("ERROR - OPERANDO SOBRANTE");
            return false;
        }
        
        return true;
    } //Fin de método 'comprobacionCODOP'
    
    /**
     * Método para agregar los bytes faltantes por calcular del código máquina del archivo temporal
     * NOTA: El archivo temporal ya contiene los bytes calculados al final de cada fila, solo se agregan los faltantes al final de cada una.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void codigoMaquina() throws FileNotFoundException, IOException{
        Scanner sc = new Scanner(System.in);
        Scanner in = null;
        String instruccion[];
        String linea;
        String todo;
        int contador = 1;
        Detector detector = new Detector();
        
        try{
            
            in = new Scanner(new FileReader("P10TMP.txt")); //Abrir el fichero de texto con FileReader (Iniciador)
            todo = in.nextLine()+ "\n";
            System.out.println("\n|| CÓDIGOS MÁQUINA ||");
            
            while(in.hasNextLine()){ //Bucle mientras haya datos que leer
                
                linea = in.nextLine(); //Obtener la fila en curso
                instruccion = linea.split("\\s+");
                if(instruccion[0].equalsIgnoreCase("DIR_INIC") || instruccion[0].equalsIgnoreCase("VALOR_EQU") || instruccion[3].equalsIgnoreCase("END")){
                    todo = todo + linea + "\n";
                    System.out.println(">> Linea " + contador + ": No genera código máquina"); //Impresion en consola
                }
                else if(instruccion[5].equalsIgnoreCase("DIRECTIVA") || instruccion[3].equalsIgnoreCase("FCC")){
                    
                    todo = todo + linea + "\n";
                    
                    if(instruccion[instruccion.length-1].equalsIgnoreCase("NULL")){ ///En caso de no generar codigo maquina
                        System.out.println(">> Linea " + contador + ": No genera código máquina"); //Impresion en consola
                    }else{ //En caso de si generar codigo maquina
                        System.out.println(">> Linea " + contador + ": " + instruccion[instruccion.length-1]); //Impresion en consola
                    }
                    
                }
                else{
                
                    if(instruccion[5].equalsIgnoreCase("Inherente")){
                        todo = todo + linea + "\n";
                        System.out.println(">> Linea " + contador + ": " + instruccion[7]); //Impresion en consola
                    }else if(instruccion[5].equalsIgnoreCase("Directo")){
                        todo = todo + linea + relleno(conversorHex(instruccion[4]),2) + "\n";
                        System.out.println(">> Linea " + contador + ": " + instruccion[7] + relleno(conversorHex(instruccion[4]),2)); //Impresion en consola
                    }else if(instruccion[5].equalsIgnoreCase("Extendido")){

                        if(detector.etiqueta(instruccion[4])){

                            String etiqueta = tabsim(instruccion[4]);

                            if(etiqueta.equals("NO HAY")){
                                throw new IllegalArgumentException("ERROR - Etiqueta no existente");
                            }else{
                                todo = todo + linea + relleno(etiqueta) + "\n";
                                System.out.println(">> Linea " + contador + ": " + instruccion[7] + relleno(etiqueta)); //Impresion en consola
                            }

                        }else{
                            todo = todo + linea + relleno(conversorHex(instruccion[4])) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + relleno(conversorHex(instruccion[4]))); //Impresion en consola
                        }
                    }else if(instruccion[5].equalsIgnoreCase("Inmediato")){
                        
                        String valor = conversorHex(instruccion[4].split("#")[1]);
                        
                        if(valor.length()>2 && instruccion[6].equals("1")){
                            throw new IllegalArgumentException("ERROR - Cantidad de bytes del operando no coincide con los bytes por calcular de la instruccion");
                        }else if(instruccion[6].equals("2")){
                            todo = todo + linea + relleno(valor) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + relleno(valor)); //Impresion en consola
                        }else{
                            todo = todo + linea + relleno(valor,2) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + relleno(valor,2)); //Impresion en consola
                        }

                    }
                    else if(instruccion[5].equalsIgnoreCase("REL")){
                        Conversor conver = new Conversor();
                        
                        String siguiente = Integer.toString(Integer.parseInt(conver.hexDec(instruccion[1])) + (instruccion[7].length()/2) + Integer.parseInt(instruccion[6]));
                        todo = todo + linea + "\t"+ instruccion[7] + relativo(tabsim(instruccion[4]),siguiente,instruccion[6].equalsIgnoreCase("2")) + "\n";
                        System.out.println(">> Linea " + contador + ": "+ instruccion[7] + relativo(tabsim(instruccion[4]),siguiente,instruccion[6].equalsIgnoreCase("2"))); //Impresion en consola
                        
                    }
                    else{
                        Indizados MacInd = new Indizados();
                        if(instruccion[8].equalsIgnoreCase("pre")||instruccion[8].equalsIgnoreCase("post")){
                            todo = todo + linea + "\t" + instruccion[7] + MacInd.pre_post(instruccion[4]) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + MacInd.pre_post(instruccion[4])); //Impresion en consola
                        }else if(instruccion[9].equalsIgnoreCase("5")){
                            todo = todo + linea +"\t" + instruccion[7] + MacInd.offset5(instruccion[4]) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + MacInd.offset5(instruccion[4])); //Impresion en consola
                        }else if(instruccion[9].equalsIgnoreCase("acumulador,")){
                            todo = todo + linea +"\t" + instruccion[7] + MacInd.acumulador(instruccion[4]) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + MacInd.acumulador(instruccion[4])); //Impresion en consola
                        }else if(instruccion[9].equalsIgnoreCase("9")){
                            todo = todo  +linea +"\t" + instruccion[7] + MacInd.offset9(instruccion[4]) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + MacInd.offset9(instruccion[4])); //Impresion en consola
                        }else if(instruccion[9].equalsIgnoreCase("16")){
                            todo = todo + linea +"\t"+ instruccion[7] + MacInd.offset16(instruccion[4]) + "\n";
                            System.out.println(">> Linea " + contador + ": " + instruccion[7] + MacInd.offset16(instruccion[4])); //Impresion en consola
                        }
                        else if(instruccion[9].equalsIgnoreCase("indirecto")){
                            if(instruccion[10].equalsIgnoreCase("16")){
                                todo = todo + linea +"\t"+ instruccion[7] + MacInd.indirecto16(instruccion[4]) + "\n";
                                System.out.println(">> Linea " + contador + ": " + instruccion[7] + MacInd.indirecto16(instruccion[4])); //Impresion en consola
                            }else{
                               todo = todo + linea +"\t"+ instruccion[7] + MacInd.indirectoAcumulador(instruccion[4]) + "\n";
                               System.out.println(">> Linea " + contador + ": " + instruccion[7] + MacInd.indirectoAcumulador(instruccion[4])); //Impresion en consola 
                            }
                        }
                        else{
                            todo = todo + linea + "\n";
                            System.out.println(">> Linea " + contador + ": Código máquina pendiente por calcular"); //Impresion en consola
                        }
                        
                    }
                }
                
                contador++;
                
            }
            
            reescribir(todo);
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            } //Fin de if
        }
    } //Fin de método 'codigoMaquina'
    
    /**
     * Método para calcular el salto (Offset) de los modos de direccionamiento de tipo Relativo
     * @param etiqueta Valor en hexadecimal de la etiqueta (Que se encuentra en el TABSIM) - Destino
     * @param operando Valor en hexadecimal del operando - Origen
     * @param es16 True si es un salto de 16 bits, False si es un salto de 9 bits
     * @return Valor del salto (Offset) en hexadecimal
     */
    public static String relativo(String etiqueta, String operando, boolean es16){
        Conversor conver = new Conversor();
        Indizados comp = new Indizados();
        int resta;
        resta = Integer.parseInt(conver.hexDec(etiqueta)) - Integer.parseInt(operando);
        //NOTA: Si 'resta' es negativo, entonces salto hacia atrás (Aplicar complemento a 2). Si 'resta' es positivo, entonces salto hacia adelante.
        
        if(es16){
            if(resta<=-32768 || resta>=32767){
                throw new IllegalArgumentException("ERROR - El valor del salto no es de 16 bits");
            }
            if(resta<0){
                return  relleno(conver.binHex(comp.complemento2(resta, 16)));
            }else{
                return relleno(conver.decHex(resta),4);
            }
        }else{
            if(resta<=-128 || resta>=127){
                throw new IllegalArgumentException("ERROR - El valor del salto no es de 8 bits");
            }
            if(resta<0){
                return  relleno(conver.binHex(comp.complemento2(resta, 8)),2);
            }else{
                return relleno(conver.decHex(resta),2);
            }
        }
    } //Fin de método 'relativo'
    
    
     /**
     * Método para rescribir el documento txt con las modificaciones deseadas
     * @param texto Variable que contiene todo el documento anterior junto con las modificaciones
     * @throws IOException 
     */
    public static void reescribir(String texto) throws IOException{
        
        PrintWriter out = null; //Instanciar PrintWriter (filtro)
        
        try{
            out = new PrintWriter(new FileWriter("P10TMP.txt")); //Abrir fichero de texto con FileWriter (iniciador)
            out.print(texto); //Escribir todo el texto (modificado)
            
        } finally{
            if(out!=null){ //En caso de estar abierto el documento
                out.close(); //Cerrar el documento
            }
        } //Fin de finally
        
    } //Fin de método 'reescribir'
    
    /**
     * Método para retornar el valor de la etiqueta deseada
     * @param etiqueta Etiqueta a buscar en el TABSIM
     * @return Valor de la etiqueta, 'NO HAY' de caso contrario.
     * @throws FileNotFoundException 
     */
    public static String tabsim(String etiqueta) throws FileNotFoundException{
        Scanner in = null;
        String simbolo[];
        
        try{
            
            in = new Scanner(new FileReader("TABSIM.txt")); //Abrir el fichero de texto con FileReader (Iniciador)
            in.nextLine();
            
            while(in.hasNext()){
                
                simbolo = in.nextLine().split("\\s+");
            
                if(simbolo[3].equals(etiqueta)){
                    return simbolo[4];
                }
                
            }
            
        }finally{
            if(in!=null){ //En caso de estar abierto el documento
                in.close(); //Cerrar el documento
            } //Fin de if
        }
        
        return "NO HAY";
        
    } //Fin de método 'tabsim'
    
    /**
     * Método que retorna el valor en hexadecimal de los distintos sistemas numericos (Separa simbolo inicial $, @, %)
     * @param numero Numero a convertir a hexadecimal
     * @return Numero convertido a hexadecimal
     */
    public static String conversorHex(String numero){
        String comp[];
        String valor;
        Conversor conversor = new Conversor();
        
        if(numero.startsWith("$")){
            comp = numero.split("\\$");
            valor = comp[1];
        }else if(numero.startsWith("@")){
            comp = numero.split("@");
            valor = conversor.octHex(comp[1]);
        }else if(numero.startsWith("%")){
            comp = numero.split("%");
            valor = conversor.binHex(comp[1]);
        }else{
            valor = conversor.decHex(Integer.parseInt(numero));
        }
        
        return valor;
        
    } //Fin de método 'conversorHex'
    
    /**
     * Método para generar el archivo objeto (código objeto) de todas las instrucciones del programa
     * @param original Archivo original (el que contiene las instrucciones)
     * @param temporal Archivo temporal (donde se guarda el contador de localidades y códigos máquina)
     * @param name_obj Archivo para guardar el código objeto generado
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void codigoObjeto(String original, String temporal, String name_obj) throws FileNotFoundException, IOException{
        Scanner read = null;
        PrintWriter write = null; //Instanciar PrintWriter (filtro)
        Conversor conver = new Conversor();
        
        try{
            
            read = new Scanner(new FileReader(temporal)); //Abrir el fichero de texto con FileReader (Iniciador)
            write = new PrintWriter(new FileWriter(name_obj)); //Abrir fichero de texto con FileWriter (iniciador)
            
            read.nextLine(); //Tomar primera linea (encabezados)
            
            String [] instrucciones;
            int suma;
            int contloc = 0;
            String machinecode = "";
            String S1 = "";
            
            String S0 = relleno(conver.decHex(4 + original.length()),2) + "0000";
            char nombre[] = original.toCharArray();
            String valor = "";
            
            for(int i=0; i<nombre.length; i++){
                valor = valor + conver.decHex((int)nombre[i]); //Calcular código ASCII de los caracteres
            }
            
            S0 = S0 + valor + "0A";
            
            String [] ck = S0.split("(?<=\\G..)");
            suma = 0;

            for(int i=0; i<ck.length; i++){

                suma = suma + Integer.parseInt(conver.hexDec(ck[i]));

            }

            S0 = "S0" + S0 + conver.binHex(complemento1(suma));
            write.println(S0); //Escribir en archivo de texto (S0)
            boolean reserva_memoria = false;
            
            while(read.hasNextLine()){
                
                instrucciones = read.nextLine().split("\\s+");
                
                if(reserva_memoria==true && instrucciones[0].equalsIgnoreCase("CONTLOC")){
                    contloc = Integer.parseInt(conver.hexDec(instrucciones[1]));
                    reserva_memoria = false;
                }
                
                if(instrucciones[0].equalsIgnoreCase("DIR_INIC")){
                    
                    contloc = Integer.parseInt(conver.hexDec(instrucciones[1]));
                    
                }else if(instrucciones[0].equalsIgnoreCase("CONTLOC") && !instrucciones[instrucciones.length-1].equalsIgnoreCase("NULL")){
                    
                    machinecode = machinecode + instrucciones[instrucciones.length-1];
                    
                    if(machinecode.length()>=32){
                        String bytes16 = machinecode.substring(0, 32);
                        machinecode = machinecode.substring(32);
                        
                        S1 = "13" + relleno(conver.decHex(contloc)) + bytes16;
                        contloc = contloc + 16;
                        
                        String [] bytes = S1.split("(?<=\\G..)");
                        suma = 0;
                        
                        for(int i=0; i<bytes.length; i++){

                            suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                        }
                        
                        S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                        write.println(S1); //Escribir en archivo de texto (S1)
                    }
                    
                }else if(instrucciones[0].equalsIgnoreCase("VALOR_EQU")){
                    
                    if(machinecode.length()>=32){
                        String bytes16 = machinecode.substring(0, 32);
                        machinecode = machinecode.substring(32);
                        
                        S1 = "13" + relleno(conver.decHex(contloc)) + bytes16;
                        contloc = contloc + 16;
                        
                        String [] bytes = S1.split("(?<=\\G..)");
                        suma = 0;
                        
                        for(int i=0; i<bytes.length; i++){

                            suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                        }
                        
                        S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                        write.println(S1); //Escribir en archivo de texto (S1)
                    }else if(machinecode.length()>0){
                        
                        S1 = relleno(conver.decHex(3 + machinecode.length()/2),2) + relleno(conver.decHex(contloc)) + machinecode;
                        contloc = contloc + machinecode.length()/2;
                        
                        String [] bytes = S1.split("(?<=\\G..)");
                        suma = 0;
                        
                        for(int i=0; i<bytes.length; i++){

                            suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                        }
                        
                        S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                        write.println(S1); //Escribir en archivo de texto (S1)
                        machinecode = "";
                    }
                    
                    S1 = "03" + instrucciones[1];
                    
                    String [] bytes = S1.split("(?<=\\G..)");
                    suma = 0;

                    for(int i=0; i<bytes.length; i++){

                        suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                    }

                    S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                    write.println(S1); //Escribir en archivo de texto (S1)
                    machinecode = "";
                    
                    
                }else if(instrucciones[0].equalsIgnoreCase("CONTLOC") && !instrucciones[3].equalsIgnoreCase("END") && instrucciones[instrucciones.length-1].equalsIgnoreCase("NULL")){
                    
                    reserva_memoria = true;
                    
                    if(machinecode.length()>=32){
                        String bytes16 = machinecode.substring(0, 32);
                        machinecode = machinecode.substring(32);
                        
                        S1 = "13" + relleno(conver.decHex(contloc)) + bytes16;
                        contloc = contloc + 16;
                        
                        String [] bytes = S1.split("(?<=\\G..)");
                        suma = 0;
                        
                        for(int i=0; i<bytes.length; i++){

                            suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                        }
                        
                        S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                        write.println(S1); //Escribir en archivo de texto (S1)
                        
                    }else{
                        
                        S1 = relleno(conver.decHex(3 + machinecode.length()/2),2) + relleno(conver.decHex(contloc)) + machinecode;
                        contloc = contloc + machinecode.length()/2;
                        
                        String [] bytes = S1.split("(?<=\\G..)");
                        suma = 0;
                        
                        for(int i=0; i<bytes.length; i++){

                            suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                        }
                        
                        S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                        write.println(S1); //Escribir en archivo de texto (S1)
                        machinecode = "";
                    }
                    
                }else if(instrucciones[3].equalsIgnoreCase("END")){
                    
                    while(!machinecode.equals("")){
                        
                        if(machinecode.length()>=32){

                            //Hasta que se termine el codigo maquina del programa

                            String bytes16 = machinecode.substring(0, 32);
                            machinecode = machinecode.substring(32);

                            S1 = "13" + relleno(conver.decHex(contloc)) + bytes16;
                            contloc = contloc + 16;

                            String [] bytes = S1.split("(?<=\\G..)");
                            suma = 0;

                            for(int i=0; i<bytes.length; i++){

                                suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                            }

                            S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                            write.println(S1); //Escribir en archivo de texto (S1)
                            
                        }else{

                            S1 = relleno(conver.decHex(3 + machinecode.length()/2),2) + relleno(conver.decHex(contloc)) + machinecode;
                            contloc = contloc + machinecode.length()/2;

                            String [] bytes = S1.split("(?<=\\G..)");
                            suma = 0;

                            for(int i=0; i<bytes.length; i++){

                                suma = suma + Integer.parseInt(conver.hexDec(bytes[i]));

                            }

                            S1 = "S1" + S1 + relleno(conver.binHex(complemento1(suma)),2);
                            write.println(S1); //Escribir en archivo de texto (S1)
                            machinecode = "";
                        }
                    
                    }
                    
                }
                S1 = "";
            }
            
            write.println("S9030000FC"); //Escribir en archivo de texto (S9)
            
        }finally{
            
            if(read!=null){
                read.close();
            }
            if(write!=null){
                write.close();
            }
            
        }
    } //Fin de método 'códigoObjeto'
    
    /**
     * Método para realizar el complemento a 1 de numero
     * @param numero Numero en decimal a aplicar complemento a 2
     * @return Número en binario de 8 bits (Numero decimal => Numero binario con complemento 1)
     */
    public static String complemento1(int numero){
        
        Conversor c = new Conversor();
        
        String [] binario = rellenoComplemento(c.decBin(numero),8).split("");
        //System.out.println(relleno(c.decBin(numero),8));
        for(int i=0; i<binario.length; i++){
            
            if(binario[i].equals("0")){
                binario[i] = "1";
            }else{
                binario[i] = "0";
            }
            
        }
        
        String valor = "";

        for(int i=0; i<binario.length; i++){
            valor = valor + binario[i];
        }
        
        if(valor.length()>=9){
            
            valor = valor.substring(valor.length()-8);
            
        }
        
        return valor;
        
    } //Fin de método 'complemento1'
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        try {

            tabop(lectura("P10ASM.txt"));
            codigoMaquina();
            codigoObjeto("P10ASM.txt","P10TMP.txt", "machinecode.asm");
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HC12.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HC12.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    } //Fin de main
    
} //Fin de clase
