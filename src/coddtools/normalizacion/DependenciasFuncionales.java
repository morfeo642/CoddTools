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
 * Representa un conjunto de dependencias funcionales.
 * @author victor
 */
public class DependenciasFuncionales extends Conjunto<DependenciaFuncional>{
    public DependenciasFuncionales()
    {
        
        
    }
    
    /* obtención de cierres */
    /**
     * @return Devuelve el cierre de un descriptor bajo este conjunto de
     * dependencias funcionales.
     */
    public Descriptor obtenerCierre(final Descriptor descriptor)
    {
        Descriptor cierre = new Descriptor();
        cierre.insertar(descriptor);
        if(!esVacio())
        {
            while(true)
            {
                /* buscamos la primera DF X->Y tal que X sea subconjunto del cierre, e Y
                no sea subconjunto del cierre */
                Iterator<DependenciaFuncional> it = iterator();
                DependenciaFuncional df = it.next();
                while(it.hasNext() && ((!df.obtenerDeterminante().esContenido(cierre)) ||
                        (df.obtenerDeterminado().esContenido(cierre))))
                    df = it.next();
                /* si existe una df X->Y que lo cumpla insertamos Y en el cierre, y seguimos buscando */
                if(df.obtenerDeterminante().esContenido(cierre) && !df.obtenerDeterminado().esContenido(cierre))
                {
                    cierre.insertar(df.obtenerDeterminado());
                    continue;
                }
                break;
            }
        }
        return cierre;
    }
    
    /**
     * @return Devuelve el cierre de este conjunto de dependencias funcinales.
     */
    public CierreDependenciasFuncionales obtenerCierre()
    {
        return new CierreDependenciasFuncionales(this);
    }
    
    /* operaciones con dependencias funcionales */
    /**
     * Determina si una dependencia funcional es completa, es decir, si no existe ningún subconjunto del
     * determinante que determine al determinado.
     * @param df Es una dependencia funcional que debe estar dentro de este conjunto de dependencias 
     * funcionales.
     * @return Devuelve un valor booleano indicando si la dependencia funcional es completa.
     */
    public boolean esCompleta(final DependenciaFuncional df)
    {
        assert contiene(df);
        
        Descriptor determinante = df.obtenerDeterminante();
        if(determinante.esAtributoUnico())
            return true;
        
        /* existe algún subconjunto de n-1 atributos, donde n es el número de atributos del determinante,
        si existe algún subconjunto de menos de n-1 atributos que determine al determinado, entonces,
        exisitirá un subconjunto de n-1 elementos que también lo determine.
        */
       Iterator<Conjunto<Atributo>> it = new Combinaciones<Atributo>(determinante, determinante.obtenerCardinal() - 1).iterator();
       /* al menos existirá una combinación... */
       Descriptor subconjunto = new Descriptor();
       subconjunto.insertar(it.next());
       while(it.hasNext() && !df.obtenerDeterminado().esContenido(obtenerCierre(subconjunto)))
       {
           subconjunto.eliminarTodo();
           subconjunto.insertar(it.next());
       }
       
       return !df.obtenerDeterminado().esContenido(obtenerCierre(subconjunto));
    }
    
    /**
     * Obtiene los atributos ajenos de una dependencia funcional. Si X -> Y es la dependencia
     * funcional, si existe un conjunto Z tal que X / Z -> Y es completa, entonces, los atributos de
     * Z son ajenos a la dependencia original.
     * @param df Es una dependencia funcional que debe existir en este conjunto de dependencias 
     * funcionales.
     * @return Devuelve los atributos ajenos, o null si la dependencia es completa.
     */
    public Descriptor obtenerAtributosAjenos(final DependenciaFuncional df)
    {
        assert contiene(df);
        return _obtenerAtributosAjenos(df);
    }
    
    
    private Descriptor _obtenerAtributosAjenos(final DependenciaFuncional df)
    {
        Descriptor determinante = df.obtenerDeterminante();
        if(determinante.esAtributoUnico())
            return null;
        
       Iterator<Conjunto<Atributo>> it = new Combinaciones<Atributo>(determinante, determinante.obtenerCardinal() - 1).iterator();
       /* al menos existirá una combinación... */
       Descriptor subconjunto = new Descriptor();
       subconjunto.insertar(it.next());
       while(it.hasNext() && !df.obtenerDeterminado().esContenido(obtenerCierre(subconjunto)))
       {
           subconjunto.eliminarTodo();
           subconjunto.insertar(it.next());
       }
       
       if(df.obtenerDeterminado().esContenido(obtenerCierre(subconjunto)))
       {
           Descriptor aux = _obtenerAtributosAjenos(new DependenciaFuncional(subconjunto, df.obtenerDeterminado()));
           if(aux != null)
               subconjunto.eliminar(aux);
           Descriptor ajenos = new Descriptor();
           ajenos.insertar(determinante);
           ajenos.eliminar(subconjunto);
           return ajenos;
       }
       return null;
    }
    
    /**
     * Comprueba si una dependencia funcional es parcial, es decir, si no es completa.
     * @param df Es una dependencia funcional que existe en este conjunto de dependencias funcionales.
     * @return Devuelve un valor booleano indicando si la df es parcial.
     */
    public boolean esParcial(final DependenciaFuncional df)
    {
        assert contiene(df);
        return !esCompleta(df);
    }

    /**
     * Comprueba si una dependencia funcional es elemental, es decir, si es completa, pero además
     * el determinado es atributo único.
     * @param df Es una dependencia que debe existir en este conjunto de dependencias.
     * @return Devuelve un valor booleano indicando si esta dependencia es elemental.
     */
    public boolean esElemental(final DependenciaFuncional df)
    {
        assert contiene(df);
        return esCompleta(df) && df.obtenerDeterminado().esAtributoUnico();
    }
    
    /**
     * 
     * @param descriptor
     * @return Devuelve un valor booleano indicando si, todas las dependencias funcionales contenidas
     * en este conjunto, tanto su determinante, como su determinado, son subconjuntos (no estrictos),
     * del descriptor indicado.
     */
    public boolean estanCompuestasPor(final Descriptor descriptor)
    {
        Iterator<DependenciaFuncional> it = this.iterator();
        if(it.hasNext())
        {
            DependenciaFuncional df = it.next();
            while(it.hasNext() && df.estaCompuestaPor(descriptor))
                df = it.next();
            return df.estaCompuestaPor(descriptor);
        }
        return true;
    }
    

    /* operaciones entre diferentes conjuntos de dependencias funcionales */
    /**
     * Comprueba si este conjunto de DFs, cubre a otro conjunto de DFs.
     * Un conjunto A de DFs cubre a un conjunto B de DFs, si para cada DF de B, es contenida
     * por el cierre del conjunto de dependencias funcionales de A.
     * @param dfs
     * @return Devuelve un valor booleano indicando si este conjunto de DFs cubre al otro
     * conjunto.
     */
    public boolean cubre(final DependenciasFuncionales dfs)
    {
        return obtenerCierre().contiene(dfs);
    }
    
    public boolean esCubierto(final DependenciasFuncionales dfs)
    {
        return dfs.cubre(this);
    }
    
    /**
     * Comprueba si dos conjuntos de dependencias funcionales son equivalentes, es decir,
     * si este conjunto de DFs cubre al otro conjunto y viceversa.
     */
    public boolean esEquivalente(final DependenciasFuncionales dfs)
    {
        return cubre(dfs) && esCubierto(dfs);
    }
    
    /**
     * Obtiene el recubrimiento mínimal de este conjunto de dependencias funcionales.
     * Es un conjunto de dependencias funcionales no triviales, elementales, sin atributos ajenos, es decir,
     * completas, y sin dependencias funcionales redundantes.
     * @return Devuelve el recubrimiento minimal.
     */
    public DependenciasFuncionales obtenerRecubrimientoMinimal()
    {
        DependenciasFuncionales minimal = new DependenciasFuncionales();
        if(!esVacio())
        {
            minimal.insertar(this);
            DependenciasFuncionales aux = new DependenciasFuncionales();
            aux.insertar(minimal);
            
            /* un único atributo implicado */
            Iterator<DependenciaFuncional> it = minimal.iterator();
            while(it.hasNext())
            {
                DependenciaFuncional df = it.next();
                aux.eliminar(df);
                aux.insertar(df.distribuirDeterminado());
            }
            
            minimal.eliminarTodo();
            minimal.insertar(aux);
            
            /* eliminar dependencias triviales */
            it = minimal.iterator();
            while(it.hasNext())
            {
                DependenciaFuncional df = it.next();
                if(df.esTrivial())
                    aux.eliminar(df);
            }
            
            minimal.eliminarTodo();
            minimal.insertar(aux);            
            
            /* eliminar atributos extraños */
            it = minimal.iterator();
            while(it.hasNext())
            {
                DependenciaFuncional df = it.next();
                Descriptor ajenos = minimal.obtenerAtributosAjenos(df);
                if(ajenos != null) /* si no es completa */
                {
                    Descriptor noAjenos = new Descriptor();
                    noAjenos.insertar(df.obtenerDeterminante());
                    noAjenos.eliminar(ajenos);
                    DependenciaFuncional completa = new DependenciaFuncional(noAjenos, df.obtenerDeterminado());
                    
                    aux.eliminar(df);
                    aux.insertar(completa);
                }
            }
            
            minimal.eliminarTodo();
            minimal.insertar(aux);
            
            /* eliminar dependencias redundantes */
            it = minimal.iterator();
            while(it.hasNext())
            {          
                DependenciaFuncional df = it.next();
                aux.eliminar(df); /* suponemos que es redundante */
                
                if(!aux.obtenerCierre(df.obtenerDeterminante()).contiene(df.obtenerDeterminado()))
                    aux.insertar(df); /* si no lo es, la reinsertamos */
            }
            
            minimal.eliminarTodo();
            minimal.insertar(aux);
        }
        return minimal;
    }
    
    @Override
    public String toString()
    {
        String aux = new String();
        if(!esVacio())
        {
            Iterator<DependenciaFuncional> it = iterator();
            DependenciaFuncional atr = it.next();
            while(it.hasNext())
            {
                aux += atr + "; ";
                atr = it.next();
            }
            aux += atr;
        }
        return aux;
    }
    
    /**
     * 
     * @return Devuelve el conjunto de dependencias funcionales reprsentado por la
     * cadena de caracteres tomada como parámetro. La cadena de caracteres debe
     * tener la siguiente estructura:
     * X1 -> Y1; X2 -> Y2; X3 -> Y3; ... ;XN -> YN
     * Donde Xi, Yi son descriptores válidos. Las dependencias funcionales se separan 
     * mediante ; Se ignoran los espacios que puedan existir entre una dependencia funcional y 
     * otra, y entre el separador. 
     * Son válidos: X1 -> Y1  ;X2 -> Y2 , o X1 -> Y1  ;  X2 -> Y2
     * @throws IllegalArgumentException Si la cadena no puede ser convertida a un conjunto de
     * dependencias funcionales (no tiene un formato válido)
     */
    public static DependenciasFuncionales fromString(String str) throws IllegalArgumentException
    {
        if(str.replaceAll(" ", "").isEmpty())
            return new DependenciasFuncionales();
        String[] tokens = str.split(";");
        DependenciasFuncionales dfs = new DependenciasFuncionales();
        for(String token : tokens)
            dfs.insertar(DependenciaFuncional.fromString(token));
        return dfs;
    }
}
