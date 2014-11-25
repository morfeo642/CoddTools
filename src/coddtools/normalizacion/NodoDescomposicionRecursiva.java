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

import coddtools.normalizacion.util.Conjunto;

/**
 * Representa el nodo de un árbol que es generado al descomponer de forma recursiva una
 * relación.
 * @author victor
 */
public class NodoDescomposicionRecursiva implements Comparable<NodoDescomposicionRecursiva> {
    /* Constructores */
    NodoDescomposicionRecursiva(Relacion r, FormaNormal fnDeseada, boolean debeSerLegal, boolean debeSerSinPerdida) 
    {
        this.r = r;
        hijos = new Conjunto<NodoDescomposicionRecursiva>();
        if(!r.obtenerFormaNormal().contiene(fnDeseada))
        {
            Descomposicion d = r.descomponer();
            
            boolean esLegal = d.esLegal();
            boolean esSinPerdida = d.esReunionSinPerdida();
            
            if((!debeSerLegal || esLegal) && (!debeSerSinPerdida || esSinPerdida))
                for(Relacion hija : d.obtenerRelacionesHijas())
                    hijos.insertar(new NodoDescomposicionRecursiva(hija, fnDeseada, debeSerLegal, debeSerSinPerdida));
            
            this.descomposicion = d;
        }
        else
            this.descomposicion = null;
    }
    
    
    /* Consultores */
    
    /**
     * 
     * @return Devuelve la relación asociada a este nodo (Es parte de la traza de la
     * descomposición recursiva de una relación inicial)
     */
    public final Relacion obtenerRelacion() 
    {
        return r;
    }
    
    /**
     * @return Devuelve la descomposición realizada a esta relación, o null, si esta relación
     * ya está en la forma normal deseada (no se intento realizar una nueva descomposición).
     * Si la descomposición no satisface alguno de los dos requisitos (reúnión sin pérdida, legal),
     * descomposición, será la descomposición que no satisface dichos criterios de división, y por tanto,
     * no se generarán nuevas relaciones hijas. Se cumplirá que: obtenerNodosHijos().esVacio() == true
     */
    public final Descomposicion obtenerDescomposicion() 
    {
        return descomposicion;
    }
    
    /**
     * @return Devuelve los nodos hijos de este, o un conjunto vacío, 
     * si la descomposición de este nodo, no satisface los criterios de descomposición o bién, 
     * su nivel de normalización es el deseado. 
     */
    public final Conjunto<NodoDescomposicionRecursiva> obtenerNodosHijos() 
    {
        return hijos;
    }
    
    
    
    Conjunto<NodoDescomposicionRecursiva> obtenerNodosFinales() 
    {
        Conjunto<NodoDescomposicionRecursiva> finales = new Conjunto<NodoDescomposicionRecursiva>();
        for(NodoDescomposicionRecursiva hijo : obtenerNodosHijos())
            finales.insertar(hijo.obtenerNodosFinales()); 
        if(finales.esVacio())
           finales.insertar(this);
        return finales;
    }
    
    
    @Override
    public String toString() 
    {
        return obtenerRelacion().toString();
    }
    
    @Override
    public int compareTo(NodoDescomposicionRecursiva otro)
    {
        return this.obtenerRelacion().compareTo(otro.obtenerRelacion());
    }
    
    
    private Relacion r;
    private Conjunto<NodoDescomposicionRecursiva> hijos;
    private Descomposicion descomposicion;
}
