/*
 * The MIT License
 *
 * Copyright 2014 victor.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package coddtools.normalizacion;

import java.util.IllegalFormatException;
import java.util.Iterator;

/**
 * Representa una dependencia funcional del estilo x -> y; y depende funcionalmente
 * de x. 
 * @author victor
 */
public class DependenciaFuncional implements Comparable<DependenciaFuncional> {
    /**
     * Crea una dependencia funcional x -> y
     * @param determinante Es el determinante de la dependencia funcional.
     * @param determinado Es el determinado de la dependencia funcional.
     */
    public DependenciaFuncional(final Descriptor determinante, final Descriptor determinado)
    {
        this.determinante = determinante;
        this.determinado = determinado;
    }
    
    /**
     * Crea una dependencia funcional trivial del estilo x -> x
     */
    public DependenciaFuncional(Descriptor descriptor)
    {
        this(descriptor, descriptor);
    }
    
    /**
     * @return Devuelve el determinante de esta dependencia funcional.
     */
    public Descriptor obtenerDeterminante() 
    {
        return determinante;
    }
    
    /**
     * @return Devuelve el descriptor determinado de esta dependencia funcional.
     */
    public Descriptor obtenerDeterminado()
    {
        return determinado;
    }
    
    /**
     * @return Devuelve un valor booleano indicando si la esta dependencia funcional
     * es trivial, es decir, si el determinado es un subconjunto del determinante.
     */
    public boolean esTrivial()
    {
        return determinado.esContenido(determinante);
    }
    
    /**
     * 
     * @return Devuelve un conjunto de dependencias funcionales X->A1, X->A2, X->A3, ... X->AN,
     * donde el determinado Y = A1 u A2 u ... u AN.
     */
    public DependenciasFuncionales distribuirDeterminado()
    {
        DependenciasFuncionales aux = new DependenciasFuncionales();
        Iterator<Atributo> it = obtenerDeterminado().iterator();
        while(it.hasNext())
        {
            Descriptor determinante = new Descriptor();
            determinante.insertar(obtenerDeterminante());
            Descriptor determinado = new Descriptor();
            determinado.insertar(it.next());
            aux.insertar(new DependenciaFuncional(determinante, determinado));
        }
        return aux;
    }
    
    /**
     * 
     * @return Devuelve un valor booleano indicando si tanto el determinante como el determinado
     * de esta relación son subconjuntos del descriptor indicado.
     */
    public boolean estaCompuestaPor(final Descriptor descriptor)
    {
        return obtenerDeterminante().esContenido(descriptor) && 
                obtenerDeterminado().esContenido(descriptor);
    }
    
    @Override
    public String toString()
    {
        String aux = new String();
        aux += obtenerDeterminante() + " -> " + obtenerDeterminado();
        return aux;
    }
    
    @Override
    public int compareTo(DependenciaFuncional otra)
    {
        int r;
        if((r = obtenerDeterminante().compareTo(otra.obtenerDeterminante())) != 0)
            return r;
        return obtenerDeterminado().compareTo(otra.obtenerDeterminado());
    }
    
    /**
     *
     * @param str Es una cadena con el siguiente formato: X -> Y, tanto X como Y son descriptores.
     * Los espacios son opcionales. X e Y deben tener un formato de descriptor válido. 
     * @see Descriptor.fromString
     * @return Devuelve una dependencia funcional tal y como indica la cadena de caracteres.
     * @throws IllegalArgumentException Lanza IllegalFormatException si la cadena no tiene un formato
     * válido.
     */
    public static DependenciaFuncional fromString(String str) throws IllegalArgumentException
    {
        /* verificar si el formato es válido */
        if(!str.matches(".+->.+"))
            throw new IllegalArgumentException();
        String[] tokens = str.split("->");
        if(tokens.length != 2)
            throw new IllegalArgumentException();
        
        return new DependenciaFuncional(Descriptor.fromString(tokens[0]), Descriptor.fromString(tokens[1]));
    }
   
    private final Descriptor determinante, determinado;
}
