package gace.modelo;

import java.util.Date;
import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "excursion")
public class Excursion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_excursion")
    private int id;
    @Column(nullable = false, length = 15)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descripcion;
    @Column(name = "fecha", nullable = false)
    private Date fecha;
    @Column(name = "no_dias")
    private int noDias;
    @Column(name = "precio")
    private double precio;

    public Excursion(int id,String codigo, String descripcion, Date fecha, int noDias, double precio) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.noDias = noDias;
        this.precio = precio;
    }

    public Excursion(String codigo, String descripcion, Date fecha, int noDias, double precio) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.noDias = noDias;
        this.precio = precio;
    }

    public Excursion() {
    }

    //getters

    public int getId() {
        return id;
    }
    public String getCodigo() {
        return codigo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public Date getFecha() {
        return fecha;
    }
    public int getNoDias() {
        return noDias;
    }
    public double getPrecio() {
        return precio;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setNoDias(int noDias) {
        this.noDias = noDias;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Excursion "+id+ " Codigo: " + codigo + ", Descripcion: " + descripcion + ", Fecha: " + fecha + ", Duracion(dias): " + noDias + ", Precio: " + precio + ".";
    }
}
