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

/**
 * Esta clase de utilidad permite obtener los nombres de las relaciones hijas obtenidas
 * por descomposición de una relación padre. (a partir del nombre del padre).
 * Si el nombre es "A", los nombres obtenidos serán A.1, A.2, A.3, A.4, ... A.N, y consecuentemente
 * los nombres de los nietos serán A.1.1, A.1.2, ...
 * @author victor
 */
public class SolucionadorNombres {
    public static Conjunto<String> descomponerNombre(String padre, int numeroHijos) 
    {
        Conjunto<String> hijos = new Conjunto<String>();
        for(int i = 1; i <= numeroHijos; ++i)
            hijos.insertar(padre + "." + Integer.toString(i));
        return hijos;
    }
}
