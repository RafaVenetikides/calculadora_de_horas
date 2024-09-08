package rafael.venetikides.calc_backend.classes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

public class Periodo implements Iterable<Horario>{

    ArrayList<Horario> marcacoes;
    Duration cargaHoraria;
    Duration tolerance;

    /*
     * Construtor da classe de Periodos.
     * Uma lista de Horarios marcados no dia.
     */
    public Periodo(Duration cargaHoraria){
        this.marcacoes = new ArrayList<>();
        this.cargaHoraria = cargaHoraria;
        this.tolerance = Duration.ofMinutes(10);
    }

    
    /** 
     * @return ArrayList<Horario>
     */
    public ArrayList<Horario> getMarcacoes() {
        return marcacoes;
    }

    public Duration getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Duration cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Duration getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = Duration.ofMinutes(tolerance);
    }
    
    public void addMarcacao(Integer hora, Integer minuto, Integer dia, Integer mes, Integer ano){
        marcacoes.add(criaHorario(hora, minuto, dia, mes, ano));
    }

    public Horario criaHorario(Integer hora, Integer minuto, Integer dia, Integer mes, Integer ano){
        LocalDateTime m = LocalDateTime.of(ano, mes, dia, hora, minuto);

		return new Horario(m);
    }

    public void ordenaPeriodo(){
        Collections.sort(marcacoes);
    }

    @Override
    public Iterator<Horario> iterator(){
        return marcacoes.iterator();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        Iterator<Horario> i = iterator();
        while(i.hasNext()){
            Horario horario = i.next();
            s.append(horario.hourToString()); 

            if(i.hasNext()){ 
                s.append(" | ");
            }
        }
        s.append("\n");

        Iterator<Horario> j = iterator();
        while(j.hasNext()){
            Horario horario = j.next();
            s.append(horario.dataToString());

            if(j.hasNext()){
                s.append(" | ");
            }
        }

        return s.toString();
    }

    public Duration calculaHorasTrabalhadas(){

        Iterator<Horario> i = iterator();
        Duration horasTrabalhadas = Duration.ZERO;
        Duration essePeriodo;
    
        while(i.hasNext()){
            Horario entrada = i.next();
            
            if(i.hasNext()){
                Horario saida = i.next();
                
                essePeriodo = Duration.between(entrada.getMarcacao(), saida.getMarcacao());
                horasTrabalhadas = horasTrabalhadas.plus(essePeriodo);
            }
        }
        return horasTrabalhadas;
    }
    
    public Duration calculaSaldo(){
        Duration horasTrabalhadas = calculaHorasTrabalhadas();

        if (horasTrabalhadas.compareTo(cargaHoraria.minus(tolerance)) < 0){
            return cargaHoraria.minus(horasTrabalhadas);

        } else if(horasTrabalhadas.compareTo(cargaHoraria.plus(tolerance)) > 0){
            return horasTrabalhadas.minus(cargaHoraria);

        } else{
            return Duration.ZERO;
        }
    }

    public String getSaldo(){
        StringBuilder s = new StringBuilder();
        Duration saldo = calculaSaldo();

        if (calculaHorasTrabalhadas().compareTo(cargaHoraria) >= 0){
            s.append("Credito: ").append(saldo);
        } else{
            s.append("Débito: ").append(saldo);
        }

        return s.toString();
    }

    public Duration calculaIntervalo(){
        Duration intervalo = Duration.ZERO;
        Duration esseIntervalo;
        ListIterator<Horario> i = marcacoes.listIterator();

        while(i.hasNext()){
            i.next();
            if(i.hasNext()){
                Horario entradaIntervalo = i.next();

                if(i.hasNext()){
                    Horario saidaIntervalo = i.next();

                    esseIntervalo = Duration.between(entradaIntervalo.getMarcacao(), saidaIntervalo.getMarcacao());
                    intervalo = intervalo.plus(esseIntervalo);
                    i.previous();
                }
            }
        }

        return intervalo;
    }

    // public Duration calculaAdicionalNoturno(){
    //     Duration adicionalNoturno;
    // }
}
    

