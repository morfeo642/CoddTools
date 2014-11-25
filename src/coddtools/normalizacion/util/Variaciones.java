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
import java.util.List;
import java.util.NoSuchElementException;

class IteradorVariaciones<E> implements Iterator<List<E>>
{
    public IteradorVariaciones(final Conjunto<E> elementos, int tamanoVariacion)
    {
        this.elementos = elementos;
        this.tamanoVariacion = tamanoVariacion;
        if(!elementos.esVacio())
        {
            iteradorCombinaciones = new IteradorCombinaciones<E>(elementos, tamanoVariacion);
            iteradorPermutaciones = new IteradorPermutaciones<E>(iteradorCombinaciones.next());
        }
    }
    
    @Override
    public boolean hasNext() {
       if(!elementos.esVacio())
           return iteradorCombinaciones.hasNext() || iteradorPermutaciones.hasNext();
       return false;
    }

    @Override
    public List<E> next() {
        if(!elementos.esVacio())
        {
            if(iteradorCombinaciones.hasNext() || iteradorPermutaciones.hasNext())
            {
                if(iteradorCombinaciones.hasNext() && (!iteradorPermutaciones.hasNext()))
                    iteradorPermutaciones = new IteradorPermutaciones(iteradorCombinaciones.next());
                return iteradorPermutaciones.next();
            }
        }
        throw new NoSuchElementException();
    }
    
    private final Conjunto<E> elementos;
    private int tamanoVariacion;
    private IteradorCombinaciones<E> iteradorCombinaciones; /* es un iterador que 
    itera sobre el conjunto de todas las posibles combinaciones de este conjunto tomando
    grupos de n en n, donde n es el tamaño de una variación.
    */
    private IteradorPermutaciones<E> iteradorPermutaciones; /* es un iterador que 
    itera sobre el conjunto de todas las posibles permutaciones de cada combinación generada
    por el anterior iterador 
    */
}

/**
 * Las instancias de esta clase permite obtener las variaciones de un conjunto de
 * elementos específico. (Son variaciones en las que si importa el orden y no hay 
 * repeticiones). Las variaciones son grupos de n elementos.  
 * @author victor
 */
public class Variaciones<E> implements Iterable<List<E>> {
    /* Constructor */
    /**
     * Crea un generador de variaciones de los elementos indicados tomados de n
     * en n. n debe ser menor o igual que el número de elementos y mayor o igual que 1.
     * @param elementos
     * @param n 
     */
    public Variaciones(final Conjunto<E> elementos, int n) 
    {
        assert (n <= elementos.obtenerCardinal()) && (n >= 1);
        this.elementos = elementos;
        this.tamanoVariacion = n;
    }
    
    /**
     * 
     * @return Devuelve el número de variaciones de este conjunto teniendo en 
     * cuenta que se toman elementos de n en n, no hay repeticiones y importa
     * el orden.
     */
    public long obtenerNumeroVariaciones() 
    {
        return Factoriales.calcularDivisionFactorial(elementos.obtenerCardinal(), elementos.obtenerCardinal() - tamanoVariacion);
    }
    
    /**
     * 
     * @return Devuelve un iterador que itera sobre las variaciones del conjunto
     * asociado. El orden en el cual se itera sobre las variaciones no está determinado.
     * 
     */
    @Override
    public Iterator<List<E>> iterator() 
    {
        return new IteradorVariaciones(elementos, tamanoVariacion);
    }
    
    protected final Conjunto<E> elementos;
    protected int tamanoVariacion;
}
