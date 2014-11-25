/*
 * The MIT License
 *
 * Copyright 2014 Víctor Ruiz Gómez
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Representa un conjunto de elementos de un tipo específico.
 * Estos elementos deben tener un orden. (Deben implementar el interfaz
 * Comparable<E>)
 * @author Víctor Ruiz Gómez
 */
public class Conjunto<E> implements Iterable<E>
{
    /* Constructores */
    /**
     * Construir un conjunto vacío.
     */
    public Conjunto()
    {
        elementos = new TreeSet<E>();
    }
    
    /* Operaciones básicas */
    /**
     * Inserta un nuevo elemento en el conjunto
     * @param elemento
     * @return Devuelve un valor booleano indicando si el
     * elemento se insertó en el conjunto o por el contrario, ya
     * estaba en el conjunto.
     */
    public final boolean insertar(final E elemento)
    {
        return elementos.add(elemento);
    }
    
    /**
     * Inserta un conjunto de elementos a este conjunto.
     * El resultado es como si este conjunto fuera el conjunto resultante
     * de hacer la unión entre el mismo y el otro conjunto.
     * @return Devuelve un valor booleano indicando si se inertó o no 
     * algún elemento en este conjunto.
     */
    public final boolean insertar(final Conjunto<? extends E> otro)
    {
        return elementos.addAll(otro.elementos);
    }
    
    /**
     * Elimina un elemento del conjunto.
     * @param elemento
     * @return Devuelve un valor booleano indicando si el elemento
     * se eliminó o por el contrario, no estaba en el conjunto.
     */
    public final boolean eliminar(final E elemento)
    {
        return elementos.remove(elemento);
    }
    
    /**
     * Inerta un conjunto de elementos cualesquiera en este conjunto.
     * @return Devuelve un valor booleano indicando si se insertó o no
     * algún elemento en este conjunto.
     */
    public final boolean insertar(final Collection<? extends E> elementos)
    {
        return this.elementos.addAll(elementos);
    }
    
    /**
     * Elimina un conjunto de elementos de este elemento.
     * Es como si a este conjunto le asignaramos el resultado de la diferencia entre
     * este mismo conjunto y el otro conjunto.
     * @return Devuelve un valor booleano indicando si se eliminó algún elemento 
     * del conjunto.
     */
    public final boolean eliminar(final Conjunto<? extends E> otro)
    {
        return elementos.removeAll(otro.elementos);
    }
    
    /**
     * Elimina todos los elementos de este conjunto, de manera que este conjunto pasará
     * a ser el conjunto vacío.
     */
    public final void eliminarTodo()
    {
        elementos.clear();
    }
    
    /* Operadores contiene/ es contenido */ 
    /**
     * Comprueba si este conjunto contiene dicho elemento.
     * @param elemento
     * @return Devuelve un valor booleano indicando si el elemento es
     * o no contenido por el conjunto.
     */
    public final boolean contiene(final E elemento)
    {
        return elementos.contains(elemento);
    }
    
    /**
     * Comprueba si un conjunto es un subconjunto de este conjunto.
     * @param otro
     * @return Devuelve un valor booleano indicando si bien este conjunto 
     * contiene al conjunto específicado como parámetro.
     */
    public final boolean contiene(final Conjunto<? extends E> otro)
    {
        return elementos.containsAll(otro.elementos);
    }
    
    /**
     * Comprueba si este conjunto es un subconjunto de otro conjunto.
     * @return Devuelve otro.contiene(this)
     */
    public final boolean esContenido(final Conjunto<? extends E> otro)
    {
        return otro.elementos.containsAll(this.elementos);
    }
    
    /**
     * Comprueba si este conjunto contiene estríctamente a otro conjunto, es decir,
     * si es subconjunto estrícto de este. 
     * @param otro
     * @return 
     */
    public final boolean contieneEstrictamente(final Conjunto<? extends E> otro)
    {
        return contiene(otro) && (otro.obtenerCardinal() < this.obtenerCardinal());
    }
    
    /**
     * Comprueba si este conjunto es contenido estríctamente por otro conjunto.
     * 
     */
    public final boolean esContenidoEstrictamente(final Conjunto<? extends E> otro)
    {
        return ((Conjunto<E>)otro).contieneEstrictamente(this);
    }
    
    /**
     * Comprueba si este conjunto posee los mismos elementos que otro conjunto.
     * @return Devuelve un valor booleano indicando si bien ambos conjuntos contienen
     * los mismos elementos.
     */
    public final boolean esIgual(final Conjunto<? extends E> otro)
    {
        return contiene(otro) && esContenido(otro);
    }
    
    /**
     * 
     * @return Devuelve un valor booleano indicando si este conjunto es vacío 
     * o no, es decir, no contiene elementos.
     */
    public final boolean esVacio() 
    {
        return obtenerCardinal() == 0;
    }
    
    /* Operaciones union, diferencia, ... */
    /**
     * Une dos conjuntos.
     * @param otro
     * @return Devuelve un conjunto con los elementos de este conjunto y
     * los elementos del conjunto especificado como parámetro.
     */
    public final Conjunto union(final Conjunto<? extends E> otro)
    {
        Conjunto aux = new Conjunto();
        aux.insertar(this);
        aux.insertar(otro);
        return aux;
    }
    
    /**
     * Intersecciona dos conjuntos.
     * @param otro
     * @return Devuelve un conjunto con los elementos que están tanto en
     * este conjunto como en el conjunto específicado como argumento.
     */
    public final Conjunto interseccion(final Conjunto<? extends E> otro)
    {
        Conjunto aux = new Conjunto();
        Iterator<? extends E> it = otro.iterator();
        while(it.hasNext())
        {
            E x = it.next();
            if(this.contiene(x))
                aux.insertar(x);
        }
        return aux;
    }
    
    /**
     * Resta este conjunto con otro conjunto.
     * @param otro
     * @return Devuelve un conjunto que posee los elementos que están en este
     * conjunto y no en el conjunto tomado como argumento.
     */
    public final Conjunto diferencia(final Conjunto<? extends E> otro)
    {
        Conjunto aux = new Conjunto();
        aux.insertar(this);
        Iterator<? extends E> it = otro.iterator();
        while(it.hasNext())
            aux.eliminar(it.next());
        return aux;
    }
    
    /**
     * Realiza la diferencia simétrica entre dos conjuntos. En lógica
     * se denomina operacion XOR.
     * @param otro
     * @return Devuelve un conjunto cuyos elementos están en este
     * conjunto o en el otro, pero no en los dos al mismo tiempo.
     */
    public final Conjunto diferenciaSimétrica(final Conjunto<? extends E> otro)
    {
        /*
        return this.suma(otro).diferencia(this.interseccion(otro));
        */
        Conjunto aux = new Conjunto();
        aux.insertar(this);
        Iterator<? extends E> it = otro.iterator();
        while(it.hasNext())
        {
            E x = it.next();
            if(this.contiene(x))
                aux.eliminar(x);
            else
                aux.insertar(x);
        }
        return aux;
    }
    
    /* Cardinal del conjunto */
    /**
     * 
     * @return Devuelve el cardinal de este conjunto, es decir, el número
     * de elementos de este conjunto
     */
    public final int obtenerCardinal()
    {
        return elementos.size();
    }
    
    /* Iteración sobre el conjunto */
    @Override
    public final Iterator<E> iterator() 
    {
        return elementos.iterator();
    }
    
    /* conversión a cadena de caracteres */
    @Override
    public String toString()
    {
        String aux = new String();
        if(!esVacio())
        {
            Iterator<E> it = iterator();
            E atr = it.next();
            while(it.hasNext())
            {
                aux += atr + ", ";
                atr = it.next();
            }
            aux += atr;
        }
        return aux;
    }
    
    /* Atributos */
    Set<E> elementos;
}
