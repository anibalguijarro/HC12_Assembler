/*
 * "Proyecto simulador - Ensamblador de procesador HC12"
 * Universidad de Guadalajara
 * Centro Universitario de Los Altos
 * Seminario de Solución de Problemas de Traductores de Lenguaje I
 */
package hc12;

import java.util.ArrayList;

/**
 * @author Anibal Uriel Guijarro Rocha
 * @author Martin Raygoza De León
 */
public class EstructuraArray {
    
    protected ArrayList list = new ArrayList(); //Variable arraylist para almacenar valores (palabras) de las lineas
    private EstructuraArray before; //Para enlazar debidamente las lineas (listas)
    private EstructuraArray next; //Para enlazar debidamente las lineas (listas)
    private EstructuraArray first; //Para crear y obtener la primera linea (lista)
    private int size; //Para obtener el tamaño de la estructura
    private int ActualLine; //Para obtener el indice de la linea en curso
    

    /**
     * Constructor con valores por defecto, tomando en cuenta el mismo como la linea uno y tamaño de uno
     */
    public EstructuraArray() {
        this.before = null;
        this.next = null;
        this.first = null;
        this.size = size;
        this.ActualLine = 1;
    }
    
    /**
     * Constructor con todos los atributos
     * @param size tamaño actual de La lista
     * @param next siguiente epacio en la lista
     * @param before espacio anterior de la lista
     * @param first primer espacio de la lista
     * @param actualLine contador de linea actual 
     */
    public EstructuraArray(int size, EstructuraArray next, EstructuraArray before, EstructuraArray first, int actualLine) {
        this.before = before;
        this.next = next;
        this.first = first;
        this.size = size;
        this.ActualLine = actualLine;
    }

    /**
     * Método para obtener la primera linea (lista)
     * @return first
     */
    public EstructuraArray getFirst() {
        return first;
    }

    /**
     * Método para establecer la primera linea (lista)
     * @param first 
     */
    public void setFirst(EstructuraArray first) {
        this.first = first;
    }
    
    /**
     * Método para obtener el tamaño de la estructura
     * @return 
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Método para establecer el tamaño de la estructura
     * @param size 
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Método para obtener la linea (lista) anterior
     * @return before
     */
    public EstructuraArray getBefore() {
        return before;
    }

    /**
     * Método para establecer la linea (lista) anterior
     * @param before 
     */
    public void setBefore(EstructuraArray before) {
        this.before = before;
    }

    /**
     * Método para obtener la linea (lista) siguiente
     * @return next
     */
    public EstructuraArray getNext() {
        return next;
    }

    /**
     * Método para establecer la linea (lista) siguiente
     * @param next 
     */
    public void setNext(EstructuraArray next) {
        this.next = next;
    }

    /**
     * Método para obtener el indice de la linea (lista) en curso
     * @return 
     */
    public int getActualLine() {
        return ActualLine;
    }

    /**
     * Método para establecer el indice de la linea (lista) en curso
     * @param ActualLine 
     */
    public void setActualLine(int ActualLine) {
        this.ActualLine = ActualLine;
    }
    
}
