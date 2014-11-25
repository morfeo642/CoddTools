/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package coddtools.normalizacion;

import coddtools.normalizacion.util.Conjunto;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**
 *
 * @author linux1
 */
public class CoddTools {
    
    public static DependenciasFuncionales introducirDependencias(Scanner scan) throws IllegalArgumentException
    {
        DependenciasFuncionales dfs = new DependenciasFuncionales();
        
        String str = scan.nextLine();
        while(!str.isEmpty())
        {
            dfs.insertar(DependenciaFuncional.fromString(str));
            str = scan.nextLine();
        }
        return dfs;
    }
    
    public static Descriptor introducirDescriptor(Scanner scan) throws IllegalArgumentException
    {
        return Descriptor.fromString(scan.nextLine());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        try(Scanner scan = new Scanner(System.in)) {
            
            Relacion r = new Relacion("R", introducirDescriptor(scan), introducirDependencias(scan));
            
            
            Conjunto<Relacion> finales = new DescomposicionRecursiva(r, FormaNormal.FNBC, true, true).obtenerRelacionesFinales();
            for(Relacion hija : finales)
                System.out.println(hija + " && " + hija.obtenerFormaNormal()); 
            /*
            DependenciasFuncionales dfs = introducirDependencias(scan);
            System.out.println(dfs.esCompleta(dfs.iterator().next())); */
            
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace(System.out);
        }
        
        
    }
    
}
