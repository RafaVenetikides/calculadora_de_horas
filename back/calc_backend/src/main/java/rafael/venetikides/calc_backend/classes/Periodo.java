package rafael.venetikides.calc_backend.classes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ChronoUnit;
import java.time.Duration;
import java.time.LocalDate;

public class Periodo implements Iterable<Horario>{

    ArrayList<Horario> marcacoes;
    LocalTime cargaHoraria;

    /*
     * Construtor da classe de Periodos.
     * Uma lista de Horarios marcados no dia.
     */
    public Periodo(LocalTime cargaHoraria){
        this.marcacoes = new ArrayList<>();
        this.cargaHoraria = cargaHoraria;
    }

    
    /** 
     * @return ArrayList<Horario>
     */
    public ArrayList<Horario> getMarcacoes() {
        return marcacoes;
    }

    public LocalTime getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(LocalTime cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
    
    public void addMarcacao(Integer hora, Integer minuto, Integer dia, Integer mes, Integer ano){
        marcacoes.add(criaHorario(hora, minuto, dia, mes, ano));
    }

    public Horario criaHorario(Integer hora, Integer minuto, Integer dia, Integer mes, Integer ano){
        LocalTime h = LocalTime.of(hora,minuto);
		LocalDate d = LocalDate.of(ano,mes,dia);

		return new Horario(h, d);
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
            s.append(horario.toString()); 
            
            if(i.hasNext()){ 
                s.append(" | ");
            }
        }
        return s.toString();
    }

    // public LocalTime calculaHorasTrabalhadas(){

    //     Iterator<Horario> i = iterator();
    //     Duration horasTrabalhadas = Duration;
    
    //     while(i.hasNext()){
    //         Horario entrada = i.next();
            
    //         if(i.hasNext()){
    //             Horario saida = i.next();

                
    //         }
    //     }
        
    //     return horasTrabalhadas;
    //     }
    }

    // retornaTrabalhada(marcacoes.get(1), marcacoes.get(0));

    // public Long retornaTrabalhada(Long saida, Long entrada){
    //     return saida - entrada;
    // }
    

