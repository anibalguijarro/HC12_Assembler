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
public class Conversor {
    
    //MÉTODOS DE VALIDACION DE DATOS
    
    /**
     * Método para saber si el numero binario ingresado es correcto
     * @param numero Número ingresado
     * @return True si es binario, False de lo contrario
     */
    public boolean binario(String numero){
        String binario [] = numero.split("");
        
        for(int i=0; i<binario.length; i++){
            
            if(!binario[i].equals("0") && !binario[i].equals("1")){
                return false;
            }
            
        }
        return true;
    } //Fin de método 'binario'
    
    /**
     * Comprobación si un numero es entero
     * @param numero número a comprobar en formato String
     * @return  True si es entero, False de lo contrario
     */
    public boolean decimal(String numero){//Inicio de método decimal
        try{
            Integer.parseInt(numero);
            
            return true;
        }catch(Exception e){
            return false;
        }
    }//Fin de método decimal
    
    /**
     * Método para saber si el número octal ingresado es correcto
     * @param numero Número ingresado
     * @return True si es octal, False de lo contrario
     */
    public boolean octal(String numero){
        
        String octal [] = numero.split("");
        
        for(int i=0; i<octal.length; i++){
            
            if(octal[i].compareToIgnoreCase("0")<0 || octal[i].compareToIgnoreCase("7")>0){
                return false;
            }
        }
        return true;
    } //Fin de método 'octal'
    
    /**
     * Método para saber si el numero hexadecimal ingresado es correcto
     * @param numero Número ingresado
     * @return True si es hexadecimal, False de lo contrario
     */
    public boolean hexadecimal(String numero){
        
        String hexadecimal [] = numero.split("");
        
        for(int i=0; i<hexadecimal.length; i++){
            
            if((hexadecimal[i].compareToIgnoreCase("A")<0 || hexadecimal[i].compareToIgnoreCase("F")>0) && (hexadecimal[i].compareToIgnoreCase("0")<0 || hexadecimal[i].compareToIgnoreCase("9")>0)){
                return false;
            }
        }
        return true;
    } //Fin de método 'hexadecimal'
    
    //MÉTODOS DE SISTEMAS A DECIMAL
    
    /**
     * Método para convertir de binario a decimal
     * @param binario Número binario a convertir
     * @return Numero decimal
     */
    public String binDec (String binario){
        long decimal = 0; //Numero decimal
        long potencia = 0; //Numero de la potencia
        long binary = Long.parseLong(binario);
        
        //Ciclo infinito hatas que binario sea 0
        while(true){ 
            if(binary==0){
                break;
            }
            else {
                long temp = binary % 10; //Residuo de binario/10
                decimal += temp * Math.pow(2, potencia); //Operaciones
                binary = binary / 10; //Igualar binario en su division entre 10
                potencia++; //Potencia más 1
            }
        }
        
        return String.valueOf(decimal);
        
    } //Fin de metodo 'binDec'
    
    /**
     * Método para convertir de octal a decimal
     * @param octal Número octal a convertir
     * @return Numero decimal
     */
    public String octDec(String octal){
        long decimal = 0; //Numero decimal
        long power = 0; //Numero de la potencia
        long oct = Long.parseLong(octal);
        
        //Ciclo infinito que se rompe cuando octal sea 0
        while(true){
            if(oct==0){
                break;
            }
            else{
                long temp = oct % 10; //Residuo de octal/10
                decimal += temp * Math.pow(8, power); //Operaciones
                oct = oct / 10; //Igualar octal en su division entre 10
                power++; //Potencia más 1
            }
        }
        
        return String.valueOf(decimal);
        
    } //Fin de metodo 'octDec'
    
    
    /**
     * Método para convertir de octal a hexadecimal
     * @param octal Número octal a convertir
     * @return Numero hexadecimal
     */
    public String octHex(String octal){
        octal = octDec(octal);
        return decHex(Integer.parseInt(octal));
        
    } //Fin de metodo 'octDec'
    
    /**
     * Método para convertir de binario a hexadecimal
     * @param binario Número binario a convertir
     * @return Numero hexadecimal
     */
    public String binHex(String binario){
        binario = binDec(binario);
        //System.out.println("HOLA " + binario);
        return decHex(Integer.parseInt(binario));
    } //Fin de metodo 'octDec'
    
    /**
     * Método para convertir de hexadecimal a decimal
     * @param hexadecimal número hexadecimal a convertir 
     * @return Numero decimal
     */
    public String hexDec(String hexadecimal){
        String hexChar = "0123456789ABCDEF"; //Cadena de los caracteres de hexadecimal
        hexadecimal = hexadecimal.toUpperCase(); //Acomodo de cada caracter
        long decimal = 0; //Numero decimal
        
        for(int i=0; i<hexadecimal.length(); i++){
            char character = hexadecimal.charAt(i); //Guardar los caracteres
            long positionChar = hexChar.indexOf(character); //Convertir los caracteres en numero entero
            decimal = 16*decimal + positionChar; //Operaciones para acomodo de decimal
        }
        
        return String.valueOf(decimal);
        
    } //Fin de metodo 'hexDec'
    
    //MÉTODOS DE DECIMAL A SISTEMAS
    
    /**
     * Método para convertir de decimal a binario
     * @param decimal número decimal a convertir
     * @return Numero binario
     */
    public String decBin(int decimal){
        String binario = ""; //Numero binario
        
        while(decimal>0){
            binario = decimal % 2 + binario; //Igualar binario en su residuo entre 2 y su anterior valor
            decimal = decimal / 2; //Igualar decimal entre su division entre 2
        }
        
        return binario;
        
    } //Fin de metodo 'decBin'
    
    /**
     * Método para convertir de decimal a octal
     * @param decimal número decimal a convertir
     * @return Numero octal
     */
    public String decOct(int decimal){
        int residue; //Numero del residuo
        String octal = ""; //Numero octal
        char[] octChar = {'0', '1', '2', '3', '4', '5', '6', '7'}; //Caracteres de la base octal
        
        while(decimal>0){
            residue = decimal % 8; //Igualar residuo por el residuo de decimal/8
            char character = octChar[residue]; ////Igualar caracteres o numeros en su posicion
            octal = character + octal; //Igualar octal por el caracter correspondido y su anterior valor
            decimal = decimal / 8; //Igualar decimal entre su division entre 8
        }
        
        return octal;
        
    } //Fin de metodo 'decOct'
    
    /**
     * Método para convertir de decimal a hexadecimal
     * @param decimal número decimal a convertir
     * @return Número hexadecimal
     */
    public String decHex(int decimal){
        String hexadecimal = ""; //Numero hexadecimal
        String hexChar = "0123456789ABCDEF"; //Cadena de los caracteres de hexadecimal
        
        while(decimal>0){
            int residue = decimal % 16; //Igualar residuo por el residuo de decimal/16
            hexadecimal = hexChar.charAt(residue)+ hexadecimal; //Igualar hexadecimal por la posicion de los caracteres y su anterior
            decimal = decimal / 16; //Igualar decimal por su division entre 16
        }
        return hexadecimal;
        
    } //Fin de metodo 'decHex'
    
}
