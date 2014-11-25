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
import java.util.Iterator;

/**
 * Un descriptor es un conjunto de atributos de relación.
 * @author victor
 */
public class Descriptor extends Conjunto<Atributo> implements Comparable<Descriptor> {
    public Descriptor()
    {
        
    }

    @Override
    public int compareTo(Descriptor otro) 
    {
        if(this.obtenerCardinal() != otro.obtenerCardinal())
            return this.obtenerCardinal() - otro.obtenerCardinal();
        if(this.obtenerCardinal() > 0)
        {
            Iterator<Atributo> it = this.iterator();
            Iterator<Atributo> it2 = otro.iterator();

            Atributo a, b;
            a = it.next(); b = it2.next();
            while(it.hasNext() && (a.compareTo(b) == 0))
            {
                a = it.next();
                b = it2.next();
            }
            return a.compareTo(b);
        }
        return 0;
    }
    
    /**
     * 
     * @return Devuelve un valor booleano indicando si este descriptor solo tiene un único
     * atributo.
     */
    public boolean esAtributoUnico()
    {
        return (obtenerCardinal() == 1);
    }
    
    
    
    /**
     * @return Devuelve un descriptor cuyos atributos son los especificados por 
     * la cadena de caracteres. Los atributos van separados por comas y opcionalmente
     * por espacios. Los atributos podrán estar compuestos por cualquier caracter alfanumérico y los simbolos
     * _,@,%,$
     * (A-Z,a-z,0-9,_)
     * @throws Lanza IllegalArgumentException si la cadena no tiene un formato correcto.
     */
    public static Descriptor fromString(String str) throws IllegalArgumentException
    {
        str = str.replaceAll("[ ]+", "");
        String[] tokens = str.split(",");
        /* verificar si los atributos tienen un formato válido */
        for(int i = 0; i != tokens.length; ++i)
            if(!tokens[i].matches("[A-Z,a-z,0-9,_,,@,$,%]+"))
                throw new IllegalArgumentException();
        
        
        /* crear el descriptor con los atributos */
        Descriptor descriptor = new Descriptor();
        for(int i = 0; i != tokens.length; ++i)
            descriptor.insertar(new Atributo(tokens[i]));
       
        return descriptor;
    }
}
