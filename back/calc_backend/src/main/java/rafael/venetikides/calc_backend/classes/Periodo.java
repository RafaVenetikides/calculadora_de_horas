package rafael.venetikides.calc_backend.classes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ChronoUnit;

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

    public ArrayList<Horario> getMarcacoes() {
        return marcacoes;
    }

    public LocalTime getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(LocalTime cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
    
    public void addMarcacao(Horario horario){
        marcacoes.add(horario);
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

    public LocalTime calculaHorasTrabalhadas(){

        Iterator<Horario> i = iterator();
        LocalTime horasTrabalhadas = LocalTime.of(0, 0);

        while(i.hasNext()){
            Horario entrada = i.next();

            if(i.hasNext()){
                Horario saida = i.next();
                
                horasTrabalhadas.plus(entrada.getHora().until(saida.getHora(), ChronoUnit.HOURS));
            }
        }
        
        return horasTrabalhadas;
    }
    
}
