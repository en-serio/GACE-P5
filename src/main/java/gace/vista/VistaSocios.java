package gace.vista;

import gace.modelo.SocioEstandar;
import gace.modelo.SocioFederado;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class VistaSocios {
    private Scanner scanner;
    private DatosUtil datosUtil;

    public VistaSocios() {
        this.scanner = new Scanner(System.in);
        this.datosUtil = new DatosUtil();
    }



    public String formSocio() {
        System.out.println("Ingrese el nombre: ");
        String nombre = datosUtil.devString();
        System.out.println("Ingrese el apellido: ");
        String apellido = datosUtil.devString();
        System.out.println("Ingrese el tipo de socio \n1-ESTÁNDAR \n2-FEDERADO \n3-INFANTIL");
        int tipoSocio = datosUtil.leerEntero(3,1, " ");
        return tipoSocio  + "," + nombre + "," + apellido;
    }

    public void mostrarSocio(String socio) {
        System.out.println(socio);
    }

    public int requerirFiltro() {
        int opcion = -1;
        try {
            System.out.println("¿Qué socios quieres ver?");
            System.out.println("1. Socios estandar");
            System.out.println("2. Socios federados");
            System.out.println("3. Socios infantiles");
            System.out.println("4. Todos los socios");
            System.out.println("0. Salir");
            opcion = scanner.nextInt();

            if (opcion < 0 || opcion > 5) {
                throw new IllegalArgumentException("Opción no valida. Debe ser un número entre 0 y 5.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Entrada invalida. Por favor, ingresa un numero.");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return opcion;
    }

    public String formNif(){
        System.out.print("Ingrese el NIF: ");
        String nif = datosUtil.devString();
        return nif;
    }

    public int formTutor(){
        System.out.print("Ingrese el número de tutor: ");
        return datosUtil.leerEntero(99999,"");
    }

    public String formFederacion(){
        System.out.print("Ingrese el código de la federación: ");
        String codigoFederacion = datosUtil.devString();
        System.out.print("Ingrese el nombre de la federación: ");
        String nombreFederacion = datosUtil.devString();
        return codigoFederacion + "," + nombreFederacion;
    }

    public String formSeguro(){
        int tipoSeguro = datosUtil.leerEntero(2,1, "Quin tipus de seguro: 1-COMPLETO  2-ESTÁNDAR : ");
        if (tipoSeguro == -1) {
            return null;
        }
        System.out.println("Preu del seguro:");
        double precioSeguro = datosUtil.leerDouble(0, "");
        if (precioSeguro == -1) {
            return null;
        }

        return tipoSeguro + "," + precioSeguro;
    }

    public int pedirSocio(){
        System.out.print("Ingrese el número de socio: ");
        return datosUtil.leerEntero(99999,"");
    }
    public String pedirNif(){
        System.out.print("Ingrese el nif del socio: ");
        return datosUtil.devString();
    }

    public void noTutor(){
        System.out.print("No existe ningún tutor con ese número de socio.");
    }

    public boolean confirmar(String mensaje){
        return datosUtil.confirmar(mensaje);
    }

}
