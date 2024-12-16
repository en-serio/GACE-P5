package gace.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "estandar")
@DiscriminatorValue("1")
public class SocioEstandar extends Socio {

    @Column(name = "nif", nullable = false, length = 9)
    private String nif;
    @ManyToOne
    @JoinColumn(name = "id_seguro", referencedColumnName = "id_seguro", nullable = false)
    private Seguro seguro;
    public SocioEstandar() {}

    public SocioEstandar(int idSocio, String nombre, String apellido, String nif, Seguro seguro) {
        super(idSocio, nombre, apellido);
        this.nif = nif;
        this.seguro = seguro;
    }
    public SocioEstandar(String nombre, String apellido, String nif, Seguro seguro) {
        super(nombre, apellido);
        this.nif = nif;
        this.seguro = seguro;
    }

    //getters
    public String getNif() {
        return nif;
    }
    public Seguro getSeguro() {
        return seguro;
    }

    //setters
    public void setNif(String nif) {
        this.nif = nif;
    }
    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }

    @Override
    public String toString() {
        String tipo = seguro.isTipo() ? "Completo" : "Estándar";
        return "Socio nº:" + this.getIdSocio() +", Nombre: " + this.getNombre() +
                ", Apellido: " + this.getApellido() +
                ", Tipo: Estandar" +
                ", Nif: '" + nif +
                ", Tipo de Seguro: " + tipo +
                ", cuotaBase=" + 100 +
                '.';
    }

    @Override
    public double calcularCuota() {
        return 100;
    }

    @Override
    public double costeExcursion(double precio) {
        return precio;
    }

    @Override
    public String getTipoSocio() {
        return "Estandar"; // Esto se mostrará en la columna "Tipo de Socio" del TableView
    }
}
