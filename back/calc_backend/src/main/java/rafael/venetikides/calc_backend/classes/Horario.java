package rafael.venetikides.calc_backend.classes;
import java.time.LocalDate;
import java.time.LocalTime;

public class Horario implements Comparable<Horario>{
    LocalTime hora;
    LocalDate data;

    /*
     * Construtor da classe Horario para a hora e dia atual
     * Define um horario marcado
     */
    public Horario(){
        this.hora = LocalTime.now();
        this.data = LocalDate.now();
    }

    /*
     * Construtor da classe Horario com hora definida e dia atual
     * @param hora hora deinida no horario
     */
    public Horario(LocalTime hora){
        this.hora = hora;
        this.data = LocalDate.now();
    }

    /*
     * Construtor da classe Horario com hora atual e dia definido
     * @param data dia definido no horario
     */
    public Horario(LocalDate data){
        this.hora = LocalTime.now();
        this.data = data;
    }

    /*
     * Construtor da classe Horario com hora e dia definidos
     * @param hora hora deinida no horario
     * @param data dia definido no horario
     */
    public Horario(LocalTime hora, LocalDate data){
        this.hora = hora;
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public int compareTo(Horario o) {
        if(this.data.compareTo(o.getData()) == 0){
            return this.hora.compareTo(o.getHora());
        }
        else{
            return this.data.compareTo(o.getData());
        }
    }

    @Override
    public String toString() {
        return hora.toString();
    }
    
}
