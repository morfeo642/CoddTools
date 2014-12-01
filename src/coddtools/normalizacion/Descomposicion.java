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

import coddtools.util.Conjunto;
import java.util.Iterator;

/**
 * Las instancias de esta clase contienen información sobre la descomposición de una
 * relación (el resultado de la misma).
 * @author victor
 */
public class Descomposicion {
    /* Constructor */
    /**
     * @note El número de relaciones hijas debe ser mayor o igual a dos.
     * @param padre
     * @param hijas 
     */
    public Descomposicion(final Relacion padre, final Conjunto<Relacion> hijas)
    {
        assert hijas.obtenerCardinal() >= 2;
        this.padre = padre;
        this.hijas = hijas;
    }
    
    /**
     * Construye una descomposición binaria. La relación padre se divide en dos relaciones
     * hijas.
     * @param padre
     * @param a
     * @param b 
     */
    public Descomposicion(final Relacion padre, final Relacion a, final Relacion b)
    {
        this.padre = padre;
        hijas = new Conjunto<Relacion>();
        hijas.insertar(a);
        hijas.insertar(b);
    }
    
    /**
     * Construye una descomposición binaria. La relación padre se divide en dos relaciones
     * hijas; Estas tendrán los atributos y las dependencias funcionales indicadas como parámetros.
     * Los nombres de estas relaciones serán obtenidos a través del solucionador de nombres
     * (SolucionadorNombres)
     * @param padre
     * @param attrsA
     * @param dfsA
     * @param attrsB
     * @param dfsB 
     */
    public Descomposicion(final Relacion padre, final Descriptor attrsA, final DependenciasFuncionales dfsA,
            final Descriptor attrsB, final DependenciasFuncionales dfsB)
    {
        Iterator<String> nombres = SolucionadorNombres.descomponerNombre(padre.obtenerNombre(),2).iterator();
        this.padre = padre;
        hijas = new Conjunto<Relacion>();
        hijas.insertar(new Relacion(nombres.next(), attrsA, dfsA));
        hijas.insertar(new Relacion(nombres.next(), attrsB, dfsB));
        
    }
    /* Consultores */
    /**
     * 
     * @return Devuelve las relaciones obtenidas como resultado de haber realizado esta
     * descomposición.
     */
    public final Conjunto<Relacion> obtenerRelacionesHijas() 
    {
        return hijas;
    }
    
    /**
     * @return Devuelve la relación a la que se le aplicó la descomposición.
     */
    public final Relacion obtenerRelacionPadre()
    {
        return padre;
    }
    
    /**
     * Comprueba si la descomposición es legal, es decir, sin que se pierda ninguna dependencia
     * funcional de la relación descompuesta.
     * @return Devuelve un valor booleano indicando si la descomposición es legal.
     */
    public final boolean esLegal()
    {
        /* si F1, F2, ..., FN son el conj. de DFs de las relaciones hijas, la desc. será legal
        si F1 u F2 u .. u Fn = F
        */
        DependenciasFuncionales aux = new DependenciasFuncionales();
        Iterator<Relacion> it = obtenerRelacionesHijas().iterator();
        while(it.hasNext())
            aux.insertar(it.next().obtenerDependenciasFuncionales());
        return aux.esIgual(obtenerRelacionPadre().obtenerRecubrimientoMinimal());
    }
    
    /**
     * Comprueba si la descomposición es de reunión sin pérdida, es decir, sin pérdida de información.
     * (sin generación de tuplas espurias)
     */
    public final boolean esReunionSinPerdida()
    {
        /*
        Si R1,R2,...,Rn son los conj. de los atributos de las relaciones hijas, será de reunión sin pérdida,
        si en F+ existe una de las siguientes DFs: R1^R2^..^Rn -> R1,  R1^R2^...^Rn -> R2, ... R1^R2^...^Rn -> Rn
        */
        
        /* Obtenemos R1^R2^R3^...^RN */
        Descriptor aux = new Descriptor();
        Descriptor interseccion = new Descriptor();
        Iterator<Relacion> it = obtenerRelacionesHijas().iterator();
        interseccion.insertar(it.next().obtenerAtributos());
        while(it.hasNext())
        {
            aux.eliminarTodo();
            aux.insertar(interseccion.interseccion(it.next().obtenerAtributos()));
            interseccion.eliminarTodo();
            interseccion.insertar(aux);
        }
        
        /* Calcular F+ */
        CierreDependenciasFuncionales cierre = obtenerRelacionPadre().obtenerRecubrimientoMinimal().obtenerCierre();
        
        
        /* buscar una DF que satisfaga la condición para que la descomp. sea sin pérdida */
        it = obtenerRelacionesHijas().iterator();
        Relacion hija = it.next();
        while(it.hasNext() && !cierre.contiene(new DependenciaFuncional(interseccion, hija.obtenerAtributos())))
            hija = it.next();
        return cierre.contiene(new DependenciaFuncional(interseccion, hija.obtenerAtributos()));
    }
    
    @Override
    public String toString() 
    {
        String aux = new String();
        aux += "Descomposicion de " + obtenerRelacionPadre().obtenerNombre() + " en ";
        Iterator<Relacion> it = obtenerRelacionesHijas().iterator();
        Relacion hija = it.next();
        while(it.hasNext())
        {
            aux += hija.obtenerNombre() + ", ";
            hija = it.next();
        }
        aux += hija.obtenerNombre() + " ";
        boolean legal = esLegal();
        boolean sinPerdida = esReunionSinPerdida();
        
        if(legal || sinPerdida)
            aux += "( ";
        
        if(legal)
        {
            aux += "es legal";
            if(sinPerdida)
                aux += " y de reunion sin perdida";
        }
        else if(sinPerdida)
            aux += "es de reunion sin perdida";
        
        
        if(legal || sinPerdida)
            aux += " )";
        
        return aux;
    }
    
    private final Conjunto<Relacion> hijas;
    private final Relacion padre;
}
