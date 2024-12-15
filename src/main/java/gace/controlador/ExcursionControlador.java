package gace.controlador;

import gace.modelo.Excursion;
import gace.modelo.Inscripcion;
import gace.modelo.dao.DAOFactory;
import gace.modelo.dao.ExcursionDao;
import gace.vista.DatosUtil;
import gace.vista.VistaExcursion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    private AnchorPane listaExcursion;

    public ExcursionControlador() {
        this.vistaExcursion = new VistaExcursion();
        this.excursionDao = DAOFactory.getExcursionDao();
        this.datosUtil = new DatosUtil();
    }

    public boolean novaExcursio(){
        String strExcursio = this.vistaExcursion.formExcursion();
        String[] datosExc = strExcursio.split(",");
        if (datosExc.length < 4) {
            datosUtil.mostrarError("Datos de la excursión incompletos");
            return false;
        }
        Date data = null;
        try{
            data = validarFecha(datosExc[2]);
        } catch (ParseException e) {
            datosUtil.mostrarError("Fecha no válida");
            return false;
        }
        if(data == null){
            datosUtil.mostrarError("Fecha no válida");
            return false;
        }
        Excursion exc = new Excursion(datosExc[0], datosExc[1], data, Integer.parseInt(datosExc[3]), Double.parseDouble(datosExc[4]));
        excursionDao.insertar(exc);
        vistaExcursion.detalleExcursion(exc.toString());
        return true;
    }

    private Date validarFecha(String fechaString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(fechaString);
    }

    public ArrayList<Excursion> mostrarExcursiones(){
        ArrayList<Excursion> excursiones = DAOFactory.getExcursionDao().listar();
        if(excursiones == null){
            datosUtil.mostrarError("No hay excursiones para mostrar");
            return null;
        }
        return excursiones;
    }
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

    public void menuExcursion(AnchorPane contenedorCentral, Scene scene){
        //ArrayList<Excursion> excs = DAOFactory.getExcursionDao().listar();
        ArrayList<Excursion> excs = falsalistaExc();
        if(excs == null){
            datosUtil.mostrarError("No hay excursiones para mostrar");
            return;
        }
        try{
            FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/vista/MenuExcursion.fxml"));
            Parent menuRoot = menuLoader.load();

            contenedorCentral.getChildren().clear();
            contenedorCentral.getChildren().add(menuRoot);

            listaExcursion = (AnchorPane) scene.lookup("#listaExcursion");
            for (Excursion exc : excs) {
                Label label = new Label("ID: " + exc.getId() + " Código: " + exc.getCodigo() + " " + exc.getDescripcion());

                VBox socioBox = new VBox(5, label);
                socioBox.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: #f9f9f9;");

                listaExcursion.getChildren().add(socioBox);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public ArrayList<Excursion> falsalistaExc(){
        ArrayList<Excursion> excs = new ArrayList<>();
        excs.add(new Excursion(1,"EXC1", "Excursión a la playa", new Date(), 2, 100.0));
        excs.add(new Excursion(2,"EXC2", "Excursión a la montaña", new Date(), 3, 150.0));
        excs.add(new Excursion(3,"EXC3", "Excursión a la ciudad", new Date(), 1, 50.0));
        excs.add(new Excursion(4,"EXC4", "Excursión a la selva", new Date(), 4, 200.0));
        excs.add(new Excursion(5,"EXC5", "Excursión a la nieve", new Date(), 5, 250.0));
        excs.add(new Excursion(6,"EXC6", "Excursión a la granja", new Date(), 1, 50.0));
        return excs;
    }

}
