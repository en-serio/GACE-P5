package gace.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "federado")
@DiscriminatorValue("2")
public class SocioFederado extends Socio {
    @Column(nullable = false, length = 9)
    private String nif;
    @ManyToOne
    @JoinColumn(name = "id_federacion", referencedColumnName = "id_federacion")
    private Federacion federacion;


    public SocioFederado(String nombre, String apellido, String nif, Federacion federacion) {
        super(nombre, apellido);
        this.nif = nif;
        this.federacion = federacion;
    }

    public SocioFederado() {}

    public String getNif() {
        return nif;
    }



    public void setNif(String nif) {
        this.nif = nif;
    }

    public Federacion getFederacion() {
        return federacion;
    }

    public void setFederacion(Federacion federacion) {
        this.federacion = federacion;
    }

    @Override
    public String toString() {
        return "Socio nº:" + this.getIdSocio() +", Nombre de socio: " + this.getNombre() +
                ", Apellido: " + this.getApellido() +
                ", Tipo: Federado" +
                ", Nif: '" + nif +
                ", Federación: " + federacion.getNombre() +
                ", Cuota: " + 100 +
                '.';
    }
    @Override
    public double calcularCuota() {
        return 100 * 0.95; // 5% de descuento
    }

    @Override
    public double costeExcursion(double precio) {
        return precio * 0.90; // 10% de descuento
    }
}
