package gace.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "infantil")
@DiscriminatorValue("3")
public class SocioInfantil extends Socio {
    @Column(name = "id_tutor")
    private int noTutor;
    public SocioInfantil() {}

    public SocioInfantil(int id, String nombre, String apellido, int noTutor) {
        super(id, nombre, apellido);
        this.noTutor = noTutor;
    }

    public SocioInfantil(String nombre, String apellido, int noTutor) {
        super(nombre, apellido);
        this.noTutor = noTutor;
    }

    //getters
    public int getNoTutor() {
        return noTutor;
    }

    //setters
    public void setNoTutor(int noTutor) {
        this.noTutor = noTutor;
    }

    @Override
    public String toString() {
        return "Socio nº:" + this.getIdSocio() +", Nombre: " + this.getNombre() +
                ", Apellido: " + this.getApellido() +
                ", Tipo: Infantil" +
                ", Nº Tutor: " + noTutor +
                '.';
    }

    @Override
    public double calcularCuota() {
        return 100 * 0.50; // 50% de descuento
    }

    @Override
    public double costeExcursion(double precio) {
        return precio; // Precio completo
    }

    @Override
    public String getTipoSocio() {
        return "Infantil"; // Esto se mostrará en la columna "Tipo de Socio" del TableView
    }
}
