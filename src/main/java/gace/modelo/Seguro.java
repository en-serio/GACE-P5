package gace.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "seguro")
public class Seguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seguro")
    private int idSeguro;

    @Column(name = "tipo", nullable = false)
    private boolean tipo;

    @Column(name ="precio", nullable = false)
    private double precio;

    public Seguro(int idSeguro, boolean tipo, double precio) {
        this.idSeguro = idSeguro;
        this.tipo = tipo;
        this.precio = precio;
    }

    public Seguro(boolean tipo, double precio) {
        this.tipo = tipo;
        this.precio = precio;
    }

    public Seguro() { }

    public int getIdSeguro() {
        return idSeguro;
    }
    public void setIdSeguro(int idSeguro) {
        this.idSeguro = idSeguro;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Seguro{" +
                ", tipo=" + (tipo ? "COMPLETO" : "ESTÁNDAR") +
                ", precio=" + precio +
                '}';
    }
}
