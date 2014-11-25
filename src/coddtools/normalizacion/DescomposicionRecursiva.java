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

/**
 * Descompone una relación de forma recursiva, hasta obtener las relaciones hijas deseadas
 * en la forma normal que estimemos oportuna. 
 * (indicamos también explictamente si queremos que las descomposiciones que se realizen sean legales 
 * y de reunión sin pérdida).
 * @author victor
 */
public class DescomposicionRecursiva {
    /* Constructor */
    /**
     * Genera una descomposición recursiva sobre una relación específica. Se descompondrán las subrelaciones
     * hasta que todas ellas alcanzen el nivel de normalización deseado. Pero, si se indica explicitamente
     * que las sucesivas descomposiciones deben ser legales y/o de reunión sin pérdida, si una relación no
     * puede descomponerse siguiendo ese criterio, no se subdivirá en más subrelaciones, y por lo tanto,
     * su nivel de normalización puede no ser el deseado.
     * @param r
     * @param fnDeseada
     * @param debeSerLegal
     * @param debeSerSinPerdida 
     */
    public DescomposicionRecursiva(Relacion r, FormaNormal fnDeseada, boolean debeSerLegal, boolean debeSerSinPerdida) 
    {
        raiz = new NodoDescomposicionRecursiva(r, fnDeseada, debeSerLegal, debeSerSinPerdida);
    }
    
    /* Consultores */
    /*
     * @return Devuelve el nodo raíz del árbol generado al descomponer recursivamente la relación inicial.
     *   El nodo raíz representa la relación inicial.
     */
    public NodoDescomposicionRecursiva obtenerNodoRaiz() 
    {
        return raiz;
    }
    
    /**
     * @return Devuelve un conjunto de relaciones que son el resultado final
     * de la descomposición recursiva. 
     * Son las hojas del árbol trazado al descomponer la relación inicial
     */
    public Conjunto<Relacion> obtenerRelacionesFinales() 
    {
        Conjunto<Relacion> finales = new Conjunto<Relacion>();
        Conjunto<NodoDescomposicionRecursiva> nodosFinales = obtenerNodoRaiz().obtenerNodosFinales();
        for(NodoDescomposicionRecursiva nodoFinal : nodosFinales)
            finales.insertar(nodoFinal.obtenerRelacion());
        return finales;
    }
    
    
    private NodoDescomposicionRecursiva raiz;
}
