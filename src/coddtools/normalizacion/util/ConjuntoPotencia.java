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

class IteradorConjuntoPotencia<E> implements Iterator<Conjunto<E>> 
{
    public IteradorConjuntoPotencia(final Conjunto<E> elementos)
    {
        this.elementos = elementos;
        this.tamanoSubconjunto = elementos.obtenerCardinal();
        if(!elementos.esVacio())
            iteradorCombinaciones = new IteradorCombinaciones(elementos, elementos.obtenerCardinal());
    }
    
    @Override
    public boolean hasNext() {
        return tamanoSubconjunto >= 0;
    }

    @Override
    public Conjunto<E> next() {
        if(tamanoSubconjunto >= 0)
        {
            if(tamanoSubconjunto > 0)
            {
                if(!iteradorCombinaciones.hasNext())
                {
                    tamanoSubconjunto--;
                    if(tamanoSubconjunto > 0)
                        iteradorCombinaciones = new IteradorCombinaciones(elementos, tamanoSubconjunto);
                }
                if(tamanoSubconjunto > 0)
                    return iteradorCombinaciones.next();
            }
            
            Conjunto<E> vacio = new Conjunto<E>(); /* generamos el conjunto vacío */
            tamanoSubconjunto--;
            return vacio;
        }
        throw new NoSuchElementException();
    }
    
    private final Conjunto<E> elementos;
    private int tamanoSubconjunto;
    private IteradorCombinaciones<E> iteradorCombinaciones;
}

/**
 * Es el conjunto formado por todos los subconjuntos de un conjunto, incluyendo el 
 * conjunto vacío y el conjunto total.
 * @author victor
 */
public class ConjuntoPotencia<E> implements Iterable<Conjunto<E>> {
    public ConjuntoPotencia(final Conjunto<E> elementos)
    {
        this.elementos = elementos;
    }
    
    /**
     * 
     * @return Devuelve un iterador que es capaz de iterar sobre los elementos de 
     * este conjunto, es decir, sobre todos los subconjuntos del conjunto asociado.
     * Este iterador itera primero por los conjuntos de n elementos, luego por los conjuntos de
     * n-1 elementos, ...
     */
    @Override
    public Iterator<Conjunto<E>> iterator()
    {
        return new IteradorConjuntoPotencia<E>(elementos);
    }
    
    
            
    private final Conjunto<E> elementos;    
}
