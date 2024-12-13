package gace.vista;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DatosUtil {
    private Scanner scanner;

    public DatosUtil() {
        this.scanner = new Scanner(System.in);
    }

    public int leerEntero(int maximo, String mensaje) {
        do {
            if(!mensaje.isEmpty()) {
                System.out.print(mensaje);
            }
            try{
                int valor = 0;
                valor = scanner.nextInt();
                scanner.nextLine();
                if (valor >= 0 && valor <= maximo) {
                    return valor;
                }
                System.out.println("Error: Valor fuera de rango. Debe ser un número entre 0 y " + maximo + ".");
            } catch (InputMismatchException e) {
                System.err.println("Error: Entrada invalida. Por favor, ingresa un numero.");
                if(salir()==-1) {
                    return -1;
                }
            }
        } while (true);
    }
    public int leerEntero(int maximo, int minimo, String mensaje) {
        do {
            if(!mensaje.isEmpty()){
                System.out.print(mensaje);
            }
            try{
                int valor = 0;
                valor = scanner.nextInt();
                scanner.nextLine();
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("Error: Valor fuera de rango. Debe ser un número entre "+ minimo +" y " + maximo + ".");
                return leerEntero(maximo, minimo, mensaje);
            } catch (InputMismatchException e) {
                System.out.println("Error: Entrada invalida. Por favor, ingresa un numero.");
                int opcion = salir();
                if(opcion == -1) {
                    return -1;
                }else{
                    return leerEntero(maximo, minimo, mensaje);
                }
            }
        } while (true);
    }


    public String devString() {
        do {
            try{
                String valor = scanner.nextLine();
                if(!valor.isEmpty()){
                    return valor;
                }
                System.out.println("Error: No puedes dejar este campo vacío.");
                if (salir()==-1) {
                    return null;
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Entrada invalida. Por favor, ingresa un numero.");
                if(salir()==-1) {
                    return null;
                }
            }
        } while (true);
    }
    public String devString(String mensaje) {
        do {
            try{
                System.out.println(mensaje);
                String valor = scanner.nextLine();
                if(!valor.isEmpty()){
                    return valor;
                }
                System.out.println("Error: No puedes dejar este campo vacío.");
                if (salir()==-1) {
                    return null;
                }
            } catch (InputMismatchException e) {
                System.err.println("Error: Entrada invalida. Por favor, ingresa un numero.");
                if(salir()==-1) {
                    return null;
                }
            }
        } while (true);
    }

    public int salir() {
        System.out.println("Desea salir? 1-Si / 2-no");
        int opcion = scanner.nextInt();
        return opcion == 1 ? -1 : 0;
    }

    public int mostrarMenu() {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Socios");
        System.out.println("2. Excursiones");
        System.out.println("3. Inscripciones");
        System.out.println("4. TEST - PROBAR CONEXION");
        System.out.println("0. Salir");
        int opcion = leerEntero(4, "");
        return opcion;
    }

    public int menuSocios() {
        System.out.println("Seleccione una opción:");
        System.out.println("1. Nuevo socio");
        System.out.println("2. Mostrar Socios");
        System.out.println("3. Eliminar Socio");
        System.out.println("4. Buscar Socio");
        System.out.println("5. Modificar Seguro");
        System.out.println("6. Modificar Federacion");
        System.out.println("7. Calcular Cuota Socios");
        System.out.println("0. Salir");
        int opcion = leerEntero(7, "");
        return opcion;
    }

    public int menuExcursiones(){
        System.out.println("Seleccione una opción:");
        System.out.println("1. Nueva Excursión");
        System.out.println("2. Mostrar Excursiones");
        System.out.println("3. Eliminar Excursión");
        System.out.println("4. Buscar Excursión");
        System.out.println("5. Cancelar Excursión");
        System.out.println("0. Salir");
        int opcion = leerEntero(5, "");
        return opcion;
    }

    public int menuInscripciones(){
        System.out.println("Seleccione una opción:");
        System.out.println("1. Nueva Inscripción");
        System.out.println("2. Mostrar Inscripciones por excursión");
        System.out.println("3. Eliminar Inscripción");
        System.out.println("4. Buscar Inscripción");
        System.out.println("0. Salir");
        int opcion = leerEntero(4, "");
        return opcion;
    }

    public int asistente(){
        System.out.println("Deseas ver los socios que no han sido inscritos en ninguna excursión?");
        System.out.println("1. Sí");
        System.out.println("2. No");
        int opcion = leerEntero(2, "");
        scanner.nextLine();
        return opcion;
    }

    public boolean confirmar(String mensaje){
        System.out.println(mensaje);
        System.out.println("1. Sí");
        System.out.println("2. No");
        int opcion = leerEntero(2, "");
        return opcion == 1;
    }

    public double leerDouble(int minimo, String mensaje) {
        if(!mensaje.isEmpty()){
            System.out.print(mensaje);
        }
        do {
            try{
                double valor = scanner.nextDouble();
                scanner.nextLine();
                if (valor >= minimo) {
                    return valor;
                }
                System.out.println("Error: Valor fuera de rango. Debe ser superior a " + minimo + ".");
            } catch (InputMismatchException e) {
                System.err.println("Error: Entrada invalida. Por favor, ingresa un numero.");
                if(salir()==-1) {
                    return -1;
                }
                leerDouble(minimo, mensaje);
            }
        } while (true);
    }

    public void cerrarTeclado(){
        scanner.close();
    }

    public void mostrarError(String mensaje){
        System.err.println(mensaje);
    }

    public int pedirOpcion(String pregunta, String opcionA, String opcionB){
        System.out.println(pregunta);
        System.out.println("1. " + opcionA);
        System.out.println("2. " + opcionB);
        return leerEntero(2, "");
    }
    public int pedirOpcion(String pregunta, String opcionA, String opcionB, String opcionC){
        System.out.println(pregunta);
        System.out.println("1. " + opcionA);
        System.out.println("2. " + opcionB);
        System.out.println("3. " + opcionC);
        return leerEntero(3, "");
    }

    public void mostrarInfo(String mensaje){
        System.out.println(mensaje);
    }

}
