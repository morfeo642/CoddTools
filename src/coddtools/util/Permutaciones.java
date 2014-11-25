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

package coddtools.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/* clase auxiliar que representa el iterador para
iterar sobre las permutaciones del conjunto.
*/
class IteradorPermutaciones<E> implements Iterator<List<E>>{
    public IteradorPermutaciones(final Conjunto<E> elementos)
    {
        this.elementos = elementos;
        if(!elementos.esVacio())
        {
            /* escoger un pivote */
            pivote = elementos.iterator().next();
            
            if(elementos.obtenerCardinal() > 1)
            {
                subconjuntoSinPivote = new Conjunto<E>();
                subconjuntoSinPivote.insertar(elementos);
                subconjuntoSinPivote.eliminar(pivote);
                it = new IteradorPermutaciones(subconjuntoSinPivote);
            }
        }
    }
    
    @Override
    public boolean hasNext() {
        if(!elementos.esVacio())
        {
            if(elementos.obtenerCardinal() > 1)
                return (posicionPivote < (elementos.obtenerCardinal() - 1)) || it.hasNext();
            return (posicionPivote == 0);
        }
        return false;
    }

    @Override
    public List<E> next() {
        if(!elementos.esVacio())
        {
            if(elementos.obtenerCardinal() > 1)
            {
                int ultimaPosicion = elementos.obtenerCardinal() - 1;
                if((posicionPivote < ultimaPosicion) || it.hasNext())
                {
                    if((posicionPivote < ultimaPosicion) && !it.hasNext())
                    {
                        /* volvemos a permutar el subconjunto */
                        it = new IteradorPermutaciones(subconjuntoSinPivote); 
                        posicionPivote++;
                    }
                    
                    List<E> aux = it.next(); /* es la siguiente permutación del subconjunto */
                    List<E> perm = new LinkedList<E>(aux);
                    /* ahora insertamos el pivote en la permutación, en su posición */
                    perm.add(posicionPivote, pivote);
                    
                    return perm;
                }
            }
            else 
            {
                if(posicionPivote == 0)
                {
                    posicionPivote++;
                    List<E> aux = new ArrayList<E>(1);
                    aux.add(pivote);
                    return aux;
                }
            }
        }
        throw new NoSuchElementException();
    }
    
    private E pivote; /* es un elemento cualquiera del conjunto */
    private IteradorPermutaciones it; /* es un iterador de permutaciones del conjunto que resulta
    de quitar al conjunto el pivote 
    */
    private int posicionPivote = 0;
    private final Conjunto<E> elementos;
    private Conjunto<E> subconjuntoSinPivote; /* es un subconjunto del conjunto de elementos. Están
    todos los elementos a excepción del pivote */
    
}

/**
 * Es un caso particular de variaciones sin repetición donde si importa el orden. 
 * @author victor
 */
public class Permutaciones<E> extends Variaciones<E>{
    
    public Permutaciones(final Conjunto<E> elementos)
    {
        super(elementos, elementos.obtenerCardinal());
        
    }
    
    @Override
    public Iterator<List<E>> iterator() 
    {
        return new IteradorPermutaciones<E>(this.elementos);
    }
    
    
}
