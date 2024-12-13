package gace.modelo;

import jakarta.persistence.*;


@Entity
@Table(name = "socio")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    private int idSocio;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    private String apellido;

    protected Socio() {}

    public Socio(int idSocio, String nombre, String apellido) {
        this.idSocio = idSocio;
        //this.noSocio = noSocio;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    public Socio(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    //getters
    public int getIdSocio() {
        return idSocio;
    }
   /* public String getNoSocio() {
        return noSocio;
    }*/
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {return apellido; }

    //setters
    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }
/*    public void setNoSocio(String noSocio) {
        this.noSocio = noSocio;
    }*/
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {this.apellido = apellido; }

    @Override
    public String toString() {
        return "Socio nº:" + idSocio + ' ' + ", nombre: " + nombre + ' ' + apellido + '.' ;
    }

    public abstract double calcularCuota();
    public abstract double costeExcursion(double precio);


}
