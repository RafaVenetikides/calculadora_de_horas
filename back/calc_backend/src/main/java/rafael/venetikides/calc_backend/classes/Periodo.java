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
     * Ordena os valores das marcacoes de acordo com o dia e hora da marcação
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
     * Analisa as jornadas de trabalho que acontecem a noite.
     * Caso ele perceba que a jornada passou pelo inicio ou fim noturno,
     * arruma a marcação para calcular somente o período do noturno
     * Retorna uma Duration sendo a duração do adicional noturno
     */

     public Duration calculaAdicionalNoturno() {
        Duration tempoNoturno = Duration.ZERO;
        LocalTime inicioNoturno = LocalTime.of(22, 0);
        LocalTime fimNoturno = LocalTime.of(5, 0);
        
        ListIterator<Horario> i = marcacoes.listIterator();
    
        while (i.hasNext()) {
            Horario entrada = i.next();
            if (i.hasNext()) {
                Horario saida = i.next();
    
                LocalDateTime inicioJornada = entrada.getMarcacao();
                LocalDateTime fimJornada = saida.getMarcacao();


                // Caso 1: A jornada se inicia antes das 5:00
                if (inicioJornada.toLocalTime().isBefore(fimNoturno)) {

                    // Caso 1.1: A jornada termina antes das 5:00
                    if(fimJornada.toLocalTime().isBefore(fimNoturno)){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada.toLocalTime(), fimJornada));
                    }

                    // Caso 1.2: a jornada termina depois das 5:00
                    else if(fimJornada.toLocalTime().isAfter(fimNoturno) && fimJornada.toLocalTime().isBefore(inicioNoturno)){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada.toLocalTime(), fimNoturno));
                    }

                    // Caso 1.3: a jornada termina depois das 22:00 ou antes do início da jornada(outro dia)
                    else if(fimJornada.toLocalTime().isAfter(inicioNoturno) || fimJornada.toLocalTime().isBefore(inicioJornada.toLocalTime())){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada.toLocalTime(), fimNoturno));
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioNoturno, fimJornada.toLocalTime()));
                    }
                }

                // Caso 2: A jornada se inicia antes das 22:00
                else if(inicioJornada.toLocalTime().isBefore(inicioNoturno)){

                    // Caso 2.0: A jornada termina antes das 22:00
                    if (fimJornada.isBefore(LocalDateTime.of(inicioJornada.toLocalDate(), inicioNoturno)) && fimJornada.toLocalTime().isAfter(fimNoturno)){
                        continue;
                    }
                    
                    // Caso 2.1: A jornada termina depois das 22:00 e antes das 5:00 (cruza para o dia seguinte)
                    if (fimJornada.toLocalTime().isAfter(inicioNoturno) || fimJornada.toLocalTime().isBefore(fimNoturno) || fimJornada.toLocalTime().equals(fimNoturno)) {
                        
                        // Se a jornada cruzar para o dia seguinte (fimJornada é antes das 5:00 do dia seguinte)
                        if (fimJornada.toLocalTime().isBefore(fimNoturno) || fimJornada.toLocalTime().equals(fimNoturno)) {
                            // Calcula o tempo noturno entre 22:00 do dia atual e 5:00 do dia seguinte
                            tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(inicioJornada.toLocalDate(), inicioNoturno), fimJornada));
                        } 
                        // Se a jornada ainda estiver no mesmo dia mas entre 22:00 e 5:00
                        else if (fimJornada.toLocalTime().isAfter(inicioNoturno)) {
                            tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(inicioJornada.toLocalDate(), inicioNoturno), fimJornada));
                        }
                    }
                    
                    // Caso 2.2: A jornada termina depois das 5:00
                    else if(fimJornada.toLocalTime().isAfter(fimNoturno)){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioNoturno, fimNoturno));
                    }
                }

                // Caso 3: A jornada se inicia depois das 22:00
                else if (inicioJornada.toLocalTime().equals(inicioNoturno) || inicioJornada.toLocalTime().isAfter(inicioNoturno)) {

                    if(inicioJornada.toLocalDate().equals(fimJornada.toLocalDate())){
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, fimJornada));
                    }

                    // Caso 3.1: A jornada termina antes das 5:00
                    else if ((fimJornada.toLocalTime().isAfter(inicioNoturno) && fimJornada.toLocalTime().isBefore(fimNoturno)) || fimJornada.toLocalTime().equals(fimNoturno)) {
                        // A jornada começa após as 22:00 e termina antes das 5:00 do próximo dia
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, fimJornada));
                    }

                    // Caso 3.2: A jornada termina depois das 5:00 do dia seguinte
                    else if (fimJornada.toLocalTime().isAfter(fimNoturno) && fimJornada.toLocalTime().isBefore(inicioNoturno)) {
                        // Se a jornada começa depois das 22:00 e termina após as 5:00 do próximo dia
                        // Calcula o tempo noturno entre o início da jornada até as 5:00 do dia seguinte
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, LocalDateTime.of(fimJornada.toLocalDate(), fimNoturno)));
                    }

                    // Caso 3.3: A jornada termina depois das 22:00 em outro dia
                    else if (fimJornada.toLocalTime().isAfter(inicioNoturno) || fimJornada.toLocalTime().equals(inicioNoturno) || fimJornada.toLocalTime().isBefore(fimNoturno)) {
                        // Se a jornada cruza a noite e termina no dia seguinte, considera dois períodos noturnos:
                        // 1. Entre o início da jornada até 5:00
                        tempoNoturno = tempoNoturno.plus(Duration.between(inicioJornada, LocalDateTime.of(inicioJornada.toLocalDate().plusDays(1), fimNoturno)));

                        // 2. Entre as 22:00 do dia seguinte até o fim da jornada
                        tempoNoturno = tempoNoturno.plus(Duration.between(LocalDateTime.of(fimJornada.toLocalDate(), inicioNoturno), fimJornada));
                    }
                }
            
            }
        }        

        Integer horasNoturnas = (int) (tempoNoturno.toMinutes() / 52.5f) * 60;
        Float minutosNoturnos = (tempoNoturno.toMinutes() % 52.5f);
        Long horarioNoturno = (long) (horasNoturnas + minutosNoturnos);
        Duration adicionalNoturno = Duration.ofMinutes(horarioNoturno);
    
        return adicionalNoturno;
    }
}
    

