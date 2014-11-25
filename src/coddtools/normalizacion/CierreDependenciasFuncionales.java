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

import java.util.Iterator;

/**
 * Representa un conjunto de dependencias funcionales abstracto; Es el cierre de un conjunto
 * de dependencias funcionales, formado teoricamente por todas las dependencias funcionales que pueden
 * obtenerse usando los axiomas de amstrong sobre las dependencias funcionales.
 * @author victor
 */
public class CierreDependenciasFuncionales {
    CierreDependenciasFuncionales(final DependenciasFuncionales dfs)
    {
        this.dfs = dfs;
    }
    
    /**
     * @return Devuelve un valor booleano indicando si esta dependencia funcional está en el cierre
     * del conjunto de dependencias funcionales asociado.
     */
    public boolean contiene(final DependenciaFuncional df)
    {
        return dfs.obtenerCierre(df.obtenerDeterminante()).contiene(df.obtenerDeterminado());
    }
    
    /**
     * Comprueba si este cierre contiene todas las dependencias funcionales del conjunto.
     * @param dfs Es un conjunto de dependencias funcionales.
     * @return Devuelve un valor booleano si este cierre contiene todas y cada una de las dependencias
     * funcionales del conjunto.
     */
    public boolean contiene(final DependenciasFuncionales dfs)
    {
        if(!dfs.esVacio())
        {
            Iterator<DependenciaFuncional> it = dfs.iterator();
            DependenciaFuncional df = it.next();
            while(it.hasNext() && contiene(df))
                df = it.next();
            return contiene(df);
        }
        return true;
    }
    
    private final DependenciasFuncionales dfs;
}
