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

package coddtools.util;

/**
 * Clase con utilidades para trabajar con factoriales.
 * @author victor
 */
public class Factoriales {
    /**
     * Calcula el factorial de un número.
     * @param n Número mayor o igual que 0.
     * @return Devuelve n!
     */
    public static long calcularFactorial(int n)
    {
        long r = 1;
        while(n > 1)
            r *= n--;
        return r;
    }
    
    /**
     * Calcula la división entre dos factoriales.
     * @param n
     * @param m
     * n debe ser mayor o igual que m.
     * @return Devuelve n! / m!
     */
    public static long calcularDivisionFactorial(int n, int m)
    {
        long r = 1;
        int q = 0;
        while (q < (n - m))
            r *= (n - q++);
        return r;
    }
    
    /**
     * Calcula un coeficiente binomial (m k)
     * m, k deben ser mayores o iguales que 0, y además, m >= k.
     * @return Devuelve el coeficiente binomial (m k)
     */
    public static long calcularCoeficienteBinomial(int m, int k)
    {
        long r = 1;
        int s = 0; 
        while(s < k)
        {
            r *= ((m - s) / (s + 1));
            s++;
        }
        return r;
    }
}
