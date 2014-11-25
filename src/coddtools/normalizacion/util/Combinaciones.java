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

package coddtools.normalizacion.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

class IteradorCombinaciones<E> implements Iterator<Conjunto<E>>
{
    public IteradorCombinaciones(final Conjunto<E> elementos, int tamanoCombinacion)
    {
        this.tamanoCombinacion = tamanoCombinacion;
        this.elementos = elementos;
        
        if(!elementos.esVacio())
        {
            subconjuntoElementos = new Conjunto<E>();
            subconjuntoElementos.insertar(elementos);
            pivote = subconjuntoElementos.iterator().next();
            subconjuntoElementos.eliminar(pivote);
            if(tamanoCombinacion > 1)
            {
                Conjunto<E> aux = new Conjunto<E>();
                aux.insertar(subconjuntoElementos);
                it = new IteradorCombinaciones(aux, tamanoCombinacion - 1);
            }
        }
    }
            
    @Override
    public boolean hasNext() {
        if(!elementos.esVacio())
        {
            if(tamanoCombinacion > 1)
                return (subconjuntoElementos.obtenerCardinal() >= tamanoCombinacion) || it.hasNext();
            return (pivote != null);
        }       
        return false;
    }

    @Override
    public Conjunto<E> next() {
        if(!elementos.esVacio())
        {
            if(tamanoCombinacion > 1)
            {
                if((subconjuntoElementos.obtenerCardinal() >= tamanoCombinacion) || it.hasNext())
                {
                    if((subconjuntoElementos.obtenerCardinal() >= tamanoCombinacion) && (!it.hasNext()))
                    {
                        pivote = subconjuntoElementos.iterator().next();
                        subconjuntoElementos.eliminar(pivote);
                        Conjunto<E> aux = new Conjunto<E>();
                        aux.insertar(subconjuntoElementos);
                        it = new IteradorCombinaciones(aux, tamanoCombinacion - 1);
                    }
                    
                    Conjunto<E> aux = it.next(); /* obtenemos una combinación del subconjunto */
                    aux.insertar(pivote); /* añadimos el pivote para formar una combinación de este
                    conjunto
                    */
                    return aux;
                }
            }
            else 
            {
                if(pivote != null)
                {
                    Conjunto<E> aux = new Conjunto<E>();
                    aux.insertar(pivote);
                    if(!subconjuntoElementos.esVacio())
                    {
                        pivote = subconjuntoElementos.iterator().next();
                        subconjuntoElementos.eliminar(pivote);
                        return aux;
                    }
                    pivote = null;
                    return aux;
                }
            }
        }
        throw new NoSuchElementException();
    }
    
    private Conjunto<E> elementos;
    private int tamanoCombinacion; /* es el tamaño de la combinación */
    private E pivote; /* es un elemento del conjunto, se irá desplazando hacía la derecha */
    private Conjunto<E> subconjuntoElementos; /* es un subconjunto del conjunto de elementos que posee
    todos los elementos a excepción de todos aquellos que hayan sido pivotes
    */
    private IteradorCombinaciones<E> it; /* Es un iterador del conjunto de combinaciones del subconjunto
    de elementos
    */
    
}



/**
 * Instancias de esta clase permiten iterar sobre el conjunto de combinaciones de un conjunto 
 * específico de elementos.
 * Son combinaciones de un conjunto de elementos. Variaciones donde no importa el orden y no hay
 * repeticiones de elementos, tomados de n en n.
 * @author victor
 */
public class Combinaciones<E> implements Iterable<Conjunto<E>> {

    /**
     * Crea una instancia de un generador de combinaciones de un conjunto.
     * Combinaciones de los elementos del conjunto especificado tomando los elementos de n en
     * n. n debe ser menor o igual al número de elementos del conjunto.
     * @param elementos
     * @param n Es el tamaño de la combinación.
     * @note Notese, que si n es igual al número de elementos del conjunto, solo hay una combinación
     * posible, el propio conjunto.
     */
    public Combinaciones(final Conjunto<E> elementos, int n)
    {
        assert (n <= elementos.obtenerCardinal()) && (n >= 1);
        this.elementos = elementos;
        this.tamanoCombinacion = n;
    }
    
    
    @Override
    public Iterator<Conjunto<E>> iterator() {
        return new IteradorCombinaciones<E>(elementos, tamanoCombinacion);
    }
    
    /**
     * 
     * @return Devuelve el número de combinaciones sobre el conjunto (formando grupos de n
     * en n) 
     */
    public long obtenerNumeroCombinaciones() 
    {
        return Factoriales.calcularCoeficienteBinomial(elementos.obtenerCardinal(), tamanoCombinacion);
    }
    
    private final Conjunto<E> elementos;
    private int tamanoCombinacion;
}
