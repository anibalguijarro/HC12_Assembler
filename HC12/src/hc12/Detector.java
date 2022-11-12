/*
 * "Proyecto simulador - Ensamblador de procesador HC12"
 * Universidad de Guadalajara
 * Centro Universitario de Los Altos
 * Seminario de Solución de Problemas de Traductores de Lenguaje I
 */
package hc12;


/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Martin Raygoza De León
 */
public class Detector {//Inicio de clase Detector
    
    /**
     * Método para comprobar si la línea es un comentario
     * @param linea Cadena de texto a comprobar
     * @return True si es comentario; False de lo contrario
     */
    public boolean comentario (String linea){
        
        if(linea.lastIndexOf(";")==0){

            if(linea.length()<=81 && linea.length()>1){
                
                return true;
                
            }

        }
        
        return false;
        
    } //Fin de método 'comentario'
    
    /**
     * Método para comprobar si la palabra es etiqueta
     * @param palabra - Cadena de texto a comprobar
     * @return True si es etiqueta; False de lo contrario
     */
    public boolean etiqueta (String palabra){
        
        if(palabra.length()<=8 && palabra.length()>=1){
            
            String etiqueta[] = palabra.split("");
            
            if(etiqueta[0].compareToIgnoreCase("a")>=0 && etiqueta[0].compareToIgnoreCase("z")<=0){
                
                for(int i=1; i<etiqueta.length; i++){
                    
                    if((etiqueta[i].compareToIgnoreCase("a")<0 || etiqueta[i].compareToIgnoreCase("z")>0) && (etiqueta[i].compareToIgnoreCase("0")<0 || etiqueta[i].compareToIgnoreCase("9")>0) && !etiqueta[i].equals("_")){
                        
                        return false;
                        
                    }
                    
                }
                
                return true;
                
            }
            
        }
        
        return false;
        
    } //Fin del método 'etiqueta'
    
    /**
     * Método para comprobar si la palabra es código de operación
     * @param palabra Cadena de texto a comprobar
     * @return True si es código de operación; False de lo contrario
     */
    public boolean codop (String palabra){
        
        if(palabra.length()<=5 && palabra.length()>=1){
            
            String codop[] = palabra.split("");
            
            if(codop[0].compareToIgnoreCase("a")>=0 && codop[0].compareToIgnoreCase("z")<=0){
                
                boolean punto = false;
                
                for(int i=1; i<codop.length; i++){
                    
                    if((codop[i].compareToIgnoreCase("a")<0 || codop[i].compareToIgnoreCase("z")>0) && !codop[i].equals(".")){
                        
                        return false;
                        
                    }
                    else if(codop[i].equals(".")){

                        if(punto==true){
                            
                            return false;
                        }
                        
                        punto = true;

                    }
                    
                }
                
                return true;
                
            }
            
        }
        
        return false;
        
    } //Fin de método 'codop'
    
    /**
     * Método para comprobar si el operando es un inmediato de 8 bits
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es inmediato de 8 bits, True de lo contrario
     */
    public boolean Inmediato8bit(EstructuraArray doc){
        Conversor c = new Conversor();
        String []dato = doc.list.get(2).toString().split("#");
        if(doc.list.get(2).toString().lastIndexOf("#")!=0){
            //doc.list.set(3, "ERROR - ESTRUCTURA ERROREA DE SIMBOLO (#) ");
            return false;
        }else if(dato.length<1){
            //VALIDACIÓN - NINGUN VALOR EN OPERANDO, SOLO '#'
            return false;
        }else{
            
            if(dato[1].lastIndexOf("%")==0){
                dato = dato[1].split("%");
                if(dato.length<1){
                    return false;
                }else{
                    if(c.binario(dato[1])){
                        int dat = Integer.parseInt(c.binDec(dato[1]));
                        
                        if(dat>255 || dat<0){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        return false;
                    }
                }
                
            }else if(dato[1].lastIndexOf("$")==0){
                dato = dato[1].split("\\$");
                if(dato.length<1){
                    return false;
                }else{
                    if(c.hexadecimal(dato[1])){
                        int dat = Integer.parseInt(c.hexDec(dato[1]));
                        
                        if(dat>255 || dat<0){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        return false;
                    }
                }
                                
            }else if(dato[1].lastIndexOf("@")==0){
                dato = dato[1].split("@");
                
                if(dato.length<1){
                    return false;
                }else{
                    if(c.octal(dato[1])){
                        int dat = Integer.parseInt(c.octDec(dato[1]));
                        
                        if(dat>255 || dat<0){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        return false;
                    }
                }
                                    
            }else{
                if(c.decimal(dato[1])){
                    int dat = Integer.parseInt(dato[1]);
                    
                    if(dat>255 || dat<0){
                        return false;
                    }else{
                        return true;
                    }
                }else{
                    return false;
                }
            }
        }
    }//Fin método 'Inmediato8bit'
    
    /**
     * Método para comprobar si el operando es un inmediato de 16 bits
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es inmediato de 16 bits, True de lo contrario
     */
    public boolean Inmediato16bit(EstructuraArray doc){
        Conversor c = new Conversor();
        String []dato = doc.list.get(2).toString().split("#");
        if(doc.list.get(2).toString().lastIndexOf("#")!=0){
            //doc.list.set(3, "Error simbolo (#) mal estructurado");
            return false;
        }else if(dato.length<1){
            //VALIDACIÓN - NINGUN VALOR EN OPERANDO, SOLO '#'
            return false;
        }else{
            if(dato[1].lastIndexOf("%")==0){
                dato = dato[1].split("%");
                if(dato.length<1){
                    doc.list.set(3, "Error no se encontro numero a trabajar");
                    return false;
                }else{
                    if(c.binario(dato[1])){
                        int dat = Integer.parseInt(c.binDec(dato[1]));
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error la cantidad de bits es incorrecta");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el numero ingresado no es binario");
                        return false;
                    }
                }
                
            }else if(dato[1].lastIndexOf("$")==0){
                dato = dato[1].split("\\$");
                if(dato.length<1){
                    doc.list.set(3, "Error no se encontro numero a trabajar");
                    return false;
                }else{
                    if(c.hexadecimal(dato[1])){
                        int dat = Integer.parseInt(c.hexDec(dato[1]));
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error la cantidad de bits es incorrecta");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el numero no es hexadecimal");
                        return false;
                    }
                }
                
            }else if(dato[1].lastIndexOf("@")==0){
                dato = dato[1].split("@");
                if(dato.length<1){
                    doc.list.set(3, "Error no se encontro numero a trabajar");
                    return false;
                }else{
                    if(c.octal(dato[1])){
                        int dat = Integer.parseInt(c.octDec(dato[1]));
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error la cantidad de bits es incorrecta");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el número no es octal");
                        return false;
                    }
                }
                    
            }else{
                if(c.decimal(dato[1])){
                    int dat = Integer.parseInt(dato[1]);
                    if(dat>65535 || dat<256){
                        doc.list.set(3, "Error, rango incorrecto");
                        return false;
                    }else{
                        doc.list.set(3, "N");
                        return true;
                    }
                }else{
                    doc.list.set(3, "Error el numero no es decimal entero");
                    return false;
                }
            }
        }
    }//Fin método 'Inmediato16bit'
    
    /**
     * Método para comprobar si el operando es directo
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es directo, True de lo contrario
     */
    public boolean Directo(EstructuraArray doc){
        Conversor c = new Conversor();
        String comp[]= doc.list.get(2).toString().split("");
        switch(comp[0]){
            case"$":
                comp = doc.list.get(2).toString().split("\\$");
                
                if(comp.length<1){
                    return false;
                }else{
                    if(c.hexadecimal(comp[1])){
                        int dat = Integer.parseInt(c.hexDec(comp[1]));
                        
                        if(dat>255 || dat<0){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        return false;
                    }
                }
                
            case"%":
                comp = doc.list.get(2).toString().split("%");
                
                if(comp.length<1){
                    return false;
                }else{
                    if(c.binario(comp[1])){
                        int dat = Integer.parseInt(c.binDec(comp[1]));
                        
                        if(dat>255 || dat<0){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        return false;
                    }
                }
                
            case"@":
                   comp = doc.list.get(2).toString().split("@");
                   
                   if(comp.length<1){
                        return false;
                   }else{
                        if(c.octal(comp[1])){
                            int dat = Integer.parseInt(c.octDec(comp[1]));
                        
                            if(dat>255 || dat<0){
                                return false;
                            }else{
                                return true;
                            }
                        }else{
                            return false;
                        }
                   }
                   
            default:
                    if(c.decimal(doc.list.get(2).toString())){
                        int dat = Integer.parseInt(doc.list.get(2).toString());
                    
                        if(dat>255 || dat<0){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        return false;
                    }
        }
    }//Fin método 'Directo'
    
    /**
     * Método para comprobar si el operando es Extendido
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Extendido, True de lo contrario
     */
    public boolean Extendido(EstructuraArray doc){
        Conversor c = new Conversor();
        String comp[]= doc.list.get(2).toString().split("");
        switch(comp[0]){
            case"$":
                comp = doc.list.get(2).toString().split("\\$");
                
                if(comp.length<1){
                    doc.list.set(3, "Error no se encontro digito con el que trabajar");
                    return false;
                }else{
                    if(c.hexadecimal(comp[1])){
                        int dat = Integer.parseInt(c.hexDec(comp[1]));
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error rango de bits incorrecto");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el numero no es hexadecimal");
                        return false;
                    }
                }
                
            case"%":
                comp = doc.list.get(2).toString().split("%");
                
                if(comp.length<1){
                    doc.list.set(3, "Error no se encontro digito con el que trabajar");
                    return false;
                }else{
                    if(c.binario(comp[1])){
                        int dat = Integer.parseInt(c.binDec(comp[1]));
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error rango de bits incorrecto");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el numero no es binario");
                        return false;
                    }
                }
                
            case"@":
                   comp = doc.list.get(2).toString().split("@");
                   
                    if(comp.length<1){
                        doc.list.set(3, "Error no se encontro digito con el que trabajar");
                        return false;
                    }else{
                        if(c.octal(comp[1])){
                            int dat = Integer.parseInt(c.octDec(comp[1]));
                        
                            if(dat>65535 || dat<256){
                                doc.list.set(3, "Error rango de bits incorrecto");
                                return false;
                            }else{
                                doc.list.set(3, "N");
                                return true;
                            }
                        }else{
                            doc.list.set(3, "Error el numero no es octal");
                            return false;
                        }
                    }
                   
            default:
                    if(c.decimal(doc.list.get(2).toString())){
                        int dat = Integer.parseInt(doc.list.get(2).toString());
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error rango de bits incorrecto");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else if(etiqueta(doc.list.get(2).toString())){
                        doc.list.set(3, "N");
                        return true;
                    }else{
                        doc.list.set(3, "Error el número no es etiqueta ni numero");
                        return false;
                    }
        }
    }//Fin método 'Extendido'
        
    /**
     * Método para comprobar si el operando es Extendido no acepta etiquetas
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Extendido, True de lo contrario
     */
    public boolean ExtendidoNoEtiqueta(EstructuraArray doc){
        Conversor c = new Conversor();
        String comp[]= doc.list.get(2).toString().split("");
        switch(comp[0]){
            case"$":
                comp = doc.list.get(2).toString().split("\\$");
                
                if(comp.length<1){
                    doc.list.set(3, "Error no se encontro digito con el que trabajar");
                    return false;
                }else{
                    if(c.hexadecimal(comp[1])){
                        int dat = Integer.parseInt(c.hexDec(comp[1]));
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error rango de bits incorrecto");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el numero no es hexadecimal");
                        return false;
                    }
                }
                
            case"%":
                comp = doc.list.get(2).toString().split("%");
                
                if(comp.length<1){
                    doc.list.set(3, "Error no se encontro digito con el que trabajar");
                    return false;
                }else{
                    if(c.binario(comp[1])){
                        int dat = Integer.parseInt(c.binDec(comp[1]));
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error rango de bits incorrecto");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el numero no es binario");
                        return false;
                    }
                }
                
            case"@":
                   comp = doc.list.get(2).toString().split("@");
                   
                    if(comp.length<1){
                        doc.list.set(3, "Error no se encontro digito con el que trabajar");
                        return false;
                    }else{
                        if(c.octal(comp[1])){
                            int dat = Integer.parseInt(c.octDec(comp[1]));
                        
                            if(dat>65535 || dat<256){
                                doc.list.set(3, "Error rango de bits incorrecto");
                                return false;
                            }else{
                                doc.list.set(3, "N");
                                return true;
                            }
                        }else{
                            doc.list.set(3, "Error el numero no es octal");
                            return false;
                        }
                    }
                   
            default:
                    if(c.decimal(doc.list.get(2).toString())){
                        int dat = Integer.parseInt(doc.list.get(2).toString());
                        
                        if(dat>65535 || dat<256){
                            doc.list.set(3, "Error rango de bits incorrecto");
                            return false;
                        }else{
                            doc.list.set(3, "N");
                            return true;
                        }
                    }else{
                        doc.list.set(3, "Error el número no es etiqueta ni numero");
                        return false;
                    }
        }
    }//Fin método 'ExtendidoNoEtiqueta'
        

   /**
     * Método para comprobar si el operando es indizado de 5 bit
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Indizado 5 bits, True de lo contrario
     */
    public boolean Indizado5bit(EstructuraArray doc){
        String datos = doc.list.get(2).toString();
        Conversor c = new Conversor();
        if(datos.contains(",") && !datos.endsWith(",")){
            String dat[] = datos.split(",");
            if(dat.length==2){
                if(c.decimal(dat[0])){
                    int dec = Integer.parseInt(dat[0]);
                    if(dec > 15 || dec < -16){
                        return false;
                    }else{
                        if(dat[1].compareToIgnoreCase("SP")==0 || dat[1].compareToIgnoreCase("PC")==0 || dat[1].compareToIgnoreCase("X")==0 || dat[1].compareToIgnoreCase("Y")==0){
                            return true;
                        }else{
                            return false;
                        }
                    }
                }else if(dat[0].isBlank()){
                    if(dat[1].compareToIgnoreCase("SP")==0 || dat[1].compareToIgnoreCase("PC")==0 || dat[1].compareToIgnoreCase("X")==0 || dat[1].compareToIgnoreCase("Y")==0){
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }//Fin método 'Indizado5bit'
    
    /**
     * Método para comprobar si el operando es indizado de 9 bit
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Indizado 9 bits, True de lo contrario
     */
    public boolean Indizado9bit(EstructuraArray doc){
        String datos = doc.list.get(2).toString();
        Conversor c = new Conversor();
        if(datos.contains(",") && !datos.endsWith(",")){
            String dat[] = datos.split(",");
            if(dat.length==2){
                if(c.decimal(dat[0])){
                    int dec = Integer.parseInt(dat[0]);
                    if((dec > -17 || dec < -256) && (dec < 16 || dec > 255)){
                        return false;
                    }else{
                        if(dat[1].compareToIgnoreCase("SP")==0 || dat[1].compareToIgnoreCase("PC")==0 || dat[1].compareToIgnoreCase("X")==0 || dat[1].compareToIgnoreCase("Y")==0){
                            return true;
                        }else{
                            return false;
                        }
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }//Fin método 'Indizado9bit'
    
    /**
     * Método para comprobar si el operando es indizado de 16 bit
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Indizado 16 bits, True de lo contrario
     */
    public boolean Indizado16bit(EstructuraArray doc){
        String datos = doc.list.get(2).toString();
        Conversor c = new Conversor();
        if(datos.contains(",") && !datos.endsWith(",")){
            String dat[] = datos.split(",");
            if(dat.length==2){
                if(c.decimal(dat[0])){
                    int dec = Integer.parseInt(dat[0]);
                    if(dec < 256 || dec > 65535){
                        doc.list.set(3, "Error rango de bits incorrecto");
                        return false;
                    }else{
                        if(dat[1].compareToIgnoreCase("SP")==0 || dat[1].compareToIgnoreCase("PC")==0 || dat[1].compareToIgnoreCase("X")==0 || dat[1].compareToIgnoreCase("Y")==0){
                            doc.list.set(3, "N");
                            return true;
                        }else{
                            doc.list.set(3, "Error no se encontro el registro");
                            return false;
                        }
                    }
                }else{
                    doc.list.set(3, "Error el numero no es decimal");
                    return false;
                }
            }else{
                doc.list.set(3, "Error no carencia de datos");
                return false;
            }
        }else{
            doc.list.set(3, "Error no contiene el separador (,)");
            return false;
        }
    }//Fin método 'Indizado16bit'
    
    /**
     * Método para comprobar si el operando es Indizado indirecto de 16 bits
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Indizado indirecto 16 bits, True de lo contrario
     */
    public boolean IndizadoIndirecto16bit(EstructuraArray doc){
        String datos = doc.list.get(2).toString();
        Conversor c = new Conversor();
        
        if(datos.startsWith("[") && datos.endsWith("]") && datos.contains(",")){
            
            if(cantidadCaracteres(datos,'[')==1 && cantidadCaracteres(datos,']')==1 && cantidadCaracteres(datos,',')==1){
                String dat[] = datos.split("\\[");
                dat = dat[1].split("]");
                dat = dat[0].split(",");
                
                if(dat.length>1){
                    if(!dat[0].equals("") && !dat[1].equals("")){
                        if(c.decimal(dat[0])){
                            int dec = Integer.parseInt(dat[0]);

                            if(dec>=0 && dec<=65535){

                                if(dat[1].compareToIgnoreCase("SP")==0 || dat[1].compareToIgnoreCase("PC")==0 || dat[1].compareToIgnoreCase("X")==0 || dat[1].compareToIgnoreCase("Y")==0){
                                    doc.list.set(3, "N");
                                    return true;
                                }else{
                                    doc.list.set(3, "Error registro incorrecto o no encontrado");
                                    return false;
                                }

                            }else{
                                doc.list.set(3, "Error numero fuera del rango de bits");
                                return false;
                            }

                        }else{
                            doc.list.set(3, "Error el dato presentado no es decimal");
                            return false;
                        }

                    }else{
                        doc.list.set(3, "Error falta de numero decimal");
                        return false;
                    }
                }else{
                    doc.list.set(3, "Error falta de informacion para trabajar");
                    return false;
                }
            }else{
                return false;
            }
        }else{
            doc.list.set(3, "Error en la estructura del operando");
            return false;
        }
  
    } //Fin de método 'IndizadoIndirecto16bit'
    
    /**
     * Método para comprobar si el operando es Indizado Pre/Post Decremento/Incremento
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Indizado Pre/Post Decremento/Incremento, True de lo contrario
     */
    public boolean IndizadoPrePost(EstructuraArray doc){
        String datos = doc.list.get(2).toString();
        Conversor c = new Conversor();
        if(datos.contains(",") && !datos.endsWith(",")){
            String dat[] = datos.split(",");
            if(dat.length==2){
                if(c.decimal(dat[0])){
                    int dec = Integer.parseInt(dat[0]);
                    if(dec < 1 || dec > 8){
                        doc.list.set(3, "Error rango de bits incorrecto");
                        return false;
                    }else{
                        if(dat[1].compareToIgnoreCase("-SP")==0   || dat[1].compareToIgnoreCase("-X")==0    || dat[1].compareToIgnoreCase("-Y")==0   ){
                            doc.list.set(4,"pre decremento, (IDX)");
                            doc.list.set(3, "N");
                            return true;
                        }else if(dat[1].compareToIgnoreCase("SP-")==0 || dat[1].compareToIgnoreCase("X-")==0 || dat[1].compareToIgnoreCase("Y-")==0){
                            doc.list.set(4,"post decremento, (IDX)");
                            doc.list.set(3, "N");
                            return true;
                        }else if(dat[1].compareToIgnoreCase("+SP")==0 || dat[1].compareToIgnoreCase("+X")==0 || dat[1].compareToIgnoreCase("+Y")==0){
                            doc.list.set(4,"pre incremento, (IDX)");
                            doc.list.set(3, "N");
                            return true;
                        }else if(dat[1].compareToIgnoreCase("SP+")==0 || dat[1].compareToIgnoreCase("X+")==0 || dat[1].compareToIgnoreCase("Y+")==0){
                            doc.list.set(4,"post incremento, (IDX)");
                            doc.list.set(3, "N");
                            return true;
                        }else{
                            doc.list.set(3, "Error registro no encontrado o no existente");
                            return false;
                        }
                    }
                }else{
                    doc.list.set(3, "Error el dato presentado no es decimal");
                    return false;
                }
            }else{
                doc.list.set(3, "Error operando mal estructurado");
                return false;
            }
        }else{
            doc.list.set(3, "Error operando mal estructurado");
            return false;
        }
    } //Fin de método 'IndizadoPrePost'
    
    /**
     * Método para comprobar si el operando es Indizado acumulador
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Indizado acumulador, True de lo contrario
     */
    public boolean IndizadoAcumulador(EstructuraArray doc){
        String datos = doc.list.get(2).toString();
        Conversor c = new Conversor();
        if(datos.contains(",") && !datos.endsWith(",")){
            String dat[] = datos.split(",");
            if(dat.length==2){
                if(dat[0].compareToIgnoreCase("A")==0 || dat[0].compareToIgnoreCase("B")==0 || dat[0].compareToIgnoreCase("D")==0){
                    
                    if(dat[1].compareToIgnoreCase("SP")==0 || dat[1].compareToIgnoreCase("PC")==0 || dat[1].compareToIgnoreCase("X")==0 || dat[1].compareToIgnoreCase("Y")==0){
                        doc.list.set(3, "N");
                        return true;
                    }else{
                        doc.list.set(3, "Error no se encontro el registro");
                        return false;
                    }
                    
                }else{
                    doc.list.set(3, "Error no se encontro el registro acumulador");
                    return false;
                }
            }else{
                doc.list.set(3, "Error operando mal estructurado");
                return false;
            }
        }else{
            doc.list.set(3, "Error operando mal estructurado");
            return false;
        }
    } //Fin de método 'IndizadoAcumulador'
    
    /**
     * Método para comprobar si el operando es Indizado acumulador indirecto
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Indizado acumulador indirecto, True de lo contrario
     */
    public boolean IndizadoAcumuladorIndirecto(EstructuraArray doc){
        String datos = doc.list.get(2).toString();
        Conversor c = new Conversor();
        if(datos.startsWith("[") && datos.endsWith("]") && datos.contains(",")){
            
            if(cantidadCaracteres(datos,'[')==1 && cantidadCaracteres(datos,']')==1 && cantidadCaracteres(datos,',')==1){
                String dat[] = datos.split("\\[");
                dat = dat[1].split("]");
                dat = dat[0].split(",");
                
                if(dat.length>1){
                    
                    if(!dat[0].equals("") && !dat[1].equals("")){

                        if(dat[0].compareToIgnoreCase("D")==0){

                            if(dat[1].compareToIgnoreCase("SP")==0 || dat[1].compareToIgnoreCase("PC")==0 || dat[1].compareToIgnoreCase("X")==0 || dat[1].compareToIgnoreCase("Y")==0){
                                doc.list.set(3, "N");    
                                return true;
                            }else{
                                doc.list.set(3, "Error no se encontro el registro");
                                return false;
                            }

                        }else{
                            doc.list.set(3, "N");
                            return false;
                        }
                    }else{
                        doc.list.set(3, "Falta de informacion con la que trabajar");
                        return false;
                    }
                }else{
                    doc.list.set(3, "Error operando mal estructurado");
                    return false;
                }
            }else{
                doc.list.set(3, "Error operando mal estructurado");
                return false;
            } 
        }else{
            doc.list.set(3, "Error operando mal estructurado");
            return false;
        }
        
    } //Fin de método 'IndizadoAcumuladorIndirecto'
    
    /**
     * Método para comprobar si el operando es Relativo de 8 y 16 bits
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return False si no es Relativo de 8 y 16 bits, True de lo contrario
     */
    public boolean Relativo(EstructuraArray doc){
        if(etiqueta(doc.list.get(2).toString())){
            doc.list.set(3, "N");
            return true;
        }else{
            doc.list.set(3, "Etiqueta no aceptable");
            return false;
        }
    } //Fin de método 'Relativo'
    
    /**
     * Método para conocer la cantidad que aparece un caracter en una cadena
     * @param cadena String donde se realizará la busqueda
     * @param caracter Char a buscar
     * @return Cantidad de apariciones o -1 en caso de no encontrar
     */
    public int cantidadCaracteres(String cadena, char caracter) {
        int posicion, contador = 0;
        //se busca la primera vez que aparece
        posicion = cadena.indexOf(caracter);
        while (posicion != -1) { 
            contador++;
            posicion = cadena.indexOf(caracter, posicion + 1);
        }
        return contador;
   } //Fin de método 'cantidadCaracteres'
    
    /**
     * Método para identificar las directivas
     * Calcula el código maquina de las directivas constantes, o el valor a aumentar a CONTLOC de las directivas de reserva de espacio de memoria
     * @param doc objeto EstructuraArray donde se encuentra el operando
     * @return Código maquina calculado (Directivas constantes), o el valor a aumentar a CONTLOC (Directivas de reserva de espacio de memoria)
     */
    public String Directivas(EstructuraArray doc){
        Conversor c = new Conversor();
        String dir = doc.list.get(1).toString();
        
        //DIRECTIVAS DE CONSTANTES
        if(dir.equalsIgnoreCase("DB") || dir.equalsIgnoreCase("DC.B") || dir.equalsIgnoreCase("FCB")){
            if(Directo(doc)){
                
                return relleno(toHex(doc.list.get(2).toString()),2);
                
            }else{
                throw new IllegalArgumentException("ERROR - Operando de directiva debe ser un valor de 0 a 255 en decimal, hexadecimal, octal o binario");
            }
        }else if(dir.equalsIgnoreCase("DW") || dir.equalsIgnoreCase("DC.W") || dir.equalsIgnoreCase("FDB")){
            if(Directo(doc) || ExtendidoNoEtiqueta(doc)){
                return relleno(toHex(doc.list.get(2).toString()),4);
            }else{
                throw new IllegalArgumentException("ERROR - Operando de directiva debe ser un valor de 0 a 65535 en decimal, hexadecimal, octal o binario");
            }
        }else if(dir.equalsIgnoreCase("FCC")){
            if(doc.list.get(2).toString().startsWith("\"") && doc.list.get(2).toString().endsWith("\"")){
                
                char operando[] = doc.list.get(2).toString().toCharArray();
                String valor = "";
                
                for(int i=1; i<operando.length-1; i++){
                    
                    valor = valor + c.decHex((int)operando[i]); //Calcular código ASCII de los caracteres
                    
                }
                
                return valor; //Incremento a CONTLOC
                
            }else{
                throw new IllegalArgumentException("ERROR - Operando de directiva debe comenzar y terminar con comillas (\") ");
            }
        }
        
        //DIRECTIVAS DE RESERVA DE ESPACIO DE MEMORIA
        else if(dir.equalsIgnoreCase("DS") || dir.equalsIgnoreCase("DS.B") || dir.equalsIgnoreCase("RMB")){
            if(Directo(doc) || ExtendidoNoEtiqueta(doc)){
                String operando [] = doc.list.get(2).toString().split("");
                int valor = 0;
                
                switch(operando[0]){
                    case "$":
                        operando = doc.list.get(2).toString().split("\\$");
                        valor = Integer.parseInt(c.hexDec(operando[1]));
                    break;
                    
                    case "@":
                        operando = doc.list.get(2).toString().split("@");
                        valor = Integer.parseInt(c.octDec(operando[1]));
                    break;
                    
                    case "%":
                        operando = doc.list.get(2).toString().split("%");
                        valor = Integer.parseInt(c.binDec(operando[1]));
                    break;
                    
                    default:
                        valor = Integer.parseInt(doc.list.get(2).toString());
                    break;
                }
                
                return "VALOR-" + valor; //Incremento a CONTLOC
            }else{
                throw new IllegalArgumentException("ERROR - Operando de directiva debe ser un valor de 0 a 65535 en decimal, hexadecimal, octal o binario");
            } 
        }else if(dir.equalsIgnoreCase("DS.W") || dir.equalsIgnoreCase("RMW")){
            if(Directo(doc) || ExtendidoNoEtiqueta(doc)){
                String operando [] = doc.list.get(2).toString().split("");
                int valor = 0;
                
                switch(operando[0]){
                    case "$":
                        operando = doc.list.get(2).toString().split("\\$");
                        valor = Integer.parseInt(c.hexDec(operando[1]));
                    break;
                    
                    case "@":
                        operando = doc.list.get(2).toString().split("@");
                        valor = Integer.parseInt(c.octDec(operando[1]));
                    break;
                    
                    case "%":
                        operando = doc.list.get(2).toString().split("%");
                        valor = Integer.parseInt(c.binDec(operando[1]));
                    break;
                    
                    default:
                        valor = Integer.parseInt(doc.list.get(2).toString());
                    break;
                }
                
                return "VALOR-" + valor*2; //Incremento a CONTLOC
            }else{
                throw new IllegalArgumentException("ERROR - Operando de directiva debe ser un valor de 0 a 65535 en decimal, hexadecimal, octal o binario");
            } 
        }
        
        return "NO DIRECTIVA"; //EN CASO DE NO SER NINGUNA DIRECTIVA
        
    } //Fin de método 'Directivas'
    
    /**
     * Método para convertir de Decimal, Octal, Binario a Hexadecimal
     * @param operando Operando junto al su símbolo de identificación ($, @, %) de ser necesario
     * @return Número en convertido a Hexadecimal
     */
    private String toHex(String operando){
        Conversor conver = new Conversor();
        Indizados ind = new Indizados();
        String calculo [] = operando.split("");
        
        //int valor = 0;
        
        switch(calculo[0]){
            case "$":
                calculo = operando.split("\\$");
                return calculo[1];

            case "@":
                calculo = operando.split("@");
                return conver.octHex(calculo[1]);

            case "%":
                calculo = operando.split("%");
                return conver.binHex(calculo[1]);

            default:
                return conver.decHex(Integer.parseInt(operando));
        }
        
    } //Fin de método 'toHex'
    
    /**
     * Método para rellenar numero hexadecimal con 0s a la izquierda
     * @param valor Numero hexadecimal sin rellenar
     * @param lenght Tamaño del relleno
     * @return Numero hexadecimal rellenado
     */
    private static String relleno(String valor, int lenght){
        if(valor.length()>lenght){
            valor = valor.substring(valor.length()-lenght);
        }else{
            for(int i = valor.length(); i<lenght; i++){
                valor = "0" + valor;
            }
        }
        return valor;
    } //Fin de método 'relleno'
    
    
}//Fin de clase Detector

