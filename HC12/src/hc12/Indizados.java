/*
 * "Proyecto simulador - Ensamblador de procesador HC12"
 * Universidad de Guadalajara
 * Centro Universitario de Los Altos
 * Seminario de Solución de Problemas de Traductores de Lenguaje I
 */
package hc12;

import java.util.Arrays;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Martin Raygoza De León
 */
public class Indizados {
    Conversor conver = new Conversor();
    
    /**
     * Método para calcular 'xb' del operando en modo offset de 5 bits
     * @param operando Operando a calcular
     * @return Valor de 'xb' en hexadecimal
     */
    public String offset5(String operando){
        String instruccion[] = operando.split(",");
        String valor = "";
        
        if(instruccion[0].equals("") || instruccion[0].equals("0")){
            
            valor = "00000";
            
        }else if(Integer.parseInt(instruccion[0])>0 && Integer.parseInt(instruccion[0])<=15){
            
            valor = relleno(conver.decBin(Integer.parseInt(instruccion[0])),5);
            
        }else if(Integer.parseInt(instruccion[0])<0 && Integer.parseInt(instruccion[0])>=-16){
            
            valor = complemento2(Integer.parseInt(instruccion[0]),5);
            
        }else{
            throw new IllegalArgumentException("ERROR - Cantidad en decimal incorrecto. Rango debe ser de -16 a 15");
        }
        
        if(instruccion[1].equalsIgnoreCase("X")){
            return relleno(conver.binHex("000" + valor),2);
        }else if(instruccion[1].equalsIgnoreCase("Y")){
            return relleno(conver.binHex("010" + valor),2);
        }else if(instruccion[1].equalsIgnoreCase("SP")){
            return relleno(conver.binHex("100" + valor),2);
        }else if(instruccion[1].equalsIgnoreCase("PC")){
            return relleno(conver.binHex("110" + valor),2);
        }else{
            throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
        }
    } //Fin de método 'offset5'
    
    /**
     * Método para calcular 'xb' y 'ff' del operando en modo offset de 9 bits
     * @param operando Operando a calcular
     * @return Valor de 'xb' y 'ff' en hexadecimal
     */
    public String offset9(String operando){
        String instruccion[] = operando.split(",");
        String valor = "";
        String ff;
        
        if(Integer.parseInt(instruccion[0])>=16 && Integer.parseInt(instruccion[0])<=255){
            
            valor = "00";
            ff = relleno(conver.decHex(Integer.parseInt(instruccion[0])),2);
            
        }else if(Integer.parseInt(instruccion[0])<=-17 && Integer.parseInt(instruccion[0])>=-256){
            
            valor = "01";
            ff = relleno(conver.binHex(complemento2(Integer.parseInt(instruccion[0]),9)),2);
            
        }else{
            throw new IllegalArgumentException("ERROR - Cantidad en decimal incorrecto. Rango debe ser de -256 a -17 o de 16 a 255");
        }
        
        if(instruccion[1].equalsIgnoreCase("X")){
            return relleno(conver.binHex("111000" + valor),2) + ff;
        }else if(instruccion[1].equalsIgnoreCase("Y")){
            return relleno(conver.binHex("111010" + valor),2) + ff;
        }else if(instruccion[1].equalsIgnoreCase("SP")){
            return relleno(conver.binHex("111100" + valor),2) + ff;
        }else if(instruccion[1].equalsIgnoreCase("PC")){
            return relleno(conver.binHex("111110" + valor),2) + ff;
        }else{
            throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
        }
    } //Fin de método 'offset9'
    
    /**
     * Método para calcular 'xb', 'ee' y 'ff' del operando en modo offset de 16 bits
     * @param operando Operando a calcular
     * @return Valor de 'xb', 'ee' y 'ff' en hexadecimal
     */
    public String offset16(String operando){
        String instruccion[] = operando.split(",");
        String eeff;
        
        if(Integer.parseInt(instruccion[0])>=256 && Integer.parseInt(instruccion[0])<=65535){
            
            eeff = relleno(conver.decHex(Integer.parseInt(instruccion[0])),4);
            
        }else{
            throw new IllegalArgumentException("ERROR - Cantidad en decimal incorrecto. Rango debe ser de 256 a 65535");
        }
        
        if(instruccion[1].equalsIgnoreCase("X")){
            return relleno(conver.binHex("11100010"),2) + eeff;
        }else if(instruccion[1].equalsIgnoreCase("Y")){
            return relleno(conver.binHex("11101010"),2) + eeff;
        }else if(instruccion[1].equalsIgnoreCase("SP")){
            return relleno(conver.binHex("11110010"),2) + eeff;
        }else if(instruccion[1].equalsIgnoreCase("PC")){
            return relleno(conver.binHex("11111010"),2) + eeff;
        }else{
            throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
        }
    } //Fin de método 'offset16'
    
    /**
     * Método para calcular 'xb' del operando en modo Pre/Post Incremento/Decremento
     * @param operando Operando a calcular
     * @return Valor de 'xb' en hexadecimal
     */
    public String pre_post(String operando){
        String instruccion[] = operando.split(",");
        String valor = "";
        int resta;

        if(Integer.parseInt(instruccion[0])<1 || Integer.parseInt(instruccion[0])>8){
            throw new IllegalArgumentException("ERROR - Cantidad en decimal incorrecto. Rango debe ser de 1 a 8");
        }else{
            
            if(instruccion[1].contains("+")){
                resta = Integer.parseInt(instruccion[0])-1;
                valor = relleno(conver.decBin(resta),4);
            }else if(instruccion[1].contains("-")){
                valor = complemento2(Integer.parseInt(instruccion[0]),4);
            }else{
                throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
            }
            
            
            
            if(instruccion[1].equalsIgnoreCase("-X")){
                return relleno(conver.binHex("0010" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("X-")){
                return relleno(conver.binHex("0011" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("-Y")){
                return relleno(conver.binHex("0110" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("Y-")){
                return relleno(conver.binHex("0111" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("-SP")){
                return relleno(conver.binHex("1010" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("SP-")){
                return relleno(conver.binHex("1011" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("+X")){
                return relleno(conver.binHex("0010" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("X+")){
                return relleno(conver.binHex("0011" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("+Y")){
                return relleno(conver.binHex("0110" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("Y+")){
                return relleno(conver.binHex("0111" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("+SP")){
                return relleno(conver.binHex("1010" + valor),2);

            }else if(instruccion[1].equalsIgnoreCase("SP+")){
                return relleno(conver.binHex("1011" + valor),2);

            }else{
                throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
            }
            
        }
        
    } //Fin de método 'pre_post'
    
    /**
     * Método para calcular 'xb' y 'eeff' del operando en modo Indizado indirecto de 16 bits
     * @param operando Operando a calcular
     * @return Valor de 'xb' y 'eeff' en hexadecimal
     */
    public String indirecto16(String operando){
        
        String instruccion[] = operando.split("\\[");
        instruccion = instruccion[1].split("\\]");
        instruccion = instruccion[0].split(",");
        String eeff="";
        
        if(Integer.parseInt(instruccion[0])>=0 && Integer.parseInt(instruccion[0])<=65535){
            
            eeff = relleno(conver.decHex(Integer.parseInt(instruccion[0])),4);
            
        }else{
            throw new IllegalArgumentException("ERROR - Cantidad en decimal incorrecto. Rango debe ser de 256 a 65535");
        }
        
        if(instruccion[1].equalsIgnoreCase("X")){
            return relleno(conver.binHex("11100011"),2) + eeff;
        }else if(instruccion[1].equalsIgnoreCase("Y")){
            return relleno(conver.binHex("11101011"),2) + eeff;
        }else if(instruccion[1].equalsIgnoreCase("SP")){
            return relleno(conver.binHex("11110011"),2) + eeff;
        }else if(instruccion[1].equalsIgnoreCase("PC")){
            return relleno(conver.binHex("11111011"),2) + eeff;
        }else{
            throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
        }
    } //Fin de método 'indirecto16'
    
    /**
     * Método para calcular 'xb' del operando en modo Indizado indirecto Acumulador
     * @param operando Operando a calcular
     * @return Valor de 'xb' en hexadecimal
     */
    public String indirectoAcumulador(String operando){
        
        String instruccion[] = operando.split("\\[");
        instruccion = instruccion[1].split("\\]");
        instruccion = instruccion[0].split(",");
        
        if(instruccion[1].equalsIgnoreCase("X")){
            return relleno(conver.binHex("11100111"),2) ;
        }else if(instruccion[1].equalsIgnoreCase("Y")){
            return relleno(conver.binHex("11101111"),2);
        }else if(instruccion[1].equalsIgnoreCase("SP")){
            return relleno(conver.binHex("11110111"),2) ;
        }else if(instruccion[1].equalsIgnoreCase("PC")){
            return relleno(conver.binHex("11111111"),2);
        }else{
            throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
        }
    } //Fin de método 'IndirectoAcumulador'
    
    
    /**
     * Método para calcular 'xb' del operando en modo Acumulador
     * @param operando Operando a calcular
     * @return Valor de 'xb' en hexadecimal
     */
    public String acumulador(String operando){
        String instruccion[] = operando.split(",");
        String acum;
        
        if(instruccion[0].equalsIgnoreCase("A")){
            acum = "00";
        }else if(instruccion[0].equalsIgnoreCase("B")){
            acum = "01";
        }else if(instruccion[0].equalsIgnoreCase("D")){
            acum = "10";
        }else{
            throw new IllegalArgumentException("ERROR - Acumulador incorrecto. Debe ser A, B o D");
        }
        
        
        if(instruccion[1].equalsIgnoreCase("X")){
            return relleno(conver.binHex("111001" + acum),2);
        }else if(instruccion[1].equalsIgnoreCase("Y")){
            return relleno(conver.binHex("111011" + acum),2);
        }else if(instruccion[1].equalsIgnoreCase("SP")){
            return relleno(conver.binHex("111101" + acum),2);
        }else if(instruccion[1].equalsIgnoreCase("PC")){
            return relleno(conver.binHex("111111" + acum),2);
        }else{
            throw new IllegalArgumentException("ERROR - Registro no existente o no encontrado");
        }
        
    } //Fin de método 'acumulador'
    
    /**
     * Método para realizar el complemento a 2 del numero pedido
     * @param numero Numero entero (acepta negativos) a aplicar complemento
     * @param cantidad Cantidad de bits de longitud del complemento
     * @return Numero en binario con complemento a 2 ya aplicado
     */
    protected String complemento2(int numero, int cantidad){
        String numAbsoluto;
        
        if(String.valueOf(numero).startsWith("-")){
            numAbsoluto = String.valueOf(numero).split("-")[1]; //Quitar signo negativo
        }else{
            numAbsoluto = String.valueOf(numero);
        }
        
        String [] binario = relleno(conver.decBin(Integer.parseInt(numAbsoluto)), cantidad).split(""); //Numero a binario en arreglo
        boolean encontrado = false;
        
        for(int i=cantidad-1; i>=0; i--){

            if(binario[i].equals("1") && encontrado!=true){
                encontrado = true;
            }else if(binario[i].equals("1") && encontrado==true){
                binario[i] = "0";
            }else if(binario[i].equals("0") && encontrado==true){
                binario[i] = "1";
            }

        }
        
        String valor = "";

        for(int i=0; i<binario.length; i++){
            valor = valor + binario[i];
        }
        
        if(valor.length()==9){
            
            valor = valor.substring(valor.length()-8);
            
        }
        
        return valor;
        
    } //Fin de método 'complemento2'
    
   
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
    
} //Fin de clase 'Indizados'
