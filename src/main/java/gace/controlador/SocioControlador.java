package gace.controlador;


import gace.modelo.*;
import gace.modelo.dao.DAOFactory;
import gace.vista.VistaSocios;
import gace.vista.DatosUtil;

import javafx.scene.control.*;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

public class SocioControlador {
    private VistaSocios vistaSocios;
    private DatosUtil datosUtil;
    private int fakeID = 1;
    @FXML
    private TableView<Socio> tablaSocios;

    @FXML
    private TableColumn<Socio, Integer> columnaID;

    @FXML
    private TableColumn<Socio, String> columnaNombre;

    @FXML
    private TableColumn<Socio, String> columnaApellido;

    private ObservableList<Socio> listaSocios;

    public void initialize() {
        // Configurar las columnas para que apunten a los getters de la clase Socio
        columnaID.setCellValueFactory(new PropertyValueFactory<>("idSocio"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));

        // Cargar los socios falsos en la tabla
        cargarTablaSocios();
    }

    private void cargarTablaSocios() {
        // Crear una lista de socios falsos
        listaSocios = FXCollections.observableArrayList(
                new SocioEstandar(fakeID,"CARLES","JULIA","303030A", new Seguro(1, true,40)),
                new SocioEstandar(fakeID= fakeID +1,"ALBERT","TEST","303030A", new Seguro(1, true,40)),
                new SocioEstandar(fakeID= fakeID +1,"ENRIQUE","TEST","303030A", new Seguro(1, true,40))
                );

        // Vincular la lista de socios con la TableView
        tablaSocios.setItems(listaSocios);
    }
    @FXML
    private void handleRegistrar(ActionEvent event) {
        // Crear los campos de entrada para el nombre, apellido y tipo de socio
        TextField nombreField = new TextField();
        TextField apellidoField = new TextField();
        ComboBox<String> tipoSocioCombo = new ComboBox<>();
        tipoSocioCombo.getItems().addAll("ESTÁNDAR", "FEDERADO", "INFANTIL");

        // Crear el diálogo
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Registrar Socio");
        dialog.setHeaderText("Por favor ingrese los datos del socio:");

        // Crear los botones para aceptar o cancelar el registro
        ButtonType buttonTypeOk = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        // Añadir los elementos (campos de texto y combo box) al panel del diálogo
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(new Label("Nombre:"), nombreField,
                new Label("Apellido:"), apellidoField,
                new Label("Tipo de Socio:"), tipoSocioCombo);
        dialog.getDialogPane().setContent(vbox);

        // Al hacer clic en "Registrar", se captura la información y se procesa
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                String tipoSocio = tipoSocioCombo.getValue();

                // Validar que los campos no estén vacíos
                if (nombre.isEmpty() || apellido.isEmpty() || tipoSocio == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, complete todos los campos.");
                    alert.show();
                    return null;
                }
                // Devolver la información formateada (como en tu código original)
                return tipoSocio + "," + nombre + "," + apellido;
            }
            return null;
        });

        // Mostrar el diálogo y esperar la respuesta
        dialog.showAndWait().ifPresent(result -> {
            if (result != null) {
                // Procesar los datos del nuevo socio
                String[] datosSocio = result.split(",");
                String tipoSocio = datosSocio[0];
                String nombre = datosSocio[1];
                String apellido = datosSocio[2];

                // Crear el nuevo socio en función del tipo
                Socio nuevoSocio = null;
                switch (tipoSocio) {
                    case "ESTÁNDAR":
                        nuevoSocio = new SocioEstandar(fakeID= fakeID +1, nombre, apellido, "NIF123", new Seguro(1, true, 40));
                        break;
                    case "FEDERADO":
                        nuevoSocio = new SocioFederado(nombre,apellido,"NIF456", new Federacion("020","0202"));
                        break;
                    case "INFANTIL":
                        nuevoSocio = new SocioInfantil(fakeID= fakeID +1, nombre, apellido, 123);
                        break;
                }

                if (nuevoSocio != null) {
                    // Añadir el nuevo socio a la lista
                    listaSocios.add(nuevoSocio);

                    // Actualizar la TableView (esto se hace automáticamente con ObservableList)
                    tablaSocios.setItems(listaSocios);
                }
            }
        });
    }
    @FXML
    private void handleEliminar(ActionEvent event) {
        // Crear un cuadro de entrada para que el usuario ingrese el ID del socio a eliminar
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Socio");
        dialog.setHeaderText("Por favor, ingrese el ID del socio a eliminar:");
        dialog.setContentText("ID del Socio:");

        // Mostrar el diálogo y obtener el ID ingresado por el usuario
        dialog.showAndWait().ifPresent(idInput -> {
            // Intentamos convertir el ID ingresado a un número
            try {
                int idSocio = Integer.parseInt(idInput);  // Convertir el ID ingresado a un entero

                // Buscar el socio en la lista usando el ID
                Socio socioAEliminar = null;
                for (Socio socio : listaSocios) {
                    if (socio.getIdSocio() == idSocio) {
                        socioAEliminar = socio;
                        break;
                    }
                }

                // Si encontramos el socio con el ID, lo eliminamos
                if (socioAEliminar != null) {
                    listaSocios.remove(socioAEliminar);  // Eliminar de la lista
                    tablaSocios.setItems(listaSocios);  // Actualizar la tabla
                    datosUtil.mostrarError("Socio eliminado El socio con ID " + idSocio + " ha sido eliminado.");
                } else {
                    // Si no se encuentra el socio
                    datosUtil.mostrarError("Socio no encontrado No se encontró un socio con el ID " + idSocio + ".");
                }

            } catch (NumberFormatException e) {
                // Si el ID no es un número válido
                datosUtil.mostrarError("ID no válido El ID ingresado no es válido.");
            }
        });
    }


    public SocioControlador(VistaSocios vistaSocios) {
        this.vistaSocios = vistaSocios;
        this.datosUtil = new DatosUtil();
    }

    public SocioControlador() {
        this.vistaSocios = new VistaSocios();
        this.datosUtil = new DatosUtil();
    }

    public int nouSoci(){
        String strSocio = vistaSocios.formSocio();
        if (strSocio == null) {
            datosUtil.mostrarError("Error al crear el socio");
            return 0;
        }
        String[] datosSocio = strSocio.split(",");
        if (datosSocio.length < 3) {
            datosUtil.mostrarError("Datos del socio incompletos");
            return 0;
        }
        int tipoSocio = Integer.parseInt(datosSocio[0]);
        int id = 0;
        switch (tipoSocio) {
            //EST
            case 1:
                SocioEstandar socioEst = nouSociEstandar(datosSocio[1], datosSocio[2]);
                if (socioEst == null) {
                    datosUtil.mostrarError("Error al crear el socio estándar");
                    return 0;
                }
                DAOFactory.getSocioDao().insertar(socioEst);
                break;
            //FED
            case 2:
                SocioFederado socioFed = nouSociFederado(datosSocio[1], datosSocio[2]);
                if (socioFed == null) {
                    datosUtil.mostrarError("Error al crear el socio estándar");
                    return 0;
                }
                DAOFactory.getSocioDao().insertar(socioFed);
                break;
            //INF
            case 3:
                SocioInfantil socioInf = nouSociInfantil(datosSocio[1], datosSocio[2]);
                if (socioInf == null) {
                    datosUtil.mostrarError("Error al crear el socio infantil");
                    return 0;
                }
                DAOFactory.getSocioDao().insertar(socioInf);
                vistaSocios.mostrarSocio(socioInf.toString());
                break;
            default:
                datosUtil.mostrarError("Tipo de socio no válido");
                return 0;
        }
        return id;
    }

    public Socio crearSocio(){
        int id = nouSoci();
        return DAOFactory.getSocioDao().buscar(id);
    }


    public SocioEstandar nouSociEstandar(String nombre,String apellido){
        String nif = vistaSocios.formNif();
        if(nif == null){
            datosUtil.mostrarError("Nif no válido.");
            return null;
        }
        if(existeNif(nif)){
            datosUtil.mostrarError("Nif ya existe.");
            return null;
        }
        Seguro seg = nuevoSeg();
        if(seg == null){
            datosUtil.mostrarError("Seguro no válido.");
            return null;
        }
        DAOFactory.getSeguroDao().insertar(seg);
        return new SocioEstandar( nombre, apellido, nif, seg);
    }

    public SocioFederado nouSociFederado(String nombre, String apellido){
        String nif = vistaSocios.formNif();
        if(nif == null){
            datosUtil.mostrarError("Nif no válido.");
            return null;
        }
        if(existeNif(nif)){
            datosUtil.mostrarError("Nif ya existe.");
            return null;
        }
        Federacion fed = pedirFed();
        if(fed == null){
            datosUtil.mostrarError("Federación no válida.");
            return null;
        }
        //DAOFactory.getFederacionDao().insertar(fed);
        return new SocioFederado(nombre, apellido, nif, fed);
    }

    public boolean existeNif(String nif){
        return DAOFactory.getSocioDao().hayNif(nif);
    }

    public SocioInfantil nouSociInfantil(String nombre, String apellido){
        int noTutor = vistaSocios.formTutor();
        if(noTutor == 0){
            return null;
        }
        if(!buscarTutor(noTutor)){
            return null;
        }
        return new SocioInfantil(nombre, apellido, noTutor);
    }



//    public SocioInfantil nouSociInfantil(String nombre, String apellido, int noTutor) {
//        if(noTutor == 0){
//            return null;
//        }
//        if(!buscarTutor(noTutor)){
//            return null;
//        }
//        return new SocioInfantil(nombre, apellido, noTutor);
//    }

    public boolean buscarTutor(int noTutor) {
        Socio socio = DAOFactory.getSocioEstandarDao().buscar(noTutor);
        if(socio == null){
            socio = DAOFactory.getSocioFederadoDao().buscar(noTutor);
            if(socio == null){
                return false;
            }
        }
        return true;
    }

    public boolean mostrarSocios(int mostrarFiltro, int filtro) {
        int opcionSocios = 0;
        if(mostrarFiltro == 1){
            opcionSocios = vistaSocios.requerirFiltro();
        }else {
            opcionSocios = filtro;
        }
        switch (opcionSocios) {
            case 1:
                //error de tipos de socio amb el socioEstandarDao
                //list = DAOFactory.getSocioEstandarDao().listar();
                List<SocioEstandar> list = DAOFactory.getSocioEstandarDao().listar();
                if(list == null){
                    datosUtil.mostrarError("No hay socios estándar");
                    return false;
                }
                for(Socio socio : list) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 2:
                List<SocioFederado> listFed = DAOFactory.getSocioFederadoDao().listar();
                if(listFed == null){
                    datosUtil.mostrarError("No hay socios federados");
                    return false;
                }
                for(Socio socio : listFed) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 3:
                List<SocioInfantil> listInf = DAOFactory.getSocioInfantilDao().listar();
                if(listInf == null){
                    datosUtil.mostrarError("No hay socios infantiles");
                    return false;
                }
                for(Socio socio : listInf) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 4:
                List<Socio> todos = DAOFactory.getSocioDao().listar();
                if(todos == null){
                    datosUtil.mostrarError("No hay socios");
                    return false;
                }
                for(Socio socio : todos) {
                    vistaSocios.mostrarSocio(socio.toString());
                }
                break;
            case 0:
                break;
            default:
                datosUtil.mostrarError("Opción no válida. Intente de nuevo.");
        }
        return true;
    }

    public Socio buscarSocio(int noSocio) {
        return DAOFactory.getSocioDao().buscar(noSocio);
    }


    public Federacion pedirFed(){
        int accion = datosUtil.pedirOpcion("¿Desea seleccionar una federación ya existente o crear una nueva?", "Seleccionar", "Crear nueva");
        if(accion == -1){
            return null;
        }
        if(accion == 1){
            Federacion fed = seleccionarFed();
            if(fed != null){
                return fed;
            }
        }
        return nuevaFed();
    }

    public Federacion seleccionarFed(){
        ArrayList<Federacion> fedes = null;
        fedes = DAOFactory.getFederacionDao().listar();
        if(fedes.isEmpty()){
            datosUtil.mostrarError("No hay federaciones");
            return null;
        }else{
            for(Federacion fed : fedes){
                vistaSocios.mostrarSocio(fed.toString());
            }
            String codigo = datosUtil.devString("Introduce el código de la federación");
            if(codigo == null){
                return null;
            }
            return DAOFactory.getFederacionDao().buscar(codigo);
        }
    }
    public Federacion nuevaFed(){
        String fed = vistaSocios.formFederacion();
        String[] datosFed = fed.split(",");
        if (datosFed.length < 2) {
            datosUtil.mostrarError("Datos de la federación incompletos");
            return null;
        }
        Federacion federacion = new Federacion(datosFed[0], datosFed[1]);
        DAOFactory.getFederacionDao().insertar(federacion);
        return federacion;
    }

    public Seguro nuevoSeg(){
        String seg = vistaSocios.formSeguro();
        if (seg == null) {
            return null;
        }
        String[] datosSeg = seg.split(",");
        if (datosSeg.length < 2) {
            datosUtil.mostrarError("Datos del seguro incompletos");
            return null;
        }
        boolean tipo = Integer.parseInt(datosSeg[0]) == 1;
        return new Seguro(tipo, Double.parseDouble(datosSeg[1]));
    }

//    public boolean seleccionarSocio(ArrayList<Socio> socios){
//        String codigo = vistaSocios.pedirSocio();
//        for(Socio socio : socios){
//            if(socio.getNoSocio().equals(codigo)) {
//                vistaSocios.mostrarSocio("Es este el socio que desea eliminar " + socio.toString() + "?");
//                if (vistaSocios.confirmar()) {
//                    listaSocios.getListaSocios().remove(socio);
//                    datosUtil.mostrarError("Socio eliminado");
//                    return true;
//                }
//                return false;
//            }
//        }
//        return false;
//    }

    public boolean pedirSocio(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        vistaSocios.mostrarSocio(socio.toString());
        return true;
    }

    public boolean eliminarSocio(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        vistaSocios.mostrarSocio(socio.toString());
        List<Inscripcion> insc = null;
        if(socio instanceof SocioEstandar) {
            insc = DAOFactory.getInscripcionDao().ListarXSocioEst(socio);
        }else if(socio instanceof SocioFederado){
            insc = DAOFactory.getInscripcionDao().ListarXSocioFed(socio);
        }else{
            insc = DAOFactory.getInscripcionDao().ListarXSocioInf(socio);
        }
        if(insc != null){
            datosUtil.mostrarError("No se puede eliminar el socio, tiene inscripciones");
            return false;
        }
        if(vistaSocios.confirmar("¿Está seguro de que desea eliminar este socio?")){
            DAOFactory.getSocioDao().eliminar(socio.getIdSocio());
            datosUtil.mostrarError("Socio eliminado");
            return true;
        }
        return false;
    }

    public Socio obtenerSocio(){
        int formaBuscar = datosUtil.pedirOpcion("¿Como desea buscar?", "NIF", "Número de socio");
        int opcion = datosUtil.pedirOpcion("Deseas disponer de ayudas?","Sí","No");
        if(opcion == 1){
            mostrarSocios(0,4);
        }
        if (formaBuscar == -1) {
            return null;
        }
        Socio socio = null;
        if(formaBuscar == 1){
            socio = buscarNIF();
        }else{
            socio = buscarNoSocio();
        }
        if(socio == null){
            return null;
        }
        return socio;
    }

    public Socio buscarNoSocio(){
        int noSocio = vistaSocios.pedirSocio();
        if(noSocio == 0){
            return null;
        }
        Socio socio = DAOFactory.getSocioDao().buscar(noSocio);
        if(socio == null){
            datosUtil.mostrarError("Socio no encontrado");
            return null;
        }
        return socio;
    }

    public Socio buscarNIF(){
        String nif = vistaSocios.pedirNif();
        if(nif == null){
            return null;
        }
        Socio socio = DAOFactory.getSocioDao().buscar(nif);
        if(socio == null){
            datosUtil.mostrarError("Socio no encontrado");
            return null;
        }
        return socio;
    }

    public boolean modificarSeguro(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        if(!(socio instanceof SocioEstandar)){
            datosUtil.mostrarError("Error Socio no Estandar");
            return false;
        }
        String strSeg = vistaSocios.formSeguro();
        if (strSeg == null) {
            return false;
        }
        String[] datosSeg = strSeg.split(",");
        if (datosSeg.length < 2) {
            datosUtil.mostrarError("Datos del seguro incompletos");
            return false;
        }
        boolean tipo = Integer.parseInt(datosSeg[0]) == 1;
        Seguro seg = new Seguro(tipo, Double.parseDouble(datosSeg[1]));
        DAOFactory.getSeguroDao().insertar(seg);
        ((SocioEstandar) socio).setSeguro(seg);
        DAOFactory.getSocioEstandarDao().modificar((SocioEstandar) socio);
        return true;
    }
    public boolean modificarFederacion(){
        Socio socio = obtenerSocio();
        if(socio == null){
            return false;
        }
        if(!(socio instanceof SocioFederado)){
            datosUtil.mostrarError("Error socio no Federado");
            return false;
        }
        Federacion fed = pedirFed();
        ((SocioFederado) socio).setFederacion(fed);
        DAOFactory.getSocioFederadoDao().modificar((SocioFederado) socio);
        return true;
    }
}
