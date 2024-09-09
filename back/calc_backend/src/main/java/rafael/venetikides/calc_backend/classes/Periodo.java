package rafael.venetikides.calc_backend.classes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    /*
     * Ordena os valores de acordo com o dia e hora da marcação
     */
    public void ordenaPeriodo(){
        Collections.sort(marcacoes);
    }

    @Override
    public Iterator<Horario> iterator(){
        return marcacoes.iterator();
    }

    /*
     * toString() para impressão no terminal
     */
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

    /*
     * Calcula as horas trabalhadas de acordo com as marcações
     * Serão somente calculadas as horas trabalhadas em períodos fechados, com entrada e saída
     */
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
    
    /*
     * Calcula o Saldo de horas respeitando a tolerância de 10 minutos.
     * Caso tenha até 10 minutos a mais ou a menos, retorna a carga horária
     * Se tiver feito hora extra ou estar devendo horas, calcula esse valor e retorna
     */
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

    /*
     * Utiliza o valor calculado de calculaSaldo() para retornar se foi um crédito ou um Débito
     */
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

    /*
     * Calcula o tempo de intervalo
     * Primeiro procura aonde está a saída e calcula o tempo até a próxima entrada
     * Retorna um Duration sendo a soma de todos os intervalos
     */
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

    /*
     * Calcula o adicional noturno
     * verifica o quanto tempo depois das 22:00 foi feito a marcacao
     * transforma o valor em minutos, dividindo por 52:30 para pegar as horas
     * e utiliza o resto da divisão como minutos.
     * Retorna uma Duration sendo a duração do adicional noturno
     */
    public Duration calculaAdicionalNoturno(){
        Duration tempoNoturno = Duration.ZERO;
        ListIterator<Horario> i = marcacoes.listIterator();

        while(i.hasNext()){
            Horario h1 = i.next();
            if(h1.getMarcacao().isAfter(LocalDateTime.of(h1.getData(), LocalTime.of(22, 0)))){
                tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(h1.getData(), LocalTime.of(22,0)), h1.getMarcacao()));
            }
        }

        Integer horasNoturnas = (int) (tempoNoturno.toMinutes() / 52.5f) * 60;
        Float minutosNoturnos = (tempoNoturno.toMinutes() % 52.5f);
        Long horarioNoturno = (long) (horasNoturnas + minutosNoturnos);

        Duration adicionalNoturno = Duration.ofMinutes(horarioNoturno);

        return adicionalNoturno;
    }
}
    

