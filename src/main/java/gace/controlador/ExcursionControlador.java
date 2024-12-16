package gace.controlador;

import gace.modelo.Excursion;
import gace.modelo.Inscripcion;
import gace.modelo.dao.DAOFactory;
import gace.modelo.dao.ExcursionDao;
import gace.vista.DatosUtil;
import gace.vista.VistaExcursion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jdk.jfr.Percentage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcursionControlador {
    private DatosUtil datosUtil;
    private VistaExcursion vistaExcursion;
    private ExcursionDao excursionDao;
    @FXML
    private TableView<Excursion> tableView;
    @FXML
    private TableColumn<Excursion, Integer> columnId;
    @FXML
    private TableColumn<Excursion, String> columnCodigo;
    @FXML
    private TableColumn<Excursion, String> columnDescripcion;
    @FXML
    private TableColumn<Excursion, Date> columnFecha;
    @FXML
    private TableColumn<Excursion, Integer> columnCantidad;
    @FXML
    private TableColumn<Excursion, Double> columnPrecio;


    public ExcursionControlador() {
        this.vistaExcursion = new VistaExcursion();
        this.excursionDao = DAOFactory.getExcursionDao();
        this.datosUtil = new DatosUtil();
    }

    public void novaExcursio() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Ingresar Excursión");

        Label descripcionLabel = new Label("Descripción:");
        TextField descripcionField = new TextField();
        descripcionField.setPromptText("Ingrese descripción");

        Label fechaLabel = new Label("Fecha:");
        DatePicker fechaPicker = new DatePicker();

        Label diasLabel = new Label("Número de días:");
        TextField diasField = new TextField();
        diasField.setPromptText("Ingrese número de días");

        Label precioLabel = new Label("Precio de inscripción:");
        TextField precioField = new TextField();
        precioField.setPromptText("Ingrese precio");

        Button aceptarButton = new Button("Aceptar");
        Button cancelarButton = new Button("Cancelar");
        aceptarButton.setOnAction(event -> {
            String descripcion = descripcionField.getText();
            String fecha = (fechaPicker.getValue() != null) ? fechaPicker.getValue().toString() : "";
            String dias = diasField.getText();
            String precio = precioField.getText();

            if (descripcion.isEmpty() || fecha.isEmpty() || dias.isEmpty() || precio.isEmpty()) {
                mostrarAlerta("Por favor, complete todos los campos.");
                return;
            }

            try {
                int numeroDias = Integer.parseInt(dias);
                double precioInscripcion = Double.parseDouble(precio);

                modalStage.close(); 
            } catch (NumberFormatException e) {
                mostrarAlerta("Número de días y precio deben ser valores numéricos.");
            }
        });
        cancelarButton.setOnAction(event -> modalStage.close());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(descripcionLabel, 0, 0);
        grid.add(descripcionField, 1, 0);

        grid.add(fechaLabel, 0, 1);
        grid.add(fechaPicker, 1, 1);

        grid.add(diasLabel, 0, 2);
        grid.add(diasField, 1, 2);

        grid.add(precioLabel, 0, 3);
        grid.add(precioField, 1, 3);

        HBox buttonBox = new HBox(10, aceptarButton, cancelarButton);
        buttonBox.setPadding(new Insets(10));
        grid.add(buttonBox, 1, 4);

        Scene scene = new Scene(grid, 400, 250);
        modalStage.setScene(scene);
        modalStage.showAndWait();



    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Advertencia");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
//        String strExcursio = this.vistaExcursion.formExcursion();
//        String[] datosExc = strExcursio.split(",");
//        if (datosExc.length < 4) {
//            datosUtil.mostrarError("Datos de la excursión incompletos");
//            return false;
//        }
//        Date data = null;
//        try{
//            data = validarFecha(datosExc[2]);
//        } catch (ParseException e) {
//            datosUtil.mostrarError("Fecha no válida");
//            return false;
//        }
//        if(data == null){
//            datosUtil.mostrarError("Fecha no válida");
//            return false;
//        }
//        Excursion exc = new Excursion(datosExc[0], datosExc[1], data, Integer.parseInt(datosExc[3]), Double.parseDouble(datosExc[4]));
//        excursionDao.insertar(exc);
//        vistaExcursion.detalleExcursion(exc.toString());
//        return true;
//    }

    private Date validarFecha(String fechaString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(fechaString);
    }

//    public ArrayList<Excursion> mostrarExcursiones(){
//        ArrayList<Excursion> excursiones = DAOFactory.getExcursionDao().listar();
//        if(excursiones == null){
//            datosUtil.mostrarError("No hay excursiones para mostrar");
//            return null;
//        }
//        return excursiones;
//    }
    public Excursion pedirExcursion(){
        String codigo = vistaExcursion.pedirExc();
        Excursion exc = buscarExcursion(codigo);
        if(exc == null){
            datosUtil.mostrarError("Excursion no encontrada");
            return null;
        }
        vistaExcursion.detalleExcursion(exc.toString());
        return exc;
    }

    public Excursion buscarExcursion(int id_excursion){
        return DAOFactory.getExcursionDao().buscar(id_excursion);
    }
    public Excursion buscarExcursion(String codigo){
        return DAOFactory.getExcursionDao().buscar(codigo);
    }

      public boolean eliminarExcursion(){
        ArrayList<Excursion> excursiones = DAOFactory.getExcursionDao().listar();
        if(excursiones== null){
            datosUtil.mostrarError("No hay excursiones para eliminar");
            return false;
        }
        for(Excursion excursion : excursiones){
            vistaExcursion.detalleExcursion(excursion.toString());
        }
        if(seleccionarExc(excursiones)){
            return true;
        }
        return false;
    }

    public boolean seleccionarExc(ArrayList<Excursion> excursiones){
        String codigo = vistaExcursion.pedirExc();
        for(Excursion excur : excursiones){
            if(excur.getCodigo().equals(codigo)) {
                List<Inscripcion> insc = DAOFactory.getInscripcionDao().listarXExc(excur);
                if(insc != null){
                    datosUtil.mostrarError("No se puede eliminar la excursión porque tiene inscripciones");
                    return false;
                }
                int opcion = datosUtil.pedirOpcion("Es esta la excursion que desea eliminar", "Sí", "No");
                if (opcion == 1) {
                    DAOFactory.getExcursionDao().eliminar(excur.getId());
                    return true;
                }
                return false;
            }
        }
        datosUtil.mostrarError("Excursion no encontrada");
        return false;
    }

    public boolean cancelarExcursion(){
        ArrayList<Excursion> excursiones = DAOFactory.getExcursionDao().listar();
        if(excursiones == null){
            datosUtil.mostrarError("No hay excursiones para cancelar");
            return false;
        }
        for(Excursion excursion : excursiones){
            vistaExcursion.detalleExcursion(excursion.toString());
        }
        Excursion exc = pedirExcursion();
        if(exc == null){
            return false;
        }
        int opcion = datosUtil.pedirOpcion("¿Está seguro de que desea cancelar la excursión?", "Sí", "No");
        int cantidad = 0;
        if(opcion == 1){
            cantidad = DAOFactory.getExcursionDao().cancelar(exc);
            datosUtil.mostrarInfo("Se han cancelado "+cantidad+" inscripciones");
        }
        return false;
    }

//    public void menuExcursion(AnchorPane contenedorCentral, Scene scene){
//        //ArrayList<Excursion> excs = DAOFactory.getExcursionDao().listar();
//        ArrayList<Excursion> excs = falsalistaExc();
//        if(excs == null){
//            datosUtil.mostrarError("No hay excursiones para mostrar");
//            return;
//        }
//        try{
//            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/vista/MenuExcursion.fxml"));
//            Parent menuRoot = menuLoader.load();
//
//            contenedorCentral.getChildren().clear();
//            contenedorCentral.getChildren().add(menuRoot);
//
//            listaExcursion = (AnchorPane) scene.lookup("#listaExcursion");
//            for (Excursion exc : excs) {
//                Label label = new Label("ID: " + exc.getId() + " Código: " + exc.getCodigo() + " " + exc.getDescripcion());
//
//                VBox socioBox = new VBox(5, label);
//                socioBox.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: #f9f9f9;");
//
//                listaExcursion.getChildren().add(socioBox);
//            }
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//    }

    public void mostrarDetalle(Excursion exc){
        Label idExc = new Label("ID: " + exc.getId());
        Label codiExc = new Label("Codi: " + exc.getCodigo());
        Label nomExc = new Label("Descripción: " + exc.getDescripcion());
        Label dataExc = new Label("Data: " + exc.getFecha().toString());
        Label preuExc = new Label("Preu: " + exc.getPrecio());

        HBox hboxId = new HBox(idExc);
        HBox hboxCodi = new HBox(codiExc);
        HBox hboxNom = new HBox(nomExc);
        HBox hboxData = new HBox(dataExc);
        HBox hboxPreu = new HBox(preuExc);

        VBox vbox = new VBox(10, hboxId, hboxCodi, hboxNom, hboxData, hboxPreu);
        vbox.setSpacing(10);

        Scene scene = new Scene(vbox, 300, 200);
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Detalles de la Excursión");
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }




    public void mostrarExcursiones(){
        ArrayList<Excursion> excs = DAOFactory.getExcursionDao().listar();
        if(excs == null){
            datosUtil.mostrarError("No hay excursiones para mostrar");
            return;
        }

        ObservableList<Excursion> datos = FXCollections.observableArrayList(excs);

        // Establecer los datos en la ListView
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        columnDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnCantidad.setCellValueFactory(new PropertyValueFactory<>("noDias"));
        columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnFecha.setCellFactory(column -> {
            return new TableCell<Excursion, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(new SimpleDateFormat("dd/MM/yyyy").format(item));
                    }
                }
            };
        });
        columnPrecio.setCellFactory(column -> {
            return new TableCell<Excursion, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f €", item));
                    }
                }
            };
        });

        tableView.setItems(datos);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalle(newSelection);
            }
        });
    }

}
