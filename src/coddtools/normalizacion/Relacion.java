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

import coddtools.util.Combinaciones;
import coddtools.util.Conjunto;
import java.util.Iterator;

/**
 * Representa una relación R(A, F); Posee un conjunto de dependencias funcionales,
 * y un conjunto de atributos (al menos uno)
 * @author victor
 */
public class Relacion implements Comparable<Relacion> {
    
    /* Constructor */
    public Relacion(String nombre, Descriptor atributos, DependenciasFuncionales dfs)
    {
        assert !atributos.esVacio();
        this.atributos = atributos;
        this.dfs = dfs;
        this.nombre = nombre;
        generarRecubrimientoMinimal();
        generarClavesMinimas();
        resolverFormaNormal();
    }
    
    
    private void generarRecubrimientoMinimal() 
    {
       recubrimientoMinimal = dfs.obtenerRecubrimientoMinimal();
    }
    
    private void generarClavesMinimas()
    {
        clavesMinimas = generarClavesMinimas(obtenerAtributos());
    }
    
    /**
     * @param descriptor
     * @return Devuelve un conjunto de descriptores que serán claves mínimas de la
     * relación pero que ademas son subconjuntos del descriptor.
     */
    private Conjunto<Descriptor> generarClavesMinimas(Descriptor descriptor)
    {
        Conjunto<Descriptor> clavesMinimas = new Conjunto<Descriptor>();
        /* comprobamos si este descriptor es superclave */
        if(obtenerRecubrimientoMinimal().obtenerCierre().contiene(new DependenciaFuncional(descriptor, obtenerAtributos())))
        {
            if(descriptor.esAtributoUnico())
            {
                /* el descriptor es superclave y es atributo único, luego es clave mínima */
                clavesMinimas.insertar(descriptor);
            }
            else
            {
                /* generamos todas las claves mínimas de n-1,n-2, ..., 1 elementos */
                Iterator<Conjunto<Atributo>> it = new Combinaciones(descriptor, descriptor.obtenerCardinal() - 1).iterator();
                while(it.hasNext())
                {
                    Conjunto<Atributo> aux = it.next();
                    Descriptor subconjunto = new Descriptor();
                    subconjunto.insertar(aux);
                    clavesMinimas.insertar(generarClavesMinimas(subconjunto));
                }
                
                /* si no hay claves mínimas de cualquier subconjunto del descriptor, y como el 
                descriptor es superclave, este será clave mínima
                */
                if(clavesMinimas.esVacio())
                    clavesMinimas.insertar(descriptor);
            }
        }
        return clavesMinimas;
    }
    
    private void resolverFormaNormal() 
    {
        formaNormal = FormaNormal.obtenerFormaNormal(this);
    }
    
    /* Consultores */
    /**
     * 
     * @return Devuelve el conjunto de atributos de la relación 
     */
    public final Descriptor obtenerAtributos()
    {
        return atributos;
    }
    
    /**
     * 
     * @return Devuelve el conjunto de dependencias funcionales de esta relación.
     */
    public final DependenciasFuncionales obtenerDependenciasFuncionales()
    {
        return dfs;
    }
    
    /**
     * @return Devuelve el recubrimiento mínimal del conjunto de dependencias funcionales
     * de esta relación
     */
    public final DependenciasFuncionales obtenerRecubrimientoMinimal() 
    {
        // return obtenerDependenciasFuncionales().obtenerRecubrimientoMinimal();
        return recubrimientoMinimal;
    }
    
    /**
     * 
     * @return Devuelve el nombre de esta relación. 
     */
    public final String obtenerNombre()
    {
        return nombre;
    }
    
    /**
     * 
     * @return Devuelve el nivel de normalización de esta relación. 
     */
    public final FormaNormal obtenerFormaNormal()
    {
        return formaNormal;
    }
    
    /**
     * 
     * @return Devuelve las superclaves que tienen una dependencia funcional completa
     * sobre el cierre del conjunto de dependencias funcionales sobre el conjunto de 
     * atributos de la relación (claves mínimas). 
     * @note Notese que al menos habrá una clave mínima.
     */
    public final Conjunto<Descriptor> obtenerClavesMinimas()
    {
        return clavesMinimas;
    }
    
    /**
     * @return Devuelve el conjunto de atributos que forman parte de alguna de las claves
     * mínimas de la relación
     */
    public final Descriptor obtenerAtributosPrimos() 
    {
        Iterator<Descriptor> it = obtenerClavesMinimas().iterator();
        Descriptor primos = new Descriptor();
        while(it.hasNext())
            primos.insertar(it.next());
        return primos;
    }
   
    /**
     * Comprueba si un descriptor es superclave de la relación. 
     * @return Devuelve un valor boleano indicando si es superclave o no
     */
    public final boolean esSuperClave(final Descriptor descriptor)
    {
        Iterator<Descriptor> it = obtenerClavesMinimas().iterator();
        Descriptor claveMinima = it.next();
        while(it.hasNext() && !claveMinima.esContenido(descriptor))
            claveMinima = it.next();
        return claveMinima.esContenido(descriptor);
    }
    
    /**
     * Comprueba si un descriptor es clave mínima o no.
     * @return Devuelve un valor booleano indicando si es clave mínima o no.
     */
    public final boolean esClaveMinima(final Descriptor descriptor)
    {
        Iterator<Descriptor> it = obtenerClavesMinimas().iterator();
        Descriptor claveMinima = it.next();
        while(it.hasNext() && !claveMinima.esIgual(descriptor))
            claveMinima = it.next();
        return claveMinima.esIgual(descriptor);
    }
    
    /**
     * Comprueba si un descriptor es primo o no, es decir, si forma parte de alguna
     * de las claves minimas de la relación.
     */
    public final boolean esPrimo(final Descriptor descriptor)
    {
        Iterator<Descriptor> it = obtenerClavesMinimas().iterator();
        Descriptor claveMinima = it.next();
        while(it.hasNext() && !claveMinima.contiene(descriptor))
            claveMinima = it.next();
        return claveMinima.contiene(descriptor);        
    }
    
    /**
     * Comprueba si un descriptor es primo, es decir, que forma parte de alguna clave
     * mínima, pero además, que es estríctamente, un subconjunto de esta. Es decir,
     * comprueba si es primo pero además no es clave candidata.
     *
     */
    public final boolean esPrimoEstricto(final Descriptor descriptor)
    {
        Iterator<Descriptor> it = obtenerClavesMinimas().iterator();
        Descriptor claveMinima = it.next();
        while(it.hasNext() && !claveMinima.contieneEstrictamente(descriptor))
            claveMinima = it.next();
        return claveMinima.contieneEstrictamente(descriptor);        
    }
    
    /**
     * Descompone esta relación. 
     * @return Devuelve el resultado de la descomposición de esta relación
     */
    public final Descomposicion descomponer()
    {
        return obtenerFormaNormal().descomponer(this);
    }
    
    @Override
    public String toString()
    {
        return obtenerNombre() + "({" + obtenerAtributos() + "}, {" + obtenerDependenciasFuncionales() + "})";
    }
    
    @Override
    public int compareTo(Relacion otra)
    {
        return obtenerNombre().compareTo(otra.obtenerNombre());
    }
    
    private DependenciasFuncionales dfs;
    private DependenciasFuncionales recubrimientoMinimal;
    private Descriptor atributos;
    private Conjunto<Descriptor> clavesMinimas;
    private String nombre;
    private FormaNormal formaNormal;
}
