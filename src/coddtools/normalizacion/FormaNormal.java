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
 * Representa la forma normal en la que se encuentra una relación determinado( 1FN,2FN,3FN,FNBC,)
 * @author victor
 */
public abstract class FormaNormal implements Comparable<FormaNormal> {
    
    private FormaNormal()
    {
        nivel = 1;
    }
    
    private FormaNormal(FormaNormal formaInferior)
    {
        nivel = formaInferior.nivel + 1; 
    }
    
    /**
     * Comprueba si una forma normal es incluida por esta forma normal.e.g 
     * 3FN incluye a 2FN y 1FN, y FNBC incluye a todas las anteriores.
     * @param otra
     * @return Devuelve un valor booleano indicando si la forma normal es incluida
     * por esta forma normal.
     * @note this.incluye(this) = true, es decir una forma normal se contiene a si misma.
     */
    public final boolean contiene(FormaNormal otra)
    {
        return nivel >= otra.nivel;
    }
    
    /**
     * Comprueba si esta forma normal es incluida por otra forma normal.
     * @param otra
     * @return 
     * @see contiene(FormaNormal)
     */
    public final boolean esContenida(FormaNormal otra)
    {
        return otra.contiene(this);
    }
    
    /**
     * Comprueba si el nivel de normalización de una relación, es el indicado por
     * esta forma normal. Se supondrá que, al menos, la forma normal en la que está
     * la relación incluya todas los formas normales incluidas por esta forma normal(exceptuando esta misma).
     * e.g. Si esta forma normal representa 3FN, supondremos que la forma normal de la
     * relación incluye 2FN y 1FN, es decir, es 2FN, y devolveremos un valor booleano indicando
     * si está relación está en 3FN.
     */
    public abstract boolean esNormalizada(Relacion r);
    
    /**
     * Descompone una relación. Supondremos que la forma normal de la relación es esta forma 
     * normal.
     * e.g: Si esta forma normal representa 3FN, la relación deberá estar en 3FN, y dará lugar a 
     * relaciones (mediante la descomposición), cuyo nivel de normalización es mayor o igual que esta
     * forma normal.
     * @param r
     * @return Devuelve la descomposición de la relación, o null si la relación ya no puede descomponerse,
     * es decir, su nivel de normalización es máximo (FNBC)
     * descomposición
     */
    public abstract Descomposicion descomponer(Relacion r);
    
    /**
     * 
     * @param r
     * @return Devuelve el nivel de normalización de una relación dada. Suponiendo que los atributos
     * de la relación son atómicos. El nivel de normalización devuelto será al menos 1FN.
     */
    public static FormaNormal obtenerFormaNormal(Relacion r)
    {
        if(!FN2.esNormalizada(r))
            return FN1;
        if(!FN3.esNormalizada(r))
            return FN2;
        if(!FNBC.esNormalizada(r))
            return FN3;
        return FNBC;
    }
    
    
    private int nivel; /* a cada forma le asignamos un nivel, cuanto mayor sea, más general es
    (contendrá a más formas normales) */
    
    
    
    /* Orden */
    @Override
    public int compareTo(FormaNormal otra)
    {
        return nivel - otra.nivel;
    }
    
    
    /* Formas normales */
    public static FormaNormal FN1 = new FormaNormal() 
        { 
            @Override
            public boolean esNormalizada(Relacion r) 
            {
                return true;
            }

            @Override
            public Descomposicion descomponer(Relacion r)
            {
                /* buscar la dep. funcional cuyo determinante es sub.estricto de una CC.
                y su determinado es un atr. no primo. "Impide la forma normal 2FN"
                */
  
                DependenciasFuncionales dfs = r.obtenerRecubrimientoMinimal();
                Descriptor noPrimos = new Descriptor();
                noPrimos.insertar(r.obtenerAtributos());
                noPrimos.eliminar(r.obtenerAtributosPrimos());
                
                Iterator<DependenciaFuncional> it = dfs.iterator();
                DependenciaFuncional df = it.next();
                while(!(r.esPrimoEstricto(df.obtenerDeterminante()) && df.obtenerDeterminado().esContenido(noPrimos)))
                    df = it.next();
                
                /* Descomponemos en dos relaciones A y B, una que contenga la df buscada y otra con el resto
                de dependencias funcionales
                El atributo no primo que forma parte del determinado de la d.f buscada, se eliminará de la 
                segunda relación, si no está presente en ninguna otra d.f
                */
                
                /* Dependencias funcionales de la relación B */
                DependenciasFuncionales dfsB = new DependenciasFuncionales();
                dfsB.insertar(dfs);
                dfsB.eliminar(df);
                
                /* Dependencias funcionales de la relación A */
                DependenciasFuncionales dfsA = new DependenciasFuncionales();
                dfsA.insertar(df);
                
                /* Atributos de la relación A */
                Descriptor attrsA = new Descriptor();
                attrsA.insertar(df.obtenerDeterminante().union(df.obtenerDeterminado()));

                /* Atributos de la relación B */
                Descriptor attrsB = new Descriptor();
                attrsB.insertar(r.obtenerAtributos());
                
                /* El atributo que forma parte del determinado de la dependencia funcional anteriormente
                buscada está presente en alguna dependencia funcional de la relación B ? */
               
                it = dfsB.iterator();
                if(it.hasNext())
                {
                    DependenciaFuncional dfB = it.next();
                    while(it.hasNext() && !(dfB.obtenerDeterminante().contiene(df.obtenerDeterminado()) ||  
                            dfB.obtenerDeterminado().contiene(df.obtenerDeterminado())))
                        dfB = it.next();

                    if(!(dfB.obtenerDeterminante().contiene(df.obtenerDeterminado()) ||  
                            dfB.obtenerDeterminado().contiene(df.obtenerDeterminado())))
                        attrsB.eliminar(df.obtenerDeterminado());
                }
                
                return new Descomposicion(r, attrsA, dfsA, attrsB, dfsB);
            }
            
            @Override
            public String toString()
            {
                return "1FN";
            }
        };
    
    public static FormaNormal FN2 = new FormaNormal(FN1) 
        { 
            @Override
            public boolean esNormalizada(Relacion r) 
            {
                DependenciasFuncionales dfs = r.obtenerRecubrimientoMinimal();
                if(dfs.esVacio())
                    return true;
                Descriptor noPrimos = new Descriptor();
                noPrimos.insertar(r.obtenerAtributos());
                noPrimos.eliminar(r.obtenerAtributosPrimos());
                
                /* buscamos una dependencia funcional en el recubrimiento minimal tal que
                el determinante sea un subconjunto estricto de una clave minimal y el determinado
                sea un atributo no primo 
                */
                Iterator<DependenciaFuncional> it = dfs.iterator();
                DependenciaFuncional df = it.next();
                while(it.hasNext() && !(r.esPrimoEstricto(df.obtenerDeterminante()) && df.obtenerDeterminado().esContenido(noPrimos)))
                    df = it.next();
               return !(r.esPrimoEstricto(df.obtenerDeterminante()) && df.obtenerDeterminado().esContenido(noPrimos));
            }

            @Override
            public Descomposicion descomponer(Relacion r)
            {
                /* Buscar la dependencia funconal X -> Y tal que X no sea superclave y además, Y es
                atributo no primo.
                */
                DependenciasFuncionales dfs = r.obtenerRecubrimientoMinimal();
                Descriptor primos = r.obtenerAtributosPrimos();
                
                Iterator<DependenciaFuncional> it = dfs.iterator();
                DependenciaFuncional df = it.next();
                while(r.esSuperClave(df.obtenerDeterminante()) || df.obtenerDeterminado().esContenido(primos))
                    df = it.next();
                
                /* Descomponemos la relación en dos, una con la dependencia funcional que incumple
                3FN, y otra con el resto de dfs.
                */
                DependenciasFuncionales dfsA = new DependenciasFuncionales();
                dfsA.insertar(df);
                
                DependenciasFuncionales dfsB = new DependenciasFuncionales();
                dfsB.insertar(dfs);
                dfsB.eliminar(dfsA);
                
                Descriptor attrsA = new Descriptor();
                attrsA.insertar(df.obtenerDeterminante().union(df.obtenerDeterminado()));
                
                /* Que atributos tendrá la otra relación: Todos los que aparezcan o bién en el
                determinante o en el determinado de alguna dependencia funcional en su conjunto de
                dfs. */
                Descriptor attrsB = new Descriptor();
                for(DependenciaFuncional dfB : dfsB)
                    attrsB.insertar(dfB.obtenerDeterminante().union(dfB.obtenerDeterminado()));
                
                return new Descomposicion(r, attrsA, dfsA, attrsB, dfsB);
            }
            
            @Override
            public String toString() 
            {
                return "2FN";
            }
        };
   
    public static FormaNormal FN3 = new FormaNormal(FN2) 
        { 
            @Override
            public boolean esNormalizada(Relacion r) 
            {
                DependenciasFuncionales dfs = r.obtenerRecubrimientoMinimal();
                if(dfs.esVacio())
                    return true;
                Descriptor primos = r.obtenerAtributosPrimos();
                
                /* buscamos la primera df no trivial que cumple que el determinante no es superclave y
                el determinado no es atributo primo. Notese que buscamos en el recubrimiento minimal, luego
                no hay dependencias funcionales triviales.
                */
                Iterator<DependenciaFuncional> it = dfs.iterator();
                DependenciaFuncional df = it.next();
                while(it.hasNext() && (r.esSuperClave(df.obtenerDeterminante()) || df.obtenerDeterminado().esContenido(primos)))
                    df = it.next();
                return r.esSuperClave(df.obtenerDeterminante()) || df.obtenerDeterminado().esContenido(primos);
            }

            @Override
            public Descomposicion descomponer(Relacion r)
            {
                DependenciasFuncionales dfs = r.obtenerRecubrimientoMinimal(); 
               
                /* Buscar la primera dependencia funcional cuyo determinante no sea
                superclave 
                */
                Iterator<DependenciaFuncional> it = dfs.iterator();
                DependenciaFuncional df = it.next();
                while(r.esSuperClave(df.obtenerDeterminante()))
                    df = it.next();
                
                /*
                Si X -> Y es la df que no permite a la relación ser FNBC, descomponemos
                en dos relaciones: Una con los atributos X u Y, y con la DF X -> Y,
                y otra con todos los atributos de la relación menos Y. (pueden perderse dfs)
                */
                DependenciasFuncionales dfsA = new DependenciasFuncionales();
                dfsA.insertar(df);
                
                Descriptor attrsA = new Descriptor();
                attrsA.insertar(df.obtenerDeterminante().union(df.obtenerDeterminado()));
                
                Descriptor attrsB = new Descriptor();
                attrsB.insertar(r.obtenerAtributos());
                attrsB.eliminar(df.obtenerDeterminado());
                
                DependenciasFuncionales dfsB = new DependenciasFuncionales();
                
                for(DependenciaFuncional df2 : dfs)
                    if(df2.obtenerDeterminante().esContenido(attrsB) && df2.obtenerDeterminado().esContenido(attrsB))
                        dfsB.insertar(df2);
                
                return new Descomposicion(r, attrsA, dfsA, attrsB, dfsB);
            }
            
            @Override
            public String toString()
            {
                return "3FN";
            }
        };
 
    public static FormaNormal FNBC = new FormaNormal(FN3) 
        { 
            @Override
            public boolean esNormalizada(Relacion r) 
            {
                DependenciasFuncionales dfs = r.obtenerRecubrimientoMinimal();
                if(dfs.esVacio())
                    return true;
                
                /* buscamos una df cuyo determinante no es superclave. Si existe
                alguna df que cumpla esa condición, la relación no está en FNBC.
                */
                Iterator<DependenciaFuncional> it = dfs.iterator();
                DependenciaFuncional df = it.next();
                while(it.hasNext() && r.esSuperClave(df.obtenerDeterminante()))
                    df = it.next();
                return r.esSuperClave(df.obtenerDeterminante());
            }

            @Override
            public Descomposicion descomponer(Relacion r)
            {
                return null;
            }
            
            @Override
            public String toString()
            {
                return "FNBC";
            }
        };
}
