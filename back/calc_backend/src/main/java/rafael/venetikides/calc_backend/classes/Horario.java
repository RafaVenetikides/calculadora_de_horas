package rafael.venetikides.calc_backend.classes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Horario implements Comparable<Horario>{
    LocalDateTime marcacao;

    /*
     * Construtor da classe Horario para a hora e dia atual
     * Define um horario marcado
     */
    public Horario(){
        this.marcacao = LocalDateTime.now();
    }

    /*
     * Construtor da classe Horario com hora definida e dia atual
     * @param hora hora deinida no horario
     */
    public Horario(LocalTime hora){
        this.marcacao = LocalDateTime.of(LocalDate.now(), hora);
    }

    /*
     * Construtor da classe Horario com hora atual e dia definido
     * @param data dia definido no horario
     */
    public Horario(LocalDate data){
        this.marcacao = LocalDateTime.of(data, LocalTime.now());
    }

    /*
     * Construtor da classe Horario com hora e dia definidos
     * @param hora hora deinida no horario
     * @param data dia definido no horario
     */
    public Horario(LocalTime hora, LocalDate data){
        this.marcacao = LocalDateTime.of(data, hora);
    }

    public Horario(LocalDateTime m){
        this.marcacao = m;
    }

    public LocalDateTime getMarcacao() {
        return marcacao;
    }

    public void setMarcacao(LocalDateTime marcacao) {
        this.marcacao = marcacao;
    }

    public LocalTime getHora() {
        return marcacao.toLocalTime();
    }

    public void setHora(LocalTime hora) {
        this.marcacao = marcacao.with(hora);
    }

    public LocalDate getData() {
        return marcacao.toLocalDate();
    }

    public void setData(LocalDate data) {
        this.marcacao = marcacao.with(data);
    }

    @Override
    public int compareTo(Horario o) {
        return marcacao.compareTo(o.getMarcacao());
    }

    /* 
     * toStrin() para impressão no terminal
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        return s.append(hourToString()).append(" [").append(dataToString()).append("] ").toString();
    }

    public String hourToString() {
        return getHora().toString();
    }

    public String dataToString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        return getData().format(formatter);
    }
    
}
