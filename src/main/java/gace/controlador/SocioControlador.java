package gace.controlador;


import gace.modelo.*;
import gace.modelo.dao.DAOFactory;
import gace.vista.VistaSocios;
import gace.vista.DatosUtil;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.FXML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import static javafx.geometry.Pos.CENTER;

/**
 * -fet Canviar eventlistener per doble click com a excursion
 * mostrar detalle per socio Federado i per socio Infantil Com el de Socio estandar
 * Posar be el mostrar socio
 * implementar les funcions del todo.
 */

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

    @FXML
    private TableColumn<Socio, String> columnaTipo;

    private ObservableList<Socio> listaSocios;

    public void initialize() {
        // Configurar las columnas para que apunten a los getters de la clase Socio
        columnaID.setCellValueFactory(new PropertyValueFactory<>("idSocio"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoSocio"));

        // Cargar los socios falsos en la tabla
        cargarTablaSocios();
    }

    private void cargarTablaSocios() {
        List<Socio> socios = DAOFactory.getSocioDao().listar();
        listaSocios = FXCollections.observableArrayList(socios);

        tablaSocios.setItems(listaSocios);
        tablaSocios.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Socio soc = tablaSocios.getSelectionModel().getSelectedItem();
                mostrarDetalle(soc);
            }
        });
    }

    private void setLabelStyle(Label... labels) {
        for (Label label : labels) {
            label.setStyle("-fx-background-color: lightgreen; -fx-padding: 10; -fx-alignment: center;");
            label.setMaxWidth(Double.MAX_VALUE);
            label.setAlignment(CENTER);
            //Pos.CENTER
        }
    }
    public void mostrarDetalle(Socio soc){
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Detalles Socio " + soc.getIdSocio());
        Button cancelarExcursio = new Button("Eliminar socio");
        cancelarExcursio.setOnAction(event -> {
            borrarSocio(soc);
            modalStage.close();

        });
        GridPane grid = null;
        Label excLabel = null;
        List<Inscripcion> insc = null;
        //VBox o posem StackPane?
        if(soc instanceof SocioEstandar){
            grid = mostrarSocioEst((SocioEstandar) soc);
            insc = DAOFactory.getInscripcionDao().ListarXSocioEst(soc);
        }else if(soc instanceof SocioFederado){
            grid = mostrarSocioFed((SocioFederado) soc);
            insc = DAOFactory.getInscripcionDao().ListarXSocioFed(soc);
        }else if(soc instanceof SocioInfantil){
            grid = mostrarSocioInf((SocioInfantil) soc);
            insc = DAOFactory.getInscripcionDao().ListarXSocioInf(soc);
        }
        if(grid == null){
            return;
        }
        if(insc == null){
            excLabel = new Label("Excursiones Inscritas: 0");
        }else{
            excLabel = new Label("Excursiones Inscritas: " + insc.size());
        }

        excLabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                //TODO PER FER
                System.out.println("Mostrar insc");
                mostrarInsc(soc);
                cargarTablaSocios();
            }
        });

        Button crearInscripcion = new Button("Crear Inscripción");
        crearInscripcion.setOnAction(event -> {
            //TODO PONER BIEN
            System.out.println("Crear Inscripción");
            //crearInscripcion(soc);
            modalStage.close();
        });

        Button modificarExcursio = new Button("Modificar Socio");
        modificarExcursio.setOnAction(event -> {
            handleRegistrar(soc);
            modalStage.close();
            cargarTablaSocios();
        });
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        for (int i = 0; i < 6; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(16.67);
            grid.getColumnConstraints().add(col);
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        HBox hbox = new HBox(10, excLabel, spacer);
        hbox.setAlignment(CENTER);
        hbox.setMaxWidth(Double.MAX_VALUE);

        grid.add(hbox, 0, 3, 6, 1);

        cancelarExcursio.setMaxWidth(Double.MAX_VALUE);
        crearInscripcion.setMaxWidth(Double.MAX_VALUE);
        modificarExcursio.setMaxWidth(Double.MAX_VALUE);

        HBox buttonsBox = new HBox(10, crearInscripcion, modificarExcursio, cancelarExcursio);
        buttonsBox.setAlignment(CENTER);
        HBox.setHgrow(cancelarExcursio, Priority.ALWAYS);
        HBox.setHgrow(crearInscripcion, Priority.ALWAYS);
        HBox.setHgrow(modificarExcursio, Priority.ALWAYS);
        buttonsBox.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        grid.add(buttonsBox, 0, 4, 6, 1);

        StackPane root = new StackPane(grid);
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Scene scene = new Scene(root,400,250);
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }

    private GridPane mostrarSocioEst(SocioEstandar soc){
        GridPane grid = new GridPane();

        Label idSoc = new Label("ID: " + soc.getIdSocio());

        Label nombreSoc = new Label("Nombre: " + soc.getNombre());

        Label apellidoSoc = new Label("Apellido: " + soc.getApellido());
        Label segudoSoc = new Label("Tipo de Seguro: " + (soc.getSeguro().isTipo() ? "Completo" : "Estándar") + " - (" + soc.getSeguro().getPrecio()+")");

        Label nifSoc = new Label("Nif: "+ soc.getNif());
        grid.add(idSoc, 0, 0, 3,1);
        grid.add(nifSoc, 3, 0, 3,1);

        setLabelStyle(nombreSoc, apellidoSoc);
        grid.add(nombreSoc, 0, 1,3,1);
        grid.add(apellidoSoc, 3, 1,3,1);

        segudoSoc.setStyle("-fx-background-color: lightgreen; -fx-padding: 20;");
        segudoSoc.setMaxWidth(Double.MAX_VALUE);
        segudoSoc.setAlignment(CENTER);
        grid.add(segudoSoc, 0, 2, 6, 2);
        return grid;
    }
    private GridPane mostrarSocioFed(SocioFederado soc){
        GridPane grid = new GridPane();
        Label idSoc = new Label("ID: " + String.valueOf(soc.getIdSocio()));

        Label nomSoc = new Label("Nombre: " + soc.getNombre());

        Label cgnomSoc = new Label("Apellido: " + soc.getApellido());

        Label nifSoc = new Label("Nif: " + soc.getNif());

        Label segSoc = new Label("Tipo de Seguro" + soc.getFederacion().getCodigo() + " - " + soc.getFederacion().getNombre());
        return grid;
    }
    private GridPane mostrarSocioInf(SocioInfantil soc){
        GridPane grid = new GridPane();
        grid.add(new Label("ID: " ), 0, 0);
        grid.add(new Label(String.valueOf(soc.getIdSocio())), 1, 0);

        grid.add(new Label("Nombre: "), 0, 1);
        grid.add(new Label(soc.getNombre()), 1, 1);

        grid.add(new Label("Apellido: "), 0, 2);
        grid.add(new Label(soc.getApellido()), 1, 2);

        grid.add(new Label("Tutor: "), 0, 3);
        grid.add(new Label(String.valueOf(soc.getNoTutor())), 1, 3);
        grid.add(new Label(""), 0, 4);
        return grid;
    }

    @FXML
    private void handleBuscar(ActionEvent event) {
        // Crear un cuadro de entrada para que el usuario ingrese el ID del socio a buscar
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Socio");
        dialog.setHeaderText("Por favor, ingrese el ID del socio a buscar:");
        dialog.setContentText("ID del Socio:");

        // Mostrar el diálogo y obtener el ID ingresado por el usuario
        dialog.showAndWait().ifPresent(idInput -> {
            try {
                // Convertir el ID ingresado a un número
                int idSocio = Integer.parseInt(idInput);

                // Buscar el socio en la lista usando el ID
                Socio socioEncontrado = listaSocios.stream()
                        .filter(socio -> socio.getIdSocio() == idSocio)
                        .findFirst()
                        .orElse(null);

                // Mostrar los detalles del socio si se encuentra
                if (socioEncontrado != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Socio Encontrado");
                    alert.setHeaderText("Detalles del Socio:");
                    alert.setContentText(
                            "ID: " + socioEncontrado.getIdSocio() + "\n" +
                                    "Nombre: " + socioEncontrado.getNombre() + "\n" +
                                    "Apellido: " + socioEncontrado.getApellido() + "\n" +
                                    "Tipo: " + getTipoSocio(socioEncontrado)
                    );
                    alert.showAndWait();
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

    private void mostrarInsc(Socio soc){

    }

    private void borrarSocio(Socio soc){
        List<Inscripcion> insc = null;
        if(soc instanceof SocioEstandar){
            insc = DAOFactory.getInscripcionDao().ListarXSocioEst(soc);
        }else if(soc instanceof SocioFederado){
            insc = DAOFactory.getInscripcionDao().ListarXSocioFed(soc);
        }else if(soc instanceof SocioInfantil){
            insc = DAOFactory.getInscripcionDao().ListarXSocioInf(soc);
        }
        if(insc == null){
            DAOFactory.getSocioDao().eliminar(soc.getIdSocio());
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "El socio cuenta con inscripciones activas");
            alert.show();
        }
    }

    @FXML
    private void handleRegistrarF(){
        handleRegistrar(null);
    }
    @FXML
    private void handleRegistrar(Socio soc) {
        TextField nombreField = new TextField();
        TextField apellidoField = new TextField();
        TextField nifField = new TextField();
        /* per estandar */
        TextField precioField = new TextField();
        ComboBox<String> tipoSeguro = new ComboBox<>();
        tipoSeguro.getItems().addAll("COMPLETO", "ESTÁNDAR");
        /* per federat */
        TextField codigoField = new TextField();
        TextField nombreFedField = new TextField();
        /* per infantil */
        TextField tutorField = new TextField();
        ComboBox<String> tipoSocioCombo = new ComboBox<>();
        tipoSocioCombo.getItems().addAll("ESTÁNDAR", "FEDERADO", "INFANTIL");

        Dialog<String> dialog = new Dialog<>();
        dialog.setHeight(400);
        dialog.setWidth(500);
        dialog.setTitle("Registrar Socio");
        dialog.setHeaderText("Por favor ingrese los datos del socio:");

        ButtonType buttonTypeOk = new ButtonType("Registrar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

        VBox vbox = new VBox(10);
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(new Label("Nombre:"), nombreField, new Label("Apellido:"), apellidoField);
        VBox infoExtra = new VBox(10);
        if(soc == null) {
            vbox.getChildren().addAll(hbox, new Label("Tipo de Socio:"), tipoSocioCombo);
        }else{
            String value = null;
            if(soc instanceof SocioEstandar){
                tipoSocioCombo.setValue("ESTANDAR");
                value = "ESTANDAR";
            }else if(soc instanceof SocioFederado){
                tipoSocioCombo.setValue("FEDERADO");
                value = "FEDERADO";
            }else{
                tipoSocioCombo.setValue("INFANTIL");
                value = "INFANTIL";
            }
            vbox.getChildren().addAll(hbox, new Label("Tipo: " + value));
        }
        dialog.getDialogPane().setContent(vbox);


        VBox contenido = new VBox(10);
        contenido.getChildren().add(vbox);
        if(soc == null) {
            tipoSocioCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals(oldValue)) {
                    return;
                }
                infoExtra.getChildren().clear();
                switch (newValue) {
                    case "ESTÁNDAR":
                        HBox hboxInfo = new HBox(10);
                        HBox hboxOption = new HBox(10);
                        hboxInfo.getChildren().addAll(new Label("NIF:"), nifField, new Label("Precio:"), precioField);
                        hboxOption.getChildren().addAll(new Label("Tipo de Seguro:"), tipoSeguro);
                        infoExtra.getChildren().addAll(hboxInfo, hboxOption);
                        break;
                    case "FEDERADO":
                        infoExtra.getChildren().addAll(new Label("NIF:"), nifField,
                                new Label("Código Fed:"), codigoField,
                                new Label("Nombre Fed:"), nombreFedField);
                        break;
                    case "INFANTIL":
                        infoExtra.getChildren().addAll(
                                new Label("Tutor:"), tutorField);
                        break;
                }
                contenido.getChildren().add(infoExtra);
            });
        }
        if(soc != null){
            nombreField.setText(soc.getNombre());
            apellidoField.setText(soc.getApellido());

            if(soc instanceof SocioEstandar){
                tipoSocioCombo.setValue("ESTÁNDAR");
                nifField.setText(((SocioEstandar) soc).getNif());
                precioField.setText(String.valueOf(((SocioEstandar) soc).getSeguro().getPrecio()));
                tipoSeguro.setValue(((SocioEstandar) soc).getSeguro().isTipo()? "COMPLETO" : "ESTANDAR");
                HBox hboxInfo = new HBox(10);
                HBox hboxOption = new HBox(10);
                hboxInfo.getChildren().addAll(new Label("NIF:"), nifField, new Label("Precio:"), precioField);
                hboxOption.getChildren().addAll(new Label("Tipo de Seguro:"), tipoSeguro);
                infoExtra.getChildren().addAll(hboxInfo, hboxOption);
            }else if(soc instanceof SocioFederado){
                tipoSocioCombo.setValue("FEDERADO");
                nifField.setText(((SocioFederado) soc).getNif());
                codigoField.setText(((SocioFederado) soc).getFederacion().getCodigo());
                nombreFedField.setText(((SocioFederado) soc).getFederacion().getNombre());
                infoExtra.getChildren().addAll(new Label("NIF:"), nifField,
                        new Label("Código Fed:"), codigoField,
                        new Label("Nombre Fed:"), nombreFedField);
            }else{
                tipoSocioCombo.setValue("INFANTIL");
                tutorField.setText(String.valueOf(((SocioInfantil) soc).getNoTutor()));
                infoExtra.getChildren().addAll(
                        new Label("Tutor:"), tutorField);
            }
            contenido.getChildren().add(infoExtra);
        }
        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonTypeOk) {
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                if (nombre.isEmpty() || apellido.isEmpty() || tipoSocioCombo.getValue() == null ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, complete todos los campos.");
                    alert.show();
                    return null;
                }
                if(soc != null){
                    soc.setNombre(nombre);
                    soc.setApellido(apellido);
                }

                switch (tipoSocioCombo.getValue()){
                    case "ESTÁNDAR":
                        String nifEst = nifField.getText();
                        double precio = Double.parseDouble(precioField.getText());
                        boolean tipo = tipoSeguro.getValue().equals("COMPLETO");
                        if(soc == null){
                            if(existeNif(nifEst)){
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Nif ya registrado");
                                alert.show();
                                return null;
                            }
                        }
                        if (nifEst.isEmpty() || precio == 0 ) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, complete todos los campos.");
                            System.out.println("1- " + nifEst);
                            System.out.println("2- "+ precio);
                            System.out.println("3- "+existeNif(nifEst));
                            alert.show();
                            return null;
                        }
                        Seguro seg = new Seguro(tipo, precio);
                        DAOFactory.getSeguroDao().insertar(seg);
                        if(soc == null) {
                            DAOFactory.getSocioDao().insertar(new SocioEstandar(nombre, apellido, nifEst, seg));
                        }else{
                            System.out.println("ESTADFASDNASDF¿ASDFNJMASDF");
                            if(soc instanceof SocioEstandar){
                                ((SocioEstandar)soc).setNif(nifEst);
                                ((SocioEstandar)soc).setSeguro(seg);
                            }
                            DAOFactory.getSocioDao().modificar(soc);
                        }
                        break;
                    case "FEDERADO":
                        String nifFed = nifField.getText();
                        String codigo = codigoField.getText();
                        String nombreFed = nombreFedField.getText();
                        if(soc == null){
                            if(existeNif(nifFed)){
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Nif ya registrado");
                                alert.show();
                                return null;
                            }
                        }
                        if (nifFed.isEmpty() || codigo.isEmpty() ) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, complete todos los campos.");
                            alert.show();
                            return null;
                        }
                        Federacion fed = new Federacion(codigo, nombreFed);
                        DAOFactory.getFederacionDao().insertar(fed);
                        if(soc == null) {
                            DAOFactory.getSocioDao().insertar(new SocioFederado(nombre, apellido, nifFed, fed));
                        }else{
                            System.out.println("ASDFISOFSPDOFJEAFMSOEJF");
                            if(soc instanceof SocioFederado){
                                ((SocioFederado)soc).setNif(nifFed);
                                ((SocioFederado)soc).setFederacion(fed);
                            }
                            DAOFactory.getSocioDao().modificar(soc);
                        }
                        break;
                    case "INFANTIL":
                        int tutor = Integer.parseInt(tutorField.getText());
                        if (tutor == 0 ) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Por favor, complete todos los campos.");
                            System.out.println("1- " + tutor);
                            alert.show();
                            return null;
                        }
                        if(soc == null) {
                            DAOFactory.getSocioDao().insertar(new SocioInfantil(nombre, apellido, tutor));
                        }else {
                            System.out.println("INDFASDFSDIFPLASJKFASLD");

                            if (soc instanceof SocioInfantil) {
                                ((SocioInfantil) soc).setNoTutor(tutor);
                            }
                            DAOFactory.getSocioDao().modificar(soc);
                        }
                        break;
                }
                cargarTablaSocios();
            }
            return null;
        });

        dialog.showAndWait();
    }
    @FXML
    private void handleEliminar(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Eliminar Socio");
        dialog.setHeaderText("Por favor, ingrese el ID del socio a eliminar:");
        dialog.setContentText("ID del Socio:");

        // Mostrar el diálogo y obtener el ID ingresado por el usuario
        dialog.showAndWait().ifPresent(idInput -> {
            try {
                // Convertir el ID ingresado a un número
                int idSocio = Integer.parseInt(idInput);

                // Buscar el socio en la base de datos
                Socio socio = DAOFactory.getSocioDao().buscar(idSocio);

                if (socio != null) {
                    // Eliminar según el tipo de socio
                    if (socio instanceof SocioEstandar) {
                        SocioEstandar socioEstandar = (SocioEstandar) socio;
                        Seguro seguro = socioEstandar.getSeguro();
                        DAOFactory.getSocioEstandarDao().eliminar(idSocio);
                        DAOFactory.getSeguroDao().eliminar(seguro.getIdSeguro());
                    } else if (socio instanceof SocioFederado) {
                        SocioFederado socioFederado = (SocioFederado) socio;
                        Federacion federacion = socioFederado.getFederacion();
                        DAOFactory.getSocioFederadoDao().eliminar(idSocio);
                        DAOFactory.getFederacionDao().eliminar(federacion.getIdFederacion());
                    } else if (socio instanceof SocioInfantil) {
                        DAOFactory.getSocioInfantilDao().eliminar(idSocio);
                    }

                    // Actualizar la lista de socios desde la base de datos
                    listaSocios.setAll(DAOFactory.getSocioDao().listar());

                    // Actualizar la tabla con la lista actualizada
                    tablaSocios.setItems(listaSocios);

                    // Mostrar mensaje de éxito
                    datosUtil.mostrarMensaje("Socio eliminado", "El socio con ID " + idSocio + " ha sido eliminado.");
                } else {
                    datosUtil.mostrarError("No se encontró un socio con el ID " + idSocio + ".");
                }

            } catch (NumberFormatException e) {
                datosUtil.mostrarError("El ID ingresado no es válido.");
            } catch (Exception e) {
                datosUtil.mostrarError("Ocurrió un error al eliminar el socio: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleModificar(ActionEvent event) {
        // Crear un cuadro de entrada para que el usuario ingrese el ID del socio a modificar
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Modificar Socio");
        dialog.setHeaderText("Por favor, ingrese el ID del socio a modificar:");
        dialog.setContentText("ID del Socio:");

        // Mostrar el diálogo y obtener el ID ingresado por el usuario
        dialog.showAndWait().ifPresent(idInput -> {
            try {
                // Convertir el ID ingresado a un número
                int idSocio = Integer.parseInt(idInput);

                // Buscar el socio en la lista
                Socio socioAModificar = listaSocios.stream().filter(socio -> socio.getIdSocio() == idSocio).findFirst().orElse(null);

                // Si el socio se encuentra
                if (socioAModificar != null) {
                    // Crear los campos de entrada con los datos actuales del socio
                    TextField nombreField = new TextField(socioAModificar.getNombre());
                    TextField apellidoField = new TextField(socioAModificar.getApellido());
                    ComboBox<String> tipoSocioCombo = new ComboBox<>();
                    tipoSocioCombo.getItems().addAll("ESTÁNDAR", "FEDERADO", "INFANTIL");
                    tipoSocioCombo.setValue(getTipoSocio(socioAModificar)); // Establecer el valor correcto en el ComboBox

                    // Crear el diálogo
                    Dialog<ButtonType> modificarDialog = new Dialog<>();
                    modificarDialog.setTitle("Modificar Socio");
                    modificarDialog.setHeaderText("Modifique los datos del socio:");

                    ButtonType buttonTypeOk = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
                    ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                    modificarDialog.getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

                    VBox vbox = new VBox(10);
                    vbox.getChildren().addAll(new Label("Nombre:"), nombreField,
                            new Label("Apellido:"), apellidoField,
                            new Label("Tipo de Socio:"), tipoSocioCombo);
                    modificarDialog.getDialogPane().setContent(vbox);

                    // Manejar la respuesta del usuario (si hizo clic en "Guardar")
                    Optional<ButtonType> result = modificarDialog.showAndWait();
                    if (result.isPresent() && result.get() == buttonTypeOk) {
                        String nuevoNombre = nombreField.getText();
                        String nuevoApellido = apellidoField.getText();
                        String nuevoTipoSocio = tipoSocioCombo.getValue();

                        // Actualizar el socio existente con los nuevos datos
                        socioAModificar.setNombre(nuevoNombre);
                        socioAModificar.setApellido(nuevoApellido);

                        // Si el tipo de socio cambia, crear el nuevo socio con el tipo actualizado
                        if (!getTipoSocio(socioAModificar).equals(nuevoTipoSocio)) {
                            Socio socioNuevo = null;
                            switch (nuevoTipoSocio) {
                                case "ESTÁNDAR":
                                    socioNuevo = new SocioEstandar(socioAModificar.getIdSocio(), nuevoNombre, nuevoApellido, "NIF123", new Seguro(1, true, 40));
                                    break;
                                case "FEDERADO":
                                    socioNuevo = new SocioFederado(socioAModificar.getIdSocio(), nuevoNombre, nuevoApellido, "NIF456", new Federacion("020", "0202"));
                                    break;
                                case "INFANTIL":
                                    socioNuevo = new SocioInfantil(socioAModificar.getIdSocio(), nuevoNombre, nuevoApellido, 123);
                                    break;
                            }

                            // Reemplazar el socio antiguo por el nuevo en la lista
                            listaSocios.remove(socioAModificar);
                            listaSocios.add(socioNuevo);
                            socioAModificar = socioNuevo; // Ahora socioAModificar se convierte en el socio nuevo
                        }

                        // Actualizar la tabla
                        tablaSocios.setItems(listaSocios);
                        tablaSocios.refresh();  // Forzar actualización de la tabla

                        // Modificar el socio en la base de datos
                        if (socioAModificar instanceof SocioEstandar) {
                            DAOFactory.getSocioEstandarDao().modificar((SocioEstandar) socioAModificar);
                        } else if (socioAModificar instanceof SocioFederado) {
                            DAOFactory.getSocioFederadoDao().modificar((SocioFederado) socioAModificar);
                        } else if (socioAModificar instanceof SocioInfantil) {
                            DAOFactory.getSocioInfantilDao().modificar((SocioInfantil) socioAModificar);
                        }

                        // Mostrar mensaje de éxito
                        datosUtil.mostrarMensaje("Socio modificado", "El socio con ID " + idSocio + " ha sido modificado.");
                    }
                } else {
                    // Mostrar mensaje de error si no se encontró el socio
                    datosUtil.mostrarError("No se encontró un socio con el ID " + idSocio + ".");
                }
            } catch (NumberFormatException e) {
                // Mostrar error si el ID ingresado no es un número válido
                datosUtil.mostrarError("El ID ingresado no es válido.");
            }
        });
    }





    private String getTipoSocio(Socio socio) {
        if (socio instanceof SocioEstandar) {
            return "ESTÁNDAR";
        } else if (socio instanceof SocioFederado) {
            return "FEDERADO";
        } else if (socio instanceof SocioInfantil) {
            return "INFANTIL";
        }
        return null;
    }




    public SocioControlador(VistaSocios vistaSocios) {
        this.vistaSocios = vistaSocios;
        this.datosUtil = new DatosUtil();
    }

    public SocioControlador() {
        this.vistaSocios = new VistaSocios();
        this.datosUtil = new DatosUtil();
    }

//    public int nouSoci(){
//        String strSocio = vistaSocios.formSocio();
//        if (strSocio == null) {
//            datosUtil.mostrarError("Error al crear el socio");
//            return 0;
//        }
//        String[] datosSocio = strSocio.split(",");
//        if (datosSocio.length < 3) {
//            datosUtil.mostrarError("Datos del socio incompletos");
//            return 0;
//        }
//        int tipoSocio = Integer.parseInt(datosSocio[0]);
//        int id = 0;
//        switch (tipoSocio) {
//            //EST
//            case 1:
//                SocioEstandar socioEst = nouSociEstandar(datosSocio[1], datosSocio[2]);
//                if (socioEst == null) {
//                    datosUtil.mostrarError("Error al crear el socio estándar");
//                    return 0;
//                }
//                DAOFactory.getSocioDao().insertar(socioEst);
//                break;
//            //FED
//            case 2:
//                SocioFederado socioFed = nouSociFederado(datosSocio[1], datosSocio[2]);
//                if (socioFed == null) {
//                    datosUtil.mostrarError("Error al crear el socio estándar");
//                    return 0;
//                }
//                DAOFactory.getSocioDao().insertar(socioFed);
//                break;
//            //INF
//            case 3:
//                SocioInfantil socioInf = nouSociInfantil(datosSocio[1], datosSocio[2]);
//                if (socioInf == null) {
//                    datosUtil.mostrarError("Error al crear el socio infantil");
//                    return 0;
//                }
//                DAOFactory.getSocioDao().insertar(socioInf);
//                vistaSocios.mostrarSocio(socioInf.toString());
//                break;
//            default:
//                datosUtil.mostrarError("Tipo de socio no válido");
//                return 0;
//        }
//        return id;
//    }

//    public Socio crearSocio(){
//        int id = nouSoci();
//        return DAOFactory.getSocioDao().buscar(id);
//    }


//    public SocioEstandar nouSociEstandar(String nombre,String apellido){
//        String nif = "1567848F";
////        if(existeNif(nif)){
////            datosUtil.mostrarError("Nif ya existe.");
////            return null;
////        }
//        Seguro seg = new Seguro(true,15.5);/*nuevoSeg();*/
//        DAOFactory.getSeguroDao().insertar(seg);
//        DAOFactory.getSocioDao().insertar(new SocioEstandar(nombre, apellido, nif, seg));
//        System.out.println("Socio creado");
//        return new SocioEstandar( nombre, apellido, nif, seg);
//    }
//
//    public SocioFederado nouSociFederado(String nombre, String apellido){
//        String nif = vistaSocios.formNif();
//        if(nif == null){
//            datosUtil.mostrarError("Nif no válido.");
//            return null;
//        }
//        if(existeNif(nif)){
//            datosUtil.mostrarError("Nif ya existe.");
//            return null;
//        }
//        Federacion fed = pedirFed();
//        if(fed == null){
//            datosUtil.mostrarError("Federación no válida.");
//            return null;
//        }
//        //DAOFactory.getFederacionDao().insertar(fed);
//        return new SocioFederado(nombre, apellido, nif, fed);
//    }

    public boolean existeNif(String nif){
        return DAOFactory.getSocioDao().hayNif(nif);
    }

//    public SocioInfantil nouSociInfantil(String nombre, String apellido){
//        int noTutor = vistaSocios.formTutor();
//        if(noTutor == 0){
//            return null;
//        }
//        if(!buscarTutor(noTutor)){
//            return null;
//        }
//        return new SocioInfantil(nombre, apellido, noTutor);
//    }



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
